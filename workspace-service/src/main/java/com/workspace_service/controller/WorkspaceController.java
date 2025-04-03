package com.workspace_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workspace_service.dto.InviteRequest;
import com.workspace_service.dto.WorkspaceDto;
import com.workspace_service.dto.WorkspaceMemberDto;
import com.workspace_service.dto.WorkspaceRequest;
import com.workspace_service.entity.Workspace;
import com.workspace_service.entity.WorkspaceMember;
import com.workspace_service.helper.ApiResponse;
import com.workspace_service.service.WorkspaceService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("/workspace")
@RestController
public class WorkspaceController {
	
	private final WorkspaceService workspaceService;
	
	@PostMapping
	public ResponseEntity<?> createWorkspace(@Valid @RequestBody WorkspaceRequest request)
	{
		Workspace workspace = workspaceService.createWorkspace(request);
		return ResponseEntity.ok(ApiResponse.success("Workspace saved succesfully", workspace));
	}
	
	@GetMapping
	public ResponseEntity<?> getWorkspaces()
	{
		List<WorkspaceDto> workspaces = workspaceService.getWorkspaces();
		return ResponseEntity.ok(ApiResponse.success("Worksspaces fetched successfully.",workspaces));
	}
	
	@PostMapping("/{id}/invite")
	public ResponseEntity<?> inviteUser(@PathVariable("id")Long workspaceId,@RequestBody InviteRequest inviteRequest)
	{
		workspaceService.invite(workspaceId,inviteRequest);
		return ResponseEntity.ok(ApiResponse.success("Invitation successfull", null));
	}	
	
	@GetMapping("/{workspaceId}/members")
	public ResponseEntity<?> getWorkspaceMembers(@PathVariable(name="workspaceId",required=true)Long workspaceId)
	{
		List<WorkspaceMemberDto> workspaceMembers = workspaceService.getWorkspaceMembers(workspaceId);
		return ResponseEntity.ok(ApiResponse.success("Workspace Members fetched successfull", workspaceMembers));
	}
	
	
	//internal api call
	@GetMapping("/{workspaceId}")
	public ResponseEntity<WorkspaceDto> getWorkSpaceById(@PathVariable(name="workspaceId",required=true)Long workspaceId)
	{
		WorkspaceDto workspaceDto = workspaceService.getWorkSpaceById(workspaceId);
		return ResponseEntity.ok(workspaceDto);
	}
	
}
