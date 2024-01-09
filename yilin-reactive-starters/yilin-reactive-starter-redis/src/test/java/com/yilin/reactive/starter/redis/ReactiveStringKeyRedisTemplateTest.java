package com.yilin.reactive.starter.redis;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.BitFieldSubCommands.BitFieldGet;
import org.springframework.data.redis.connection.BitFieldSubCommands.BitFieldIncrBy;
import org.springframework.data.redis.connection.BitFieldSubCommands.BitFieldIncrBy.Overflow;
import org.springframework.data.redis.connection.BitFieldSubCommands.BitFieldSet;
import org.springframework.data.redis.connection.BitFieldSubCommands.BitFieldType;
import org.springframework.data.redis.connection.BitFieldSubCommands.Offset;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;


/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/18 16:03
 * @since 2024.0.1
 */

public class ReactiveStringKeyRedisTemplateTest extends AbstractRedisReactiveTestSupport {

	static {
		Hooks.onOperatorDebug();
	}

	private final ReactiveStringKeyRedisTemplate<String, String> template;


	@Autowired
	public ReactiveStringKeyRedisTemplateTest(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
		super(reactiveRedisTemplate);
		this.template = new ReactiveStringKeyRedisTemplate<>(reactiveRedisTemplate, Redisson.create().reactive());
	}


	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", redis::getHost);
		registry.add("spring.data.redis.port", redis::getFirstMappedPort);
	}

	@Test
	void scan() {

		Map<String, String> tuples = new LinkedHashMap<>(Map.of("k1", "v1", "k2", "v2", "k3", "v3", "k4", "v4", "k5", "v5", "k6", "v6", "k7", "v7", "k8", "v8", "k9", "v9", "k10", "v10"));
		template.stringOps().stringMultiSet(tuples)
				.as(StepVerifier::create)
				.expectNext(true)
				.verifyComplete();
	}

	@Test
	void bitField() {
		BitFieldType type = BitFieldType.unsigned(1);
		Offset offset = Offset.offset(1);

		// method1 createWithCreate
		BitFieldGet subGetCommand = BitFieldGet.create(type, offset);
		BitFieldSet subSetCommand = BitFieldSet.create(type, offset, 1);
		BitFieldIncrBy subIncrByCommand = BitFieldIncrBy.create(type, offset, 1);
		BitFieldIncrBy subIncrByCommand2 = BitFieldIncrBy.create(type, offset, 1, Overflow.FAIL);
		BitFieldSubCommands createWithCreate = BitFieldSubCommands.create(subGetCommand, subSetCommand, subIncrByCommand, subIncrByCommand2);
		// method2 createWithBuilder
		BitFieldSubCommands createWithBuilder = BitFieldSubCommands.create()
				.get(type).valueAt(offset)
				.set(type).valueAt(offset).to(1)
				.incr(type).valueAt(offset).by(1);

	}

	@Test
	void stringSetWithSupplier() {
		template.stringOps().stringSet(() -> "Jcohy", "2")
				.as(StepVerifier::create)
				.expectNext(true)
				.verifyComplete();
	}

	@Test
	void hashMSet() {

	}

	@Test
	void hashSet() {

	}
}