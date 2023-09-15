package com.yilin.reactive.starter.redis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.yilin.reactive.starter.redis.props.YiLinRedisProperties;
import com.yilin.reactive.starter.redis.serializer.ReactiveRedisSerializer;
import com.yilin.reactive.starter.redis.serializer.StringRedisSerializer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/10:17:13
 * @since 2023.0.1
 */
@AutoConfiguration(before = RedisReactiveAutoConfiguration.class)
@EnableConfigurationProperties(YiLinRedisProperties.class)
public class ReactiveRedisConfiguration implements ReactiveRedisSerializer {


	/**
	 * value 值序列化.
	 * @return redisSerializer
	 */
	@Bean
	@ConditionalOnMissingBean(RedisSerializer.class)
	@Override
	public RedisSerializer<Object> redisSerializer(YiLinRedisProperties properties) {
		return ReactiveRedisSerializer.defaultRedisSerializer(properties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "reactiveRedisTemplate")
	@ConditionalOnBean(ReactiveRedisConnectionFactory.class)
	public ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate(
			ReactiveRedisConnectionFactory factory, RedisSerializer<Object> redisSerializer) {
		var context = RedisSerializationContext.newSerializationContext(new StringRedisSerializer())
				.value(redisSerializer)
				.hashValue(redisSerializer)
				.build();
		return new ReactiveRedisTemplate<>(factory, context);
	}

	@Bean
	@ConditionalOnMissingBean(name = "reactiveStringRedisTemplate")
	@ConditionalOnBean(ReactiveRedisConnectionFactory.class)
	public ReactiveStringRedisTemplate reactiveStringRedisTemplate(
			ReactiveRedisConnectionFactory factory) {
		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		RedisSerializationContext<String, String> context = RedisSerializationContext.<String, String>newSerializationContext()
				.key(stringSerializer)
				.value(stringSerializer)
				.hashKey(stringSerializer)
				.hashValue(stringSerializer)
				.build();
		return new ReactiveStringRedisTemplate(factory, context);
	}

}
