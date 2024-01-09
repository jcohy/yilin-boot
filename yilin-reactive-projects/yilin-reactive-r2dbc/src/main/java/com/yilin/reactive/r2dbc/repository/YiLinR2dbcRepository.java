package com.yilin.reactive.r2dbc.repository;


import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;

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
}
