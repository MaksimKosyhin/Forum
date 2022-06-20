package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.model.Comment;
import com.example.demo.model.Discussion;

import com.example.demo.repository.*;


@DataJpaTest 
class RepositoryTests {
	
	@Autowired
	private ThemeRepository themes;
	@Autowired
	private DiscussionRepository discussions;
	@Autowired
	private CommentRepository comments;
	
	@AfterEach
	public void reset() {
		themes.deleteAll();
		discussions.deleteAll();
		comments.deleteAll();
	}
	
	@Test
	public void checkDBRelationsWork() {
		//given
		Comment c1 = new Comment(Timestamp.from(Instant.now()), "first comment", "");
		Comment c2 = new Comment(Timestamp.from(Instant.now()), "second comment", "");
		
		Discussion d1 = new Discussion("first", c1);
		d1.getComments().add(c1);
		d1.getComments().add(c2);
		
		discussions.save(d1);
		comments.save(c1);
		comments.save(c2);
		
		Comment c3 = new Comment(Timestamp.from(Instant.now()), "third comment", "");
		
		Discussion d2 = new Discussion("second", c3);
		d2.getComments().add(c3);
		
		comments.save(c3);
		discussions.save(d2);
		
		//when
		List<Comment> list = discussions.findAll()
				.stream()
				.map(Discussion::getHeaderComment)
				.toList();
		
		//then
		assertThat(list).containsOnly(c1, c3);
	}
}
