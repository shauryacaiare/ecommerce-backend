package com.ecommerce.backend.dto.responsedto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductResDto {

    private Long id;
    private String name;
    private String desc;
    private BigDecimal price;
    private Integer stock;
    private Boolean isActive;
    private Long companyId;
    private Set<Long> categories;
    private LocalDateTime updatedAt;
}
