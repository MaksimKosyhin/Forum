package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForumApplication {

	public static void main(String[] args) {
		// there's a bug with autoclosing jvm before database initialization
		//It appears only when I set up spring.datasource.url property
		//Is resolved by when added rest dependency (mabe because jvm runs constantly this way)
		
		//I'm not sure I correctly configured db properties
		//TODO: set relations between jpa classes
		//TODO: check if I can set File type for img_location field in Comments
		//TODO: know what constructors/getters/setters are needed for model
		SpringApplication.run(ForumApplication.class, args);
	}

}
