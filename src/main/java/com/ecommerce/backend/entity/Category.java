package com.ecommerce.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "products")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Category name is required")
    @Column(nullable = false,length = 50)
    private String name;

    @NotBlank(message = "Category description is required")
    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "categories",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products;
}
