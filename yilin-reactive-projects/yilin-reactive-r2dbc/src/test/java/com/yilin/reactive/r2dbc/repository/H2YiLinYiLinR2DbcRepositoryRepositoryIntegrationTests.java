package com.yilin.reactive.r2dbc.repository;

import io.r2dbc.spi.ConnectionFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

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

	@Configuration
	static class IntegrationTestConfiguration extends AbstractR2dbcConfiguration {
		@Override
		public ConnectionFactory connectionFactory() {
			return H2TestSupport.createConnectionFactory();
		}
	}
}
