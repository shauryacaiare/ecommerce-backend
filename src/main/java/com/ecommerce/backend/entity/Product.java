package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"categories","company"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name",nullable = false)
    @NotBlank(message = "please provide name for the product")
    private String name;

    @Column(name = "description",nullable = false)
    @NotBlank(message = "please provide description for the product")
    private String desc;

    @Column(name = "product_price", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;

    @Column(name = "stock",nullable = false)
    @Min(value = 0 , message = "stock for a product cannot be less less than 0")
    private Integer stock;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @JoinColumn(name = "company_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    public void addCategories(Category category){
        this.categories.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(Category category){
        this.categories.remove(category);
        category.getProducts().remove(this);
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
