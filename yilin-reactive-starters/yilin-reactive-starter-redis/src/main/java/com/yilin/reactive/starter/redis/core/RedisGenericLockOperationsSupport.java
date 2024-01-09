package com.yilin.reactive.starter.redis.core;

import java.util.function.Supplier;

import org.redisson.api.RLockReactive;
import org.redisson.api.RedissonReactiveClient;
import reactor.core.publisher.Mono;

import org.springframework.util.Assert;

import com.yilin.reactive.starter.redis.lock.GenericLockClient;
import com.yilin.reactive.starter.redis.lock.RedisLockConfigurer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: Redisson 基本锁操作.
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/4 16:34
 * @since 2024.0.1
 */
public class RedisGenericLockOperationsSupport implements RedisGenericLockOperations {

	private final RedissonReactiveClient redissonClient;

	public RedisGenericLockOperationsSupport(RedissonReactiveClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public RedissonGenericLock reentrant(String name) {
		Assert.notNull(name, "Redis distributed lock name must not be null;");
		return new RedissonGenericLockSupport(this.redissonClient.getLock(name), null);
	}

	@Override
	public RedissonGenericLock fairLock(String name) {
		Assert.notNull(name, "Redis distributed lock name must not be null;");
		return new RedissonGenericLockSupport(this.redissonClient.getLock(name), null);
	}

	public RedissonGenericLock spinLock(String name) {
		Assert.notNull(name, "Redis distributed lock name must not be null;");
		return new RedissonGenericLockSupport(this.redissonClient.getSpinLock(name), null);
	}

	static class RedissonGenericLockSupport implements RedissonGenericLock {

		private final RLockReactive lock;

		private final RedisLockConfigurer configurer;

		public RedissonGenericLockSupport(RLockReactive lock, RedisLockConfigurer configurer) {
			this.lock = lock;
			this.configurer = configurer;
		}

		@Override
		public GenericLockClient using() {
			return new GenericLockClientImpl(this.lock, configurer);
		}

		@Override
		public GenericLockTerminating apply(RedisLockConfigurer configurer) {
			return new RedissonGenericLockSupport(this.lock, configurer);
		}
	}

	static class GenericLockClientImpl implements com.yilin.reactive.starter.redis.lock.GenericLockClient {

		private final RLockReactive lock;

		private final RedisLockConfigurer configurer;

		public GenericLockClientImpl(RLockReactive lock, RedisLockConfigurer configurer) {
			this.lock = lock;
			this.configurer = configurer;
		}


		@Override
		public <T> Mono<T> synchronize(Supplier<Mono<T>> executableSupplier) {
			var name = lock.getName();
			return synchronize(this.lock,
					name,
					this.configurer,
					executableSupplier);
		}

		@Override
		public RLockReactive getReactiveLock() {
			return this.lock;
		}
	}
}

