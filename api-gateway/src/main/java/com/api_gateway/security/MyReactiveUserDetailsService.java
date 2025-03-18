package com.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.api_gateway.helper.UserRepository;
import reactor.core.publisher.Mono;

@Service
public class MyReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(user -> User.withUsername(user.getUsername())
                                  .password(user.getPassword())
                                  .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                                  .build())
            .switchIfEmpty(Mono.error(new RuntimeException("User not found: " + username)));
    }
}
