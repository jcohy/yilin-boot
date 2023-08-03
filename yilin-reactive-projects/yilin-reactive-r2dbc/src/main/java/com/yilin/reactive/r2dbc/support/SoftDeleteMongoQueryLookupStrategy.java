package com.yilin.reactive.r2dbc.support;

import java.lang.reflect.Method;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.ReactiveQueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/1:09:54
 * @since 2023.0.1
 */
public class SoftDeleteMongoQueryLookupStrategy implements QueryLookupStrategy {
	private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

	private final R2dbcEntityOperations entityOperations;

	private final ReactiveQueryMethodEvaluationContextProvider evaluationContextProvider;

	private final R2dbcConverter converter;

	private final ReactiveDataAccessStrategy dataAccessStrategy;

	private final QueryLookupStrategy strategy;

	public SoftDeleteMongoQueryLookupStrategy(QueryLookupStrategy strategy, R2dbcEntityOperations entityOperations) {
		this.strategy = strategy;
		this.entityOperations = entityOperations;
	}

	@Override
	public RepositoryQuery resolveQuery(
			Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
		RepositoryQuery repositoryQuery = strategy.resolveQuery(method, metadata, factory, namedQueries);

		// revert to the standard behavior if requested
		if (method.getAnnotation(CanSeeSoftDeletedRecords.class) != null) {
			return repositoryQuery;
		}

		if (!(repositoryQuery instanceof ReactivePartTreeMongoQuery)) {
			return repositoryQuery;
		}
		ReactivePartTreeMongoQuery partTreeQuery = (ReactivePartTreeMongoQuery) repositoryQuery;

		return new SoftDeletePartTreeMongoQuery(
				method, partTreeQuery, this.mongoOperations, EXPRESSION_PARSER, evaluationContextProvider);
	}
}
