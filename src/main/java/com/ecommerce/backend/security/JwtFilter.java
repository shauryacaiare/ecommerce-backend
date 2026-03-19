package com.ecommerce.backend.security;

import com.ecommerce.backend.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        String auhHeader = request.getHeader("Authorization");

        // check if authHeader starts with "Bearer"
        if(auhHeader == null || !auhHeader.startsWith("Bearer ")){
            log.debug("No Bearer token found, continuing filter chain");
            filterChain.doFilter(request,response);
            return;
        }

        //extract token from authHeader
        String token = auhHeader.split(" ")[1];

        //extract userName from the token
        String userName = jwtUtil.extractUserName(token);
        if(userName == null){
            log.error("UserName not present in the token");
            filterChain.doFilter(request,response);
            return;
        }

        //check if securtyContextHolder is null or not
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            filterChain.doFilter(request,response);
            return;
        }

        //Load user using UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        //validate token using jwtUtil
        boolean isToKenVerified = jwtUtil.validateToken(token,userDetails);

        //check if token is verified
        if(isToKenVerified){
            UsernamePasswordAuthenticationToken authToken  =
                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request,response);
    }
}
