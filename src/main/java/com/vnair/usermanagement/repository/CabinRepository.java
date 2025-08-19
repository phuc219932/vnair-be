package com.vnair.usermanagement.repository;

import com.vnair.usermanagement.entity.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CabinRepository extends JpaRepository<Cabin, Long> {
    // Có thể thêm các phương thức custom nếu cần
}
