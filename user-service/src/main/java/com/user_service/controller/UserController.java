package com.user_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user_service.common.ApiResponse;
import com.user_service.entity.Workspace;
import com.user_service.external.WorkspaceService;
import com.user_service.projection.UserProjection;
import com.user_service.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final WorkspaceService workspaceService;

	
	@GetMapping("/test")
	public ResponseEntity<?> getworkspace(){
		Workspace workSpace = workspaceService.getWorkSpace();
		return ResponseEntity.ok(new ApiResponse<>(true, "Workspace fetched successfully.", workSpace));
	}
	
	@GetMapping
	public UserProjection getUser() {
		return userService.getUserByUsername();
	}
	
	@GetMapping("/exist")
	public UserProjection getUserByEmail(@RequestParam String email) {
		return userService.getUserByEmail(email);
	}
	
	@PostMapping("/get-users-by-emails")
	public List<UserProjection> getUsersByEmails(@RequestBody List<String> emails){
		return userService.getUsersByEmails(emails);
	}
	
	@PostMapping("/get-users-by-ids")
	public List<UserProjection> getUsersByIds(@RequestBody List<Long> ids){
		return userService.getUsersByIds(ids);
	}
}
