package com.example.demo.config.utils;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

public class CommentRepliesValidator implements ConstraintValidator<CommentReplies, String>{

	@Autowired
	private Function<String, List<Long>> repliesConverter;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if(!value.matches("(\\d+;)*")) {
			return false;
		}
		
		List<Long> commentsId = repliesConverter.apply(value);
		
		if(new HashSet<Long>(commentsId).size() != commentsId.size()) {
			return false;
		}
		
		return true;
	}

}
