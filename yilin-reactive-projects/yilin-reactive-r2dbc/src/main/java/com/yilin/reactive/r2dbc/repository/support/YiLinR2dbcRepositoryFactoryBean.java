package com.yilin.reactive.r2dbc.repository.support;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.r2dbc.core.DatabaseClient;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/31 16:46
 * @since 2024.0.1
 */
public class YiLinR2dbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends R2dbcRepositoryFactoryBean<T, S, ID> {


	/**
	 * Creates a new {@link R2dbcRepositoryFactoryBean} for the given repository interface.
	 *
	 * @param repositoryInterface must not be {@literal null}.
	 */
	public YiLinR2dbcRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}


	@Override
	protected RepositoryFactorySupport getFactoryInstance(@Nonnull R2dbcEntityOperations operations) {
		return new YiLinR2dbcRepositoryFactory(operations);
	}


	@Override
	public void setCustomImplementation(Object customImplementation) {
		super.setCustomImplementation(customImplementation);
	}
}
