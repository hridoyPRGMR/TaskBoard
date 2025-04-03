package com.task_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "task_permisions")
public class TaskPermission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long taskId;

	@Column(nullable = false)
	private Long userId;

	private Boolean canEdit = false;

	private Boolean canDelete = false;

	private Boolean canComment = false;

	@Column(updatable = false)
	private LocalDateTime createdAt;

	public TaskPermission(Long taskId, Long userId, Boolean canEdit, Boolean canDelete, Boolean canComment) {
		this.taskId = taskId;
		this.userId = userId;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
		this.canComment = canComment;
	}

	@PrePersist
	private void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (this.canEdit == null)
			this.canEdit = false;
		if (this.canDelete == null)
			this.canDelete = false;
		if (this.canComment == null)
			this.canComment = false;
	}

}
