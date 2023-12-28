package com.yilin.reactive.starter.redis.core;

import reactor.core.publisher.Mono;

import org.springframework.data.redis.core.ReactiveHyperLogLogOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: ReactiveStringKeyRedisTemplate.
 *
 * @author jcohy
 * @version 2023.0.1 2023/8/29 09:44
 * @since 2023.0.1
 */
@SuppressWarnings({ "varargs", "unchecked" })
public class RedisHyperLogLogOperations<V> extends RedisGenericOperations<V> {

	private final ReactiveHyperLogLogOperations<String, V> hllOps;

	public RedisHyperLogLogOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		super(reactiveRedisTemplate);
		this.hllOps = reactiveRedisTemplate.opsForHyperLogLog();
	}

	/**
	 * 将给定的 {@literal value} 添加到 {@literal key}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param values 不能为 {@literal null}.
	 * @return 1 至少有一个值被添加到键中； 否则为 0.
	 * @see <a href="https://redis.io/commands/pfadd">Redis Documentation: PFADD</a>
	 * @see <a href="http://doc.redisfans.com/key/pfadd.html">Redis 命令中文文档: PFADD</a>
	 */
	@SafeVarargs
	public final Mono<Long> pfAdd(String key, V... values) {
		return this.hllOps.add(key, values);
	}

	/**
	 * 获取 {@literal key} 中当前元素的数量.
	 *
	 * @param keys 不能为 {@literal null} 或 {@literal empty}.
	 * @return
	 * @see <a href="https://redis.io/commands/pfcount">Redis Documentation: PFCOUNT</a>
	 * @see <a href="http://doc.redisfans.com/key/pfcount.html">Redis 命令中文文档: PFCOUNT</a>
	 */
	public Mono<Long> pfCount(String... keys) {
		return this.hllOps.size(keys);
	}

	/**
	 * 将给定的 {@literal source Keys} 的所有值合并到 {@literal destination} key.
	 *
	 * @param destination key of HyperLogLog to move source keys into.
	 * @param sourceKeys 不能为 {@literal null} 或 {@literal empty}.
	 * @see <a href="https://redis.io/commands/pfmerge">Redis Documentation: PFMERGE</a>
	 * @see <a href="http://doc.redisfans.com/key/pfmerge.html">Redis 命令中文文档: PFMERGE</a>
	 */
	public Mono<Boolean> pfMerge(String destination, String... sourceKeys) {
		return this.hllOps.union(destination, sourceKeys);
	}

	/**
	 * 删除 key.
	 *
	 * @param key 键
	 * @return {@code true} 成功, {@code false} 失败
	 */
	public Mono<Boolean> pfDelete(String key) {
		return this.hllOps.delete(key);
	}

}
