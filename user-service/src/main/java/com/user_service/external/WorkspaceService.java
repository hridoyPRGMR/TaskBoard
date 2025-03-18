package com.user_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.user_service.entity.Workspace;

@FeignClient(name="WORKSPACESERVICE")
public interface WorkspaceService {
	
	@GetMapping("/workspace")
	Workspace getWorkSpace();
	
}
