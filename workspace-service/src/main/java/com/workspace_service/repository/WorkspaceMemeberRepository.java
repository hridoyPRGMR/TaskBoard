package com.workspace_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workspace_service.entity.WorkspaceMember;

public interface WorkspaceMemeberRepository extends JpaRepository<WorkspaceMember, Long> {

}
