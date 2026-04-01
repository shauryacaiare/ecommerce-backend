package com.ecommerce.backend.dto.requestdto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class  CreateProductReqDto {

    @NotBlank(message = "name cannot be null or empty")
    private String name;

    @NotBlank(message = "product description should not be null or empty")
    private String desc;

    @NotNull
    @Min(value = 0 ,message = "at least 1 product must be added")
    private Integer stock;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotEmpty(message = "At leasr one category is required")
    private Set<Long> categories;

    private Boolean isActive = true;

}
