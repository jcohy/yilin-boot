//package com.yilin.reactive.r2dbc.repository;
//
//import java.util.Arrays;
//import java.util.function.Supplier;
//
//import org.junit.jupiter.api.Test;
//import reactor.test.StepVerifier;
//
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//
//import com.yilin.reactive.r2dbc.domain.Person;
//
///**
// * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
// *
// * <p> Description:
// *
// * @author jiac
// * @version 2023.0.1 2023/7/31:10:14
// * @since 2023.0.1
// */
//@SpringBootTest
//public class AbstractReactiveReactiveRepositoryTestTest {
//
//	@DynamicPropertySource
//	static void registerProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.sql.init.mode", () -> "always");
//		registry.add("spring.r2dbc.url", () -> "jdbc:h2:mem:r2dbc;DB_CLOSE_DELAY=-1");
//	}
//
////	abstract Supplier<Object> getR2dbcUrl();
//
//	private final Person<Person,Long> repository;
//
//	public AbstractReactiveReactiveRepositoryTestTest(ReactiveRepository<Person,Long> repository) {
//		this.repository = repository;
//	}
//
////	abstract ReactiveRepository<Person,Long> getReactiveRepository();
//
//	@Test
//	void shouldInsertNewItems() {
//
//		Person person1 = new Person(null, "Jcohy", 12, 0L);
//		Person person2 = new Person(null, "YiLin", 13, 0L);
//
//		repository.saveAll(Arrays.asList(person1, person2)) //
//				.as(StepVerifier::create) //
//				.expectNextCount(2) //
//				.verifyComplete();
//	}
//}
