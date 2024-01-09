package com.yilin.reactive.starter.redis.lock;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/6 15:41
 * @since 2024.0.1
 */
public class LockSupport {

	private AtomicLong atomicLong = new AtomicLong(5000);

	public long decrement() {
		long l = atomicLong.decrementAndGet();
		return atomicLong.decrementAndGet();
	}

	public Long getAtomicLong() {
		return atomicLong.longValue();
	}
}
