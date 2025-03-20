package com.workspace_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.workspace_service.dto.InvitationMessage;
import com.workspace_service.exception.DuplicateInviteException;
import com.workspace_service.repository.InvitationRepository;

@Service
public class InvitationService {
	
	private final InvitationRepository invitationRep;
	
	private final KafkaTemplate<String, Object> kafkaTemplate;

    public InvitationService(KafkaTemplate<String, Object> kafkaTemplate,InvitationRepository invitationRep) {
        this.kafkaTemplate = kafkaTemplate;
        this.invitationRep =invitationRep;
    }

    public void sendInvitation(InvitationMessage invitation) {
    	
    	boolean alreadyInvited = invitationRep.existsByInvitedEmailAndWorkspaceId(invitation.getEmail(),
    			invitation.getWorkspaceId());
    	
    	if (alreadyInvited) {
            throw new DuplicateInviteException("User " + invitation.getEmail() +
                " already invited to this workspace.");
        }
    	
    	String key = invitation.getEmail() + "_" + invitation.getWorkspaceId();
    	
        kafkaTemplate.send("workspace-invitations",key, invitation);
    }
}
