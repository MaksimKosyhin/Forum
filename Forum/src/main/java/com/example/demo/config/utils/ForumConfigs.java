package com.example.demo.config.utils;

import java.sql.Timestamp;
import java.time.Instant;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;
import com.example.demo.model.Discussion;
import com.example.demo.model.DiscussionDTO;

@Configuration
public class ForumConfigs {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		TypeMap<CommentDTO, Comment> commentMapper = 
				modelMapper.createTypeMap(CommentDTO.class, Comment.class);
		
		commentMapper.addMappings(mapper -> mapper.skip(Comment::setId));
		commentMapper.addMapping(src -> Timestamp.from(Instant.now()), Comment::setDateCreateed);
		
		
		TypeMap<DiscussionDTO, Discussion> discussionMapper =
				modelMapper.createTypeMap(DiscussionDTO.class, Discussion.class);
		
		Converter<String, Comment> msgToComment = 
				msg -> new Comment(Timestamp.from(Instant.now()), msg.getSource());
		
		discussionMapper.addMappings(
				mapper -> mapper
					.using(msgToComment)
					.map(DiscussionDTO::getMsg, Discussion::setHeaderComment));
		
		return modelMapper;
	}
}
