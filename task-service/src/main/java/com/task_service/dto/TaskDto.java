package com.task_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.task_service.entity.Task;
import com.task_service.enums.Status;
import com.task_service.enums.TaskType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskDto {
	
	
	private Long id;
    private Long workspaceId;
    private Long createdBy;
    private Long assignedTo;
    private String title;
    private String description;
    private Status status;
    private TaskType taskType;
    private LocalDateTime dueDate;
    private List<TaskDto> subTasks; 
    
    public TaskDto(Task task) {
        this.id = task.getId();
        this.workspaceId = task.getWorkspaceId();
        this.createdBy = task.getCreatedBy();
        this.assignedTo = task.getAssignedTo();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.taskType = task.getTaskType();
        this.dueDate = task.getDueDate();
        this.subTasks = task.getSubTasks().stream()
                            .map(TaskDto::new) // Recursively map subtasks
                            .collect(Collectors.toList());
    }
}
