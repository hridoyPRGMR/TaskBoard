package com.workspace_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workspace_service.entity.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

}
