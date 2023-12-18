package com.yilin;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/12/12:20:56
 * @since 2023.0.1
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnOkWithToken() throws Exception {
		this.mockMvc.perform(get("/").header("Authorization", "Bearer TOKEN"))
				.andExpect(status().isOk());
	}

}
