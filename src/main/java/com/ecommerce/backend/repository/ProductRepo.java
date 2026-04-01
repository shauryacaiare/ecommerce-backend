package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {


    boolean existsByNameAndCompanyId(String name, Long companyId);

    @Query(value = "SELECT DISTINCT p FROM Product p " +
            "JOIN FETCH p.categories " +
            "JOIN FETCH p.company " +
            "WHERE p.isActive = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.desc) LIKE LOWER(CONCAT('%', :keyword, '%')))",
    countQuery = "SELECT COUNT(DISTINCT p) FROM Product p " +
            "WHERE p.isActive = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.desc) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchActiveProductsByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM Product p " +
            "JOIN FETCH p.categories " +
            "JOIN FETCH p.company " +
            "WHERE p.isActive = true",
    countQuery = "SELECT COUNT(DISTINCT p) FROM Product p where p.isActive = true")
    Page<Product> findByIsActiveTrue(Pageable pageable);
}
