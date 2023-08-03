package com.yilin.reactive.r2dbc.repository.support;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.ReactiveQueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;

import com.yilin.reactive.r2dbc.repository.NoQueryLookStrategyException;
import com.yilin.reactive.r2dbc.repository.query.LogicDeleteR2dbcQuery;
import com.yilin.reactive.r2dbc.repository.query.StatusR2dbcQuery;
import com.yilin.reactive.r2dbc.repository.query.TenantR2dbcQuery;
import com.yilin.reactive.r2dbc.repository.query.YiLinR2dbcQueryMethod;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/1:09:54
 * @since 2023.0.1
 */
public class YiLinQueryLookupStrategy implements QueryLookupStrategy {

	private final R2dbcEntityOperations entityOperations;

	private final ReactiveQueryMethodEvaluationContextProvider evaluationContextProvider;

	private final R2dbcConverter converter;

	private final Optional<QueryLookupStrategy> queryLookupStrategy;


	public YiLinQueryLookupStrategy(R2dbcEntityOperations entityOperations,
			ReactiveQueryMethodEvaluationContextProvider evaluationContextProvider, R2dbcConverter converter,
			Optional<QueryLookupStrategy> queryLookupStrategy) {
		this.entityOperations = entityOperations;
		this.evaluationContextProvider = evaluationContextProvider;
		this.converter = converter;
		this.queryLookupStrategy = queryLookupStrategy;
	}

	@Override
	public RepositoryQuery resolveQuery(
			Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
		YiLinR2dbcQueryMethod queryMethod = new YiLinR2dbcQueryMethod(method, metadata, factory, this.converter.getMappingContext());

		if (queryMethod.hasAnnotatedLogicDelete()) {
			return new LogicDeleteR2dbcQuery(queryMethod, this.entityOperations, this.converter);
		}
		else if (queryMethod.hasAnnotatedTenant()) {
			return new TenantR2dbcQuery(queryMethod, this.entityOperations, this.converter);
		}
		else if (queryMethod.hasAnnotatedStatus()) {
			return new StatusR2dbcQuery(queryMethod, this.entityOperations, this.converter);
		}
		else {
			if (queryLookupStrategy.isPresent()) {
				return queryLookupStrategy.get().resolveQuery(method, metadata, factory, namedQueries);
			}
		}
		throw new NoQueryLookStrategyException("not has queryLookupStrategy!");
	}

}
