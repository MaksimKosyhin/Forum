package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;
import com.example.demo.model.Discussion;
import com.example.demo.model.Theme;

public interface DBEntityService {
	public List<Theme> getThemes();
	
	public Theme getThemeById(long themeId);
	
	public Discussion getDiscussionById(long discussionId);
	
	public void addTheme(Theme theme);
	
	public void addDiscussion(long themeId, String title, Comment headerComment);
	
	public void addCommentReply(CommentDTO comment);
	
	public void deleteTheme(long themeId);
	
	public void deleteDiscussion(long discussionId);
	
	public void deleteComment(long commentId);
	
	public Map<Comment, List<Long>> getComments(long discussionId);
}
