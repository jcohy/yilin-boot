package com.yilin.reactive.r2dbc.repository;

import org.reactivestreams.Publisher;
import org.springframework.core.annotation.AnnotatedElementUtils;
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

import com.yilin.reactive.persistent.annotations.LogicDelete;
import com.yilin.reactive.persistent.annotations.TenantId;
import com.yilin.reactive.persistent.enums.DeleteStatus;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/27 17:14
 * @since 2023.0.1
 */
public class YiLinR2dbcRepositoryImpl<T, ID> extends SimpleR2dbcRepository<T, ID> implements YiLinR2dbcRepository<T, ID> {

	private final R2dbcEntityOperations entityOperations;

	private final RelationalEntityInformation<T, ID> entity;

	private final Lazy<RelationalPersistentProperty> idProperty;

	private final Optional<String> delete;

	private final Optional<String> tenantId;

	public YiLinR2dbcRepositoryImpl(RelationalEntityInformation<T, ID> entity, R2dbcEntityOperations entityOperations,
			R2dbcConverter converter) {
		super(entity, entityOperations, converter);

		this.entityOperations = entityOperations;
		this.entity = entity;
		this.idProperty = Lazy.of(() ->
				converter.getMappingContext()
						.getRequiredPersistentEntity(this.entity.getJavaType())
						.getRequiredIdProperty());
		this.delete = Arrays.stream(this.entity.getJavaType().getDeclaredFields())
				.filter(field -> AnnotatedElementUtils.hasAnnotation(field, LogicDelete.class))
				.map(Field::getName)
				.findAny();
		this.tenantId = Arrays.stream(this.entity.getJavaType().getDeclaredFields())
				.filter(field -> AnnotatedElementUtils.hasAnnotation(field, TenantId.class))
				.map(Field::getName)
				.findAny();
	}

	@Override
	@Transactional
	public Mono<Long> logicDeleteById(ID id) {
		Assert.notNull(id, "Id must not be null");
		Assert.isTrue(delete.isPresent(), "@LogicDelete annotation must not be null");
		return this.entityOperations
				.update(getIdQuery(id), Update.update(delete.get(), DeleteStatus.DELETED.getStatus()), this.entity.getJavaType());
	}

	@Override
	@Transactional
	public Flux<Long> logicDeleteById(Publisher<ID> idPublisher) {
		Assert.notNull(idPublisher, "The Id Publisher must not be null");
		Assert.isTrue(delete.isPresent(), "@LogicDelete annotation must not be null");
		return Flux.from(idPublisher).buffer().filter(ids -> !ids.isEmpty()).concatMap(ids -> {
			if (ids.isEmpty()) {
				return Flux.empty();
			}

			String idProperty = getIdProperty().getName();
			return this.entityOperations.update(Query.query(Criteria.where(idProperty).in(ids)),
					Update.update(delete.get(), DeleteStatus.DELETED.getStatus()),
					this.entity.getJavaType());
		});
	}

	@Override
	@Transactional
	public Mono<Long> logicDelete(T objectToDelete) {
		Assert.notNull(objectToDelete, "object to delete must not be null");
		Assert.isTrue(delete.isPresent(), "@LogicDelete annotation must not be null");
		return logicDeleteById(this.entity.getRequiredId(objectToDelete));
	}

	@Override
	public Mono<Long> logicDeleteAllById(Iterable<? extends ID> ids) {
		Assert.notNull(ids, "the iterable of Id's must not be null");
		var idList = Streamable.of(ids).toList();
		String idProperty = getIdProperty().getName();
		return this.entityOperations
				.update(Query.query(Criteria.where(idProperty).in(idList)),
						Update.update(delete.get(), DeleteStatus.DELETED.getStatus()),
						this.entity.getJavaType());
	}

	@Override
	@Transactional
	public Flux<Long> logicDeleteAll(Iterable<? extends T> iterable) {
		Assert.notNull(iterable, "the iterable of Id's must not be null");
		return logicDeleteAll(Flux.fromIterable(iterable));
	}

	@Override
	@Transactional
	public Flux<Long> logicDeleteAll(Publisher<? extends T> objectPublisher) {
		Assert.notNull(objectPublisher, "the object publisher must not be null");
		var idPublisher = Flux.from(objectPublisher)
				.map(this.entity::getRequiredId);
		return logicDeleteById(idPublisher);
	}

	@Override
	@Transactional
	public Mono<Long> logicDeleteAll() {
		Assert.isTrue(delete.isPresent(), "@LogicDelete annotation must not be null");
		return this.entityOperations.update(Query.empty(),
				Update.update(delete.get(), DeleteStatus.DELETED.getStatus()),
				this.entity.getJavaType());
	}

//	@Override
//	public Mono<Long> changeStatus(ID id, Integer status) {
//
//		Assert.notNull(id, "Id must not be null");
//
//		return this.entityOperations
//				.update(getIdQuery(id), Update.update("status", status), this.entity.getJavaType());
//	}
//
//	@Override
//	public Mono<Long> changeStatus(Publisher<ID> idPublisher, Integer status) {
//		Assert.notNull(idPublisher, "The Id Publisher must not be null");
//
//		return Mono.from(Flux.from(idPublisher).buffer().filter(ids -> !ids.isEmpty()).concatMap(ids -> {
//			if (ids.isEmpty()) {
//				return Flux.empty();
//			}
//			String idProperty = getIdProperty().getName();
//			return this.entityOperations.update(Query.query(Criteria.where(idProperty).in(ids)),
//					Update.update("status", status),
//					this.entity.getJavaType());
//		}));
//	}

	private RelationalPersistentProperty getIdProperty() {
		return this.idProperty.get();
	}

	private Query getIdQuery(Object id) {
		return Query.query(Criteria.where(getIdProperty().getName()).is(id));
	}
}
