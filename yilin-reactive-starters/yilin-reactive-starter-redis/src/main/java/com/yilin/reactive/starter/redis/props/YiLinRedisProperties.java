package com.yilin.reactive.starter.redis.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/10:17:14
 * @since 2023.0.1
 */
@ConfigurationProperties("yilin.redis")
public class YiLinRedisProperties {

	/**
	 * 序列化方式.
	 */
	private SerializerType serializerType;

	/**
	 * 分布式锁配置.
	 */
	private YiLinRedisLock lock;

	public SerializerType getSerializerType() {
		return serializerType;
	}

	public YiLinRedisProperties setSerializerType(SerializerType serializerType) {
		this.serializerType = serializerType;
		return this;
	}

	public YiLinRedisLock getLock() {
		return lock;
	}

	public YiLinRedisProperties setLock(YiLinRedisLock lock) {
		this.lock = lock;
		return this;
	}

	public enum SerializerType {

		/**
		 * PROTOSTUFF 序列化.
		 */
		PROTOSTUFF,
		/**
		 * FASTJSON2f 序列化.
		 */
		FASTJSON2,
		/**
		 * 默认:JACKSON 序列化.
		 */
		JACKSON,
		/**
		 * GSON 序列化.
		 */
		GSON,
		/**
		 * jdk 序列化.
		 */
		JDK

	}

}
