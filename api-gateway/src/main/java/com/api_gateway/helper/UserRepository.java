package com.api_gateway.helper;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.api_gateway.auth.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<UserDto> findByUsername(String username);

    Mono<Boolean> existsByEmail(String email);
}
