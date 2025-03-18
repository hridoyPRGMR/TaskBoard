package com.api_gateway.auth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api_gateway.helper.ApiResponse;
import com.api_gateway.helper.LoginForm;
import com.api_gateway.helper.SignupForm;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public Mono<ResponseEntity<ApiResponse<User>>> register(@Valid @RequestBody SignupForm form) {
        return authService.saveUser(form)
            .map(user -> ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully.", user)))
            .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null))));
    }
    
    @PostMapping("/login")
    public Mono<ResponseEntity<ApiResponse<Map<String, String>>>> login(@Valid @RequestBody LoginForm loginRequest) {
        
    	System.out.println(loginRequest.getEmail() + " " +loginRequest.getPassword());
    	
    	return authService.authenticate(loginRequest)
            .map(token -> ResponseEntity.ok(new ApiResponse<>(true, "Login successful.", token)))
            .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null))));
    }
    
}
