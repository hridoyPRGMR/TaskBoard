package com.task_service.dto;

import com.task_service.enums.Status;
import com.task_service.enums.TaskType;

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
public class TaskDto {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String title;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	private TaskType taskType;
}
