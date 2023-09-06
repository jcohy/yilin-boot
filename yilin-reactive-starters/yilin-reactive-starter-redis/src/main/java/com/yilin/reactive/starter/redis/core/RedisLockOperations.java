package com.yilin.reactive.starter.redis.core;

import java.util.List;
import java.util.function.Function;

import org.redisson.api.RLockReactive;
import org.redisson.api.RedissonReactiveClient;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/5:16:49
 * @since 2023.0.1
 */
public class RedisLockOperations implements RedisGenericLockOperations, RedisMultiLockOperations {

	private final RedissonReactiveClient client;

	public RedisLockOperations(RedissonReactiveClient client) {
		this.client = client;
	}

	@Override
	public RedissonGenericLock reentrant(String name) {
		return new RedisGenericLockOperationsSupport(this.client).reentrant(name);
	}

	@Override
	public RedissonGenericLock fairLock(String name) {
		return new RedisGenericLockOperationsSupport(this.client).fairLock(name);
	}

	@Override
	public RedissonGenericLock spinLock(String name) {
		return new RedisGenericLockOperationsSupport(this.client).spinLock(name);
	}

	@Override
	public RedissonReactiveMultiLock multiLock(RLockReactive... locks) {
		return new RedisMultiLockOperationsSupport(this.client).multiLock(locks);
	}

	@Override
	public RedissonReactiveMultiLock redLock(RLockReactive... locks) {
		return new RedisMultiLockOperationsSupport(this.client).redLock(locks);
	}

	@Override
	public RedissonReactiveMultiLock multiLock(Function<RedisMultiLockConfigure, List<RLockReactive>> consumer) {
		return new RedisMultiLockOperationsSupport(this.client).multiLock(consumer);
	}
}
