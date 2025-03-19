package com.workspace_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workspace_service.dto.WorkspaceDto;
import com.workspace_service.entity.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

	@Query("SELECT new com.workspace_service.dto.WorkspaceDto(w.id, w.name) FROM Workspace w WHERE w.id = :id AND w.ownerId = :ownerId")
	Optional<WorkspaceDto>  findWorkspaceDtoByIdAndOwnerId(@Param("id") Long id, @Param("ownerId") Long ownerId);
}
