package com.workspace_service.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workspace_service.dto.InviteRequest;
import com.workspace_service.dto.WorkspaceRequest;
import com.workspace_service.entity.Workspace;
import com.workspace_service.helper.ApiResponse;
import com.workspace_service.service.WorkspaceService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("/workspace")
@RestController
public class WorkspaceController {
	
	private final WorkspaceService workspaceService;
	
	@GetMapping
	public Workspace getWorkSpace() {
		
		Workspace workspace = new Workspace();
		
		workspace.setId(1L);
		workspace.setName("TestName");
		workspace.setOwnerId(1L);
		workspace.setCreatedAt(LocalDateTime.now());
		
		return workspace;
	}
	
	@PostMapping
	public ResponseEntity<?> createWorkspace(@Valid @RequestBody WorkspaceRequest request)
	{
		Workspace workspace = workspaceService.createWorkspace(request);
		return ResponseEntity.ok(ApiResponse.success("Workspace saved succesfully", workspace));
	}
	
	@PostMapping("/{id}/invite")
	public ResponseEntity<?> inviteUser(@PathVariable("id")Long workspaceId,@RequestBody InviteRequest inviteRequest)
	{
		workspaceService.invite(workspaceId,inviteRequest);
		return ResponseEntity.ok(ApiResponse.success("Invitation successfull", null));
	}	
	
}
