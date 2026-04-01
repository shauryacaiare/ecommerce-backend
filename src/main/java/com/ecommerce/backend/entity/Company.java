package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "company")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "products")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "company name cannot be null or empty")
    private String name;

    @Column(name = "contact_num",nullable = false,length = 15)
    @NotBlank(message = "company contact number is required")
    private String number;

    @Email
    @Column(name = "company_email",nullable = false)
    @NotBlank(message = "company email address is missing")
    private String email;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY)
    private Set<Product> products;

    public void addProduct(Product product){
        this.getProducts().add(product);
        product.setCompany(this);
    }

    public void removeProduct(Product product){
        products.remove(product);
        product.setCompany(null);
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
