package com.yilin.reactive.starter.redis.core;

import org.redisson.api.RCountDownLatchReactive;
import org.redisson.api.RPermitExpirableSemaphoreReactive;
import org.redisson.api.RReadWriteLockReactive;
import org.redisson.api.RSemaphoreReactive;
import org.redisson.api.RedissonReactiveClient;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/7 14:37
 * @since 2024.0.1
 */
public class RedisConcurrentLockOperationsSupport implements RedisConcurrentLockOperations {

	private final RedissonReactiveClient redissonClient;

	public RedisConcurrentLockOperationsSupport(RedissonReactiveClient redissonClient) {
		this.redissonClient = redissonClient;
	}


	@Override
	public RCountDownLatchReactive countDownLatch(String name) {
		return this.redissonClient.getCountDownLatch(name);
	}

	@Override
	public RSemaphoreReactive semaphore(String name) {
		return this.redissonClient.getSemaphore(name);
	}

	@Override
	public RPermitExpirableSemaphoreReactive permitExpirableSemaphore(String name) {
		return this.redissonClient.getPermitExpirableSemaphore(name);
	}

	@Override
	public RReadWriteLockReactive readWriteLock(String name) {
		return this.redissonClient.getReadWriteLock(name);
	}
}
