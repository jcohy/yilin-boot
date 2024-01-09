package com.yilin.reactive.starter.redis;

import java.nio.ByteBuffer;

import org.redisson.api.RedissonReactiveClient;

import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.Assert;

import com.yilin.reactive.starter.redis.core.RedisGeoOperations;
import com.yilin.reactive.starter.redis.core.RedisHashOperations;
import com.yilin.reactive.starter.redis.core.RedisHyperLogLogOperations;
import com.yilin.reactive.starter.redis.core.RedisListOperations;
import com.yilin.reactive.starter.redis.core.RedisLockOperations;
import com.yilin.reactive.starter.redis.core.RedisSetOperations;
import com.yilin.reactive.starter.redis.core.RedisSortedSetOperations;
import com.yilin.reactive.starter.redis.core.RedisStreamOperations;
import com.yilin.reactive.starter.redis.core.RedisStringOperations;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: key 为 String，尽量使用 redis 命令作为方法名.
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/15 17:46
 * @since 2024.0.1
 */
public class ReactiveStringKeyRedisTemplate<K, V> {

	private final ReactiveRedisTemplate<String, V> reactiveRedisTemplate;

	private final RedisGeoOperations<V> geoOps;

	private final RedisHashOperations<K, V> hashOps;

	private final RedisHyperLogLogOperations<V> hllOps;

	private final RedisListOperations<V> listOps;

	private final RedisSetOperations<V> setOps;

	private final RedisStreamOperations<K, V> streamOps;

	private final RedisStringOperations<V> valueOps;

	private final RedisSortedSetOperations<V> zsetOps;

	private final RedisLockOperations lockOps;

	private final ReactiveRedisConnection reactiveRedisConnection;

	private final RedisSerializationContext<String, V> redisSerializationContext;

	public ReactiveStringKeyRedisTemplate(ReactiveRedisTemplate<String, V> reactiveRedisTemplate, RedissonReactiveClient client) {
		Assert.notNull(reactiveRedisTemplate, "reactiveRedisTemplate must be set!");
		this.reactiveRedisTemplate = reactiveRedisTemplate;
		this.geoOps = new RedisGeoOperations<>(reactiveRedisTemplate);
		this.hashOps = new RedisHashOperations<>(reactiveRedisTemplate);
		this.hllOps = new RedisHyperLogLogOperations<>(reactiveRedisTemplate);
		this.listOps = new RedisListOperations<>(reactiveRedisTemplate);
		this.setOps = new RedisSetOperations<>(reactiveRedisTemplate);
		this.streamOps = new RedisStreamOperations<>(reactiveRedisTemplate);
		this.valueOps = new RedisStringOperations<>(reactiveRedisTemplate);
		this.zsetOps = new RedisSortedSetOperations<>(reactiveRedisTemplate);
		this.reactiveRedisConnection = reactiveRedisTemplate.getConnectionFactory().getReactiveConnection();
		this.redisSerializationContext = reactiveRedisTemplate.getSerializationContext();
		this.lockOps = new RedisLockOperations(client);
	}

	public ReactiveRedisTemplate<String, V> reactiveRedisTemplate() {
		return this.reactiveRedisTemplate;
	}

	public RedisGeoOperations<V> geoOps() {
		return this.geoOps;
	}

	public RedisHashOperations<K, V> hashOps() {
		return hashOps;
	}

	public RedisHyperLogLogOperations<V> hllOps() {
		return this.hllOps;
	}

	public RedisListOperations<V> listOps() {
		return this.listOps;
	}

	public RedisSetOperations<V> setOps() {
		return this.setOps;
	}

	public RedisStreamOperations<K, V> streamOps() {
		return this.streamOps;
	}

	public RedisStringOperations<V> stringOps() {
		return this.valueOps;
	}

	public RedisSortedSetOperations<V> zSetOps() {
		return this.zsetOps;
	}

	public RedisLockOperations lockOps() {
		return this.lockOps;
	}

	public ReactiveRedisConnection redisConnection() {
		return this.reactiveRedisConnection;
	}

	public RedisSerializationContext<String, V> serializationContext() {
		return this.redisSerializationContext;
	}

	private ByteBuffer rawKey(String key) {
		return this.redisSerializationContext.getKeySerializationPair().getWriter().write(key);
	}

	private String readKey(ByteBuffer buffer) {
		return this.redisSerializationContext.getKeySerializationPair().getReader().read(buffer);
	}
}
