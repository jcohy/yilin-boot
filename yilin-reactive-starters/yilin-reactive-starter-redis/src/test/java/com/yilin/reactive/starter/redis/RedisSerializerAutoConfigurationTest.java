package com.yilin.reactive.starter.redis;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.yilin.reactive.starter.redis.serializer.FastJson2RedisSerializer;
import com.yilin.reactive.starter.redis.serializer.GsonSerializer;
import com.yilin.reactive.starter.redis.serializer.ProtoStuffSerializer;
import com.yilin.reactive.starter.redis.serializer.RedisSerializerAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/11 16:26
 * @since 2024.0.1
 */

public class RedisSerializerAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(RedisSerializerAutoConfiguration.class));

	@Test
	void redisSerializerWithJdk() {
		contextRunner.withPropertyValues("yilin.redis.serializer-type:jdk")
				.run((context) -> {
					assertThat(context).hasSingleBean(JdkSerializationRedisSerializer.class);
					assertThat(context).doesNotHaveBean(ProtoStuffSerializer.class);
					assertThat(context).doesNotHaveBean(FastJson2RedisSerializer.class);
					assertThat(context).doesNotHaveBean(GsonSerializer.class);
					assertThat(context).doesNotHaveBean(GenericJackson2JsonRedisSerializer.class);
				});
	}

	@Test
	void redisSerializerWithProtostuff() {
		contextRunner.withPropertyValues("yilin.redis.serializer-type:protostuff")
				.run((context) -> {
					var bean = context.getBean(RedisSerializer.class);
					assertThat(context).hasSingleBean(ProtoStuffSerializer.class);
				});
	}

	@Test
	void redisSerializerWithFastjson2() {
		contextRunner.withPropertyValues("yilin.redis.serializer-type:fastjson2")
				.run((context) -> {
					var bean = context.getBean(RedisSerializer.class);
					assertThat(context).hasSingleBean(FastJson2RedisSerializer.class);
				});
	}

	@Test
	void redisSerializerWithGson() {
		contextRunner.withPropertyValues("yilin.redis.serializer-type:gson")
				.run((context) -> {
					var bean = context.getBean(RedisSerializer.class);
					assertThat(context).hasSingleBean(GsonSerializer.class);
				});
	}
}
