package com.yilin.reactive.starter.redis.lock;

import java.util.concurrent.TimeUnit;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/4 18:21
 * @since 2024.0.1
 */
public class RedisLockConfigurer {

	/**
	 * 名称.
	 */
	private String name;

	/**
	 * 等待时间.
	 */
	private long waitTime = 30;

	/**
	 * 自动解锁时间，自动解锁时间一定得大于方法执行时间
	 */
	private long leaseTime = 100;

	/**
	 * 时间参数.
	 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	public static RedisLockConfigurer crete() {
		return new RedisLockConfigurer();
	}

	public RedisLockConfigurer name(String name) {
		this.name = name;
		return this;
	}

	public RedisLockConfigurer waitTime(long waitTime) {
		this.waitTime = waitTime;
		return this;
	}

	public RedisLockConfigurer leaseTime(long leaseTime) {
		this.leaseTime = leaseTime;
		return this;
	}

	public RedisLockConfigurer timeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
		return this;
	}

	public String name() {
		return this.name;
	}

	public long waitTime() {
		return this.waitTime;
	}

	public long leaseTime() {
		return this.leaseTime;
	}

	public TimeUnit timeUnit() {
		return this.timeUnit;
	}

	@Override
	public String toString() {
		return "RedisLockConfigurer{" +
				"name='" + name + '\'' +
				", waitTime=" + waitTime +
				", leaseTime=" + leaseTime +
				", timeUnit=" + timeUnit +
				'}';
	}
}
