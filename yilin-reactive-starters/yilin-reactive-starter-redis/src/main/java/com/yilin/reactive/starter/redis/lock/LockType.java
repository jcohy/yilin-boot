package com.yilin.reactive.starter.redis.lock;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: 锁类型
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/1 16:54
 * @since 2024.0.1
 */
public enum LockType {

	/**
	 * 可重入锁.
	 */
	REENTRANT,
	/**
	 * 公平锁.
	 */
	FAIR,
	/**
	 * 读写锁
	 */
	READWRITE,
	MULTIL,
	SEMAPHORE,
	COUNTDOWNLATCH,
	SPIN,
	FENCED;
}
