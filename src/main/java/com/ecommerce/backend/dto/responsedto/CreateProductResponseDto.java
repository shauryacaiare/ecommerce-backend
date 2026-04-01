package com.ecommerce.backend.dto.responsedto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class CreateProductResponseDto {

    private Long productId;
    private String name;
    private String desc;
    private BigDecimal price;
    private Integer stock;
    private Boolean isActive;
    private Long companyId;
    private Set<Long> categories;
}
