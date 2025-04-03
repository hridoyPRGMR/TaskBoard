package com.task_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.task_service.dto.AssignUserRequest;
import com.task_service.dto.TaskDto;
import com.task_service.dto.TaskRequest;
import com.task_service.dto.UserDto;
import com.task_service.dto.WorkspaceDto;
import com.task_service.entity.Task;
import com.task_service.entity.TaskPermission;
import com.task_service.enums.Status;
import com.task_service.enums.TaskType;
import com.task_service.exception.ResourceNotFoundException;
import com.task_service.external.UserService;
import com.task_service.external.WorkspaceService;
import com.task_service.repository.TaskPermissionRepository;
import com.task_service.repository.TaskRepository;

import feign.FeignException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TaskService {

	private final TaskRepository taskRep;
	private final UserService userService;
	private final WorkspaceService workspaceService;
	private final TaskPermissionRepository taskPermissionRep;
	
	// helper methods start
	public Task getParentTaskById(Long id) {
		Task task = taskRep.findTaskById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found."));
		return task;
	}
	
	public Task getTask(Long id) {
		return taskRep.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found."));
	}

	private UserDto getUser() {
		UserDto user = userService.getUserByUsername();
		return user;
	}

	public WorkspaceDto fetchWorkspace(Long workspaceId) {
		try {
			return workspaceService.getWorkspaceById(workspaceId);
		} catch (FeignException.NotFound ex) {
			throw new ResourceNotFoundException("Workspace not found.");
		}
	}
	// helper methods ends

//	for creating task
//	@CacheEvict(value = "workspaceTasks",key="#workspaceId")
	public Task createTask(Long workspaceId, TaskRequest taskRequest) {
		UserDto user = getUser();
		WorkspaceDto workspace = fetchWorkspace(workspaceId);
		Task task = toEntity(taskRequest, workspace.getId(), user.getId());
		
		return taskRep.save(task);
	}

	// for creating sub task
	public Task createSubTask(Long workspaceId, Long taskId,TaskRequest subTaskRequest) {
		UserDto user = getUser();
		WorkspaceDto workspace = fetchWorkspace(workspaceId);
		Task parentTask = getParentTaskById(taskId);
		Task subTask = toEntity(subTaskRequest, workspace.getId(), parentTask, user.getId());
		return taskRep.save(subTask);
	}

//	@Cacheable(value= "workspaceTasks" , key="#workspaceId")
	public List<TaskDto> getTasks(Long workspaceId) {
		List<Task> tasks = taskRep.findAllByWorkspaceId(workspaceId);
        
		return tasks.stream()
                    .map(TaskDto::new) // Convert to DTO
                    .collect(Collectors.toList());
	}
	
	@Transactional
	public Task assignUser(Long taskId, AssignUserRequest request) {
		
		Task task = taskRep.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found."));
		task.setAssignedTo(request.getUserId());

		task.getSubTasks()
			.forEach(subtask->subtask.setAssignedTo(request.getUserId()));
		taskRep.save(task);
		
		TaskPermission taskPermission = new TaskPermission(
			taskId,
			request.getUserId(),
			request.getCanEdit(),
			request.getCanDelete(),
			request.getCanComment()
		); 
		taskPermissionRep.save(taskPermission);
		
		return task;
	}
	
	public void updateTaskStatus(Long taskId, Status status) {
	    Task task = getTask(taskId);
	    
	    if (task.getStatus() == status) {
	        return;
	    }

	    if (task.getTaskType() == TaskType.PARENT && task.getSubTasks() != null && status == Status.DONE) {
	        task.getSubTasks().forEach(subTask -> subTask.setStatus(status));
	        taskRep.saveAll(task.getSubTasks());
	    }

	    task.setStatus(status);
	    taskRep.save(task);
	}
	
	public void deleteTask(Long taskId) {
		Task task = getTask(taskId);
		taskRep.deleteById(taskId);
	}

	
	// for converting task
	private Task toEntity(TaskRequest request, Long workspaceId, Long createdBy) {
		Task task = new Task();

		task.setWorkspaceId(workspaceId);
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setTaskType(TaskType.PARENT);
		task.setCreatedBy(createdBy);
		task.setStatus(Status.TODO);

		return task;
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
