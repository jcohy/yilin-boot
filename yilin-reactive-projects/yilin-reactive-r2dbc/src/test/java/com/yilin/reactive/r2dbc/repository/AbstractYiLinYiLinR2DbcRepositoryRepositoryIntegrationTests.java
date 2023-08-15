package com.yilin.reactive.r2dbc.repository;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import com.yilin.reactive.r2dbc.YiLinR2dbcRepositoryIntegrationTestSupport;
import com.yilin.reactive.r2dbc.domain.Person;
import com.yilin.reactive.r2dbc.domain.PersonRepository;
import com.yilin.reactive.r2dbc.repository.support.YiLinR2dbcRepositoryFactoryBean;
import com.yilin.reactive.r2dbc.testing.H2TestSupport;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/28:15:22
 * @since 2023.0.1
 */
@EnableR2dbcRepositories(considerNestedRepositories = true, basePackages = { "com.yilin.reactive.r2dbc" },
		repositoryFactoryBeanClass = YiLinR2dbcRepositoryFactoryBean.class)
public abstract class AbstractYiLinYiLinR2DbcRepositoryRepositoryIntegrationTests extends YiLinR2dbcRepositoryIntegrationTestSupport {

	@Autowired
	PersonRepository repository;

	@Autowired
	private DatabaseClient databaseClient;

	@BeforeEach
	void before() {
		Hooks.onOperatorDebug();
		var statement = Arrays.asList("DROP TABLE IF EXISTS person", H2TestSupport.CREATE_TABLE_PERSON);
		statement.forEach(it -> {
			databaseClient.sql(it)
					.fetch()
					.rowsUpdated()
					.as(StepVerifier::create)
					.expectNextCount(1)
					.verifyComplete();
		});
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

	@Test
	void shouldLogicDeleteById() {
		Person person = new Person(null, "Jcohy", 12, 0L, 1, 1);

		insertPersons(person);

		repository.logicDeleteById(person.getId())
				.as(StepVerifier::create)
				.expectNext(1L)
				.verifyComplete();

		this.repository.findById(person.getId())
				.as(StepVerifier::create)
				.assertNext(actual -> {
					assertThat(actual.getDeleted()).isEqualTo(0);
				})
				.verifyComplete();
	}

	@Test
	void shouldLogicDeleteByIdPublisher() {

		Person person = new Person(null, "Jcohy", 12, 0L, 1, 1);
		insertPersons(person);

		repository.logicDeleteById(Mono.just(person.getId()))
				.as(StepVerifier::create)
				.expectNext(1L)
				.verifyComplete();

		this.repository.findById(person.getId())
				.as(StepVerifier::create)
				.assertNext(actual -> {
					assertThat(actual.getDeleted()).isEqualTo(0);
				})
				.verifyComplete();
	}

	@Test
	void shouldLogicDelete() {
		Person person = new Person(null, "Jcohy", 12, 0L, 1, 1);

		insertPersons(person);

		this.repository.logicDelete(person)
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		this.repository.findById(person.getId())
				.as(StepVerifier::create)
				.assertNext(actual -> {
					assertThat(actual.getDeleted()).isEqualTo(0);
				})
				.verifyComplete();
	}

	@Test
	void shouldLogicDeleteAllById() {
		Person person = new Person(null, "Jcohy", 12, 0L, 1, 1);
		Person person2 = new Person(null, "YiLin", 1, 0L, 1, 1);

		insertPersons(person, person2);

		this.repository.logicDeleteAllById(Arrays.asList(person.getId(), person2.getId()))
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		this.repository.findAllById(Arrays.asList(person.getId(), person2.getId()))
				.collectList()
				.as(StepVerifier::create)
				.consumeNextWith(actual -> {
					assertThat(actual).hasSize(2).extracting(Person::getDeleted).containsSequence(0, 0);
				})
				.verifyComplete();
	}


	@Test
	void shouldLogicDeleteAllWithObject() {
		Person person = new Person(null, "Jcohy", 12, 0L, 1, 1);
		Person person2 = new Person(null, "YiLin", 1, 0L, 1, 1);

		insertPersons(person, person2);

		this.repository.logicDeleteAll(Arrays.asList(person, person2))
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		this.repository.findAllById(Arrays.asList(person.getId(), person2.getId()))
				.collectList()
				.as(StepVerifier::create)
				.consumeNextWith(actual -> {
					assertThat(actual).hasSize(2).extracting(Person::getDeleted).containsSequence(0, 0);
				})
				.verifyComplete();
	}

	@Test
	void shouldLogicDeleteAllWithPublisher() {
		Person person = new Person(null, "Jcohy", 12, 0L, 1, 1);
		Person person2 = new Person(null, "YiLin", 1, 0L, 1, 1);

		insertPersons(person, person2);

		this.repository.logicDeleteAll(Flux.just(person, person2))
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		this.repository.findAllById(Arrays.asList(person.getId(), person2.getId()))
				.collectList()
				.as(StepVerifier::create)
				.consumeNextWith(actual -> {
					assertThat(actual).hasSize(2).extracting(Person::getDeleted).containsSequence(0, 0);
				})
				.verifyComplete();
	}


	@Test
	void shouldLogicDeleteAll() {

		shouldInsertNewItems();

		this.repository.logicDeleteAll()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		this.repository.findAll()
				.collectList()
				.as(StepVerifier::create)
				.consumeNextWith(actual -> {
					assertThat(actual).hasSize(2).extracting(Person::getDeleted).containsSequence(0, 0);
				})
				.verifyComplete();
	}

	@Test
	void shouldChangeStatusWithId() {

		Person person = new Person(null, "Jcohy", 12, 0L, 1, 1);

		insertPersons(person);

		this.repository.changeStatus(person.getId(), 2)
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		this.repository.findById(person.getId())
				.as(StepVerifier::create)
				.assertNext(actual -> {
					assertThat(actual.getStatus()).isEqualTo(2);
				})
				.verifyComplete();
	}

	@Test
	void shouldChangeStatusWithPublisher() {

		Person person = new Person(null, "Jcohy", 12, 0L, 1, 1);
		Person person2 = new Person(null, "YiLin", 1, 0L, 1, 1);

		insertPersons(person, person2);

		this.repository.changeStatus(Flux.just(person.getId(), person2.getId()), 2)
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		this.repository.findAll()
				.collectList()
				.as(StepVerifier::create)
				.consumeNextWith(actual -> {
					assertThat(actual).hasSize(2).extracting(Person::getStatus).containsSequence(2, 2);
				})
				.verifyComplete();
	}


	private void insertPersons(Person... persons) {
		this.repository
				.saveAll(Arrays.asList(persons))
				.as(StepVerifier::create)
				.expectNextCount(persons.length)
				.verifyComplete();
	}

}