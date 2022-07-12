package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.exceptions.DBEntryNotFoundException;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;
import com.example.demo.model.Discussion;
import com.example.demo.model.DiscussionDTO;
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
	
	private Function<String, List<Long>> repliesConverter;
	
	@Autowired
	public DBEntityServiceImpl(ThemeRepository themes, DiscussionRepository discussions, CommentRepository comments,
			ModelMapper mapper, Function<String, List<Long>> repliesConverter) {
		this.themes = themes;
		this.discussions = discussions;
		this.comments = comments;
		this.mapper = mapper;
		this.repliesConverter = repliesConverter;
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
	public void addDiscussion(DiscussionDTO discussionDTO){
		Discussion discussion = mapper.map(discussionDTO, Discussion.class);
		comments.save(discussion.getHeaderComment());
		discussions.save(discussion);
		
		Theme theme = this.getThemeById(discussionDTO.getThemeId());
		theme.getDiscussions().add(discussion);
		themes.save(theme);
	}

	@Override
	public void replyToComment(CommentDTO commentDTO) {		
		Discussion discussion = this.getDiscussionById(commentDTO.getDiscussionId());
		List<Comment> comments = this.getComments(discussion);
		List<Comment> questions = this.getComments(commentDTO);
		
		if(comments.containsAll(questions)) {
			this.saveComment(mapper.map(commentDTO, Comment.class), 
					questions.size() == 0 ? List.of(discussion.getHeaderComment()) : questions);
		} else {
			throw new DBEntryNotFoundException(
					"Some comments this one replies to are not found in discussion: %s"
						.formatted(discussion.getTitle()));
		}
	}
	
	private List<Comment> getComments(CommentDTO commentDTO) {
		return repliesConverter.apply(commentDTO.getRepliedComments())
			.stream()
			.map(commentId -> comments.findById(commentId).orElseThrow(() -> 
				new DBEntryNotFoundException(
					"comment with id: %d was not found"
					.formatted(commentId))))
			.toList();
	}
	
	private List<Comment> getComments(Discussion discussion) {
		List<Comment> comments = new ArrayList<>();
		
		Stack<Comment> stack = new Stack<>();
		stack.push(discussion.getHeaderComment());
		
		while(!stack.empty()) {
			Comment c1 = stack.pop();
			comments.add(c1);
			
			for(Comment c2: c1.getReplies()) {
				stack.push(c2);
			}
		}
		
		return comments;
	}
	
	private void saveComment(Comment answer, List<Comment> questions) {
		comments.save(answer);
		
		questions.forEach(question -> {
			question.getReplies().add(answer);
			comments.save(question);
		});
	}
	
	@Override
	public void switchThemeClosing(long themeId) {
		Theme theme = this.getThemeById(themeId);
		theme.setClosed(theme.isClosed() ? false : true);
		themes.save(theme);
	}

	@Override
	public void switchDiscussionClosing(long discussionId) {
		Discussion discussion = this.getDiscussionById(discussionId);
		discussion.setClosed(discussion.isClosed() ? false : true);
		discussions.save(discussion);
	}
	
	@Override
	public void deleteTheme(long themeId) {
		if(themes.existsById(themeId)) {
			themes.deleteById(themeId);
		} else {
			throw new DBEntryNotFoundException("theme with id: %d was not found"
					.formatted(themeId));
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
	public void deleteComment(long discussionId, long commentId) {
		Comment toDelete = comments.findById(commentId)
				.orElseThrow(() ->
				new DBEntryNotFoundException(
						"comment with id: %d was not found"
						.formatted(commentId))
				);
		
		this.getComments(this.getDiscussionById(discussionId))
			.forEach(comment -> {
				comment.getReplies().remove(toDelete);
				comments.save(comment);
			});
		
		comments.deleteById(commentId);
	}
	
	@Override
	public Map<Comment, List<Long>> getComments(long discussionId) {
		Discussion discussion = getDiscussionById(discussionId);
		
		Map<Comment, List<Long>> result = 
				new TreeMap<>((c1, c2) -> Long.compare(c1.getId(), c2.getId()));
		
		Stack<Comment> stack = new Stack<>();
		stack.push(discussion.getHeaderComment());
		
		while(!stack.empty()) {
			Comment c1 = stack.pop();
			
			for(Comment c2: c1.getReplies()) {
				if(result.containsKey(c2)) {
					result.get(c2).add(c1.getId());
				} else {
					result.put(c2, new ArrayList<>(List.of(c1.getId())));
				}
				
				stack.push(c2);
			}
		}
		
		return result;
	}
}
