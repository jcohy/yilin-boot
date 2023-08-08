package com.yilin.reactive.r2dbc;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.yilin.reactive.r2dbc.domain.Person;
import com.yilin.reactive.r2dbc.testing.H2TestSupport;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/31:11:03
 * @since 2023.0.1
 */
@DataR2dbcTest(properties = "spring.sql.init.schemaLocations=classpath:schema.sql")
@ExtendWith(SpringExtension.class)
public class ReactiveRepositoryTest {

	@Autowired
	private DatabaseClient databaseClient;

	@Autowired
	private PersonRepository repository;

	@BeforeEach
	void setUp() {
		try {
			this.databaseClient.sql("DROP TABLE person");
		}
		catch (DataAccessException e) {
		}

		this.databaseClient.sql(H2TestSupport.CREATE_TABLE_PERSON);
	}


	@Test
	void shouldInsertNewItems() {

		Person person1 = new Person(null, "Jcohy", 12, 0L);
		Person person2 = new Person(null, "YiLin", 13, 0L);

		repository.saveAll(Arrays.asList(person1, person2)) //
				.as(StepVerifier::create) //
				.expectNextCount(2) //
				.verifyComplete();
	}

//	@DynamicPropertySource
//	static void registerProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.sql.init.mode", () -> "always");
//		registry.add("spring.r2dbc.url", () -> "jdbc:h2:mem:r2dbc;DB_CLOSE_DELAY=-1");
//	}
}
