package com.yilin.reactive.starter.redis.lock;

import java.util.function.Supplier;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/2:15:31
 * @since 2023.0.1
 */
public interface RedisLockClient {


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
