package com.workspace_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic workspaceInvitationTopic() {
        return new NewTopic("workspace-invitations", 1, (short) 1)
                .configs(Map.of("cleanup.policy", "compact")); // Enables log compaction
    }
}
