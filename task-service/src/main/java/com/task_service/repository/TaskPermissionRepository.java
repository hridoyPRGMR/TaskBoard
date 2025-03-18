package com.task_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task_service.entity.TaskPermission;

public interface TaskPermissionRepository extends JpaRepository<TaskPermission, Long>{

}
