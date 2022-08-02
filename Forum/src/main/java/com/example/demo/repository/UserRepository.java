package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.WorkStaff;

@Repository
public interface UserRepository extends JpaRepository<WorkStaff, Long>{
	public Optional<WorkStaff> findByUsername(String username);
}
