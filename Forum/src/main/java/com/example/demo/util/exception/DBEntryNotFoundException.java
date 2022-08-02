package com.example.demo.util.exception;

public class DBEntryNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 4341521579523553238L;

	public DBEntryNotFoundException(String message) {
		super(message);
	}
}
