package com.ecommerce.backend.excpetion.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponseDto {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String,String> errors;
}
