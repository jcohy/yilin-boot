package com.yilin.reactive.starter.redis;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: key 为 String.
 *
 * @author jiac
 * @version 2023.0.1 2023/8/15:17:46
 * @since 2023.0.1
 */
public class ReactiveStringKeyRedisTemplate<String, V> extends ReactiveRedisTemplate<String, V> {

	private static final Logger logger = LoggerFactory.getLogger(ReactiveStringKeyRedisTemplate.class);

	public ReactiveStringKeyRedisTemplate(ReactiveRedisConnectionFactory connectionFactory, RedisSerializationContext<String, V> serializationContext) {
		super(connectionFactory, serializationContext);
	}

	// ============================= Key（键） ============================

	/**
	 * 删除给定的一个或多个 key .
	 *
	 * @param keys 可以传一个值 或多个
	 * @return 返回个数
	 * @see <a href="https://redis.io/commands/del">Redis Documentation: DEL</a>
	 * @see <a href="http://doc.redisfans.com/key/del.html">Redis 命令中文文档: DEL</a>
	 */
	public Mono<Long> delete(Collection<String> keys) {
		return super.delete(Flux.fromIterable(keys));
	}

}
