package com.api_gateway.helper;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SignupForm {
	
	@NotNull
	@Size(min=2,max=50)
	private String name;
	
	@Email
	@NotNull
	private String email;
	
	@NotNull
	@Size(min=3,max=50)
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
