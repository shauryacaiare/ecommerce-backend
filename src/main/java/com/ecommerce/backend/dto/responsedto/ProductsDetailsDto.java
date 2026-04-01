package com.ecommerce.backend.dto.responsedto;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.dto.CompanyDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ProductsDetailsDto {

    private Long productId;
    private String name;
    private String desc;
    private BigDecimal price;
    private Integer stock;
    private Boolean isActive;
    private CompanyDto companyDto;
    private Set<CategoryDto> categoryDtos;
}
