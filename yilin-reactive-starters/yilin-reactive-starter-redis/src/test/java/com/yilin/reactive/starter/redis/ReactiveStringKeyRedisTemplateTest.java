package com.yilin.reactive.starter.redis;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/18:16:03
 * @since 2023.0.1
 */
@Testcontainers(disabledWithoutDocker = true)
@DataRedisTest
public class ReactiveStringKeyRedisTemplateTest {


	@Container
	static RedisContainer redis = new RedisContainer();

	static {
		Hooks.onOperatorDebug();
	}

	private final ReactiveStringKeyRedisTemplate<String, String> template;

	@Autowired
	private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

	@Autowired
	public ReactiveStringKeyRedisTemplateTest(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
		this.reactiveRedisTemplate = reactiveRedisTemplate;
		this.template = new ReactiveStringKeyRedisTemplate<>(reactiveRedisTemplate);
	}

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", redis::getHost);
		registry.add("spring.data.redis.port", redis::getFirstMappedPort);
	}

	@Test
	void scan() {

		Map<String, String> tuples = new LinkedHashMap<>(Map.of("k1", "v1", "k2", "v2", "k3", "v3", "k4", "v4", "k5", "v5", "k6", "v6", "k7", "v7", "k8", "v8", "k9", "v9", "k10", "v10"));
		reactiveRedisTemplate.opsForValue().multiSet(tuples)
				.as(StepVerifier::create)
				.expectNext(true)
				.verifyComplete();
//		template.scanKeys("*",1,5)
//						.subscribe(System.out::println);
//		template.scanKeys("*",1,5)
//				.as(StepVerifier::create)
//				.expectNextCount(5)
//				.verifyComplete();
	}

}