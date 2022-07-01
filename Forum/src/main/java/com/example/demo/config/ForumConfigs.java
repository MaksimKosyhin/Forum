package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;

@Configuration
public class ForumConfigs {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper result = new ModelMapper();
		
		TypeMap<CommentDTO, Comment> propertyMapper = 
				result.createTypeMap(CommentDTO.class, Comment.class);
		propertyMapper.addMappings(mapper -> mapper.skip(Comment::setId));
		
		return result;
	}
}
