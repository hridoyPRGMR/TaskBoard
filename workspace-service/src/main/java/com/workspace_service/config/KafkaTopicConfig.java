package com.workspace_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
	
	@Bean
	NewTopic workspaceInvitationTopic() {
		return new NewTopic("workspace-invitations",1,(short) 1);
	}
	
}
