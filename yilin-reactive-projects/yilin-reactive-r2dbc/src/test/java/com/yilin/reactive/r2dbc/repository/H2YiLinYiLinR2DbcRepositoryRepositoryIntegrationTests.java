package com.yilin.reactive.r2dbc.repository;

import io.r2dbc.spi.ConnectionFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.dialect.H2Dialect;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;

import com.yilin.reactive.r2dbc.testing.H2TestSupport;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/28:15:37
 * @since 2023.0.1
 */
public class H2YiLinYiLinR2DbcRepositoryRepositoryIntegrationTests extends AbstractYiLinYiLinR2DbcRepositoryRepositoryIntegrationTests {

	@Override
	protected String getCreateTableStatement() {
		return H2TestSupport.CREATE_TABLE_PERSON;
	}

	@Override
	protected R2dbcDialect createDialect() {
		return H2Dialect.INSTANCE;
	}

	@Override
	protected DataSource createDataSource() {
		return H2TestSupport.createDataSource();
	}

	@Configuration
	static class IntegrationTestConfiguration extends AbstractR2dbcConfiguration {
		@Override
		public ConnectionFactory connectionFactory() {
			return H2TestSupport.createConnectionFactory();
		}
	}
}
