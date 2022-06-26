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
		//TODO: know what constructors/getters/setters are needed for model
		//TODO: refactor discussion class
		//TODO: check db entries are stored in order by id
		SpringApplication.run(ForumApplication.class, args);
	}

}
