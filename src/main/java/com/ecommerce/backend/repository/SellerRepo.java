package com.ecommerce.backend.repository;


import com.ecommerce.backend.entity.Seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface SellerRepo extends JpaRepository<Seller,Long> {

    Optional<Seller> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT s FROM Seller s JOIN FETCH s.company WHERE s.email = :email")
    Optional<Seller> findByEmailWithCompany(@Param("email") String email);

    @Query("SELECT s FROM Seller s JOIN FETCH s.company c " +
            "JOIN FETCH c.products p " +
            "LEFT JOIN FETCH p.categories " +
    "where s.email = :email")
    Optional<Seller> findByEmailWithAllDetails(@Param("email") String email);
}
