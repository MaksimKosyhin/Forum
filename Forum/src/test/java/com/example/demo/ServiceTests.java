package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
	public void canAddDiscussion() {
		//given
		long id = 1;
		Theme theme = new Theme();
		given(themes.findById(id)).willReturn(Optional.of(theme));
		
		String title = "test";
		Comment c = new Comment();
		Discussion d = new Discussion(title, c);
		
		//when
		service.addDiscussion(id, title, c);
		
		//then
		verify(comments).save(c);
		verify(discussions).save(d);
		assertThat(theme.getDiscussions()).containsOnly(d);
	}
	
	@Test
	public void canAddCommentReply() {
		//given
		long id = 1;
		CommentDTO c1 = new CommentDTO(-1, id, null, "test1", "");
		Comment c2 = new Comment(null, "test2", "");
		Comment c3 = new Comment(null, "test1", "");
		
		given(comments.findById(id)).willReturn(Optional.of(c2));
		given(mapper.map(c1, Comment.class)).willReturn(c3);
		//when
		service.addCommentReply(c1);
		
		//then
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
		//given
		Map<Comment, List<Long>> map1 = 
				new TreeMap<>((c1, c2) -> c1.getTs().compareTo(c2.getTs()));
		
		Comment c1 = new Comment(Timestamp.valueOf(LocalDateTime.of(2000, 1, 1, 1, 1)), "test1", null); 
		c1.setId(1L);
		Comment c2 = new Comment(Timestamp.valueOf(LocalDateTime.of(2001, 1, 1, 1, 1)), "test2", null); 
		c2.setId(2L);
		Comment c3 = new Comment(Timestamp.valueOf(LocalDateTime.of(2002, 1, 1, 1, 1)), "test3", null); 
		c3.setId(3L);
		Comment c4 = new Comment(Timestamp.valueOf(LocalDateTime.of(2004, 1, 1, 1, 1)), "test4", null); 
		c4.setId(4L);
		Comment c5 = new Comment(Timestamp.valueOf(LocalDateTime.of(2003, 1, 1, 1, 1)), "test5", null); 
		c5.setId(5L);
		
		map1.putAll(Map.of( c2, List.of(1L),
							c3, List.of(1L),
							c4, List.of(2L),
							c5, List.of(3L, 2L) 
						));
		
		long id = 1;
		Discussion d = new Discussion("test", c1);
		c1.getReplies().add(c2);
		c1.getReplies().add(c3);
		c2.getReplies().addAll(List.of(c4, c5));
		c3.getReplies().add(c5);
		
		given(discussions.findById(id)).willReturn(Optional.of(d));
		
		//when
		Map<Comment, List<Long>> map2 = service.getComments(id);
		
		//then
		assertThat(map2).isEqualTo(map1);
	}
}
