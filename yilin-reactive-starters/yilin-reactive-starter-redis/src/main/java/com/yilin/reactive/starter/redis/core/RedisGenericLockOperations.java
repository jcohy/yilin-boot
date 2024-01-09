package com.yilin.reactive.starter.redis.core;

import org.redisson.api.RLockReactive;

import com.yilin.reactive.starter.redis.lock.GenericLockClient;
import com.yilin.reactive.starter.redis.lock.RedisLockConfigurer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/4 11:59
 * @since 2024.0.1
 */
public interface RedisGenericLockOperations extends RedisReactiveOperations {


	/**
	 * 可重入锁
	 *
	 * @param name 名称
	 * @return 返回可重入锁
	 */
	RedissonGenericLock reentrant(String name);

	/**
	 * 获取公平锁.
	 *
	 * @param name 名称
	 * @return 返回公平锁
	 */
	RedissonGenericLock fairLock(String name);

	/**
	 * 获取自旋锁.
	 *
	 * @param name 名称
	 * @return 返回红锁
	 */
	RedissonGenericLock spinLock(String name);

	interface GenericLockTerminating {

		GenericLockClient using();

	}

	interface GenericLockConfigurer extends GenericLockTerminating {

		GenericLockTerminating apply(RedisLockConfigurer configurer);
	}

	/**
	 * 主要针对实现了 {@link RLockReactive} 对象的锁.
	 */
	interface RedissonGenericLock extends GenericLockConfigurer {
	}
}
