package com.vnair.usermanagement.repository;

import com.vnair.usermanagement.entity.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    
    /**
     * Tìm kiếm máy bay theo code hoặc name
     */
    @Query("SELECT a FROM Aircraft a WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(a.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Aircraft> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Kiểm tra code có tồn tại không
     */
    boolean existsByCode(String code);
    
    /**
     * Tìm theo code
     */
    Aircraft findByCode(String code);
}
