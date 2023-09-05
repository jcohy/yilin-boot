package com.yilin.reactive.starter.redis.core;

import java.util.function.Supplier;

import org.redisson.api.RLockReactive;

import com.yilin.reactive.starter.redis.lock.RedisLockCriteria;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/4:11:59
 * @since 2023.0.1
 */
public interface RedisLockOperations extends RedisReactiveOperations {


	/**
	 * 可重入锁
	 *
	 * @param name 名称
	 * @return 返回可重入锁
	 */
	RedissonGenericLock reentrant(String name);

//	/**
//	 * 获取公平锁.
//	 *
//	 * @param name 名称
//	 * @return 返回公平锁
//	 */
//	RLockReactive fairLock(String name);
//
//	/**
//	 * 获取读写锁.
//	 *
//	 * @param name 名称
//	 * @return 返回读写锁
//	 */
//	RReadWriteLockReactive getReadWriteLock(String name);
//
//	/**
//	 * 获取闭锁.
//	 *
//	 * @param name 名称
//	 * @return 返回闭锁
//	 */
//	RCountDownLatchReactive getCountDownLatch(String name);
//
//	/**
//	 * 获取联锁.
//	 *
//	 * @param locks 锁列表
//	 * @return 返回联锁
//	 */
//	RLockReactive getMultiLock(RLockReactive... locks);
//
////	RFencedLock getRFencedLock();
//
//	/**
//	 * 获取红锁.
//	 * 该对象已被弃用。 请改用 RLockReactive 或 RFencedLock。
//	 * @param locks 锁列表
//	 * @return 返回红锁
//	 */
//	RLockReactive getRedLock(RLockReactive... locks);
//
//	/**
//	 * 获取自旋锁.
//	 * @param name 名称
//	 * @return 返回红锁
//	 */
//	RLockReactive getSpinLock(String name);
//
//	/**
//	 * 获取信号量.
//	 *
//	 * @param name 名称
//	 * @return 返回信号量
//	 */
//	RSemaphoreReactive getSemaphore(String name);
//
//	/**
//	 * 获取可过期信号量
//	 *
//	 * @param name 名称
//	 * @return 返回可过期信号量
//	 */
//	RPermitExpirableSemaphoreReactive getPermitExpirableSemaphore(String name);
//
//
//	/**
//	 * 获取可重入锁.
//	 *
//	 * @param name 名称
//	 * @return 返回可重入锁
//	 */
//	RLockReactive getLock(String name);


	interface GenericLockTerminating {
		//		GenericLockClient using();
		GenericLockClient using();
	}

	interface GenericLockCriteria extends GenericLockTerminating {

		GenericLockTerminating apply(RedisLockCriteria criteria);
	}

	/**
	 * 主要针对实现了 {@link RLockReactive} 对象的锁.
	 */
	interface RedissonGenericLock extends GenericLockCriteria {
	}

	interface GenericLockClient {
		/**
		 * 尝试获取锁.
		 *
		 * @return 是否成功
		 * @throws InterruptedException /
		 */
		boolean tryLock() throws InterruptedException;


		/**
		 * 解锁.
		 */
		void unLock();

		/**
		 * 自定获取锁后执行方法.
		 *
		 * @param supplier 获取锁后的回调
		 * @param <T> t
		 * @return 返回的数据
		 */
		<T> T lock(Supplier<T> supplier);
	}
}
