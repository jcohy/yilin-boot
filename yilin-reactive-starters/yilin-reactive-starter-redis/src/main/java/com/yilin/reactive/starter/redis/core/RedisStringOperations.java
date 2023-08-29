package com.yilin.reactive.starter.redis.core;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import reactor.core.publisher.Mono;

import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.BitFieldSubCommands.BitFieldSubCommand;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.util.Assert;

import com.yilin.reactive.utils.Maps;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/16:16:25
 * @since 2023.0.1
 */
public class RedisStringOperations<V> {

	private final ReactiveValueOperations<String, V> valueOps;

	public RedisStringOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		this.valueOps = reactiveRedisTemplate.opsForValue();
	}

	/**
	 * 追加指定 value 到指定 key 上.
	 *
	 * @param key 键
	 * @param value 值
	 * @return 追加 value 之后， key 中字符串的长度.
	 * @see <a href="https://redis.io/commands/append">Redis Documentation: APPEND</a>
	 * @see <a href="http://doc.redisfans.com/string/append.html">Redis 命令中文文档: APPEND</a>
	 */
	public Mono<Long> stringAppend(String key, String value) {
		return (key != null) ? this.valueOps.append(key, value) : Mono.empty();
	}

	/**
	 * 将 key 中储存的数字值减一. 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作.
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内.
	 *
	 * @param key key
	 * @return 执行 DECR 命令之后 key 的值。
	 * @see <a href="https://redis.io/commands/decr">Redis Documentation: DECR</a>
	 * @see <a href="http://doc.redisfans.com/string/decr.html">Redis Documentation:
	 * DECR</a>
	 */
	public Mono<Long> stringDecr(String key) {
		return this.valueOps.decrement(key);
	}

	/**
	 * 将 key 所储存的值减去减量 decrement. 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作.
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内.
	 *
	 * @param key key
	 * @param decrement decrement 值，为 long
	 * @return 减去 decrement 之后， key 的值。
	 * @see <a href="https://redis.io/commands/decrby">Redis Documentation: DECRBY</a>
	 * @see <a href="http://doc.redisfans.com/string/decrby.html">Redis Documentation:
	 * DECRBY</a>
	 */
	public Mono<Long> stringDecrBy(String key, long decrement) {
		return this.valueOps.decrement(key, decrement);
	}

	/**
	 * 获取 key 的值。 如果 key 不存在，则返回特殊值 `nil`。 .
	 *
	 * @param key key
	 * @return key 的值，key 不存在时为 nil.
	 * @see <a href="https://redis.io/commands/get">Redis Documentation: GET</a>
	 * @see <a href="http://doc.redisfans.com/string/get.html">Redis 命令中文文档: GET</a>
	 */
	public Mono<V> stringGet(String key) {
		return this.valueOps.get(key);
	}

	/**
	 * 获取 key 的值并删除 key. .
	 *
	 * @param key key
	 * @return key 的值，key 不存在时为 nil.
	 * @see <a href="https://redis.io/commands/getdel">Redis Documentation: GETDEL</a>
	 * @see <a href="http://doc.redisfans.com/string/GETDEL.html">Redis 命令中文文档: GETDEL</a>
	 */
	public Mono<V> stringGetDel(String key) {
		return this.valueOps.getAndDelete(key);
	}

	/**
	 * 获取 key 的值并可以选择设置其过期时间.
	 *
	 * @param key key
	 * @return key 的值，如果 key 不存在则为 nil.
	 * @see <a href="https://redis.io/commands/getex">Redis Documentation: GETEX</a>
	 * @see <a href="http://doc.redisfans.com/string/GETEX.html">Redis 命令中文文档: GETEX</a>
	 */
	public Mono<V> stringGetEx(String key, Duration duration) {
		return this.valueOps.getAndExpire(key, duration);
	}

	/**
	 * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内).
	 *
	 * @param key key
	 * @return key 的值，如果 key 不存在则为 nil.
	 * @see <a href="https://redis.io/commands/getrange">Redis Documentation: GETRANGE</a>
	 * @see <a href="http://doc.redisfans.com/string/GETRANGE.html">Redis 命令中文文档: GETRANGE</a>
	 */
	public Mono<String> stringGetRange(String key, long start, long end) {
		return this.valueOps.get(key, start, end);
	}

	/**
	 * 以原子方式将 key 设置为 value 并返回存储在 key 中的旧值。.
	 *
	 * @param key key
	 * @return key 中存储的旧值，或者当 key 不存在时为 nil.
	 * @see <a href="https://redis.io/commands/getset">Redis Documentation: GETSET</a>
	 * @see <a href="http://doc.redisfans.com/string/getset.html">Redis 命令中文文档: GETSET</a>
	 */
	public Mono<V> stringGetSet(String key, V value) {
		return this.valueOps.getAndSet(key, value);
	}

	/**
	 * 将 key 中储存的数字值增一. 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作.
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内.
	 *
	 * @param key key
	 * @return 执行 INCR 命令之后 key 的值。
	 * @see <a href="https://redis.io/commands/incr">Redis Documentation: INCR</a>
	 * @see <a href="http://doc.redisfans.com/string/incr.html">Redis Documentation:
	 * INCR</a>
	 */
	public Mono<Long> stringIncr(String key) {
		return this.valueOps.increment(key);
	}

	/**
	 * 将 key 所储存的值加上增量 increment. 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令.
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之.
	 *
	 * @param key key
	 * @param decrement increment 值，为 long
	 * @return 加上 increment 之后， key 的值。
	 * @see <a href="https://redis.io/commands/incrby">Redis Documentation: INCRBY</a>
	 * @see <a href="http://doc.redisfans.com/string/incrby.html">Redis Documentation: INCRBY</a>
	 */
	public Mono<Long> stringIncrBy(String key, long decrement) {
		return this.valueOps.increment(key, decrement);
	}

	/**
	 * 为 key 中所储存的值加上浮点数增量 increment ，可以使用负值表示递减.
	 *
	 * @param key key
	 * @param decrement increment 值，为 dobule
	 * @return 加上 increment 之后， key 的值。
	 * @see <a href="https://redis.io/commands/incrbyfloat">Redis Documentation: INCRBYFLOAT</a>
	 * @see <a href="http://doc.redisfans.com/string/incrbyfloat.html">Redis Documentation: INCRBYFLOAT</a>
	 */
	public Mono<Double> stringIncrByFloat(String key, Double decrement) {
		return this.valueOps.increment(key, decrement);
	}

	/**
	 * 返回所有(一个或多个)给定 key 的值.
	 *
	 * @param keys keys
	 * @return 一个包含所有给定 key 的值的列表.
	 * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
	 * @see <a href="http://doc.redisfans.com/string/mget.html">Redis 命令中文文档: MGET</a>
	 */
	public Mono<List<V>> stringMultiGet(String... keys) {
		return this.valueOps.multiGet(Arrays.asList(keys));
	}

	/**
	 * 返回所有(一个或多个)给定 key 的值.
	 *
	 * @param keys keys
	 * @return 一个包含所有给定 key 的值的列表.
	 * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
	 * @see <a href="http://doc.redisfans.com/string/mget.html">Redis 命令中文文档: MGET</a>
	 */
	public Mono<List<V>> stringMultiGet(Collection<String> keys) {
		return this.valueOps.multiGet(keys);
	}

	/**
	 * 同时设置一个或多个 key-value 对. 如果某个给定 key 已经存在,那么 MSET 会用新值覆盖原来的旧值,如果这不是你所希望的效果,请考虑使用
	 * MSETNX 命令：它只会在所有给定 key 都不存在的情况下进行设置操作. MSET 是一个原子性(atomic)操作,所有给定 key
	 * 都会在同一时间内被设置,某些给定 key 被更新而另一些给定 key 没有改变的情况,不可能发生.
	 *
	 * @param keysValues 键值对
	 * @see <a href="https://redis.io/commands/mset">Redis Documentation: MSET</a>
	 * @see <a href="http://doc.redisfans.com/string/mset.html">Redis 命令中文文档: MGET</a>
	 */
	public final Mono<Boolean> stringMultiSet(Map<String, V> keysValues) {
		return this.valueOps.multiSet(keysValues);
	}

	/**
	 * 同时设置一个或多个 key-value 对. 如果某个给定 key 已经存在,那么 MSET 会用新值覆盖原来的旧值,如果这不是你所希望的效果,请考虑使用
	 * MSETNX 命令：它只会在所有给定 key 都不存在的情况下进行设置操作. MSET 是一个原子性(atomic)操作,所有给定 key
	 * 都会在同一时间内被设置,某些给定 key 被更新而另一些给定 key 没有改变的情况,不可能发生.
	 *
	 * @param keysValues 键值对数组
	 * @see <a href="https://redis.io/commands/mset">Redis Documentation: MSET</a>
	 * @see <a href="http://doc.redisfans.com/string/mset.html">Redis 命令中文文档: MGET</a>
	 */
	public final Mono<Boolean> stringMultiSet(Object... keysValues) {
		return this.stringMultiSet(Maps.toMap(keysValues));
	}

	/**
	 * 设置给定的键和它们对应的值。即使只有一个键已经存在，MSETNX 也不会执行任何操作。
	 *
	 * @param maps 键值对
	 * @see <a href="https://redis.io/commands/msetnx">Redis Documentation: MSETNX</a>
	 * @see <a href="http://doc.redisfans.com/string/msetnx.html">Redis 命令中文文档: MSETNX</a>
	 */
	public final Mono<Boolean> stringMultiSetNx(Map<String, V> maps) {
		return this.valueOps.multiSetIfAbsent(maps);
	}

	/**
	 * 将字符串值 value 关联到 key . 如果 key 已经持有其他值, SET 就覆写旧值,无视类型. 对于某个原本带有生存时间（TTL）的键来说, 当 SET
	 * 命令成功在这个键上执行时, 这个 key 原有的 TTL 将被清除.
	 *
	 * @param key 键
	 * @param value 值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
	 * @see <a href="http://doc.redisfans.com/string/set.html">Redis 命令中文文档: SET</a>
	 */
	public Mono<Boolean> stringSet(String key, V value) {
		Assert.notNull(key, "key must not be null.");
		try {
			return this.valueOps.set(key, value);
		}
		catch (Exception ex) {
			return Mono.just(false);
		}
	}

	public Mono<Boolean> stringSet(Supplier<String> key, V value) {
		return this.valueOps.set(key.get(), value);
	}

	/**
	 * 如果 key 存在，则设置 key 来保存字符串值.
	 *
	 * @param key 键
	 * @param value 值
	 * @return 设置成功, 返回 {@code 1},设置失败,返回 {@code 0}.
	 * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
	 * @see <a href="http://doc.redisfans.com/string/set.html">Redis Documentation: SET</a>
	 */
	public Mono<Boolean> stringSetIfPresent(String key, V value) {
		Assert.notNull(key, "key must not be null.");
		return this.valueOps.setIfPresent(key, value);
	}

	/**
	 * 如果 key 存在，则设置 key 来保存字符串值.
	 *
	 * @param key 键
	 * @param value 值
	 * @return 设置成功, 返回 {@code 1},设置失败,返回 {@code 0}.
	 * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
	 * @see <a href="http://doc.redisfans.com/string/set.html">Redis Documentation: SET</a>
	 */
	public Mono<Boolean> stringSetIfPresent(String key, V value, Duration duration) {
		Assert.notNull(key, "key must not be null.");
		return this.valueOps.setIfPresent(key, value, duration);
	}

	/**
	 * 设置 key 来保存字符串值和过期超时（如果 key 不存在）.
	 *
	 * @param key 键
	 * @param value 值
	 * @return 设置成功, 返回 {@code 1},设置失败,返回 {@code 0}.
	 * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
	 * @see <a href="http://doc.redisfans.com/string/set.html">Redis Documentation: SET</a>
	 */
	public Mono<Boolean> stringSetIfAbsent(String key, V value, Duration duration) {
		Assert.notNull(key, "key must not be null.");
		return this.valueOps.setIfAbsent(key, value, duration);
	}

	/**
	 * 将值 value 关联到 key ,并将 key 的生存时间设为 seconds (以秒为单位). 如果 key 已经存在, SETEX 命令将覆写旧值.
	 *
	 * @param key 键
	 * @param value 值
	 * @param duration 时间(秒) time 要大于 0 如果 time 小于等于 0 将设置无限期
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/setex">Redis Documentation: SETEX</a>
	 * @see <a href="http://doc.redisfans.com/string/setex.html">Redis 命令中文文档: SETEX</a>
	 */
	public Mono<Boolean> stringSetEx(String key, V value, long duration) {
		Assert.notNull(key, "key must not be null.");
		return this.valueOps.set(key, value, Duration.ofSeconds(duration));
	}

	/**
	 * 将值 value 关联到 key ,并将 key 的生存时间设为 seconds (以秒为单位). 如果 key 已经存在, SETEX 命令将覆写旧值.
	 * 从 Redis 版本 2.6.12 开始，此命令被视为已弃用。
	 *
	 * @param key 键
	 * @param value 值
	 * @param duration 时间
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/setex">Redis Documentation: SETEX</a>
	 * @see <a href="http://doc.redisfans.com/string/setex.html">Redis 命令中文文档: SETEX</a>
	 */
	public Mono<Boolean> stringSetEx(String key, V value, Duration duration) {
		Assert.notNull(key, "key must not be null.");
		return this.valueOps.set(key, value, duration);
	}

	/**
	 * 将 key 的值设为 value ,当且仅当 key 不存在.若给定的 key 已经存在,则 SETNX 不做任何动作.
	 * 从 Redis 版本 2.6.12 开始，此命令被视为已弃用。
	 *
	 * @param key 键
	 * @param value 值
	 * @return 设置成功, 返回 {@code 1},设置失败,返回 {@code 0}.
	 * @see <a href="https://redis.io/commands/setnx">Redis Documentation: SETNX</a>
	 * @see <a href="http://doc.redisfans.com/string/setnx.html">Redis Documentation:
	 * SETNX</a>
	 */
	public Mono<Boolean> stringSetNx(String key, V value) {
		Assert.notNull(key, "key must not be null.");
		return this.valueOps.setIfAbsent(key, value);
	}

	/**
	 * 使用给定的 {@code value} 覆盖从指定的 {@code offset} 开始的部分 {@code key}。
	 *
	 * @param key 键
	 * @param value 值
	 * @return 设置成功, 返回 {@code 1},设置失败,返回 {@code 0}.
	 * @see <a href="https://redis.io/commands/setrange">Redis Documentation: SETRANGE</a>
	 * @see <a href="http://doc.redisfans.com/string/setrange.html">Redis Documentation: SETRANGE</a>
	 */
	public Mono<Long> stringSetRange(String key, V value, long offset) {
		Assert.notNull(key, "key must not be null.");
		return this.valueOps.set(key, value, offset);
	}

	/**
	 * 获取存储在 {@code key} 中的值的长度.
	 *
	 * @param key must not be {@literal null}.
	 * @return key 处字符串的长度，如果 key 不存在则为 0.
	 * @see <a href="https://redis.io/commands/strlen">Redis Documentation: STRLEN</a>
	 * @see <a href="http://doc.redisfans.com/string/strlen.html">Redis Documentation: STRLEN</a>
	 */
	public Mono<Long> stringLen(String key) {
		return this.valueOps.size(key);
	}

	/**
	 * 删除 key.
	 *
	 * @param key 键
	 * @return {@code true} 成功, {@code false} 失败
	 */
	public Mono<Boolean> stringDelete(String key) {
		return this.valueOps.delete(key);
	}

	/**
	 * 对 key 所储存的字符串值,获取指定偏移量上的位(bit) 当 offset 比字符串值的长度大,或者 key 不存在时,返回 0 .
	 *
	 * @param key 键
	 * @param offset 偏移量
	 * @return 字符串值指定偏移量上的位(bit).
	 * @see <a href="https://redis.io/commands/getbit">Redis Documentation: GETBIT</a>
	 * @see <a href="http://doc.redisfans.com/string/getbit.html">Redis 命令中文文档: GETBIT</a>
	 */
	public Mono<Boolean> getBit(String key, long offset) {
		return this.valueOps.getBit(key, offset);
	}

	/**
	 * 设置存储在 {@code key} 的值中位于 {@code offset} 的位。
	 *
	 * @param key 键
	 * @param offset 偏移量
	 * @param value 值
	 * @return 存储在 offset 偏移位的原始值.
	 * @see <a href="https://redis.io/commands/setbit">Redis Documentation: SETBIT</a>
	 * @see <a href="http://doc.redisfans.com/string/setbit.html">Redis 命令中文文档: SETBIT</a>
	 */
	public Mono<Boolean> setBit(String key, long offset, boolean value) {
		return this.valueOps.setBit(key, offset, value);
	}

	/**
	 * 获取/操作不同位宽的特定整数字段和存储的任意非（必要）对齐偏移量在给定的 {@code key} 处。
	 *
	 * @param key 键
	 * @param commands 子命令
	 * @return 字符串值指定偏移量上的位(bit).
	 * @see <a href="https://redis.io/commands/bitfield">Redis Documentation: BITFIELD</a>
	 * @see <a href="http://doc.redisfans.com/string/bitfield.html">Redis 命令中文文档: BITFIELD</a>
	 */
	public Mono<List<Long>> bitField(String key, BitFieldSubCommands commands) {
		return this.valueOps.bitField(key, commands);
	}

	/**
	 * 获取/操作不同位宽的特定整数字段和存储的任意非（必要）对齐偏移量在给定的 {@code key} 处。
	 *
	 * @param key 键
	 * @param command 子命令
	 * @return 字符串值指定偏移量上的位(bit).
	 * @see <a href="https://redis.io/commands/bitfield">Redis Documentation: BITFIELD</a>
	 * @see <a href="http://doc.redisfans.com/string/bitfield.html">Redis 命令中文文档: BITFIELD</a>
	 */
	public Mono<List<Long>> bitField(String key, BitFieldSubCommand... command) {
		return this.valueOps.bitField(key, BitFieldSubCommands.create(command));
	}


}
