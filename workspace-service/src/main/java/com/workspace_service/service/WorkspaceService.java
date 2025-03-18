package com.workspace_service.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.workspace_service.client.UserService;
import com.workspace_service.dto.InvitationMessage;
import com.workspace_service.dto.InviteRequest;
import com.workspace_service.dto.UserDto;
import com.workspace_service.dto.WorkspaceRequest;
import com.workspace_service.entity.Workspace;
import com.workspace_service.repository.InvitationRepository;
import com.workspace_service.repository.WorkspaceRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class WorkspaceService {
	
	private final WorkspaceRepository workspaceRep;
	private final UserService userService;
	private final InvitationService invitationService;
	
	
	public Workspace createWorkspace(WorkspaceRequest request) 
	{
		Workspace workspace = toEntity(request);
		return workspaceRep.save(workspace);
	}
	
	private UserDto getUser() {
		UserDto user = userService.getUserByUsername();
		return user;
	}
	
	private Workspace getWorkspaceById(Long id) 
	{
		Optional<Workspace> workspaceOpt = workspaceRep.findById(id);
		if(workspaceOpt.isEmpty()) {
			throw new ResourceNotFoundException("Workspace not found.");
		}
		
		return workspaceOpt.get();
	}
	
	private Workspace toEntity(WorkspaceRequest request) 
	{
		UserDto user = getUser();
		
		Workspace workspace = new Workspace();
		workspace.setName(request.getName());
		workspace.setOwnerId(user.getId());
		
		return workspace;
	}


	public void invite(Long workspaceId, InviteRequest inviteRequest) {
		
		UserDto user = getUser();
		Workspace workspace = getWorkspaceById(workspaceId);
		
		
		InvitationMessage invitation = new InvitationMessage(
				workspace.getId(),
				inviteRequest.getEmail(), 
				user.getEmail(), 
				LocalDateTime.now());
		
		invitationService.sendInvitation(invitation);
	}

	
}
