package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.DBEntryNotFoundException;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;
import com.example.demo.model.Discussion;
import com.example.demo.model.Theme;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.DiscussionRepository;
import com.example.demo.repository.ThemeRepository;

@Service
public class DBEntityServiceImpl implements DBEntityService {
	private ThemeRepository themes;
	private DiscussionRepository discussions;
	private CommentRepository comments;
	
	private ModelMapper mapper;
	
	@Autowired
	public DBEntityServiceImpl(ThemeRepository themes, DiscussionRepository discussions,
			CommentRepository comments, ModelMapper mapper) {

		this.themes = themes;
		this.discussions = discussions;
		this.comments = comments;
		this.mapper = mapper;
	}
	
	@Override
	public List<Theme> getThemes() {
		return themes.findAll();
	}

	@Override
	public Theme getThemeById(long themeId) {
		return themes.findById(themeId)
				.orElseThrow(() -> 
					new DBEntryNotFoundException("theme with id: %d was not found"
							.formatted(themeId)));
	}

	@Override
	public Discussion getDiscussionById(long discussionId) {
		return discussions.findById(discussionId)
				.orElseThrow(() -> 
					new DBEntryNotFoundException("discussion with id: %d was not found"
						.formatted(discussionId)));
	}

	@Override
	public void addTheme(Theme theme) {
		themes.save(theme);
	}
	
	@Override
	public void addDiscussion(long themeId, String title, Comment headerComment) {
		Theme theme = themes.findById(themeId).orElseThrow(() -> 
			new DBEntryNotFoundException("cannot add discussion because theme with id: %d was not found"
					.formatted(themeId)));
		
		Discussion discussion = new Discussion(title, headerComment);
		comments.save(headerComment);
		discussions.save(discussion);
		theme.getDiscussions().add(discussion);
	}

	@Override
	public void addCommentReply(CommentDTO commentDTO) {
		Comment question = comments.findById(commentDTO.getQuestionCommentId()).orElseThrow(() -> 
			new DBEntryNotFoundException("cannot reply to comment because comment with id: %d is not found"
					.formatted(commentDTO.getQuestionCommentId())));
		
		Comment answer = mapper.map(commentDTO, Comment.class);
		
		comments.save(answer);
		question.getReplies().add(answer);
	}
	
	@Override
	public void deleteTheme(long themeId) {
		if(themes.existsById(themeId)) {
			themes.deleteById(themeId);
		} else {
			throw new DBEntryNotFoundException("theme with id: %d was not found".formatted(themeId));
		}
	}
	
	@Override
	public void deleteDiscussion(long discussionId) {
		if(discussions.existsById(discussionId)) {
			discussions.deleteById(discussionId);
		} else {
			throw new DBEntryNotFoundException("discussion with id: %d was not found"
					.formatted(discussionId));
		}
	}

	@Override
	public void deleteComment(long commentId) {
		if(comments.existsById(commentId)) {
			comments.deleteById(commentId);
		} else {
			throw new DBEntryNotFoundException("comment with id: %d was not found"
					.formatted(commentId));
		}
	}

	@Override
	public Map<Comment, List<Long>> getComments(long discussionId) {
		Discussion discussion = getDiscussionById(discussionId);
		
		Map<Comment, List<Long>> result = 
				new TreeMap<>((c1, c2) -> c1.getTs().compareTo(c2.getTs()));
		
		Stack<Comment> stack = new Stack<>();
		stack.push(discussion.getHeaderComment());
		
		while(!stack.empty()) {
			Comment c1 = stack.pop();
			
			for(Comment c2: c1.getReplies()) {
				if(result.containsKey(c2)) {
					result.get(c2).add(c1.getId());
				} else {
					List<Long> list = new ArrayList<>();
					list.add(c1.getId());
					result.put(c2, list);
				}
				
				stack.push(c2);
			}
		}
		
		return result;
	}
}
