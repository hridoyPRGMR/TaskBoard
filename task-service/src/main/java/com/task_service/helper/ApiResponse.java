package com.task_service.helper;

public class ApiResponse<T> {

	private boolean success;
	private String message;
	private T data;

	private ApiResponse() {
	}

	public ApiResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ApiResponse(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}

	public static <T> ApiResponse<T> failure(String message) {
		return new ApiResponse<>(false, message);
	}
	
	public static <T> ApiResponse<T> failure(String message, T data) {
		return new ApiResponse<>(false, message, data);
	}

	public static <T> ApiResponse<T> response(boolean success, String message, T data) {
        return new ApiResponse<>(success, message, data);
    }

}
