package com.yilin.reactive.r2dbc.repository;

import java.util.Arrays;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

import com.yilin.reactive.persistent.enums.DeleteStatus;
import com.yilin.reactive.persistent.enums.ServiceStatus;
import com.yilin.reactive.r2dbc.testing.R2dbcIntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/28:15:22
 * @since 2023.0.1
 */
public abstract class AbstractReactiveRepositoryIntegrationTests extends R2dbcIntegrationTestSupport {

	ReactiveRepository<Person, Long> repository;

	JdbcTemplate jdbc;

	@Autowired
	private DatabaseClient databaseClient;

	@Autowired
	private RelationalMappingContext mappingContext;

	@BeforeEach
	@SuppressWarnings("unchecked")
	void before() {
		MappingR2dbcConverter converter = new MappingR2dbcConverter(mappingContext);
		MappingRelationalEntityInformation<Person, Long> entityInformation =
				new MappingRelationalEntityInformation<>((RelationalPersistentEntity<Person>) mappingContext.getRequiredPersistentEntity(Person.class));
		this.repository = new ReactiveRepositoryImpl<>(entityInformation, new R2dbcEntityTemplate(databaseClient, createDialect(), converter), converter);
		this.jdbc = createJdbcTemplate(createDataSource());

		try {
			this.jdbc.execute("DROP TABLE person");
		}
		catch (DataAccessException e) {
		}

		this.jdbc.execute(getCreateTableStatement());
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

		jdbc.execute("INSERT INTO person (name,age,version,deleted,status) values ('Jcohy', 12 , 0L, 1, 1)");
		Long id = jdbc.queryForObject("SELECT id FROM person", Long.class);

		repository.logicDeleteById(id)
				.as(StepVerifier::create)
				.verifyComplete();

		Long deleted = jdbc.queryForObject("SELECT deleted FROM person", Long.class);
		assertThat(deleted).isEqualTo(DeleteStatus.DELETED.getStatus());
	}

	@Test
	void shouldLogicDeleteByIdPublisher() {

		jdbc.execute("INSERT INTO person (name,age,version,deleted,status) values ('Jcohy', 12 , 0L, 1, 1)");
		Long id = jdbc.queryForObject("SELECT id FROM person", Long.class);

		repository.logicDeleteById(Mono.just(id))
				.as(StepVerifier::create)
				.verifyComplete();

		Long deleted = jdbc.queryForObject("SELECT deleted FROM person", Long.class);
		assertThat(deleted).isEqualTo(DeleteStatus.DELETED.getStatus());
	}


	@Test
	void shouldLogicDelete() {
		jdbc.execute("INSERT INTO person (name,age,version,deleted,status) values ('Jcohy', 12 , 0L, 1, 1)");
		Long id = jdbc.queryForObject("SELECT id FROM person", Long.class);

		var person = new Person(id, "Jcohy", 12, 0L, 0, 0);

		repository.logicDelete(person)
				.as(StepVerifier::create)
				.verifyComplete();

		Long deleted = jdbc.queryForObject("SELECT deleted FROM person", Long.class);
		assertThat(deleted).isEqualTo(DeleteStatus.DELETED.getStatus());
	}

	protected abstract String getCreateTableStatement();

	protected abstract R2dbcDialect createDialect();

	@Override
	protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return super.createJdbcTemplate(dataSource);
	}


	@NoRepositoryBean
	interface PersonRepository extends ReactiveRepository<Person, Long> {

		@Lock(LockMode.PESSIMISTIC_WRITE)
		Flux<Person> getAllByName(String name);
	}

	@Table("person")
	public record Person(@Id Long id, String name, Integer age, Long version,
						 Integer deleted, Integer status) {

		public Person(Long id, String name, Integer age, Long version) {
			this(id, name, age, version, DeleteStatus.NORMAL.getStatus(), ServiceStatus.ENABLE.getStatus());
		}
	}
}
