package com.task_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignUserRequest {
	
	@NotNull
	private Long userId;
	
}
