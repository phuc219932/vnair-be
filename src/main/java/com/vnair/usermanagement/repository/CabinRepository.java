package com.vnair.usermanagement.repository;

import com.vnair.usermanagement.common.CabinPosition;
import com.vnair.usermanagement.entity.Cabin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CabinRepository extends JpaRepository<Cabin, Long> {
    
    /**
     * Tìm cabin theo aircraft ID
     */
    List<Cabin> findByAircraftId(Long aircraftId);
    
    /**
     * Tìm cabin theo aircraft ID với phân trang
     */
    Page<Cabin> findByAircraftId(Long aircraftId, Pageable pageable);
    
    /**
     * Tìm kiếm cabin theo từ khóa
     */
    @Query("SELECT c FROM Cabin c WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Cabin> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Tìm kiếm cabin với nhiều điều kiện
     */
    @Query("SELECT c FROM Cabin c WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:aircraftId IS NULL OR c.aircraft.id = :aircraftId) AND " +
           "(:position IS NULL OR c.position = :position)")
    Page<Cabin> findByKeywordAndAircraftIdAndPosition(
        @Param("keyword") String keyword, 
        @Param("aircraftId") Long aircraftId, 
        @Param("position") CabinPosition position, 
        Pageable pageable);
    
    /**
     * Tìm theo position
     */
    Page<Cabin> findByPosition(CabinPosition position, Pageable pageable);
}
