package com.task_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.task_service.dto.UserDto;


@FeignClient(name = "USERSERVICE")
public interface UserService {
	
	@GetMapping("/user")
	UserDto getUserByUsername();
	
}
