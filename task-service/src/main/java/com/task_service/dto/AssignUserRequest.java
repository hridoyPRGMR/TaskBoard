package com.task_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignUserRequest {
	
	private Long userId;
	
	private Boolean canEdit;
	
	private Boolean canDelete;
	
	private Boolean canComment;
	
}
