package com.workspace_service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
        	
            String internalToken = (String) authentication.getDetails();
            
            if (internalToken != null) {
                requestTemplate.header("X-Internal-Auth", "Bearer " + internalToken);
            }
        }
    }
}
