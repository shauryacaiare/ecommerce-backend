package com.ecommerce.backend.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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
public class LoginRequestDto {

    @NotBlank(message = "Please provide email")
    @Email(message = "Please provide valid email")
    private String email;
    @NotBlank(message = "Please provide password")
    private String password;
}
