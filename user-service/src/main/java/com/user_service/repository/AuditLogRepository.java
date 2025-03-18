package com.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user_service.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

}
