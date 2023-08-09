package com.yilin.reactive.r2dbc.repository.support;

import java.util.Optional;

import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.ReactiveQueryMethodEvaluationContextProvider;
import org.springframework.lang.Nullable;
import org.springframework.r2dbc.core.DatabaseClient;

import com.yilin.reactive.r2dbc.repository.YiLinR2dbcRepositoryImpl;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/31:16:50
 * @since 2023.0.1
 */
@SuppressWarnings("deprecation")
public class YiLinR2dbcRepositoryFactory extends R2dbcRepositoryFactory {

	private final ReactiveDataAccessStrategy dataAccessStrategy;

	private final R2dbcConverter converter;

	private final R2dbcEntityOperations operations;

	public YiLinR2dbcRepositoryFactory(DatabaseClient databaseClient, ReactiveDataAccessStrategy dataAccessStrategy) {
		this(new R2dbcEntityTemplate(databaseClient, dataAccessStrategy));
	}

	public YiLinR2dbcRepositoryFactory(R2dbcEntityOperations operations) {
		super(operations);
		this.dataAccessStrategy = operations.getDataAccessStrategy();
		this.converter = dataAccessStrategy.getConverter();
		this.operations = operations;
	}

	@Override
	protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		Optional<QueryLookupStrategy> strategy = super.getQueryLookupStrategy(key, evaluationContextProvider);
		return Optional.of(new YiLinQueryLookupStrategy(this.operations, (ReactiveQueryMethodEvaluationContextProvider) evaluationContextProvider, this.converter, strategy));
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return YiLinR2dbcRepositoryImpl.class;
	}
}
