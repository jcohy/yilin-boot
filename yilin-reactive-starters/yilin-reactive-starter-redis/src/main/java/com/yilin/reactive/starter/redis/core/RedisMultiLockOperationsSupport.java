package com.yilin.reactive.starter.redis.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.redisson.api.RLockReactive;
import org.redisson.api.RedissonReactiveClient;
import reactor.core.publisher.Mono;

import com.yilin.reactive.starter.redis.lock.GenericLockClient;
import com.yilin.reactive.starter.redis.lock.RedisLockConfigurer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: 联锁
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/6 10:01
 * @since 2024.0.1
 */
public class RedisMultiLockOperationsSupport extends RedisGenericLockOperationsSupport implements RedisMultiLockOperations {

	private final RedissonReactiveClient redissonClient;

	public RedisMultiLockOperationsSupport(RedissonReactiveClient redissonClient) {
		super(redissonClient);
		this.redissonClient = redissonClient;
	}

	@Override
	public RedissonReactiveMultiLock multiLock(RLockReactive... locks) {
		this.redissonClient.getMultiLock(locks);
		return new RedissonReactiveMultiLockSupport(this.redissonClient.getMultiLock(locks), Arrays.asList(locks), null);
	}

	@Override
	public RedissonReactiveMultiLock redLock(RLockReactive... locks) {
		return new RedissonReactiveMultiLockSupport(this.redissonClient.getMultiLock(locks), Arrays.asList(locks), null);
	}


	@Override
	public RedissonReactiveMultiLock multiLock(Function<RedisMultiLockConfigure, List<RLockReactive>> locks) {
		List<RLockReactive> multiLock = locks.apply(new RedisMultiLockConfigureImpl(this.redissonClient));

		return new RedissonReactiveMultiLockSupport(this.redissonClient.getMultiLock(multiLock.toArray(new RLockReactive[0])), multiLock, null);
	}

	static class RedissonReactiveMultiLockSupport implements RedissonReactiveMultiLock {

		private final List<RLockReactive> locks;

		private final RLockReactive lock;

		private final RedisLockConfigurer configurer;


		public RedissonReactiveMultiLockSupport(RLockReactive lock, List<RLockReactive> locks, RedisLockConfigurer configurer) {
			this.lock = lock;
			this.locks = locks;
			this.configurer = configurer;
		}

		@Override
		public GenericLockClient using() {
			return new RedissonReactiveMultiLockClientImpl(this.lock, this.locks, configurer);
		}

		@Override
		public RedissonReactiveMultiLockTerminating apply(RedisLockConfigurer configurer) {
			return new RedissonReactiveMultiLockSupport(this.lock, this.locks, configurer);
		}
	}

	static class RedissonReactiveMultiLockClientImpl implements GenericLockClient {

		private final RLockReactive lock;

		private final RedisLockConfigurer configurer;

		private final List<RLockReactive> locks;

		public RedissonReactiveMultiLockClientImpl(RLockReactive lock, List<RLockReactive> locks, RedisLockConfigurer configurer) {
			this.lock = lock;
			this.locks = locks;
			this.configurer = configurer;
		}

		@Override
		public <T> Mono<T> synchronize(Supplier<Mono<T>> executableSupplier) {
			var name = locks.stream().map(RLockReactive::getName)
					.reduce("", (a, b) -> a + "-" + b);
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

	static class RedisMultiLockConfigureImpl implements RedisMultiLockConfigure {

		private final List<RLockReactive> locks = new ArrayList<>();

		private final RedissonReactiveClient redissonClient;

		public RedisMultiLockConfigureImpl(RedissonReactiveClient redissonClient) {
			this.redissonClient = redissonClient;
		}

		@Override
		public RedisMultiLockConfigure lock(Function<RedisGenericLockOperations, RLockReactive> locksFunction) {
			locks.add(locksFunction.apply(new RedisGenericLockOperationsSupport(this.redissonClient)));
			return this;
		}

		@Override
		public List<RLockReactive> combine() {
			return this.locks;
		}
	}
}
