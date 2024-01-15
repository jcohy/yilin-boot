package com.yilin;


import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.yilin.auth.Department;
import com.yilin.auth.DepartmentRepository;


/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/12/12 20:56
 * @since 2024.0.1
 */
@SpringBootTest

@AutoConfigureMockMvc
public class ApplicationControllerTests {

//	@Autowired
//	private MockMvc mockMvc;

	@Autowired
	DepartmentRepository departmentRepository;

//	@Test
//	public void shouldReturnOkWithToken() throws Exception {
//		this.mockMvc.perform(get("/").header("Authorization", "Bearer TOKEN"))
//				.andExpect(status().isOk());
//	}

	@Test
	void addDepartment() {
		Department department = new Department();
//		department.setId(101L);
		department.setName("aa");
		departmentRepository.save(department) //
				.as(StepVerifier::create) //
				.expectNextCount(1) //
				.verifyComplete();
	}

}
