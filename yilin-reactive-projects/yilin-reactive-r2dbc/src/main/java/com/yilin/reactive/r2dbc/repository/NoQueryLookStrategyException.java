package com.yilin.reactive.r2dbc.repository;

import org.springframework.dao.DataAccessException;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/3 17:29
 * @since 2024.0.1
 */
public class NoQueryLookStrategyException extends DataAccessException {
	public NoQueryLookStrategyException(String msg) {
		super(msg);
	}

	public NoQueryLookStrategyException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
