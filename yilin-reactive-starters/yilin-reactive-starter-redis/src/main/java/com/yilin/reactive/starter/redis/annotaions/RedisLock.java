package com.yilin.reactive.starter.redis.annotaions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.yilin.reactive.starter.redis.lock.LockType;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/1:16:51
 * @since 2023.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisLock {

	/**
	 * 分布式锁的 key，必须：请保持唯一性.
	 *
	 * @return key
	 */
	String key();

	/**
	 * 分布式锁参数.
	 *
	 * @return param
	 */
	String param() default "";

	/**
	 * 等待锁超时时间，默认 30.
	 *
	 * @return int
	 */
	long waitTime() default 30;

	/**
	 * 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认 100.
	 *
	 * @return int
	 */
	long leaseTime() default 100;

	/**
	 * 时间单位，默认为秒.
	 *
	 * @return 时间单位
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 默认公平锁.
	 *
	 * @return LockType
	 */
	LockType type() default LockType.REENTRANT;

}
