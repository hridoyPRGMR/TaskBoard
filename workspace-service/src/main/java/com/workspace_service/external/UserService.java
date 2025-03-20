package com.workspace_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workspace_service.dto.UserDto;

@FeignClient(name = "USERSERVICE")
public interface UserService {
	
	@GetMapping("/user")
	UserDto getUserByUsername();
	
	@GetMapping("/user/exist")
	UserDto getUserByEmail(@RequestParam("email") String email);
}
