package com.ecommerce.backend.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyReqDto {

    @NotBlank(message = "name cannot be null or empty")
    private String name;
    @NotBlank(message = "please provide contact number")
    private String number;
    @Builder.Default
    private Boolean isActive = true;
    @NotBlank
    @Email(message = "Invalid Email")
    private String email;
}
