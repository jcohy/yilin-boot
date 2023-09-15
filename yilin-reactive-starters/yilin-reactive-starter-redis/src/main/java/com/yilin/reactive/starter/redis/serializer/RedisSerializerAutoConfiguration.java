package com.yilin.reactive.starter.redis.serializer;

import com.alibaba.fastjson2.JSON;
import io.protostuff.Schema;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/11:14:33
 * @since 2023.0.1
 */
@AutoConfiguration(before = ReactiveRedisTemplate.class)
public class RedisSerializerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(Schema.class)
	@ConditionalOnProperty(name = "yilin.redis.serializer-type", havingValue = "protostuff")
	public RedisSerializer<Object> protoStuffSerializer() {
		return new ProtoStuffSerializer();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(JSON.class)
	@ConditionalOnProperty(name = "yilin.redis.serializer-type", havingValue = "fastjson2")
	public RedisSerializer<Object> redisSerializer() {
		return new FastJson2RedisSerializer<>(Object.class);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "yilin.redis.serializer-type", havingValue = "jdk")
	public RedisSerializer<Object> jdkSerializer() {
		ClassLoader classLoader = ReactiveRedisSerializer.class.getClassLoader();
		return new JdkSerializationRedisSerializer(classLoader);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "yilin.redis.serializer-type", havingValue = "gson")
	public RedisSerializer<Object> gsonSerializer() {
		return new GsonSerializer<>(Object.class);
	}
}
