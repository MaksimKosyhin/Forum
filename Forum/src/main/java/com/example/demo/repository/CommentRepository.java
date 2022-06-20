package com.example.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comment;

@Repository 
public interface CommentRepository extends  PagingAndSortingRepository<Comment, Long>{
	
}
