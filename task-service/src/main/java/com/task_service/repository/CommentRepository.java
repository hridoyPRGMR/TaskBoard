package com.task_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task_service.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
