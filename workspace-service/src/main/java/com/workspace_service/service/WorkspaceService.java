package com.workspace_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.workspace_service.dto.InvitationMessage;
import com.workspace_service.dto.InviteRequest;
import com.workspace_service.dto.UserDto;
import com.workspace_service.dto.WorkspaceDto;
import com.workspace_service.dto.WorkspaceRequest;
import com.workspace_service.entity.Workspace;
import com.workspace_service.entity.WorkspaceMember;
import com.workspace_service.enums.Status;
import com.workspace_service.enums.WorkspaceRole;
import com.workspace_service.exception.ResourceNotFoundException;
import com.workspace_service.external.UserService;
import com.workspace_service.repository.WorkspaceMemeberRepository;
import com.workspace_service.repository.WorkspaceRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class WorkspaceService {
	
	private final WorkspaceRepository workspaceRep;
	private final WorkspaceMemeberRepository workspaceMemeberRep;
	private final UserService userService;
	private final InvitationService invitationService;
	
	public boolean WorkspaceExist(Long workspaceID) {
		return workspaceRep.existsById(workspaceID);
	}
	
	@Transactional
	public Workspace createWorkspace(WorkspaceRequest request) 
	{	
		UserDto user = getUser();
		
		Workspace workspace = toEntity(request);
		Workspace savedWorkspace = workspaceRep.save(workspace);
		
		WorkspaceMember workspaceMemeber = new WorkspaceMember();
		workspaceMemeber.setWorkspaceId(savedWorkspace.getId());
		workspaceMemeber.setUserId(user.getId());
		workspaceMemeber.setRole(WorkspaceRole.OWNER);
		workspaceMemeber.setStatus(Status.ACCEPTED);
		workspaceMemeber.setInvitedBy(user.getId());
		workspaceMemeberRep.save(workspaceMemeber);
		
		return savedWorkspace;
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
		
		UserDto invitedUser = userService.getUserByEmail(inviteRequest.getEmail());
		if(invitedUser == null) {
			throw new ResourceNotFoundException("User not exist with email: "+inviteRequest.getEmail());
		}
		
		UserDto user = getUser();
		Workspace workspace = getWorkspaceById(workspaceId);
		
		InvitationMessage invitation = new InvitationMessage(
				workspace.getId(),
				inviteRequest.getEmail(), 
				user, 
				LocalDateTime.now());
		
		invitationService.sendInvitation(invitation);
		
		WorkspaceMember workspaceMember = new WorkspaceMember();
		workspaceMember.setInvitedBy(user.getId());
		workspaceMember.setRole(WorkspaceRole.MEMBER);
		workspaceMember.setStatus(Status.PENDING);
		workspaceMember.setUserId(invitedUser.getId());
		workspaceMember.setWorkspaceId(workspaceId);
		workspaceMemeberRep.save(workspaceMember);
	}

	public WorkspaceDto getWorkSpaceById(Long workspaceId) 
	{
		UserDto user = getUser();
		WorkspaceDto workspaceDto = workspaceRep.findWorkspaceDtoByIdAndOwnerId(workspaceId, user.getId())
				.orElseThrow(()->new ResourceNotFoundException("Workspace not found"));
		
		return workspaceDto;
	}

	public List<WorkspaceMember> getWorkspaceMembers(Long workspaceId) 
	{
		UserDto user = getUser();
		
		if(!WorkspaceExist(workspaceId)) {
			throw new ResourceNotFoundException("Workspace not found.");
		}
		
		List<WorkspaceMember> workspaceMembers = workspaceMemeberRep.findAllByWorkspaceIdAndUserId(workspaceId, user.getId());
		
		return workspaceMembers;
	}

	
}
