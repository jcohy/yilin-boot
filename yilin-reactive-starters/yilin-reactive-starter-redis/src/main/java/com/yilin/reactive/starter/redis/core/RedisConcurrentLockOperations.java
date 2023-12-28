package com.yilin.reactive.starter.redis.core;

import org.redisson.api.RCountDownLatchReactive;
import org.redisson.api.RPermitExpirableSemaphoreReactive;
import org.redisson.api.RReadWriteLockReactive;
import org.redisson.api.RSemaphoreReactive;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/9/5 17:05
 * @since 2023.0.1
 */
public interface RedisConcurrentLockOperations {

	/**
	 * 获取闭锁.
	 *
	 * @param name 名称
	 * @return 返回闭锁
	 */
	RCountDownLatchReactive countDownLatch(String name);


	/**
	 * 获取信号量.
	 *
	 * @param name 名称
	 * @return 返回信号量
	 */
	RSemaphoreReactive semaphore(String name);

	/**
	 * 获取可过期信号量
	 *
	 * @param name 名称
	 * @return 返回可过期信号量
	 */
	RPermitExpirableSemaphoreReactive permitExpirableSemaphore(String name);

	/**
	 * 获取读写锁.
	 *
	 * @param name 名称
	 * @return 返回读写锁
	 */
	RReadWriteLockReactive readWriteLock(String name);
}
