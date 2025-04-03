package com.workspace_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workspace_service.dto.InvitationMessage;
import com.workspace_service.dto.InviteRequest;
import com.workspace_service.dto.UserDto;
import com.workspace_service.dto.WorkspaceDto;
import com.workspace_service.dto.WorkspaceMemberDto;
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
		workspaceMemeber.setRole(WorkspaceRole.ADMIN);
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
		
		List<UserDto> users = userService.getUsersByEmails(inviteRequest.getEmails());
		UserDto user = getUser();
		Workspace workspace = getWorkspaceById(workspaceId);
		
		for(UserDto userDto:users) 
		{
			InvitationMessage invitation = new InvitationMessage(
					workspace.getId(),
					userDto.getEmail(), 
					user, 
					LocalDateTime.now());
			invitationService.sendInvitation(invitation);
		}
		
		List<WorkspaceMember> workspaceMembers = new ArrayList<>();
		
		for(UserDto userDto:users)
		{
			WorkspaceMember workspaceMember = new WorkspaceMember();
			
			workspaceMember.setInvitedBy(user.getId());
			workspaceMember.setRole(inviteRequest.getRole());
			workspaceMember.setStatus(Status.PENDING);
			workspaceMember.setUserId(userDto.getId());
			workspaceMember.setWorkspaceId(workspaceId);
			workspaceMembers.add(workspaceMember);
		}
		
		workspaceMemeberRep.saveAll(workspaceMembers);
	}
	

	public WorkspaceDto getWorkSpaceById(Long workspaceId) 
	{
		UserDto user = getUser();
		WorkspaceDto workspaceDto = workspaceRep.findWorkspaceDtoByIdAndOwnerId(workspaceId, user.getId())
				.orElseThrow(()->new ResourceNotFoundException("Workspace not found"));
		
		return workspaceDto;
	}

	public List<WorkspaceMemberDto> getWorkspaceMembers(Long workspaceId) 
	{
		UserDto user = getUser();
		
		if(!WorkspaceExist(workspaceId)) {
			throw new ResourceNotFoundException("Workspace not found.");
		}
		
		 List<WorkspaceMember> workspaceMembers =
				 workspaceMemeberRep.findAllByWorkspaceIdAndUserId(workspaceId, user.getId());
			
		//extract user ids
		List<Long> userIds = workspaceMembers.stream()
			.map(item -> item.getUserId())
			.collect(Collectors.toList());
		
		//fetch users from userservice
		List<UserDto> users = userService.getUsersByIds(userIds);
		
		Map<Long,UserDto> userMap = users.stream()
				.collect(Collectors.toMap(UserDto::getId, u->u));
		
		return workspaceMembers.stream()
			.map(member -> new WorkspaceMemberDto(
				member.getWorkspaceId(),
				member.getUserId(),
				userMap.get(member.getUserId()).getUsername(),
				member.getStatus(),
				member.getRole()
			))
			.collect(Collectors.toList());
		
	}

	public List<WorkspaceDto> getWorkspaces() {
		UserDto user = getUser(); 
		return workspaceRep.findByOwnerId(user.getId());
	}

	
}
