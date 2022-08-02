package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.demo.model.CommentDTO;
import com.example.demo.model.DiscussionDTO;
import com.example.demo.model.Theme;
import com.example.demo.model.WorkStaff;
import com.example.demo.service.DBEntityService;
import com.example.demo.service.UserDetailsServiceImpl;
import com.example.demo.util.exception.DBEntryNotFoundException;

@Controller
public class ForumController {
	
	private final DBEntityService service;
	private final UserDetailsServiceImpl userDetails;
	
	@Autowired
	public ForumController(DBEntityService service, UserDetailsServiceImpl userDetails) {
		this.service = service;
		this.userDetails = userDetails;
	}

	@GetMapping
	public String indexPage(Model model) {
		model.addAttribute("themes", service.getThemes());
		model.addAttribute("theme", new Theme());
		
		return "index";
	}
	
	@PostMapping("/themes")
	@PreAuthorize("hasAuthority('CREATE_THEME')")
	public String createTheme(@ModelAttribute @Valid Theme theme, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return this.getErrorPage(bindingResult, model);
		} else {
			service.addTheme(theme);
			return "redirect:/";
		}
	}
	
	@PutMapping("themes/{themeId}")
	@PreAuthorize("hasAuthority('CLOSE_THEME')")
	public String updateTheme(@PathVariable long themeId) {
		service.switchThemeClosing(themeId);
		return "redirect:/";
	}
	
	@DeleteMapping("/themes/{themeId}")
	@PreAuthorize("hasAuthority('DELETE_THEME')")
	public String deleteTheme(@PathVariable long themeId) {
		service.deleteTheme(themeId);
		return "redirect:/";
	}
	
	@GetMapping("/themes/{themeId}")
	public String getThemeById(@PathVariable long themeId, Model model) {
		model.addAttribute("theme", service.getThemeById(themeId));
		model.addAttribute("discussion", new DiscussionDTO());
		
		return "theme";
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
	
	@PutMapping("/discussions/{themeId}/{discussionId}")
	@PreAuthorize("hasAuthority('CLOSE_DISCUSSION')")
	public String updateDiscussion(@PathVariable long themeId, @PathVariable long discussionId) {
		service.switchDiscussionClosing(discussionId);
		return "redirect:/themes/%d".formatted(themeId);
	}
	
	@DeleteMapping("/discussions/{themeId}/{discussionId}")
	@PreAuthorize("hasAuthority('DELETE_DISCUSSION')")
	public String deleteDiscussion(@PathVariable long themeId, @PathVariable long discussionId) {
		service.deleteDiscussion(discussionId);
		return "redirect:/themes/%d".formatted(themeId);
	}
	
	@GetMapping("/discussions/{themeId}/{discussionId}")
	public String getDiscussionById(@PathVariable long themeId, @PathVariable long discussionId, Model model) {
		model.addAttribute("themeId", themeId);
		model.addAttribute("discussion", service.getDiscussionById(discussionId));
		model.addAttribute("comments", service.getComments(discussionId));
		model.addAttribute("comment", new CommentDTO());
		
		return "discussion";
	}
	
	@PostMapping("/comments")
	public String createComment(@ModelAttribute @Valid CommentDTO comment, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return this.getErrorPage(bindingResult, model);
		}
			
		try {
			service.replyToComment(comment);
		} catch(Exception ex) {
			return this.getErrorPage(ex, model);
		}
			
		return "redirect:/discussions/%d/%d".formatted(comment.getThemeId() ,comment.getDiscussionId());
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
	@GetMapping("/login_error")
	public String loginError(Model model) {
		return this.getErrorPage(new SecurityException(
				"User is either have wrong username or password or is closed by ADMIN"), model);
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new WorkStaff());
		return "register";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute @Valid WorkStaff user, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return this.getErrorPage(bindingResult, model);
		}
		
		try {
			userDetails.saveUser(user);
		} catch(Exception ex) {
			return this.getErrorPage(ex, model);
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/workstaff")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String workstaff(Model model, Principal principal) {
		model.addAttribute("currentUser", userDetails.loadUserByUsername(principal.getName()));
		model.addAttribute("workstaff", userDetails.getAll());
		return "workstaff";
	}
	
	@PutMapping("/workstaff/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String setRoleAllowed(@PathVariable long id, Model model) {
		try {
			userDetails.setRoleAllowed(id);
		} catch(DBEntryNotFoundException ex) {
			return this.getErrorPage(ex, model);
		}
		
		return "redirect:/workstaff";
	}
	
	@DeleteMapping("/workstaff/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String deleteUser(@PathVariable long id, Model model) {
		try {
			userDetails.deleteUser(id);
		} catch(Exception ex) {
			return this.getErrorPage(ex, model);
		}
		
		return "redirect:/workstaff";
	}
	
	private String getErrorPage(BindingResult bindingResult, Model model) {
		List<String> errors = bindingResult.getAllErrors()
				.stream()
				.map(error -> error.getDefaultMessage())
				.toList();
		
		model.addAttribute("errors", errors);
		return "error";
	}
	
	private String getErrorPage(Exception ex, Model model) {
		
		model.addAttribute("errors", 
				List.of(String.format("%s: %s", ex.getClass().getSimpleName(), ex.getMessage())));
		return "error";
	}
}
