package com.ecommerce.backend.service;

import com.ecommerce.backend.constants.AppConstants;
import com.ecommerce.backend.dto.requestdto.LoginRequestDto;
import com.ecommerce.backend.dto.requestdto.SignUpRequestDto;
import com.ecommerce.backend.dto.responsedto.AuthResponseDto;
import com.ecommerce.backend.dto.responsedto.SignUpResponseDto;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.entity.UserType;
import com.ecommerce.backend.excpetion.ResourceAlreadyExistException;
import com.ecommerce.backend.repository.UserRepo;
import com.ecommerce.backend.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public SignUpResponseDto signup(SignUpRequestDto signUpRequestDto){
        String email = signUpRequestDto.getEmail();
        checkUserExists(email);
        User user = User.builder()
                .name(signUpRequestDto.getName())
                .email(email)
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .userType(UserType.CUSTOMER)
                .build();

        User savedUser = saveUser(user);
        return SignUpResponseDto.builder()
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .message(String.format(AppConstants.SIGNUP_MESSAGE, signUpRequestDto.getName()))
                .build();
    }

    private void checkUserExists(String email) {
        if(userRepo.existsByEmail(email)){
            log.info("User Already exists {}", email);
            throw new ResourceAlreadyExistException(email);
        }
    }

    private User saveUser(User user) {
        return userRepo.save(user);

    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        String token = jwtUtil.generateToken(authentication);

        String role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("UNKNOWN");

        Date expirationDate = jwtUtil.extractExpirationDate(token);

        return AuthResponseDto.builder()
                .userName(loginRequestDto.getEmail())
                .token(token)
                .tokenType("Bearer")
                .role(role)
                .expirationDate(expirationDate)
                .build();
    }
}
