package com.task_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@Configuration
public class JacksonConfig {
	
	@Bean
    public ObjectMapper objectMapper() {
		ObjectMapper mapper = JsonMapper.builder()
				   .addModule(new ParameterNamesModule())
				   .addModule(new Jdk8Module())
				   .addModule(new JavaTimeModule())
				   .build();
		return mapper;
    }
	
}
