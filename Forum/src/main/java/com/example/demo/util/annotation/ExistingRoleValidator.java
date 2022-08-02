package com.example.demo.util.annotation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.demo.security.UserRole;

public class ExistingRoleValidator implements ConstraintValidator<ExistingRole, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return Arrays
				.stream(UserRole.values())
				.anyMatch(role -> role.name().equals(value));
	}

}
