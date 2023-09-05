package com.yilin.reactive.starter.redis.lock;

import java.util.concurrent.TimeUnit;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/4:18:21
 * @since 2023.0.1
 */
public class RedisLockCriteria {

	/**
	 * 等待时间.
	 */
	private String name;

	/**
	 * 等待时间.
	 */
	private long waitTime;

	/**
	 * 自动解锁时间，自动解锁时间一定得大于方法执行时间
	 */
	private long leaseTime;

	/**
	 * 时间参数.
	 */
	private TimeUnit timeUnit;

	private long threadId;

	public static RedisLockCriteria crete() {
		return new RedisLockCriteria();
	}

	public RedisLockCriteria name(String name) {
		this.name = name;
		return this;
	}

	public RedisLockCriteria threadId(long threadId) {
		this.threadId = threadId;
		return this;
	}

	public RedisLockCriteria waitTime(long waitTime) {
		this.waitTime = waitTime;
		return this;
	}

	public RedisLockCriteria leaseTime(long leaseTime) {
		this.leaseTime = leaseTime;
		return this;
	}

	public RedisLockCriteria timeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
		return this;
	}

}
