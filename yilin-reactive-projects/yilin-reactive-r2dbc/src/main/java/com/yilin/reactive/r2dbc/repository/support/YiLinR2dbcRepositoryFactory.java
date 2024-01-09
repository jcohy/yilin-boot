package com.yilin.reactive.r2dbc.repository.support;

import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.r2dbc.core.DatabaseClient;

import com.yilin.reactive.r2dbc.repository.YiLinR2dbcRepositoryImpl;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/31 16:50
 * @since 2024.0.1
 */
public class YiLinR2dbcRepositoryFactory extends R2dbcRepositoryFactory {

	public YiLinR2dbcRepositoryFactory(DatabaseClient databaseClient, R2dbcDialect dialect, R2dbcConverter converter) {
		this(new R2dbcEntityTemplate(databaseClient, dialect,converter));
	}

	public YiLinR2dbcRepositoryFactory(R2dbcEntityOperations operations) {
		super(operations);
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return YiLinR2dbcRepositoryImpl.class;
	}


}
