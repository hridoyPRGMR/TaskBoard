package com.task_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task_service.dto.TaskRequest;
import com.task_service.helper.ApiResponse;
import com.task_service.service.TaskService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/task")
public class TaskController {
	
	private final TaskService taskService;
	
	@PostMapping("/{workspaceId}")
	public ResponseEntity<?>createTask(
			@PathVariable(name="workspaceId",required=true)Long workspaceId,
			@Valid @RequestBody List<TaskRequest> taskRequests)
	{
		taskService.createTask(workspaceId,taskRequests);
		return ResponseEntity.ok(ApiResponse.success("Task creation successfull.", null));
	}
	
	@PostMapping("/subtask/{workspaceId}/{taskId}")
	public ResponseEntity<?>createSubTask(
			@PathVariable(name="workspaceId",required=true)Long workspaceId,
			@PathVariable(name="taskId",required=true)Long taskId,
			@Valid @RequestBody List<TaskRequest> subTaskRequests)
	{
		taskService.createSubTask(workspaceId,taskId,subTaskRequests);
		return ResponseEntity.ok(ApiResponse.success("Sub Task creation successfull.", null));
	}
	
}
