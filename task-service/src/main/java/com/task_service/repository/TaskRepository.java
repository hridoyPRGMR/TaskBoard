package com.task_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.task_service.dto.TaskDto;
import com.task_service.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

	@Query("SELECT t FROM Task t WHERE t.id = :id AND t.taskType = 'PARENT'")
	Optional<Task> findTaskById(@Param("id") Long id);
	
	@Query("SELECT new com.task_service.dto.TaskDto(" +
            "t.id, " +
            "t.title, " +
            "t.description, " +
            "t.status, " +
            "t.taskType) " +
        "FROM Task t WHERE t.workspaceId = :workspaceId AND t.taskType = 'PARENT'")
	List<TaskDto> findAllByWorkspaceId(@Param("workspaceId") Long workspaceId);

}
