package com.yilin.reactive.r2dbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/8:10:22
 * @since 2023.0.1
 */
@Component
public class YiLinReactiveDataClient {

	public static final Logger log = LoggerFactory.getLogger(YiLinReactiveDataClient.class);

	private final String QUERY_ENTITY_NAME = "entity";

	private DatabaseClient client;

}
