package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.HeaderComment;

@Repository
public interface HeaderCommentRepository extends CrudRepository<HeaderComment, Long>{

}
