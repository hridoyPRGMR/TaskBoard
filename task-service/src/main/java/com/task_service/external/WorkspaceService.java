package com.task_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.task_service.dto.WorkspaceDto;

@FeignClient(name="WORKSPACESERVICE")
public interface WorkspaceService {
	
	@GetMapping("/workspace/{workspaceId}")
	WorkspaceDto getWorkspaceById(@PathVariable(name="workspaceId",required=true)Long workspaceId);
}
