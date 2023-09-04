package com.yilin.reactive.r2dbc.testing;

import io.r2dbc.spi.ConnectionFactory;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/9:16:59
 * @since 2023.0.1
 */
public class MysqlTestSupport {

	public static String CREATE_TABLE_PERSON = "CREATE TABLE person (\n" //
			+ "    id   integer AUTO_INCREMENT CONSTRAINT id1 PRIMARY KEY,\n" //
			+ "    version     integer NULL,\n" //
			+ "    name        varchar(255) NOT NULL,\n" //
			+ "    deleted     integer NOT NULL,\n" //
			+ "    status      integer NOT NULL,\n" //
			+ "    age      integer NULL\n" //
			+ ");";

	/**
	 * 创建 {@link ConnectionFactory}.
	 */
	public static ConnectionFactory createConnectionFactory() {

//		return new MySQLConnection(MySQLConnectionBuilder.builder() //
//				.inMemory("r2dbc") //
//				.username("sa") //
//				.password("") //
//				.option("DB_CLOSE_DELAY=-1").build());
		return null;
	}

	/**
	 * 创建 {@link DataSource}.
	 */
	public static DataSource createDataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setUsername("sa");
		dataSource.setPassword("");
		dataSource.setUrl("jdbc:h2:mem:r2dbc;DB_CLOSE_DELAY=-1");
		dataSource.setDriverClassName("org.h2.Driver");

		return dataSource;
	}
}
