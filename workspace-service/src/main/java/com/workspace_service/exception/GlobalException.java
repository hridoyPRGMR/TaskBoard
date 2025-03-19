package com.workspace_service.exception;

import java.util.stream.Collectors;

import org.apache.kafka.common.config.ConfigException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.workspace_service.helper.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> validationException(MethodArgumentNotValidException ex){
		
		String errorMessage = ex.getBindingResult()
								.getAllErrors()
								.stream()
								.map(ObjectError::getDefaultMessage)
								.collect(Collectors.joining(","));
		
		return new ResponseEntity<>(ApiResponse.failure(errorMessage), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(ConfigException.class)
	public ResponseEntity<ApiResponse<?>> kafkaException(ConfigException ex){
		String errorMessage = "Internal server error.";
		
		log.info("Kafka: ConfigException: {}",ex.getMessage(),ex);
		
		return new ResponseEntity<>(ApiResponse.failure(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(DuplicateInviteException.class)
	public ResponseEntity<ApiResponse<?>> runTimeException(DuplicateInviteException ex){
		String errorMessage = ex.getMessage();
		return new ResponseEntity<>(ApiResponse.failure(errorMessage), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> runTimeException(ResourceNotFoundException ex){
		String errorMessage = ex.getMessage();
		return new ResponseEntity<>(ApiResponse.failure(errorMessage), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmailSendingException.class)
	public ResponseEntity<ApiResponse<?>> runTimeException(EmailSendingException ex){
		String errorMessage = ex.getMessage();
		return new ResponseEntity<>(ApiResponse.failure(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
