package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.model.Comment;
import com.example.demo.model.Discussion;
import com.example.demo.model.Theme;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.DiscussionRepository;
import com.example.demo.repository.ThemeRepository;
import com.example.demo.service.DBEntityService;
import com.example.demo.service.DBEntityServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
	
	@Mock private ThemeRepository themes;
	@Mock private DiscussionRepository discussions;
	@Mock private CommentRepository comments;
	
	private DBEntityServiceImpl service;
	
	@BeforeEach
    void setUp() {
        service = new DBEntityServiceImpl(themes, discussions, comments);
    }
	
	@AfterEach
	public void tearDown() {
		themes.deleteAll();
		discussions.deleteAll();
		comments.deleteAll();
	}
	
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
		Comment c1 = new Comment();
		Comment c2 = new Comment();
		
		given(comments.findById(id)).willReturn(Optional.of(c1));
		
		//when
		service.addCommentReply(id, c2);
		
		//then
		verify(comments).save(c2);
		assertThat(c1.getComments()).containsOnly(c2);
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
		
		map1.putAll(Map.of(c1, Collections.emptyList(),
							c2, List.of(1L),
							c3, List.of(1L),
							c4, List.of(2L),
							c5, List.of(3L, 2L) 
						));
		
		long id = 1;
		Discussion d = new Discussion("test", c1);
		c1.getComments().add(c2);
		c1.getComments().add(c3);
		c2.getComments().addAll(List.of(c4, c5));
		c3.getComments().add(c5);
		
		given(discussions.findById(id)).willReturn(Optional.of(d));
		//when
		Map<Comment, List<Long>> map2 = service.getComments(id);
		
		//then
		assertThat(map2.entrySet()).isEqualTo(map1.entrySet());
	}
}
