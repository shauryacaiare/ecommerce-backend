package com.ecommerce.backend.security;

import com.ecommerce.backend.excpetion.ResourceNotFoundException;
import com.ecommerce.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username){

        return userRepo.findByEmail(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new ResourceNotFoundException("UserName","email",username));
    }
}
