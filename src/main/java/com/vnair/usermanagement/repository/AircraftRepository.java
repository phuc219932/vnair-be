package com.vnair.usermanagement.repository;

import com.vnair.usermanagement.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    // Có thể thêm các phương thức custom nếu cần
}
