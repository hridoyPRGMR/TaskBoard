package com.task_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.task_service.helper.ApiResponse;

@ControllerAdvice
public class GlobalException {
	
	 @ExceptionHandler(ResourceNotFoundException.class)
	 public ResponseEntity<?> notFoundException(ResourceNotFoundException ex) {
		 String errorMessage = ex.getMessage();
		 return new ResponseEntity<>(ApiResponse.failure(errorMessage), HttpStatus.NOT_FOUND);
	 }
}
