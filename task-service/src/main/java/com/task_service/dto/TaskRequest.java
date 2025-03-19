package com.task_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
	
	@NotBlank
	@Size(min=3,max=200,message="Title must be between 3 to 200 character.")
	private String title;
	
	private String description;
}
