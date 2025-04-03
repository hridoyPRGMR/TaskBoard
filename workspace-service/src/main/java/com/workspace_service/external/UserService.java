package com.workspace_service.external;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.workspace_service.dto.UserDto;

@FeignClient(name = "USERSERVICE")
public interface UserService {
	
	@GetMapping("/user")
	UserDto getUserByUsername();
	
	@GetMapping("/user/exist")
	UserDto getUserByEmail(@RequestParam("email") String email);
	
	@PostMapping("/user/get-users-by-emails")
	List<UserDto> getUsersByEmails(@RequestBody List<String>emails);
	
	@PostMapping("/user/get-users-by-ids")
	List<UserDto> getUsersByIds(@RequestBody List<Long>ids);
}
