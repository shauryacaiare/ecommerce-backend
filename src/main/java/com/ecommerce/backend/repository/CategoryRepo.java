package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Set<Category> findAllByIdIn(Set<Long> categories);

    boolean existsByName(String name);
}
