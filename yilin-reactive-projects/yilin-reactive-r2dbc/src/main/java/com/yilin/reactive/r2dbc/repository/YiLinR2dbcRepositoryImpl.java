package com.yilin.reactive.r2dbc.repository;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.data.util.Lazy;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.yilin.reactive.persistent.enums.DeleteStatus;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/27:17:14
 * @since 2023.0.1
 */
public class YiLinR2dbcRepositoryImpl<T, ID> extends SimpleR2dbcRepository<T, ID> implements YiLinR2dbcRepository<T, ID> {

	private final R2dbcEntityOperations entityOperations;

	private final RelationalEntityInformation<T, ID> entity;

	private final Lazy<RelationalPersistentProperty> idProperty;

	public YiLinR2dbcRepositoryImpl(RelationalEntityInformation<T, ID> entity, R2dbcEntityOperations entityOperations,
			R2dbcConverter converter) {
		super(entity, entityOperations, converter);

		this.entityOperations = entityOperations;
		this.entity = entity;
		this.idProperty = Lazy.of(() ->
				converter.getMappingContext()
						.getRequiredPersistentEntity(this.entity.getJavaType())
						.getRequiredIdProperty());
	}

	@Override
	@Transactional
	public Mono<Long> logicDeleteById(ID id) {

		Assert.notNull(id, "Id must not be null");


		return this.entityOperations
				.update(getIdQuery(id), Update.update("deleted", DeleteStatus.DELETED.getStatus()), this.entity.getJavaType());
	}

	@Override
	@Transactional
	public Flux<Long> logicDeleteById(Publisher<ID> idPublisher) {

		Assert.notNull(idPublisher, "The Id Publisher must not be null");

		return Flux.from(idPublisher).buffer().filter(ids -> !ids.isEmpty()).concatMap(ids -> {
			if (ids.isEmpty()) {
				return Flux.empty();
			}

			String idProperty = getIdProperty().getName();
			return this.entityOperations.update(Query.query(Criteria.where(idProperty).in(ids)),
					Update.update("deleted", DeleteStatus.DELETED.getStatus()),
					this.entity.getJavaType());
		});
	}

	@Override
	@Transactional
	public Mono<Long> logicDelete(T objectToDelete) {
		Assert.notNull(objectToDelete, "object to delete must not be null");

		return logicDeleteById(this.entity.getRequiredId(objectToDelete));
	}

	@Override
	public Mono<Void> logicDeleteAllById(Iterable<? extends ID> ids) {
		Assert.notNull(ids, "the iterable of Id's must not be null");

		var idList = Streamable.of(ids).toList();
		String idProperty = getIdProperty().getName();
		return this.entityOperations
				.update(Query.query(Criteria.where(idProperty).in(idList)),
						Update.update("deleted", DeleteStatus.DELETED.getStatus()),
						this.entity.getJavaType()).then();
	}

	@Override
	@Transactional
	public Mono<Void> logicDeleteAll(Iterable<? extends T> iterable) {
		Assert.notNull(iterable, "the iterable of Id's must not be null");
		return logicDeleteAll(Flux.fromIterable(iterable));
	}

	@Override
	@Transactional
	public Mono<Long> logicDeleteAll(Publisher<? extends T> objectPublisher) {
		Assert.notNull(objectPublisher, "the object publisher must not be null");
		var idPublisher = Flux.from(objectPublisher)
				.map(this.entity::getRequiredId);
		return logicDeleteById(idPublisher);
	}

	@Override
	@Transactional
	public Mono<Void> logicDeleteAll() {
		return this.entityOperations.update(Query.empty(),
				Update.update("deleted", DeleteStatus.DELETED.getStatus()),
				this.entity.getJavaType()).then();
	}

	@Override
	public Mono<Void> changeStatus(ID id, Integer status) {

		Assert.notNull(id, "Id must not be null");

		return this.entityOperations
				.update(getIdQuery(id), Update.update("status", status), this.entity.getClass()).then();
	}

	@Override
	public Mono<Void> changeStatus(Publisher<ID> idPublisher, Integer status) {
		Assert.notNull(idPublisher, "The Id Publisher must not be null");

		return Flux.from(idPublisher).buffer().filter(ids -> !ids.isEmpty()).concatMap(ids -> {
			if (ids.isEmpty()) {
				return Flux.empty();
			}

			String idProperty = getIdProperty().getName();
			return this.entityOperations.update(Query.query(Criteria.where(idProperty).in(ids)),
					Update.update("status", status),
					this.entity.getJavaType());
		}).then();
	}

	private RelationalPersistentProperty getIdProperty() {
		return this.idProperty.get();
	}

	private Query getIdQuery(Object id) {
		return Query.query(Criteria.where(getIdProperty().getName()).is(id));
	}
}
