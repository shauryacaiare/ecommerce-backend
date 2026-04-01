package com.ecommerce.backend.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DeleteSellerDto {


    @NotBlank(message = "seller name cannot be null or empty")
    private String name;
    @NotBlank
    @Email(message = "please provide valid email")
    private String email;

    private String registrationId;
    private String gstNumber;

    @NotBlank
    @Size(min = 8, message = "password must be at least 8 characters")
    private String password;

    @NotNull(message = "companyId for seller cannot be null or empty")
    private Long companyId;
}
