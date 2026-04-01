package com.ecommerce.backend.excpetion.handler;

import com.ecommerce.backend.excpetion.InsufficientStockException;
import com.ecommerce.backend.excpetion.InvalidOrderStateException;
import com.ecommerce.backend.excpetion.ResourceAlreadyExistException;
import com.ecommerce.backend.excpetion.ResourceNotFoundException;
import com.ecommerce.backend.excpetion.response.ErrorResponseDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put(ex.getResourceName(),ex.getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message("The requested resource was not found")
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponseDto,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ResourceAlreadyExistException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponseDto> handleResourceFound(Exception ex){
        Map<String,String> errors = new HashMap<>();
        String key = "error";
        if (ex instanceof ResourceAlreadyExistException) {
            key = ((ResourceAlreadyExistException) ex).getResourceName();
        } else if (ex instanceof DataIntegrityViolationException) {
            key = "database_constraint";
        }

        errors.put(key,ex.getMessage());
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();
        return new ResponseEntity<>(responseDto,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNoValid(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap((FieldError::getField),
                        (DefaultMessageSourceResolvable::getDefaultMessage),
                        (first,second) -> first));

        ErrorResponseDto errorResponseDto = ErrorResponseDto
                .builder()
                .message("validations failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex){
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message("Invalid email or password")
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return new ResponseEntity<>(errorResponseDto,
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(
            AccessDeniedException ex){
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message("You don't have permission to access this resource")
                .status(HttpStatus.FORBIDDEN.value())
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return new ResponseEntity<>(errorResponseDto,
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex){
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message("An unexpected error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return new ResponseEntity<>(errorResponseDto,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidOrderState(
            InvalidOrderStateException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())  // 409 — state conflict
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponseDto> handleInsufficientStock(
            InsufficientStockException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getProductName(),
                "Available: " + ex.getAvailableStock() +
                        ", Requested: " + ex.getRequestedQuantity());

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())  // 409
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }
}
