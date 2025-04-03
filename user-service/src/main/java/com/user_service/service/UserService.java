package com.user_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

	public boolean userExist(String email) {
		return userRep.existsByEmail(email);
	}

	public UserProjection getUserByEmail(String email) {
		return userRep.findByUsername(email);
	}

	public List<UserProjection> getUsersByEmails(List<String> emails) {
		return userRep.findByEmailIn(emails);
	}

	public List<UserProjection> getUsersByIds(List<Long> ids) {
		return userRep.findByIdIn(ids);
	}
	
	
}
