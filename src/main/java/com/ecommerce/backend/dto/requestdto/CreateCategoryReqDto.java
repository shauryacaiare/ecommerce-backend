package com.ecommerce.backend.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryReqDto {

    @NotBlank(message = "category should have a name")
    private String name;
    @NotBlank(message = "please provide description for the category")
    private String desc;
}
