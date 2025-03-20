package com.workspace_service.entity;

import java.time.LocalDateTime;

import com.workspace_service.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//bkxa zvqy tdtc vtot

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invitations",uniqueConstraints= {
	@UniqueConstraint(columnNames= {"workspace_id","invited_email"})
})
public class Invitation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long workspaceId;
	
	private String invitedEmail;
	
	@Column(nullable = false)
	private Long invitedBy;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(nullable=false,unique=true)
	private String token;
	
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	private void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
