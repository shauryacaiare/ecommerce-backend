package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface CompanyRepo extends JpaRepository<Company,Long> {

    boolean existsByEmail(String email);
}
