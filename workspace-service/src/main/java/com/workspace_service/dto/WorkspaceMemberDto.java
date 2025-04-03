package com.workspace_service.dto;

import com.workspace_service.enums.Status;
import com.workspace_service.enums.WorkspaceRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceMemberDto {
		
	private Long workspaceId;
	
	private Long userId;
	
	private String name;
		
	private Status status;
	
	private WorkspaceRole role;
}
