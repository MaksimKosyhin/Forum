package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.demo.config.exceptions.DBEntryNotFoundException;
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
	
	private String getErrorPage(BindingResult bindingResult, Model model) {
		List<String> errors = bindingResult.getAllErrors()
				.stream()
				.map(error -> error.getDefaultMessage())
				.toList();
		
		model.addAttribute("errors", errors);
		return "error";
	}
	
	@PostMapping("/themes")
	public String createTheme(@ModelAttribute @Valid Theme theme, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return this.getErrorPage(bindingResult, model);
		} else {
			service.addTheme(theme);
			return "redirect:/";
		}
	}
	
	@PostMapping("/discussions")
	public String createDiscussion(@ModelAttribute @Valid DiscussionDTO discussion, BindingResult bindingResult, Model model){
		if(bindingResult.hasErrors()) {
			return this.getErrorPage(bindingResult, model);
		} else {
			service.addDiscussion(discussion);
			return "redirect:/themes/%d".formatted(discussion.getThemeId());
		}
	}
	
	@PostMapping("/comments")
	public String createComment(@ModelAttribute @Valid CommentDTO comment, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return this.getErrorPage(bindingResult, model);
		} else {
			
			try {
				service.replyToComment(comment);
			} catch(DBEntryNotFoundException ex) {
				model.addAttribute("errors", List.of(ex.getMessage()));
				return "error";
			}
			
			return "redirect:/discussions/%d".formatted(comment.getDiscussionId());
		}
	}
	
	@PutMapping("themes/{themeId}")
	public String updateTheme(@PathVariable long themeId) {
		service.switchThemeClosing(themeId);
		return "redirect:/themes/%d".formatted(themeId);
	}
	
	@PutMapping("/discussions/{themeId}/{discussionId}")
	public String updateDiscussion(@PathVariable long themeId, @PathVariable long discussionId) {
		service.switchDiscussionClosing(discussionId);
		return "redirect:/themes/%d".formatted(themeId);
	}
	
	@PutMapping("/comments/{discussionId}/{commentId}")
	public String updateComment(@PathVariable long discussionId, @PathVariable long commentId) {
		service.switchCommentClosing(commentId);
		return "redirect/:discussions/%d".formatted(discussionId);
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
