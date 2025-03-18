package com.workspace_service.entity;

import java.time.LocalDateTime;

import com.workspace_service.enums.Status;
import com.workspace_service.enums.WorkspaceRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workspace_members")
public class WorkspaceMember {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long workspaceId;
	
	@Column(nullable = false)
	private Long userId;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private WorkspaceRole role;
	
	@Column(nullable = false)
	private Long invitedBy;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	
	@PrePersist
	private void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
	
}
