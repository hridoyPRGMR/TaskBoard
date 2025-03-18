package com.workspace_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workspace_service.entity.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	boolean existsByInvitedEmailAndWorkspaceId(String email, Long workspaceId);

	Optional<Invitation> findByToken(String token);

}
