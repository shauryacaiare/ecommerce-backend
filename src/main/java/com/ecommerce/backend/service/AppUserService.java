package com.ecommerce.backend.service;

import com.ecommerce.backend.excpetion.ResourceNotFoundException;
import com.ecommerce.backend.repository.UserRepo;
import com.ecommerce.backend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username){

        return userRepo.findByEmail(username)
                .map(user -> new UserPrincipal(user))
                .orElseThrow(() -> new ResourceNotFoundException("UserName","email",username));
    }
}
