package com.yilin.reactive.starter.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/6 14:53
 * @since 2024.0.1
 */
@Testcontainers(disabledWithoutDocker = true)
@DataRedisTest
public class AbstractRedisReactiveTestSupport {

	@Container
	static RedisContainer redis = new RedisContainer();

	private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

	private final RedissonReactiveClient redissonClient;


	public AbstractRedisReactiveTestSupport(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
		this.reactiveRedisTemplate = reactiveRedisTemplate;
		this.redissonClient = Redisson.create(config()).reactive();
	}


	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", redis::getHost);
		registry.add("spring.data.redis.port", redis::getFirstMappedPort);
	}

	static Config config() {
		var config = new Config();
		var address = redis.getHost() + ":" + redis.getFirstMappedPort();
		address = address.startsWith("redis://") ? address : "redis://" + address;
		SingleServerConfig serverConfig = config.useSingleServer()
				.setAddress(address);
		return config;
	}

	public ReactiveRedisTemplate<String, String> getReactiveRedisTemplate() {
		return reactiveRedisTemplate;
	}

	public RedissonReactiveClient getRedissonClient() {
		return redissonClient;
	}
}
