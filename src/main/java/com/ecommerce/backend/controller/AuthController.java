package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.requestdto.LoginRequestDto;
import com.ecommerce.backend.dto.requestdto.SignUpRequestDto;
import com.ecommerce.backend.dto.responsedto.AuthResponseDto;
import com.ecommerce.backend.dto.responsedto.SignUpResponseDto;
import com.ecommerce.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        log.info("sign up request for user {}",signUpRequestDto.getEmail());
        SignUpResponseDto responseDto = authService.signup(signUpRequestDto);
        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        log.info("login request received for user {}",loginRequestDto.getEmail());
        AuthResponseDto responseDto = authService.login(loginRequestDto);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

}
