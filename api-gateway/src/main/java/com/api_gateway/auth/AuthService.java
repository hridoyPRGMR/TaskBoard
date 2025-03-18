package com.api_gateway.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.api_gateway.helper.LoginForm;
import com.api_gateway.helper.SignupForm;
import com.api_gateway.helper.UserRepository;
import com.api_gateway.security.JwtService;

import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UserRepository userRep;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ReactiveAuthenticationManager authManager;
    private final JwtService jwtService;
    
    public AuthService(UserRepository userRep,
                       BCryptPasswordEncoder passwordEncoder,
                       ReactiveAuthenticationManager authManager,
                       JwtService jwtService) {
        this.userRep = userRep;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    // Reactive user registration
    public Mono<User> saveUser(SignupForm form) {
        return userRep.existsByEmail(form.getEmail())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new RuntimeException("Email already exists."));
                }
                User user = toEntity(form);
                return userRep.save(user);
            });
    }
    
    // Reactive user authentication
    public Mono<Map<String, String>> authenticate(LoginForm form) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword());
        return authManager.authenticate(authToken)
            .flatMap(authentication -> {
                if (authentication.isAuthenticated()) {
                    Map<String, String> tokenMap = new HashMap<>();
                    tokenMap.put("JwtToken", jwtService.generateToken(form.getEmail()));
                    return Mono.just(tokenMap);
                } else {
                    return Mono.error(new BadCredentialsException("Bad credentials."));
                }
            });
    }
    
    private User toEntity(SignupForm form) {
        User user = new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setUsername(form.getEmail());
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
