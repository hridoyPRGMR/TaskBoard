package com.task_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.task_service.dto.TaskRequest;
import com.task_service.dto.UserDto;
import com.task_service.dto.WorkspaceDto;
import com.task_service.entity.Task;
import com.task_service.enums.Status;
import com.task_service.enums.TaskType;
import com.task_service.exception.ResourceNotFoundException;
import com.task_service.external.UserService;
import com.task_service.external.WorkspaceService;
import com.task_service.repository.TaskRepository;

import feign.FeignException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TaskService {

	private final TaskRepository taskRep;
	private final UserService userService;
	private final WorkspaceService workspaceService;
		
	//helper methods start
	public Task getTaskById(Long id) {
		
		Task task = taskRep.findTaskById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task not found."));
		
		return task;
	}

	private UserDto getUser() {
		UserDto user = userService.getUserByUsername();
		return user;
	}
	
	public WorkspaceDto fetchWorkspace(Long workspaceId) {
	     try {
	    	 return workspaceService.getWorkspaceById(workspaceId);
	     } 
	     catch (FeignException.NotFound ex) {
	    	 throw new ResourceNotFoundException("Workspace not found.");
	     }
	}
	// helper methods ends

	//for creating task
	public void createTask(Long workspaceId, List<TaskRequest> taskRequests) {
		
		UserDto user = getUser();
		WorkspaceDto workspace = fetchWorkspace(workspaceId);
		
		List<Task> tasks = taskRequests.stream().map(taskRequest -> toEntity(taskRequest,workspace.getId(), user.getId()))
				.collect(Collectors.toList());

		taskRep.saveAll(tasks);
	}
	
	// for converting task
	private Task toEntity(TaskRequest request,Long workspaceId, Long createdBy) 
	{
		Task task = new Task();
		
		task.setWorkspaceId(workspaceId);
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setTaskType(TaskType.PARENT);
		task.setCreatedBy(createdBy);
		task.setStatus(Status.TODO);

		return task;
	}
	
	
	//for creating sub task 
	public void createSubTask(Long workspaceId, Long taskId, @Valid List<TaskRequest> subTaskRequests) {
		
		UserDto user = getUser();
		WorkspaceDto workspace = fetchWorkspace(workspaceId);
		Task parentTask = getTaskById(taskId);
		
		List<Task> tasks = subTaskRequests.stream().map(taskRequest -> toEntity(taskRequest,workspace.getId(),parentTask, user.getId()))
				.collect(Collectors.toList());

		taskRep.saveAll(tasks);
		
	}

	// for converting subtask
	private Task toEntity(TaskRequest request, Long workspaceId, Task parentTask, Long userId) {
		
		Task task = new Task();
		
		task.setWorkspaceId(workspaceId);
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setTaskType(TaskType.CHILD);
		task.setCreatedBy(userId);
		task.setStatus(Status.TODO);
		task.setParentTask(parentTask);
		
		return task;
	}

}
