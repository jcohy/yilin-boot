package com.yilin.reactive.r2dbc;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: R2DBC 集成测试基类.
 *
 * @author jiac
 * @version 2023.0.1 2023/7/27:10:06
 * @since 2023.0.1
 */
@DataR2dbcTest
@EnableAutoConfiguration
public abstract class YiLinR2dbcRepositoryIntegrationTestSupport {

	/**
	 * 使用 {@link DataSource} 创建一个新的 {@link JdbcTemplate} 实例.
	 * @param dataSource dataSource
	 * @return /
	 */
	protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	/**
	 * 创建本测试使用的 {@link DataSource} .
	 *
	 * @return {@link DataSource}.
	 */
	protected abstract DataSource createDataSource();

}
