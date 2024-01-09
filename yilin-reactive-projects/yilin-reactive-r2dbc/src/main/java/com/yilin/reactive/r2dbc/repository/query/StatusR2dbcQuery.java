package com.yilin.reactive.r2dbc.repository.query;

import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.query.AbstractR2dbcQuery;
import org.springframework.data.r2dbc.repository.query.R2dbcQueryMethod;
import org.springframework.data.relational.repository.query.RelationalParameterAccessor;
import org.springframework.r2dbc.core.PreparedOperation;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/3 17:22
 * @since 2024.0.1
 */
public class StatusR2dbcQuery extends AbstractR2dbcQuery {
	public StatusR2dbcQuery(R2dbcQueryMethod method, R2dbcEntityOperations entityOperations, R2dbcConverter converter) {
		super(method, entityOperations, converter);
	}

	@Override
	protected boolean isModifyingQuery() {
		return false;
	}

	@Override
	protected boolean isCountQuery() {
		return false;
	}

	@Override
	protected boolean isExistsQuery() {
		return false;
	}

	@Override
	protected Mono<PreparedOperation<?>> createQuery(RelationalParameterAccessor accessor) {
		return null;
	}
}
