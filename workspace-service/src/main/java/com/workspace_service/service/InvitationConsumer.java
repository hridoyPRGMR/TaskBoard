package com.workspace_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.workspace_service.dto.InvitationMessage;
import com.workspace_service.entity.Invitation;
import com.workspace_service.entity.Workspace;
import com.workspace_service.enums.Status;
import com.workspace_service.exception.EmailSendingException;
import com.workspace_service.helper.TokenGenerator;
import com.workspace_service.repository.InvitationRepository;
import com.workspace_service.repository.WorkspaceRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class InvitationConsumer {

	private final WorkspaceRepository workspaceRep;
	private final InvitationRepository invitationRep;
	private final EmailService emailService;

	@KafkaListener(topics = "workspace-invitations", groupId = "invitation-group", containerFactory = "kafkaListenerContainerFactory")
	public void listen(InvitationMessage invitationMessage) {
		try {

			Workspace workspace = workspaceRep.findById(invitationMessage.getWorkspaceId())
			        .orElseThrow(() -> new IllegalArgumentException(
			                "Workspace not found with ID: " + invitationMessage.getWorkspaceId()));

			// generate token & create invitation
			String token = TokenGenerator.generateToken();
			
			Invitation invitation = new Invitation();
			invitation.setWorkspaceId(invitationMessage.getWorkspaceId());
			invitation.setToken(token);
			invitation.setInvitedEmail(invitationMessage.getEmail());
			invitation.setStatus(Status.PENDING);
			invitation.setInvitedBy(invitationMessage.getInvitedBy().getId());
			
			//check 
			boolean invitationExist = invitationRep.existsByInvitedEmailAndWorkspaceId(invitation.getInvitedEmail(), invitation.getWorkspaceId());
			
			if(!invitationExist) {
				invitationRep.save(invitation);
			}
			
			sendInvitationEmail(invitationMessage);
			
		} catch (Exception e) {
			log.error("Error processing invitation message: {}", e.getMessage());
			throw e;
		}
	}
	
	private void sendInvitationEmail(InvitationMessage invitationMessage) {
	    try {
	        String subject = "You're invited to join a workspace!";
	        String body = "Hello,\n\nYou have been invited by " + invitationMessage.getInvitedBy().getEmail()
	                + " to join workspace.\nPlease log in and check the 'Invitations' section:\nVisit: taskboard.com\n\nThank you!";

	        emailService.sendInvitationEmail(invitationMessage.getEmail(), subject, body);
	    } catch (EmailSendingException ex) {
	        log.error("Email sending failed: {}", ex.getMessage(), ex);
	    }
	}

}
