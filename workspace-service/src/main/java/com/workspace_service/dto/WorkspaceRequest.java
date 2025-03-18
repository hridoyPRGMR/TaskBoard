package com.workspace_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceRequest {
	
	@NotBlank(message = "Workspace name must not be blank.")
	private String name;
}
