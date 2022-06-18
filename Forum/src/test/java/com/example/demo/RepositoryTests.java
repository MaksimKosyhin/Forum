package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.model.Comment;
import com.example.demo.model.Discussion;
import com.example.demo.model.HeaderComment;
import com.example.demo.repository.*;

@DataJpaTest 
class RepositoryTests {
	
	@Autowired
	private ThemeRepository themes;
	@Autowired
	private DiscussionRepository discussions;
	@Autowired
	private CommentRepository comments;
	@Autowired
	private HeaderCommentRepository headerComments;
	
	@AfterEach
	public void reset() {
		themes.deleteAll();
		discussions.deleteAll();
		comments.deleteAll();
		headerComments.deleteAll();
	}
	
	@Test
	public void checkDBRelationsWorkAsIntentioned() {
		//given
		Discussion d1 = new Discussion("first");
		
		Comment c1 = new Comment(Timestamp.from(Instant.now()), "first comment", "");
		d1.getComments().add(c1);
		
		Comment c2 = new Comment(Timestamp.from(Instant.now()), "second comment", "");
		d1.getComments().add(c2);
		
		discussions.save(d1);
		comments.save(c1);
		comments.save(c2);
		headerComments.save(new HeaderComment(c1));
		
		Discussion d2 = new Discussion("second");
		
		Comment c3 = new Comment(Timestamp.from(Instant.now()), "third comment", "");
		d2.getComments().add(c3);
		comments.save(c3);
		
		discussions.save(d2);
		headerComments.save(new HeaderComment(c3));
		
		//when
		Iterable<HeaderComment> i = headerComments.findAll();
		Set<Comment> set = new HashSet<>();
		for(HeaderComment e: i) {
			set.add(e.getComment());
		}
		
		//then
		assertThat(set).containsOnly(c1, c3);
	}
}
