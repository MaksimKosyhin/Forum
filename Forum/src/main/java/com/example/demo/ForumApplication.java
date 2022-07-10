package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForumApplication {
	public static void main(String[] args) {
		//TODO: refactor models' constructor/getters/setters
		//TODO: create annotation for comments field, check for id existing in discussion
			//cannot add the same id
		SpringApplication.run(ForumApplication.class, args);
	}

}
