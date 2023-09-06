package com.yilin.reactive.starter.redis.core;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.redisson.api.RLockReactive;
import reactor.core.publisher.Mono;

import com.yilin.reactive.starter.redis.lock.RedisLockConfigurer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/5:17:03
 * @since 2023.0.1
 */
public interface RedisMultiLockOperations {

	/**
	 * 获取联锁.
	 *
	 * @param locks 锁列表
	 * @return 返回联锁
	 */
	RedissonReactiveMultiLock multiLock(RLockReactive... locks);

	RedissonReactiveMultiLock multiLock(Function<RedisMultiLockConfigure, List<RLockReactive>> locks);

//	RFencedLock getRFencedLock();


	/**
	 * 获取红锁.
	 * 该对象已被弃用。 请改用 RLockReactive 或 RFencedLock。
	 *
	 * @param locks 锁列表
	 * @return 返回红锁
	 */
	RedissonReactiveMultiLock redLock(RLockReactive... locks);


	/**
	 * 配置联锁.
	 */
	interface RedisMultiLockConfigure {
		RedisMultiLockConfigure lock(Function<RedisGenericLockOperations, RLockReactive> lock);

		List<RLockReactive> combine();
	}

	/**
	 * 联锁终止操作.
	 */
	interface RedissonReactiveMultiLockTerminating {

		RedissonReactiveMultiLockClient using();
	}

	/**
	 * 锁属性构建.
	 */
	interface RedissonReactiveMultiLockConfigurer extends RedissonReactiveMultiLockTerminating {

		RedissonReactiveMultiLockTerminating apply(RedisLockConfigurer configurer);
	}

	/**
	 * 主要针对实现了 {@link RLockReactive} 对象的锁.
	 */
	interface RedissonReactiveMultiLock extends RedissonReactiveMultiLockConfigurer {
	}

	interface RedissonReactiveMultiLockClient {

		/**
		 * 自定获取锁后执行方法.
		 *
		 * @param executableSupplier 获取锁后的回调
		 * @return 返回的数据
		 */
		<T> Mono<T> synchronize(Supplier<Mono<T>> executableSupplier);

		RLockReactive getReactiveLock();
	}

}
