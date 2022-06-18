package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Discussion;

@Repository 
public interface DiscussionRepository extends CrudRepository<Discussion, Long>{

}
