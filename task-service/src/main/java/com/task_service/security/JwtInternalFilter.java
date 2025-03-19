package com.task_service.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInternalFilter extends OncePerRequestFilter {

    private static final String INTERNAL_AUTH_HEADER = "X-Internal-Auth";
    private final JwtService jwtService;

    public JwtInternalFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException 
    {
        String token = request.getHeader(INTERNAL_AUTH_HEADER);
       
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try 
            {
                String username = jwtService.extractUsername(token);
                if (jwtService.validateInternalToken(token, username)) 
                {
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    
                    authentication.setDetails(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Toekn in workspace: "+token);
                }
            } 
            catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
