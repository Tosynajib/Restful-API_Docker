package com.restapiblog.restapiblog.utils;

import com.restapiblog.restapiblog.serviceImpl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // extend your class with OncePerRequestFilter
    private JwtUtils utils;
    private UserServiceImpl userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils utils, @Lazy UserServiceImpl userService) {
        this.utils = utils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // set all this values to null
        String token = null;
        String authorizationHeader = null;
        String username = null;
        UserDetails userDetails = null;

        // first step set the authorizationHeader
        authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(7);
            username = utils.extractUsername.apply(token);
        }
        // second step check the username extracted from token is set in the security context and if it is not set we have to set it in the database
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            userDetails = userService.loadUserByUsername(username);

        // third step checks the validity of the token against the username extracted from the user details
        // if token is still valid , then create a new UPAToken with the user details

            if (userDetails != null && utils.isTokenValid.apply(token, userDetails.getUsername())){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // fourth step set more information such as the remote address and session ID to the UPAToken using the webAuthenticationDetails source
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // fifth step set the UPAToken with the userDetails and other information to the "securityContextHolder" as Spring security knows this user is now authenticated
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        filterChain.doFilter(request, response);
    }

}
