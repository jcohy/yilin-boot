package com.yilin.reactive.starter.redis.core;

import java.util.function.Supplier;

import org.redisson.api.RLockReactive;
import org.redisson.api.RedissonReactiveClient;

import org.springframework.util.Assert;

import com.yilin.reactive.starter.redis.lock.RedisLockCriteria;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/4:16:34
 * @since 2023.0.1
 */
public class RedisLockOperationSupport implements RedisLockOperations {

	private final RedissonReactiveClient redissonClient;

	public RedisLockOperationSupport(RedissonReactiveClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public RedissonGenericLock reentrant(String key) {
		Assert.notNull(key, "Redis distributed lock key must not be null;");
		RLockReactive lock = this.redissonClient.getLock(key);
		return new RedissonGenericLockSupport(lock, null);
	}

	static class RedissonGenericLockSupport implements RedissonGenericLock {

		private final RLockReactive lock;

		private final RedisLockCriteria criteria;

		public RedissonGenericLockSupport(RLockReactive lock, RedisLockCriteria criteria) {
			this.lock = lock;
			this.criteria = criteria;
		}

		@Override
		public GenericLockClient using() {
			return new GenericLockClientImpl(this.lock, criteria);
		}

		@Override
		public GenericLockTerminating apply(RedisLockCriteria criteria) {
			return new RedissonGenericLockSupport(this.lock, criteria);
		}

		static class GenericLockClientImpl implements GenericLockClient {

			private final RLockReactive lock;

			private final RedisLockCriteria criteria;

			public GenericLockClientImpl(RLockReactive lock, RedisLockCriteria criteria) {
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
				System.out.println(criteria.toString());
				return null;
			}
		}
	}
}

