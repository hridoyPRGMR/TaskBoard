package com.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user_service.entity.User;
import com.user_service.projection.UserProjection;

public interface UserRepository extends JpaRepository<User, Long> {

	UserProjection findByUsername(String username);

	boolean existsByEmail(String email);
	
}
