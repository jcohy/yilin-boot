package com.yilin.reactive.r2dbc.repository;


import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/27:10:47
 * @since 2023.0.1
 */
@NoRepositoryBean
public interface ReactiveRepository<T, ID> extends R2dbcRepository<T, ID> {

	Mono<Void> logicDeleteById(ID id);

	Mono<Void> logicDeleteById(Publisher<ID> idPublisher);

	Mono<Void> logicDelete(T objectToDelete);

	Mono<Void> logicDeleteAllById(Iterable<? extends ID> id);

	Mono<Void> logicDeleteAll(Iterable<? extends T> iterable);

	Mono<Void> logicDeleteAll(Publisher<? extends T> objectPublisher);

	Mono<Void> logicDeleteAll();

	Mono<Void> changeStatus(ID id, Integer status);

	Mono<Void> changeStatus(Publisher<ID> id, Integer status);
}
