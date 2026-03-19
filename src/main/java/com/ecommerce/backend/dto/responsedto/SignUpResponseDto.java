package com.ecommerce.backend.dto.responsedto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SignUpResponseDto {

    private String name;
    private String email;
    private String message;
}
