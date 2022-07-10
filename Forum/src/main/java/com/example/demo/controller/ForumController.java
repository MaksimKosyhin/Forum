package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.CommentDTO;
import com.example.demo.model.DiscussionDTO;
import com.example.demo.model.Theme;
import com.example.demo.service.DBEntityService;

@Controller
public class ForumController {
	
	@Autowired
	private DBEntityService service;
	
	@GetMapping
	public String indexPage(Model model) {
		model.addAttribute("themes", service.getThemes());
		model.addAttribute("theme", new Theme());
		
		return "index";
	}
	
	@GetMapping("/themes/{themeId}")
	public String getThemeById(@PathVariable long themeId, Model model) {
		model.addAttribute("theme", service.getThemeById(themeId));
		model.addAttribute("discussion", new DiscussionDTO());
		
		return "theme";
	}
	
	@GetMapping("/discussions/{discussionId}")
	public String getDiscussionById(@PathVariable long discussionId, Model model) {
		model.addAttribute("discussion", service.getDiscussionById(discussionId));
		model.addAttribute("comments", service.getComments(discussionId));
		model.addAttribute("comment", new CommentDTO());
		
		return "discussion";
	}
	
	@PostMapping("/themes")
	public String createTheme(@ModelAttribute @Valid Theme theme) {
		service.addTheme(theme);
		return "redirect:/";
	}
	
	@PostMapping("/discussions")
	public String createDiscussion(@ModelAttribute @Valid DiscussionDTO discussion){
		service.addDiscussion(discussion);
		return "redirect:/themes/%d".formatted(discussion.getThemeId());
	}
	
	@PostMapping("/comments")
	public String createComment(@ModelAttribute @Valid CommentDTO comment) {
		service.replyToComment(comment);
		return "redirect:/discussions/%d".formatted(comment.getDiscussionId());
	}
	
	@DeleteMapping("/themes/{id}")
	public String deleteTheme(@PathVariable long themeId) {
		service.deleteTheme(themeId);
		return "redirect:/";
	}
	
	@DeleteMapping("/discussions/{themeId}/{discussionId}")
	public String deleteDiscussion(@PathVariable long themeId, @PathVariable long discussionId) {
		service.deleteDiscussion(discussionId);
		return "redirect:/themes/%d".formatted(themeId);
	}
	
	@DeleteMapping("/comments/{discussionId}/{commentId}")
	public String deleteComment(@PathVariable long discussionId, @PathVariable long commentId) {
		service.deleteComment(commentId);
		return "redirect/:discussions/%d".formatted(discussionId);
	}
}
