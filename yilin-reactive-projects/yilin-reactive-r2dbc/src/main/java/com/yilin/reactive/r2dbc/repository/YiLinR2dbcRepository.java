package com.yilin.reactive.r2dbc.repository;


import java.util.Map;
import java.util.function.BiFunction;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.r2dbc.core.DatabaseClient;


/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/27 10:47
 * @since 2024.0.1
 */
@NoRepositoryBean
public interface YiLinR2dbcRepository<T, ID> extends R2dbcRepository<T, ID> {

	/**
	 * 获取 R2dbcEntityOperations.
	 * @return /
	 */
	R2dbcEntityOperations getR2dbcEntityOperations();

	/**
	 * 分页查询
	 * @param criteria 条件
	 * @param pageable 分页参数
	 * @return /
	 */
	Mono<Page<T>> pageByQuery(Criteria criteria, Pageable pageable);

	/**
	 * 条件查询计数
	 * @param criteria 条件
	 * @return /
	 */
	Mono<Long> countByQuery(Criteria criteria);

	/**
	 * 条件查询
	 * @param criteria 条件
	 * @return /
	 */
	Flux<T> findByQuery(Criteria criteria);

	/**
	 * 排序
	 * @param criteria 条件
	 * @param sort 排序
	 * @return /
	 */
	Flux<T> findByQuery(Criteria criteria, Sort sort);

	/**
	 * 条件查询
	 * @param criteria 条件
	 * @param limit limit
	 * @return /
	 */
	Flux<T> findByQuery(Criteria criteria, int limit);

	/**
	 * 条件查询
	 * @param criteria 条件
	 * @param sort 排序
	 * @param limit limit
	 * @return /
	 */
	Flux<T> findByQuery(Criteria criteria, Sort sort, int limit);

	/**
	 * 查询
	 * @param query query
	 * @return /
	 */
	Flux<T> findByQuery(Query query);

	/**
	 * 查询
	 * @param query query
	 * @return /
	 */
	Mono<T> findOneByQuery(Query query);

	/**
	 * 查询
	 * @param criteria criteria
	 * @return /
	 */
	Mono<T> findOneByQuery(Criteria criteria);

	/**
	 * 根据 ID 进行逻辑删除.
	 *
	 * @param id id
	 * @return /
	 */
	Mono<Long> logicDeleteById(ID id);

	/**
	 * 根据 idPublisher 进行逻辑删除.
	 *
	 * @param idPublisher idPublisher
	 * @return /
	 */
	Flux<Long> logicDeleteById(Publisher<ID> idPublisher);

	/**
	 * 根据实体进行逻辑删除
	 *
	 * @param objectToDelete object
	 * @return /
	 */
	Mono<Long> logicDelete(T objectToDelete);

	/**
	 * 根据 id 进行逻辑删除.
	 *
	 * @param id id 集合
	 * @return /
	 */
	Mono<Long> logicDeleteAllById(Iterable<? extends ID> id);

	/**
	 * 根据实体进行逻辑删除.
	 * @param iterable 实体集合
	 * @return /
	 */
	Flux<Long> logicDeleteAll(Iterable<? extends T> iterable);

	/**
	 * 根据提供的 objectPublisher 进行逻辑删除
	 * @param objectPublisher objectPublisher
	 * @return /
	 */
	Flux<Long> logicDeleteAll(Publisher<? extends T> objectPublisher);

	/**
	 * 所有的实体进行逻辑删除.
	 *
	 * @return /
	 */
	Mono<Long> logicDeleteAll();

//	/**
//	 * 更改状态.
//	 * @param id id
//	 * @param status status
//	 * @return /
//	 */
//	Mono<Long> changeStatus(ID id, Integer status);
//
//	/**
//	 * 更改状态
//	 * @param id ids
//	 * @param status status
//	 * @return /
//	 */
//	Mono<Long> changeStatus(Publisher<ID> id, Integer status);
default <R> Mono<R> execSqlToMono(String sql, Map<String, Object> bindMap, BiFunction<Row, RowMetadata, R> mappingFunction) {
	DatabaseClient.GenericExecuteSpec genericExecuteSpec = getR2dbcEntityOperations().getDatabaseClient().sql(sql);
	if (bindMap != null) {
		for (Map.Entry<String, Object> entry : bindMap.entrySet()) {
			genericExecuteSpec = genericExecuteSpec.bind(entry.getKey(), entry.getValue());
		}
	}
	return genericExecuteSpec.map(mappingFunction).first();
}

	default <R> Flux<R> execSqlToFlux(String sql, Map<String, Object> bindMap, BiFunction<Row, RowMetadata, R> mappingFunction) {
		DatabaseClient.GenericExecuteSpec genericExecuteSpec = getR2dbcEntityOperations().getDatabaseClient().sql(sql);
		if (bindMap != null) {
			for (Map.Entry<String, Object> entry : bindMap.entrySet()) {
				genericExecuteSpec = genericExecuteSpec.bind(entry.getKey(), entry.getValue());
			}
		}
		return genericExecuteSpec.map(mappingFunction).all();
	}
}
