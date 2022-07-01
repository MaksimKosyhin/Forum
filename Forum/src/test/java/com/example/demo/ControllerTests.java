package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.service.DBEntityService;

@WebMvcTest
public class ControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DBEntityService service;
	
	
}
