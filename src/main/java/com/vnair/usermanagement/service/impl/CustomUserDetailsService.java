package com.vnair.usermanagement.service.impl;

import com.vnair.usermanagement.common.UserStatus;
import com.vnair.usermanagement.entity.User;
import com.vnair.usermanagement.entity.UserRole;
import com.vnair.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom UserDetailsService that loads user data from the database
 * Replaces the hardcoded in-memory authentication
 */
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== CustomUserDetailsService.loadUserByUsername called with: " + username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        System.out.println("=== Found user: " + user.getUsername() + ", status: " + user.getStatus());
        System.out.println("=== Password hash from DB: " + user.getPasswordHash());

        // Check if user is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            System.out.println("=== User status is not ACTIVE: " + user.getStatus());
            throw new UsernameNotFoundException("User account is not active: " + username);
        }

        Collection<? extends GrantedAuthority> authorities = getAuthorities(user);
        System.out.println("=== User authorities: " + authorities);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(user.getStatus() == UserStatus.SUSPENDED)
                .credentialsExpired(false)
                .disabled(user.getStatus() == UserStatus.INACTIVE || user.getStatus() == UserStatus.DELETED)
                .build();
        
        System.out.println("=== Created UserDetails with username: " + userDetails.getUsername());
        System.out.println("=== UserDetails enabled: " + userDetails.isEnabled());
        System.out.println("=== UserDetails account non-locked: " + userDetails.isAccountNonLocked());
        
        return userDetails;
    }

    /**
     * Get user authorities based on their roles
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Set<UserRole> userRoles = user.getUserRoles();
        
        return userRoles.stream()
                .filter(ur -> ur.getIsActive() && !ur.isExpired())
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName().toUpperCase()))
                .collect(Collectors.toSet());
    }
}
