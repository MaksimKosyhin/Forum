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
		return "index";
	}
	
	@GetMapping("/themes/{id}")
	public String getThemeById(@PathVariable long id, Model model) {
		model.addAttribute("theme", service.getThemeById(id));
		return "theme";
	}
	
	@GetMapping("/discussions/{id}")
	public String getDiscussionById(@PathVariable long id, Model model) {
		model.addAttribute("comments", service.getComments(id));
		return "discussion";
	}
	
	@PostMapping("/themes")
	public String createTheme(@Valid @ModelAttribute Theme theme) {
		service.addTheme(theme);
		return "redirect:/themes";
	}
	
	@PostMapping("/discussions")
	public String createDiscussion(@Valid @ModelAttribute DiscussionDTO discussion) {
		service.addDiscussion(discussion.getThemeId(), discussion.getTitle(), discussion.getHeaderComment());
		return "redirect:/themes/%d".formatted(discussion.getThemeId());
	}
	
	@PostMapping("/comments")
	public String createComment(@Valid @ModelAttribute CommentDTO comment) {
		service.addCommentReply(comment);
		return "redirect:/discussions/%d".formatted(comment.getDiscussionId());
	}
	
	@DeleteMapping("/themes/{id}")
	public String deleteTheme(@PathVariable long id) {
		service.deleteTheme(id);
		return "redirect:/index";
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
