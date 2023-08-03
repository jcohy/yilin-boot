package com.yilin.reactive.r2dbc.support;

import java.util.Optional;

import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.r2dbc.core.DatabaseClient;

import com.yilin.reactive.r2dbc.repository.ReactiveRepositoryImpl;

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
public class ReactiveR2dbcRepositoryFactory extends R2dbcRepositoryFactory {

	private final ReactiveDataAccessStrategy dataAccessStrategy;

	private final R2dbcConverter converter;

	private final R2dbcEntityOperations operations;

	public ReactiveR2dbcRepositoryFactory(DatabaseClient databaseClient, ReactiveDataAccessStrategy dataAccessStrategy) {
		this(new R2dbcEntityTemplate(databaseClient, dataAccessStrategy));
	}

	public ReactiveR2dbcRepositoryFactory(R2dbcEntityOperations operations) {
		super(operations);
		this.dataAccessStrategy = operations.getDataAccessStrategy();
		this.converter = dataAccessStrategy.getConverter();
		this.operations = operations;
	}

	@Override
	protected Optional<QueryLookupStrategy> getQueryLookupStrategy(
			QueryLookupStrategy.Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		Optional<QueryLookupStrategy> optStrategy = super.getQueryLookupStrategy(key, evaluationContextProvider);
		return optStrategy.map(this::createSoftDeleteQueryLookupStrategy);
	}

	private SoftDeleteMongoQueryLookupStrategy createSoftDeleteQueryLookupStrategy(QueryLookupStrategy strategy) {
		return new SoftDeleteMongoQueryLookupStrategy(strategy, mongoOperations);
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return ReactiveRepositoryImpl.class;
	}
}
