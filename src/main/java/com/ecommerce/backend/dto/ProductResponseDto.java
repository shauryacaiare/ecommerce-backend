package com.ecommerce.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;
    private String name;
    private String desc;
    private BigDecimal price;
    private Integer stock;
    private Boolean isActive;
    private Long companyId;
    private String companyName;
    private Set<CategoryDto> categories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
