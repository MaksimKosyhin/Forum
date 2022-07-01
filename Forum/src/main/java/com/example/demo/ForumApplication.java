package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForumApplication {

	public static void main(String[] args) {
		//I'm not sure I correctly configured db properties
		//TODO: know what constructors/getters/setters are needed for model
		SpringApplication.run(ForumApplication.class, args);
	}

}
