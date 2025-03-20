package com.workspace_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvitationMessage {
	
	private Long workspaceId;
	private String email;
	private UserDto invitedBy;
	private LocalDateTime invitationTime;
	
}
