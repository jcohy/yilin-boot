package com.yilin.reactive.starter.redis.core;

import java.util.function.Supplier;

import org.redisson.api.RLockReactive;
import org.redisson.api.RedissonReactiveClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.util.Assert;

import com.yilin.reactive.starter.redis.lock.RedisLockConfigurer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: Redisson 基本锁操作.
 *
 * @author jiac
 * @version 2023.0.1 2023/9/4:16:34
 * @since 2023.0.1
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

	static class GenericLockClientImpl implements GenericLockClient {

		private static final Logger log = LoggerFactory.getLogger(GenericLockClientImpl.class);

		private final RLockReactive lock;

		private final RedisLockConfigurer configurer;

		public GenericLockClientImpl(RLockReactive lock, RedisLockConfigurer configurer) {
			this.lock = lock;
			this.configurer = configurer;
		}

		@Override
		public <T> Mono<T> synchronize(Supplier<Mono<T>> executableSupplier) {
			var name = lock.getName();
			var threadId = this.configurer.threadId();
			return lock.tryLock(this.configurer.waitTime(), this.configurer.leaseTime(), this.configurer.timeUnit(), threadId)
					.doOnSubscribe(subscription -> log.info("Locked Resource, name = {}, threadId = {}", name, threadId))
					.flatMap(acquired -> {
						if (acquired) {
							return executableSupplier.get()
									.flatMap(result -> {
										return lock.unlock().thenReturn(result);
									});
						}
						else {
							return Mono.error(new RuntimeException("Couldn't get redis lock"));
						}
					})
					.doFinally(signalType -> {
						Mono.fromRunnable(() -> log.info("Unlocked Resource, name = {}, threadId = {}", name, threadId))
								.then(lock.forceUnlock())
								.subscribe();
					});
		}

		@Override
		public RLockReactive getReactiveLock() {
			return this.lock;
		}
	}
}

