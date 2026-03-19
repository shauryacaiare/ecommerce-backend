package com.ecommerce.backend.entity;

import jakarta.persistence.*;
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
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "is_active",nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
