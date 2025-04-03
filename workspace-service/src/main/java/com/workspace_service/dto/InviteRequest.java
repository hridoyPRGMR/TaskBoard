package com.workspace_service.dto;

import java.util.List;

import com.workspace_service.enums.WorkspaceRole;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InviteRequest {
	
	
	private List<String>emails;
	
	@Enumerated(EnumType.STRING)
	private WorkspaceRole role; 
}	
