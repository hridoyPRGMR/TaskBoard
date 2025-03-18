package com.workspace_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.workspace_service.dto.UserDto;

@FeignClient(name = "USERSERVICE")
public interface UserService {
	
	@GetMapping("/user")
	UserDto getUserByUsername();
	
}
