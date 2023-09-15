package com.yilin.reactive.starter.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.Assert;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:Hash 类型的获取与设置
 *
 * @author jiac
 * @version 2023.0.1 2023/8/29:09:42
 * @since 2023.0.1
 */
public class RedisHashOperations<K, V> extends RedisGenericOperations<V> {

	private final ReactiveHashOperations<String, K, V> hashOps;

	public RedisHashOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		super(reactiveRedisTemplate);
		this.hashOps = reactiveRedisTemplate.opsForHash();
	}

	/**
	 * 删除哈希表 key 中的一个或多个指定字段,不存在的字段将被忽略.
	 *
	 * @param key 键 不能为 {@code null}
	 * @param item 项 可以使多个,不能为 {@code null}
	 * @return 被成功移除的字段的数量, 不包括被忽略的字段.
	 * @see <a href="https://redis.io/commands/hdel">Redis Documentation: HDEL</a>
	 * @see <a href="http://doc.redisfans.com/hash/hdel.html">Redis 命令中文文档: HDEL</a>
	 */
	public Mono<Long> hashDel(String key, Object... item) {
		Assert.notNull(key, "key must not be null.");
		return this.hashOps.remove(key, item);
	}

	/**
	 * 查看哈希表 key 中,给定 field 是否存在.
	 *
	 * @param key 键 不能为 {@code null}
	 * @param field 项 不能为 {@code null}
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/hexists">Redis Documentation: HEXISTS</a>
	 * @see <a href="http://doc.redisfans.com/hash/hexists.html">Redis 命令中文文档: HEXISTS</a>
	 */
	public Mono<Boolean> hashExists(String key, K field) {
		Assert.notNull(key, "key must not be null.");
		return this.hashOps.hasKey(key, field);
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值.
	 *
	 * @param key 键 不能为 {@code null}
	 * @param field 项 不能为 {@code null}
	 * @return 给定域的值, 当给定域不存在或是给定 key 不存在时,返回 {@code null} .
	 * @see <a href="https://redis.io/commands/hget">Redis Documentation: HGET</a>
	 * @see <a href="http://doc.redisfans.com/hash/hget.html">Redis 命令中文文档: HGET</a>
	 */
	public Mono<V> hashGet(String key, K field) {
		return this.hashOps.get(key, field);
	}

	/**
	 * 返回存储在 key 处的哈希的所有字段和值。 在返回值中，每个字段名称后面都跟着它的值，因此返回值的长度是哈希大小的两倍.
	 *
	 * @param key 键 不能为 {@code null}
	 * @return 存储在哈希中的字段及其值的列表，或者当 key 不存在时为空列表 .
	 * @see <a href="https://redis.io/commands/hgetall">Redis Documentation: HGETALL</a>
	 * @see <a href="http://doc.redisfans.com/hash/hgetall.html">Redis 命令中文文档: HGETALL</a>
	 */
	public Flux<Entry<K, V>> hashGetAll(String key) {
		return this.hashOps.entries(key);
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment . 增量也可以为负数,相当于对给定域进行减法操作 参考
	 * {@link #hashDecrBy(String, Object, double)} . 如果 key 不存在,一个新的哈希表被创建并执行
	 * HINCRBY 命令. 如果域 field 不存在,那么在执行命令前,域的值被初始化为 0 . 对一个储存字符串值的域 field 执行 HINCRBY
	 * 命令将造成一个错误. 本操作的值被限制在 64 位(bit)有符号数字表示之内.
	 *
	 * @param key 键
	 * @param field 项
	 * @param increment 要增加几(大于0)
	 * @return 哈希表 key 中域 field 的值
	 * @see <a href="https://redis.io/commands/hincrby">Redis Documentation: HINCRBY</a>
	 * @see <a href="http://doc.redisfans.com/hash/hincrby.html">Redis 命令中文文档: HINCRBY</a>
	 */
	public Mono<Double> hashIncrBy(String key, K field, double increment) {
		Assert.notNull(key, "key must not be null.");
		return this.hashOps.increment(key, field, increment);
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment . 增量也可以为正数,相当于对给定域进行加法操作 参考
	 * {@link #hashIncrBy(String, Object, double)}. 如果 key 不存在,一个新的哈希表被创建并执行
	 * HINCRBY 命令. 如果域 field 不存在,那么在执行命令前,域的值被初始化为 0 . 对一个储存字符串值的域 field 执行 HINCRBY
	 * 命令将造成一个错误. 本操作的值被限制在 64 位(bit)有符号数字表示之内.
	 *
	 * @param key 键
	 * @param item 项
	 * @param increment 要减少记(小于0)
	 * @return 哈希表 key 中域 field 的值
	 * @see <a href="https://redis.io/commands/hincrby">Redis Documentation: HINCRBY</a>
	 * @see <a href="http://doc.redisfans.com/hash/hincrby.html">Redis 命令中文文档: HINCRBY</a>
	 */
	public Mono<Double> hashDecrBy(String key, K item, double increment) {
		Assert.notNull(key, "key must not be null.");
		return this.hashOps.increment(key, item, -increment);
	}

	/**
	 * 返回存储在 key 处的哈希表中的所有字段名称.
	 *
	 * @param key 键
	 * @return 哈希表 key 中 field 的名称
	 * @see <a href="https://redis.io/commands/hkeys">Redis Documentation: HKEYS</a>
	 * @see <a href="http://doc.redisfans.com/hash/hkeys.html">Redis 命令中文文档: HKEYS</a>
	 */
	public Flux<K> hashKeys(String key) {
		Assert.notNull(key, "key must not be null.");
		return this.hashOps.keys(key);
	}

	/**
	 * 返回存储在 key 处的哈希中包含的字段数.
	 *
	 * @param key 键
	 * @return 哈希中的字段数，如果 key 不存在，则为 0.
	 * @see <a href="https://redis.io/commands/hlen">Redis Documentation: HLEN</a>
	 * @see <a href="http://doc.redisfans.com/hash/hlen.html">Redis 命令中文文档: HLEN</a>
	 */
	public Mono<Long> hashLen(String key) {
		Assert.notNull(key, "key must not be null.");
		return this.hashOps.size(key);
	}

	/**
	 * 返回哈希表 key 中,一个或多个给定域的值.
	 *
	 * @param key 键
	 * @return 一个包含多个给定域的关联值的表, 表值的排列顺序和给定域参数的请求顺序一样.
	 * @see <a href="https://redis.io/commands/hmget">Redis Documentation: HMGET</a>
	 * @see <a href="http://doc.redisfans.com/hash/hmget.html">Redis 命令中文文档: HMGET</a>
	 */
	public Mono<List<V>> hashMGet(String key, Collection<K> hashKeys) {
		return this.hashOps.multiGet(key, hashKeys);
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中.
	 *
	 * @param key 键
	 * @param map 对应多个键值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/hmset">Redis Documentation: HMSET</a>
	 * @see <a href="http://doc.redisfans.com/hash/hmset.html">Redis 命令中文文档: HMSET</a>
	 */
	public Mono<Boolean> hashMSet(String key, Map<? extends K, ? extends V> map) {
		Assert.notNull(key, "key must not be null.");
		try {
			return this.hashOps.putAll(key, map);
		}
		catch (Exception ex) {
			return Mono.just(false);
		}
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中, 并设置时间.
	 *
	 * @param key 键
	 * @param map 对应多个键值
	 * @param time 时间(秒)
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/hmset">Redis Documentation: HMSET</a>
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @see <a href="http://doc.redisfans.com/hash/hmset.html">Redis 命令中文文档: HMSET</a>
	 * @see <a href="http://doc.redisfans.com/key/expire.html">Redis 命令中文文档: EXPIRE</a>
	 */
	public Mono<Boolean> hashMSet(String key, Map<? extends K, ? extends V> map, long time) {
		Assert.notNull(key, "key must not be null.");
		Assert.isTrue(time > 0, "time must not be null.");
		return this.hashOps.putAll(key, map)
				.then(this.expire(key, time));
	}

	/**
	 * 从存储在 {@code key} 的哈希值返回一个随机哈希键（也称为字段）.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/hrandfield">Redis Documentation: HRANDFIELD</a>
	 * @see <a href="http://doc.redisfans.com/key/hrandfield.html">Redis 命令中文文档: HRANDFIELD</a>
	 */
	public Mono<K> hashRandFieldKey(String key) {
		return this.hashOps.randomKey(key);
	}

	/**
	 * 从存储在 {@code key} 的哈希中返回一个随机 entity.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/hrandfield">Redis Documentation: HRANDFIELD</a>
	 * @see <a href="http://doc.redisfans.com/key/hrandfield.html">Redis 命令中文文档: HRANDFIELD</a>
	 */
	public Mono<Map.Entry<K, V>> hashRandFieldEntity(String key) {
		return this.hashOps.randomEntry(key);
	}

	/**
	 * 从存储在 {@code key} 的哈希中返回随机哈希键（也称为字段）。 如果提供的 {@code count} 参数为正，则返回不同哈希键的列表，上限为 {@code count} 或哈希大小。
	 * 如果 {@code count} 为负数，则行为会发生变化，并且允许该命令多次返回相同的哈希键。 在这种情况下，返回的字段数是指定计数的绝对值。
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of fields to return.
	 * @return /
	 * @see <a href="https://redis.io/commands/hrandfield">Redis Documentation: HRANDFIELD</a>
	 * @see <a href="http://doc.redisfans.com/key/hrandfield.html">Redis 命令中文文档: HRANDFIELD</a>
	 */
	public Flux<K> hashRandFieldKeys(String key, long count) {
		return this.hashOps.randomKeys(key, count);
	}

	/**
	 * 从存储在 {@code key} 的哈希中返回随机条目。 如果提供的 {@code count} 参数为正，则返回不同条目的列表，上限为 {@code count} 或哈希大小。
	 * 如果 {@code count} 为负数，则行为会发生变化，并且允许命令多次返回相同的条目。 在这种情况下，返回的字段数是指定计数的绝对值.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of fields to return.
	 * @return {@literal null} 如果 key 不存在或在 管道/事务 中使用时。
	 * @see <a href="https://redis.io/commands/hrandfield">Redis Documentation: HRANDFIELD</a>
	 * @see <a href="http://doc.redisfans.com/key/hrandfield.html">Redis 命令中文文档: HRANDFIELD</a>
	 */
	public Flux<Map.Entry<K, V>> hashRandFieldEntries(String key, long count) {
		return this.hashOps.randomEntries(key, count);
	}

	/**
	 * 使用 {@link Flux} 迭代 {@code key} 处哈希中的条目。
	 * 由此产生的 {@link Flux} 充当游标，只要订阅者发出请求信号，它就会自行发出 {@code HSCAN} 命令.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return 如果键不存在，则通过一个或一个 {@link Flux#empty() empty flux} 发出 {@link java.util.Map.Entry }.
	 * @throws IllegalArgumentException {@code key} is {@literal null}.
	 * @see <a href="https://redis.io/commands/hscan">Redis Documentation: HSCAN</a>
	 * @see <a href="http://doc.redisfans.com/key/hscan.html">Redis 命令中文文档: HSCAN</a>
	 */
	public Flux<Map.Entry<K, V>> hashScan(String key) {
		return this.hashOps.scan(key);
	}

	/**
	 * 使用 {@link Flux} 在给定 {@link ScanOptions} 的情况下迭代 {@code key} 处的哈希中的 Entry。
	 * 由此产生的 {@link Flux} 充当游标，只要订阅者发出请求信号，它就会自行发出 {@code HSCAN} 命令.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param options 不能为 {@literal null}. 使用 {@link ScanOptions#NONE} 替代.
	 * @return 如果键不存在，则通过一个或一个 {@link Flux#empty() empty flux} 发出 {@link java.util.Map.Entry}。.
	 * @throws IllegalArgumentException when one of the required arguments is {@literal null}.
	 * @see <a href="https://redis.io/commands/hscan">Redis Documentation: HSCAN</a>
	 * @see <a href="http://doc.redisfans.com/key/hscan.html">Redis 命令中文文档: HSCAN</a>
	 */
	public Flux<Map.Entry<K, V>> hashScan(String key, ScanOptions options) {
		return this.hashOps.scan(key, options);
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value .
	 *
	 * @param key 键
	 * @param field 项
	 * @param value 值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/hset">Redis Documentation: HSET</a>
	 * @see <a href="http://doc.redisfans.com/hash/hset.html">Redis 命令中文文档: HSET</a>
	 */
	public Mono<Boolean> hashSet(String key, K field, V value) {
		return this.hashOps.put(key, field, value);
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value, 并设置时间.
	 *
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @param time 时间(秒) 注意:如果已存在的 hash 表有时间,这里将会替换原有的时间
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/hset">Redis Documentation: HSET</a>
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @see <a href="http://doc.redisfans.com/hash/hset.html">Redis 命令中文文档: HSET</a>
	 * @see <a href="http://doc.redisfans.com/key/expire.html">Redis 命令中文文档: EXPIRE</a>
	 */
	public Mono<Boolean> hashSet(String key, K item, V value, long time) {
		Assert.notNull(key, "key must not be null.");
		Assert.isTrue(time > 0, "time must not be null.");
		return this.hashOps.put(key, item, value)
				.then(this.expire(key, time));
	}

	/**
	 * 仅当 hashKey 不存在时才设置哈希 hashKey 的值.
	 *
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/hsetnx">Redis Documentation: HSETNX</a>
	 * @see <a href="http://doc.redisfans.com/key/hsetnx.html">Redis 命令中文文档: HSETNX</a>
	 */
	public Mono<Boolean> hashSetNx(String key, K item, V value) {
		Assert.notNull(key, "key must not be null.");
		return this.hashOps.putIfAbsent(key, item, value);
	}

	/**
	 * 返回存储在 key 处的哈希表中的所有值.
	 *
	 * @param key 键
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/hvals">Redis Documentation: HVALS</a>
	 * @see <a href="http://doc.redisfans.com/key/hvals.html">Redis 命令中文文档: HVALS</a>
	 */
	public Flux<V> hashValues(String key) {
		return this.hashOps.values(key);
	}

	/**
	 * 删除 key.
	 *
	 * @param key 键
	 * @return {@code true} 成功, {@code false} 失败
	 */
	public Mono<Boolean> hashDelete(String key) {
		return this.hashOps.delete(key);
	}

}
