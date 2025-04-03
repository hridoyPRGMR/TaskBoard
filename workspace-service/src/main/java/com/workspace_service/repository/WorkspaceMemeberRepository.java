package com.workspace_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workspace_service.entity.WorkspaceMember;

public interface WorkspaceMemeberRepository extends JpaRepository<WorkspaceMember, Long> {
	
	@Query("SELECT wm FROM WorkspaceMember wm WHERE wm.workspaceId = :workspaceId AND wm.invitedBy = :userId AND wm.status='ACCEPTED'")
    List<WorkspaceMember> findAllByWorkspaceIdAndUserId(@Param("workspaceId") Long workspaceId, @Param("userId") Long userId);

}

