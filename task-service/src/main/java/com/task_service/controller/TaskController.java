package com.task_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task_service.dto.AssignUserRequest;
import com.task_service.dto.TaskDto;
import com.task_service.dto.TaskRequest;
import com.task_service.entity.Task;
import com.task_service.enums.Status;
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
			@Valid @RequestBody TaskRequest taskRequest)
	{
		Task task = taskService.createTask(workspaceId,taskRequest);
		return ResponseEntity.ok(ApiResponse.success("Task creation successfull.", task));
	}
	
	@PostMapping("/subtask/{workspaceId}/{taskId}")
	public ResponseEntity<?>createSubTask(
			@PathVariable(name="workspaceId",required=true)Long workspaceId,
			@PathVariable(name="taskId",required=true)Long taskId,
			@Valid @RequestBody TaskRequest subTaskRequest)
	{
		Task subTask = taskService.createSubTask(workspaceId,taskId,subTaskRequest);
		return ResponseEntity.ok(ApiResponse.success("Sub Task creation successfull.", subTask));
	}
	
	@GetMapping("/tasks/{workspaceId}")
	public ResponseEntity<?> getTasks(@PathVariable(name="workspaceId",required=true)Long workspaceId){
		
		List<TaskDto> tasks= taskService.getTasks(workspaceId);
		return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully.", tasks));
	}
	
	@DeleteMapping("/{taskId}/delete")
	public ResponseEntity<?> deleteTask(@PathVariable(name="taskId",required=true)Long taskId){
		taskService.deleteTask(taskId);
		return ResponseEntity.ok(ApiResponse.success("Task deleted successfully.", null));
	}
	
	@PostMapping("/{taskId}/assign")
	public ResponseEntity<?> assignUserToTask(@PathVariable(name="taskId",required=true)Long taskId,
			@Valid @RequestBody AssignUserRequest request
			)
	{
		taskService.assignUser(taskId,request);
		return ResponseEntity.ok(ApiResponse.success("Task assigned successfully.", null));
	}
	
	@PutMapping("/{taskId}/update-status")
	public ResponseEntity<?> updateTaskStatus(@PathVariable(name="taskId") Long taskId,
			@RequestBody Status status
			){
		taskService.updateTaskStatus(taskId,status);
		return ResponseEntity.ok(ApiResponse.success("Task status updated successfully.", null));
	}
	
}
