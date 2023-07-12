package com.yilin.config;

import org.flywaydb.core.Flyway;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 描述: 解决 Flyway 不兼容 R2DBC，这意味着在使用 Flyway 进行数据迁移是需要使用 JDBC 阻塞驱动程序，这些迁移是在应用程序启动之前进行更改，所有同步阻塞迁移可以接受.
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2023/7/12 16:19
 * @since 1.0.0
 */
@AutoConfiguration
@EnableConfigurationProperties({ R2dbcProperties.class, FlywayProperties.class })
public class DatabaseConfig {

	@Bean(initMethod = "migrate")
	public Flyway flyway(FlywayProperties flywayProperties, R2dbcProperties r2dbcProperties) {
		return Flyway
				.configure()
				.dataSource(flywayProperties.getUrl(),
						r2dbcProperties.getUsername(),
						r2dbcProperties.getPassword()
				)
				.locations(flywayProperties.getLocations().stream()
						.toArray(String[]::new))
				.baselineOnMigrate(true)
				.load();
	}
}
