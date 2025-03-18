package com.task_service.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.task_service.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long workspaceId;
	
	@ManyToOne
	@JoinColumn(name = "parent_task_id")
	private Task parentTask;
	
	@OneToMany(mappedBy = "parentTask" , cascade = CascadeType.ALL)
	private List<Task> subTasks = new ArrayList<>();
	
	@Column(nullable = false)
	private Long createdBy;
	
	@Column(nullable = false)
	private Long assignedTo;
	
	@Column(nullable = false)
	private String title;
	
	@Column(length = 1000)
	private String description;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	private LocalDateTime dueDate;
	
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	@PrePersist
	private void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
	
	@PreUpdate
	private void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
