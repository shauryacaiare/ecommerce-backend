package com.ecommerce.backend.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SignUpRequestDto {

    @NotBlank(message = "Please provide name")
    private String name;
    @NotBlank(message = "Please provide email")
    @Email(message = "Please provide valid email")
    private String email;
    @NotBlank(message = "Please provide password")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
