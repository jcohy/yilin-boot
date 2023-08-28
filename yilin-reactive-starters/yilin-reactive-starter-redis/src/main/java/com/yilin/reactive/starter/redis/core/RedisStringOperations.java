package com.yilin.reactive.starter.redis.core;

import org.springframework.data.redis.core.ReactiveValueOperations;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/16:16:25
 * @since 2023.0.1
 */
public class RedisStringOperations<String, V> {

	private final ReactiveValueOperations<String, V> valueOperations;

	public RedisStringOperations(ReactiveValueOperations<String, V> valueOperations) {
		this.valueOperations = valueOperations;
	}
}
