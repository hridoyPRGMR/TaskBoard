package com.workspace_service.helper;

import java.util.UUID;

public class TokenGenerator {
	
	public static String generateToken() {
		return UUID.randomUUID().toString();
	}
	
}
