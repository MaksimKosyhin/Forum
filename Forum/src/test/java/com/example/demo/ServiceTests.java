package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;
import com.example.demo.model.Discussion;
import com.example.demo.model.DiscussionDTO;
import com.example.demo.model.Theme;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.DiscussionRepository;
import com.example.demo.repository.ThemeRepository;
import com.example.demo.service.DBEntityServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
	
	@Mock private ThemeRepository themes;
	@Mock private DiscussionRepository discussions;
	@Mock private CommentRepository comments;
	
	@Mock private ModelMapper mapper;
	
	@InjectMocks
	@Autowired
	private DBEntityServiceImpl service;
	
	@Test
	public void canGetThemes() {
		//when
		service.getThemes();
		//then
		verify(themes).findAll();
	}
	
	@Test
	public void canGetThemeById() {
		//given 
		long id = 1;
		Theme theme1 = new Theme();
		given(themes.findById(id)).willReturn(Optional.of(theme1));

		//when
		Theme theme2 = service.getThemeById(id);
		
		//then
		assertThat(theme1).isEqualTo(theme2);
		
	}
	
	@Test
	public void canGetDiscussionById() {
		//given 
		long id = 1;
		Discussion d1 = new Discussion();
		given(discussions.findById(id)).willReturn(Optional.of(d1));

		//when
		Discussion d2 = service.getDiscussionById(id);
				
		//then
		assertThat(d1).isEqualTo(d2);
	}
	
	@Test
	public void canAddTheme() {
		//given
		Theme theme = new Theme();
		
		//when
		service.addTheme(theme);
		
		//then
		verify(themes).save(theme);
	}
	
	@Test
	public void canAddDiscussion(){
		//given
		long id = 1;
		Theme theme = new Theme(-1, null, new ArrayList<>());
		
		String title = "test";
		Comment c = new Comment();
		Discussion d1 = new Discussion(title, c);
		DiscussionDTO d2 = new DiscussionDTO(id, title, "");
		
		given(themes.findById(id)).willReturn(Optional.of(theme));
		given(mapper.map(d2, Discussion.class)).willReturn(d1);
		//when
		service.addDiscussion(d2);
		
		//then
		verify(comments).save(c);
		verify(discussions).save(d1);
		verify(themes).save(theme);
		assertThat(theme.getDiscussions()).containsOnly(d1);
	}
	
	@Test
	public void canAddReplyToComment() {
		//given
		long id = 1;
		CommentDTO c1 = new CommentDTO(-1, "" + id, "test1");
		Comment c2 = new Comment(null, "test2", new ArrayList<>());
		c2.setId(1);
		Comment c3 = new Comment(null, "test1", new ArrayList<>());
		c3.setId(2);
		
		given(comments.findById(id)).willReturn(Optional.of(c2));
		given(mapper.map(c1, Comment.class)).willReturn(c3);
		//when
		service.replyToComment(c1);
		
		//then
		verify(comments).save(c2);
		verify(comments).save(c3);
		assertThat(c2.getReplies()).containsOnly(c3);
	}
	
	@Test
	public void canDeleteTheme() {
		//given
		long id = 1;
		given(themes.existsById(id)).willReturn(true);
		
		//when
		doNothing().when(themes).deleteById(id);
		service.deleteTheme(id);
		
		//then
		verify(themes, times(1)).deleteById(id);
	}
	
	@Test
	public void canDeleteDiscussion() {
		//given
		long id = 1;
		given(discussions.existsById(id)).willReturn(true);
		
		//when
		doNothing().when(discussions).deleteById(id);
		service.deleteDiscussion(id);
		
		//then
		verify(discussions, times(1)).deleteById(id);
	}
	
	@Test
	public void canDeleteComment() {
		//given
		long id = 1;
		given(comments.existsById(id)).willReturn(true);
		
		//when
		doNothing().when(comments).deleteById(id);
		service.deleteComment(id);
		
		//then
		verify(comments, times(1)).deleteById(id);
	}
	
	@Test
	public void canGetComments() {
		Comment c1 = new Comment(null, "q", new ArrayList<>());
		c1.setId(1);
		
		Comment c2 = new Comment(null, "qqq", new ArrayList<>());
		c2.setId(2);
		c1.getReplies().add(c2);
		
		Comment c3 = new Comment(null, "www", new ArrayList<>());
		c3.setId(3);
		c1.getReplies().add(c3);
		
		Discussion d = new Discussion("q", c1);
		
		Map<Comment, List<Long>> map1 = 
				new TreeMap<>((e1, e2) -> Long.compare(e1.getId(), e2.getId()));
		
		map1.putAll(Map.of(
			c2, List.of(1L),
			c3, List.of(1L)
		));
		
		long id = 1;
		given(discussions.findById(id)).willReturn(Optional.of(d));
		
		//when
		Map<Comment, List<Long>> map2 = service.getComments(id);
		
		//then
		assertThat(map2).isEqualTo(map1);
		
	}
}
