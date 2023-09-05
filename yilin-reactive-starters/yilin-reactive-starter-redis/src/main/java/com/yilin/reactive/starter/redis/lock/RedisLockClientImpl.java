package com.yilin.reactive.starter.redis.lock;

import java.util.function.Supplier;

import org.redisson.api.RLockReactive;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/2:15:57
 * @since 2023.0.1
 */
public class RedisLockClientImpl implements RedisLockClient {

	private final RLockReactive lock;

	private final RedisLockCriteria criteria;

	public RedisLockClientImpl(RLockReactive lock, RedisLockCriteria criteria) {
		this.lock = lock;
		this.criteria = criteria;
	}

	@Override
	public boolean tryLock() throws InterruptedException {
		return false;
	}

	@Override
	public void unLock() {
		lock.unlock();
	}

	@Override
	public <T> T lock(Supplier<T> supplier) {
		return null;
	}
}
