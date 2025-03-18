package com.user_service.service;

import org.springframework.stereotype.Service;

import com.user_service.dto.UserDto;
import com.user_service.entity.User;
import com.user_service.projection.UserProjection;
import com.user_service.repository.UserRepository;
import com.user_service.security.JwtService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRep;
	private final JwtService jwtService;
	
	

	public UserProjection getUserByUsername() 
	{
		String username = jwtService.getAuthenticatedUsername();
		UserProjection user = userRep.findByUsername(username);
		return user;
	}
	
	
}
