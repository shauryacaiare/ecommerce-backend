package com.ecommerce.backend.dto.responsedto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {

    private String message;
    private boolean success;
    private LocalDateTime timestamp;
}
