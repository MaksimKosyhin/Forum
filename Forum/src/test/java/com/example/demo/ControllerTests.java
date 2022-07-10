package com.example.demo;

import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.controller.ForumController;
import com.example.demo.model.Comment;
import com.example.demo.model.Discussion;
import com.example.demo.model.Theme;
import com.example.demo.service.DBEntityService;

@WebMvcTest(ForumController.class)
public class ControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DBEntityService service;
	
	@Test
	public void shouldReturnThemesAndDefaultTheme() throws Exception {
		//given
		List<Theme> themes = List.of(new Theme("1"), new Theme("2"));
		
		//when
		when(service.getThemes()).thenReturn(themes);
		
		//then
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("theme", Matchers.anything()))
			.andExpect(MockMvcResultMatchers.model().attribute("themes", Matchers.is(themes)));
	}
	
	@Test
	public void shouldReturnThemeAndDefaultDiscussion() throws Exception {
		//given
		long id = 1;
		Theme theme = new Theme(-1, "test", new ArrayList<>());
		theme.getDiscussions().addAll(
				List.of(new Discussion("1", new Comment()),
						new Discussion("2", new Comment())));
		
		//when
		when(service.getThemeById(id)).thenReturn(theme);
		
		//then
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/themes/" + id))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("discussion", Matchers.anything()))
			.andExpect(MockMvcResultMatchers.model().attribute("theme", Matchers.is(theme)));
	}
	
	@Test
	public void shouldReturnDiscussionWithCommentsAndDefaultComment() throws Exception {
		//given
		long id = 1;
		Comment c1 = 
				new Comment(Timestamp.from(Instant.now()), "1", new ArrayList<>());
		Comment c2 = 
				new Comment(Timestamp.from(Instant.now()), "2", Collections.emptyList());
		c1.getReplies().add(c2);
		Comment c3 = 
				new Comment(Timestamp.from(Instant.now()), "3", Collections.emptyList());
		c1.getReplies().add(c3);
		
		Discussion d = new Discussion("test", c3);
		
		Map<Comment, List<Long>> map = Map.of(
				c1, Collections.emptyList(),
				c2, Collections.emptyList());
		
		//when
		when(service.getDiscussionById(id)).thenReturn(d);
		when(service.getComments(id)).thenReturn(map);
		
		//then
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/discussions/" + id))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("comment", Matchers.anything()))
			.andExpect(MockMvcResultMatchers.model().attribute("discussion", Matchers.is(d)))
			.andExpect(MockMvcResultMatchers.model().attribute("comments", Matchers.is(map)));
	}
}
