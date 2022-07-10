package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;
import com.example.demo.model.Discussion;
import com.example.demo.model.DiscussionDTO;

@SpringBootTest
public class ModelMapperTests {

	@Autowired
	private ModelMapper modelMapper;
	
	@Test
	public void itMapsDiscussionFromDTO() {
		DiscussionDTO d1 = new DiscussionDTO(-1, "title", "comment");
		
		Discussion d2 = modelMapper.map(d1, Discussion.class);
		
		assertThat(d2.getTitle()).isEqualTo(d1.getTitle());
		assertThat(d2.getHeaderComment().getMsg()).isEqualTo(d1.getMsg());
		assertThat(d2.getHeaderComment().getDateCreated()).isNotNull();
	}
	
	@Test
	public void itMapsMessageFromDTO() {
		CommentDTO m1 = new CommentDTO(-1, null, "comment");
		
		Comment m2 = modelMapper.map(m1, Comment.class);
		
		assertThat(m2.getMsg()).isEqualTo(m1.getMsg());
		assertThat(m2.getDateCreated()).isNotNull();
	}
}
