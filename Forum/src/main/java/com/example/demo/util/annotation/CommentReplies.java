package com.example.demo.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = CommentRepliesValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CommentReplies {
	String message() default "typed comments' id this one replies to are not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
