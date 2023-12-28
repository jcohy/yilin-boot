package com.yilin.reactive.starter.redis.core;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Tuple;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveZSetOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/8/29 09:49
 * @since 2023.0.1
 */
public class RedisSortedSetOperations<V> extends RedisGenericOperations<V> {

	private final ReactiveZSetOperations<String, V> zsetOps;

	public RedisSortedSetOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		super(reactiveRedisTemplate);
		this.zsetOps = reactiveRedisTemplate.opsForZSet();
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中. 如果某个 member 已经是有序集的成员,那么更新这个 member 的
	 * score 值,并通过重新插入这个 member 元素,来保证该 member 在正确的位置上.
	 *
	 * @param key 如果 key 不存在,则创建一个空的有序集并执行 ZADD 操作.当 key 存在但不是有序集类型时,返回一个错误.
	 * @param member 元素
	 * @param score score 值可以是整数值或双精度浮点数.
	 * @return 被成功添加的新成员的数量, 不包括那些被更新的、已经存在的成员.
	 * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zadd.html">Redis 命令中文文档: ZADD</a>
	 */
	public Mono<Boolean> add(String key, V member, double score) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.add(key, member, score);
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中. 如果某个 member 已经是有序集的成员,那么更新这个 member 的
	 * score 值,并通过重新插入这个 member 元素,来保证该 member 在正确的位置上.
	 *
	 * @param key 如果 key 不存在,则创建一个空的有序集并执行 ZADD 操作.当 key 存在但不是有序集类型时,返回一个错误.
	 * @param scoreMembers 元素
	 * @return 被成功添加的新成员的数量, 不包括那些被更新的、已经存在的成员.
	 * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zadd.html">Redis 命令中文文档: ZADD</a>
	 */
	public Mono<Long> add(String key, Map<V, Double> scoreMembers) {
		Assert.notNull(key, "key must not be null.");
		Set<TypedTuple<V>> tuples = new HashSet<>();
		scoreMembers.forEach((k, v) -> tuples.add(new DefaultTypedTuple<>(k, v)));
		return this.zsetOps.addAll(key, tuples);
	}

	/**
	 * 返回有序集 key 的个数.
	 *
	 * @param key 如果 key 不存在,则创建一个空的有序集并执行 ZADD 操作.当 key 存在但不是有序集类型时,返回一个错误.
	 * @return 当 key 存在且是有序集类型时,返回有序集的个数.当 key 不存在时,返回 0 .
	 * @see <a href="https://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zcard.html">Redis 命令中文文档:
	 * ZCARD</a>
	 */
	public Mono<Long> card(String key) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.size(key);
	}

	/**
	 * 返回有序集 key 中, score 值在 {@code min} 和 {@code max} 之间(默认包括 score 值等于 min 或 max
	 * )的成员的数量. 关于参数 min 和 max 的详细使用方法,请参考 <a href=
	 * "http://doc.redisfans.com/sorted_set/zrangebyscore.html#zrangebyscore">ZRANGEBYSCORE</a>.
	 *
	 * @param key key
	 * @param min 最小值
	 * @param max 最大值
	 * @return score 值在 min 和 max 之间的成员的数量.
	 * @see <a href="https://redis.io/commands/zcount">Redis Documentation: ZCOUNT</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zadd.html">Redis 命令中文文档:
	 * ZCOUNT</a>
	 */
	public Mono<Long> count(String key, double min, double max) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.count(key, Range.closed(min, max));
	}

	/**
	 * sorted {@code sets} 差异.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zdiff">Redis Documentation: ZDIFF</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zdiff.html">Redis 命令中文文档: ZDIFF</a>
	 */
	public Flux<V> difference(String key, String otherKey) {
		return difference(key, Collections.singleton(otherKey));
	}

	/**
	 * sorted {@code sets} 差异.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zdiff">Redis Documentation: ZDIFF</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zdiff.html">Redis 命令中文文档: ZDIFF</a>
	 */
	public Flux<V> difference(String key, Collection<String> otherKeys) {
		return this.zsetOps.difference(key, otherKeys);
	}


	/**
	 * sorted {@code sets} 差异.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zdiff">Redis Documentation: ZDIFF</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zdiff.html">Redis 命令中文文档: ZDIFF</a>
	 */
	public Flux<TypedTuple<V>> differenceWithScores(String key, String otherKey) {
		return differenceWithScores(key, Collections.singleton(otherKey));
	}

	/**
	 * sorted {@code sets} 差异.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zdiff">Redis Documentation: ZDIFF</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zdiff.html">Redis 命令中文文档: ZDIFF</a>
	 */
	public Flux<TypedTuple<V>> differenceWithScores(String key, Collection<String> otherKeys) {
		return this.zsetOps.differenceWithScores(key, otherKeys);
	}

	/**
	 * 对排序后的 {@code set} 进行比较并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zdiffstore">Redis Documentation: ZDIFFSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zdiffstore.html">Redis 命令中文文档: ZDIFFSTORE</a>
	 */
	public Mono<Long> differenceStore(String key, String otherKey, String destKey) {
		return differenceStore(key, Collections.singleton(otherKey), destKey);
	}


	/**
	 * 对排序后的 {@code set} 进行比较并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zdiffstore">Redis Documentation: ZDIFFSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zdiffstore.html">Redis 命令中文文档: ZDIFFSTORE</a>
	 */
	public Mono<Long> differenceStore(String key, Collection<String> otherKeys, String destKey) {
		return this.zsetOps.differenceAndStore(key, otherKeys, destKey);
	}

	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 {@code increment} . 可以通过传递一个负数值 {@code increment}
	 * ,让 score 减去相应的值
	 * <p>
	 * ZINCRBY key -5 member ,就是让 member 的 score 值减去 5 .
	 *
	 * @param key 当 key 不存在,或 member 不是 key 的成员时, ZINCRBY key increment member 等同于 ZADD
	 * key increment member .当 key 不是有序集类型时,返回一个错误.
	 * @param member 元素
	 * @param score score 值可以是整数值或双精度浮点数.
	 * @return member 成员的新 score 值,以字符串形式表示.
	 * @see <a href="https://redis.io/commands/zincrby">Redis Documentation: ZINCRBY</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zincrby.html">Redis 命令中文文档: ZINCRBY</a>
	 */
	public Mono<Double> incrBy(String key, V member, double score) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.incrementScore(key, member, score);
	}

	/**
	 * 交集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinter.html">Redis 命令中文文档: ZINTER</a>
	 */
	public Flux<V> inter(String key, String otherKey) {
		return inter(key, Collections.singleton(otherKey));
	}

	/**
	 * 交集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinter.html">Redis 命令中文文档: ZINTER</a>
	 */
	public Flux<V> inter(String key, Collection<String> otherKeys) {
		return this.zsetOps.intersect(key, otherKeys);
	}

	/**
	 * 交集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinter.html">Redis 命令中文文档: ZINTER</a>
	 */
	public Flux<TypedTuple<V>> interWithScores(String key, String otherKey) {
		return interWithScores(key, Collections.singleton(otherKey));
	}

	/**
	 * 交集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinter.html">Redis 命令中文文档: ZINTER</a>
	 */
	public Flux<TypedTuple<V>> interWithScores(String key, Collection<String> otherKeys) {
		return this.zsetOps.intersectWithScores(key, otherKeys);
	}

	/**
	 * 在 {@code key} 和 {@code otherKeys} 处交集有序集 .
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param aggregate 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinter.html">Redis 命令中文文档: ZINTER</a>
	 */
	public Flux<TypedTuple<V>> interWithScores(String key, Collection<String> otherKeys, Aggregate aggregate) {
		return interWithScores(key, otherKeys, aggregate, Weights.fromSetCount(1 + otherKeys.size()));
	}

	/**
	 * 交集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param aggregate 不能为 {@literal null}.
	 * @param weights 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinter.html">Redis 命令中文文档: ZINTER</a>
	 */
	public Flux<TypedTuple<V>> interWithScores(String key, Collection<String> otherKeys, Aggregate aggregate, Weights weights) {
		return this.zsetOps.intersectWithScores(key, otherKeys, aggregate, weights);
	}

	/**
	 * 在 {@code key} 和 {@code otherKey} 处相交有序集并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinterstore.html">Redis 命令中文文档: ZINTERSTORE</a>
	 */
	public Mono<Long> interStore(String key, String otherKey, String destKey) {
		return interStore(key, Collections.singleton(otherKey), destKey);
	}

	/**
	 * 在 {@code key} 和 {@code otherKeys} 处相交有序集并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinterstore.html">Redis 命令中文文档: ZINTERSTORE</a>
	 */
	public Mono<Long> interStore(String key, Collection<String> otherKeys, String destKey) {
		return this.zsetOps.intersectAndStore(key, otherKeys, destKey);
	}

	/**
	 * 在 {@code key} 和 {@code otherKeys} 处相交有序集并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @param aggregate 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinterstore.html">Redis 命令中文文档: ZINTERSTORE</a>
	 */
	public Mono<Long> interStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate) {
		return interStore(key, otherKeys, destKey, aggregate, Weights.fromSetCount(1 + otherKeys.size()));
	}

	/**
	 * 在 {@code key} 和 {@code otherKeys} 处相交有序集并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @param aggregate 不能为 {@literal null}.
	 * @param weights 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zinterstore.html">Redis 命令中文文档: ZINTERSTORE</a>
	 */
	public Mono<Long> interStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate, Weights weights) {
		return this.zsetOps.intersectAndStore(key, otherKeys, destKey, aggregate, weights);
	}

	/**
	 * 应用字典顺序计算有序集中值介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间的元素数量	.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}
	 * @return /
	 * @see <a href="https://redis.io/commands/ZLEXCOUNT">Redis Documentation: ZLEXCOUNT</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/ZLEXCOUNT.html">Redis 命令中文文档: ZLEXCOUNT</a>
	 */
	public Mono<Long> lexCount(String key, Range<String> range) {
		return this.zsetOps.lexCount(key, range);
	}

	/**
	 * 从键为 {@code key} 的有序集中获取具有 {@code value} 的元素的分数.
	 *
	 * @param key key 不存在,返回 {@code null}
	 * @param o values.
	 * @return /
	 * @see <a href="https://redis.io/commands/zmscore">Redis Documentation: ZMSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zmscore.html">Redis 命令中文文档:
	 * ZMSCORE</a>
	 */
	public Mono<List<Double>> membersScore(String key, Object... o) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.score(key, o);
	}


	/**
	 * 删除并返回 {@code key} 处有序集中得分最高的值.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zpopmax">Redis Documentation: ZPOPMAX</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zpopmax.html">Redis 命令中文文档: ZPOPMAX</a>
	 */
	public Mono<TypedTuple<V>> popMax(String key) {
		return this.zsetOps.popMax(key);
	}

	/**
	 * 删除并返回 {@code count} 值，其分数在 {@code key} 处的有序集中具有最高分数.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of elements to pop.
	 * @return /
	 * @see <a href="https://redis.io/commands/zpopmax">Redis Documentation: ZPOPMAX</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zpopmax.html">Redis 命令中文文档: ZPOPMAX</a>
	 */
	public Flux<TypedTuple<V>> popMax(String key, long count) {
		return this.zsetOps.popMax(key, count);
	}


	/**
	 * 删除并返回 {@code key} 处有序集中得分最高的值.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param timeout 等待列表中 {@code key} 处的条目可用的最长持续时间。 必须为 {@link Duration#ZERO} 或更大的 {@link 1 秒}，
	 * 不能为 {@literal null}。 超时为零可用于无限期等待。 不支持零到一秒之间的持续时间.
	 * @return /
	 * @see <a href="https://redis.io/commands/zpopmax">Redis Documentation: ZPOPMAX</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zpopmax.html">Redis 命令中文文档: ZPOPMAX</a>
	 */
	public Mono<TypedTuple<V>> popMax(String key, Duration timeout) {
		return this.zsetOps.popMax(key, timeout);
	}

	/**
	 * 删除并返回 {@code key} 处有序集中得分最低的值.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zpopmin">Redis Documentation: ZPOPMIN</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zpopmin.html">Redis 命令中文文档: ZPOPMIN</a>
	 */
	public Mono<TypedTuple<V>> popMin(String key) {
		return this.zsetOps.popMin(key);
	}

	/**
	 * 删除并返回 {@code count} 值，其分数在 {@code key} 处的有序集中具有最低分数.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of elements to pop.
	 * @return /
	 * @see <a href="https://redis.io/commands/zpopmin">Redis Documentation: ZPOPMIN</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zpopmin.html">Redis 命令中文文档: ZPOPMIN</a>
	 */
	public Flux<TypedTuple<V>> popMin(String key, long count) {
		return this.zsetOps.popMin(key, count);
	}

	/**
	 * 删除并返回 {@code key} 处有序集中得分最低的值.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param timeout 等待列表中 {@code key} 处的条目可用的最长持续时间。 必须为 {@link Duration#ZERO} 或更大的 {@link 1 秒}，
	 * 不能为 {@literal null}。 超时为零可用于无限期等待。 不支持零到一秒之间的持续时间.
	 * @return /
	 * @see <a href="https://redis.io/commands/zpopmin">Redis Documentation: ZPOPMIN</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zpopmin.html">Redis 命令中文文档: ZPOPMIN</a>
	 */
	public Mono<TypedTuple<V>> popMin(String key, Duration timeout) {
		return this.zsetOps.popMin(key, timeout);
	}

	/**
	 * 从 {@code key} 处的集合中获取随机元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrandmember.html">Redis 命令中文文档: ZRANDMEMBER</a>
	 */
	public Mono<V> randomMember(String key) {
		return this.zsetOps.randomMember(key);
	}

	/**
	 * 从 {@code key} 处的集合中获取 {@code count} 个不同的随机元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of members to return.
	 * @return /
	 * @throws IllegalArgumentException if count is negative.
	 * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrandmember.html">Redis 命令中文文档: ZRANDMEMBER</a>
	 */
	public Flux<V> randomMembersDistinct(String key, long count) {
		return this.zsetOps.distinctRandomMembers(key, count);
	}

	/**
	 * 从 {@code key} 处的集合中获取 {@code count} 个随机元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of members to return.
	 * @return /
	 * @throws IllegalArgumentException if count is negative.
	 * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrandmember.html">Redis 命令中文文档: ZRANDMEMBER</a>
	 */
	public Flux<V> randomMembers(String key, long count) {
		return this.zsetOps.randomMembers(key, count);
	}

	/**
	 * 从 {@code key} 处的集合中获取随机元素及其分数.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrandmember.html">Redis 命令中文文档: ZRANDMEMBER</a>
	 */
	public Mono<TypedTuple<V>> randomMemberWithScore(String key) {
		return this.zsetOps.randomMemberWithScore(key);
	}

	/**
	 * 获取 {@code count} 个不同的随机元素及其在 {@code key} 处设置的分数.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of members to return.
	 * @return /
	 * @throws IllegalArgumentException if count is negative.
	 * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrandmember.html">Redis 命令中文文档: ZRANDMEMBER</a>
	 */
	public Flux<TypedTuple<V>> randomMembersDistinctWithScore(String key, long count) {
		return this.zsetOps.distinctRandomMembersWithScore(key, count);
	}

	/**
	 * 从 {@code key} 处的集合中获取 {@code count} 个随机元素及其分数.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of members to return.
	 * @return /
	 * @throws IllegalArgumentException if count is negative.
	 * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrandmember.html">Redis 命令中文文档: ZRANDMEMBER</a>
	 */
	public Flux<TypedTuple<V>> randomMembersWithScore(String key, long count) {
		return this.zsetOps.randomMembersWithScore(key, count);
	}

	/**
	 * 返回有序集 key 中,指定区间内的成员. 其中成员的位置按 score 值递增(从小到大)来排序.如果需要按 score 值递减(从大到小),可以使用
	 * {@link #reverseRange(String, Range)} 下标参数 {@code start} 和 {@code stop}
	 * 都以 0 为底,也就是说,以 0 表示有序集第一个成员,以 1 表示有序集第二个成员,以此类推. 你也可以使用负数下标,以 -1 表示最后一个成员, -2
	 * 表示倒数第二个成员,以此类推.
	 *
	 * @param key key
	 * @param start 开始位置
	 * @param end 结束位置
	 * @return 指定区间内, 带有 score 值(可选)的有序集成员的列表.
	 * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrange.html">Redis 命令中文文档: ZRANGE</a>
	 */
	public Flux<V> range(String key, long start, long end) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.range(key, Range.closed(start, end));
	}

	/**
	 * 从有序集中获取 {@code start} 和 {@code end} 之间的元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrange.html">Redis 命令中文文档: ZRANGE</a>
	 */
	public Flux<V> range(String key, Range<Long> range) {
		return this.zsetOps.range(key, range);
	}

	/**
	 * 从有序集中获取 {@code start} 和 {@code end} 之间的 {@link Tuple} 集合.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrange.html">Redis 命令中文文档: ZRANGE</a>
	 */
	public Flux<TypedTuple<V>> rangeWithScore(String key, Range<Long> range) {
		return this.zsetOps.rangeWithScores(key, range);
	}

	/**
	 * 返回有序集 key 中,所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员.有序集成员按 score
	 * 值递增(从小到大)次序排列. 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的,不需要额外的计算).
	 *
	 * @param key 如果 key 不存在,则创建一个空的有序集并执行 ZADD 操作.当 key 存在但不是有序集类型时,返回一个错误.
	 * @param min score 最小值
	 * @param max score 最大值
	 * @return 指定区间内, 带有 score 值(可选)的有序集成员的列表.
	 * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation:
	 * ZRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangebyscore.html">Redis 命令中文文档:
	 * ZRANGEBYSCORE</a>
	 */
	public Flux<V> rangeByScore(String key, double min, double max) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.rangeByScore(key, Range.closed(min, max));
	}

	/**
	 * 从有序集中获取从 {@code start} 到 {@code end} 范围内的元素，其中分数介于 {@code min} 和 {@code max} 之间.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangebyscore.html">Redis 命令中文文档: ZRANGEBYSCORE</a>
	 */
	public Flux<V> rangeByScore(String key, Range<Double> range, Limit limit) {
		return this.zsetOps.rangeByScore(key, range, limit);
	}

	/**
	 * 从有序集中获取分数介于 {@code min} 和 {@code max} 之间的 {@link Tuple} 集合.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangebyscore.html">Redis 命令中文文档: ZRANGEBYSCORE</a>
	 */
	public Flux<TypedTuple<V>> rangeByScoreWithScores(String key, Range<Double> range) {
		return this.zsetOps.rangeByScoreWithScores(key, range);
	}

	/**
	 * 从有序集中获取从 {@code start} 到 {@code end} 范围内的 {@link Tuple} 集合，其中分数介于 {@code min} 和 {@code max} 之间.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangebyscore.html">Redis 命令中文文档: ZRANGEBYSCORE</a>
	 */
	public Flux<TypedTuple<V>> rangeByScoreWithScores(String key, Range<Double> range, Limit limit) {
		return this.zsetOps.rangeByScoreWithScores(key, range, limit);
	}

	/**
	 * 将所有元素存储在 {@code dstKey} 中，并按字典顺序从 {@code srcKey} 中的 {@literal ZSET} 开始，其值介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param srcKey 不能为 {@literal null}.
	 * @param dstKey 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return 存储元素的数量.
	 * @see <a href="https://redis.io/commands/zrangestore">Redis Documentation: ZRANGESTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangestore.html">Redis 命令中文文档: ZRANGESTORE</a>
	 */
	public Mono<Long> rangeStoreByLex(String srcKey, String dstKey, Range<String> range) {
		return rangeStoreByLex(srcKey, dstKey, range, Limit.unlimited());
	}

	/**
	 * 将 {@literal n} 个元素存储在 {@code dstKey} 处，其中 {@literal n = } {@link Limit#getCount()}，从 {@link Limit#getOffset()} 开始，按 {@literal ZSET 的字典顺序排列 }
	 * 在 {@code srcKey} 处，其值介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param srcKey 不能为 {@literal null}.
	 * @param dstKey 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return 存储元素的数量.
	 * @see <a href="https://redis.io/commands/zrangestore">Redis Documentation: ZRANGESTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangestore.html">Redis 命令中文文档: ZRANGESTORE</a>
	 */
	public Mono<Long> rangeStoreByLex(String srcKey, String dstKey, Range<String> range, Limit limit) {
		return this.zsetOps.rangeAndStoreByLex(srcKey, dstKey, range, limit);
	}

	/**
	 * 将所有元素存储在 {@code dstKey} 中，并按照 {@code srcKey} 处的 {@literal ZSET} 的逆序字典顺序存储，其值介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param srcKey 不能为 {@literal null}.
	 * @param dstKey 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return 存储元素的数量.
	 * @see <a href="https://redis.io/commands/zrangestore">Redis Documentation: ZRANGESTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangestore.html">Redis 命令中文文档: ZRANGESTORE</a>
	 */
	public Mono<Long> reverseRangeStoreByLex(String srcKey, String dstKey, Range<String> range) {
		return reverseRangeStoreByLex(srcKey, dstKey, range, Limit.unlimited());
	}

	/**
	 * 将 {@literal n} 个元素存储在 {@code dstKey} 处，其中 {@literal n = } {@link Limit#getCount()}，从 {@link Limit#getOffset()} 开始，按照与 {@literal 相反的字典顺序
	 * 排列 {@code srcKey} 处的 ZSET}，其值介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param srcKey 不能为 {@literal null}.
	 * @param dstKey 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return 存储元素的数量.
	 * @see #removeRangeByLex(String, Range)
	 * @see <a href="https://redis.io/commands/zrangestore">Redis Documentation: ZRANGESTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangestore.html">Redis 命令中文文档: ZRANGESTORE</a>
	 * @since 3.0
	 */
	public Mono<Long> reverseRangeStoreByLex(String srcKey, String dstKey, Range<String> range, Limit limit) {
		return this.zsetOps.reverseRangeAndStoreByLex(srcKey, dstKey, range, limit);
	}


	/**
	 * 将所有元素存储在 {@code dstKey} 中，并按 {@code srcKey} 处的 {@literal ZSET} 的分数排序，分数介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param srcKey 不能为 {@literal null}.
	 * @param dstKey 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return 存储元素的数量.
	 * @see <a href="https://redis.io/commands/zrangestore">Redis Documentation: ZRANGESTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangestore.html">Redis 命令中文文档: ZRANGESTORE</a>
	 */
	@Nullable
	public Mono<Long> rangeStoreByScore(String srcKey, String dstKey, Range<Double> range) {
		return rangeStoreByScore(srcKey, dstKey, range, Limit.unlimited());
	}

	/**
	 * 将 {@literal n} 个元素存储在 {@code dstKey} 处，其中 {@literal n = } {@link Limit#getCount()}，从 {@link Limit#getOffset()} 开始，
	 * 按分数排序 {@code srcKey} 处的 ZSET} 得分介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param srcKey 不能为 {@literal null}.
	 * @param dstKey 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return 存储元素的数量.
	 * @see <a href="https://redis.io/commands/zrangestore">Redis Documentation: ZRANGESTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangestore.html">Redis 命令中文文档: ZRANGESTORE</a>
	 */
	public Mono<Long> rangeStoreByScore(String srcKey, String dstKey, Range<Double> range, Limit limit) {
		return this.zsetOps.rangeAndStoreByScore(srcKey, dstKey, range, limit);
	}


	/**
	 * 将所有元素存储在 {@code dstKey} 中，并按 {@code srcKey} 处的 {@literal ZSET} 的得分进行反向排序，得分介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param srcKey 不能为 {@literal null}.
	 * @param dstKey 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return 存储元素的数量.
	 * @see <a href="https://redis.io/commands/zrangestore">Redis Documentation: ZRANGESTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangestore.html">Redis 命令中文文档: ZRANGESTORE</a>
	 */
	public Mono<Long> reverseRangeStoreByScore(String srcKey, String dstKey, Range<Double> range) {
		return reverseRangeStoreByScore(srcKey, dstKey, range, Limit.unlimited());
	}

	/**
	 * 将 {@literal n} 个元素存储在 {@code dstKey} 处，其中 {@literal n = } {@link Limit#getCount()}，从 {@link Limit#getOffset()} 开始，按分数反向排序
	 * {@code srcKey} 处的文字 ZSET}，分数介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param srcKey 不能为 {@literal null}.
	 * @param dstKey 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return the number of stored elements.
	 * @see <a href="https://redis.io/commands/zrangestore">Redis Documentation: ZRANGESTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangestore.html">Redis 命令中文文档: ZRANGESTORE</a>
	 */
	public Mono<Long> reverseRangeStoreByScore(String srcKey, String dstKey, Range<Double> range, Limit limit) {
		return this.zsetOps.reverseRangeAndStoreByScore(srcKey, dstKey, range, limit);
	}

	/**
	 * 从 {@code key} 处的 {@literal ZSET} 获取按字典顺序排列的所有元素，其值介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @see <a href="https://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangebylex.html">Redis 命令中文文档: ZRANGEBYLEX</a>
	 */
	public Flux<V> rangeByLex(String key, Range<String> range) {
		return this.zsetOps.rangeByLex(key, range);
	}

	/**
	 * 获取所有元素 {@literal n} 个元素，其中 {@literal n = } {@link Limit#getCount()}，从 {@link Limit#getOffset()} 开始，按字典顺序从 {@literal ZSET} 处 {@code key} 的值介于 {@link Range#getLowerBound()}
	 * 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param key 不能为 {@literal null}
	 * @param range 不能为 {@literal null}.
	 * @param limit can be {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrangebylex.html">Redis 命令中文文档: ZRANGEBYLEX</a>
	 */
	public Flux<V> rangeByLex(String key, Range<String> range, Limit limit) {
		return this.zsetOps.rangeByLex(key, range, limit);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名.其中有序集成员按 score 值递增(从小到大)顺序排列. 排名以 0 为底,也就是说, score
	 * 值最小的成员排名为 0.
	 *
	 * @param key key
	 * @param member 元素
	 * @return 如果 member 是有序集 key 的成员,返回 member 的排名.如果 member 不是有序集 key 的成员,返回
	 * {@code null}
	 * @see <a href="https://redis.io/commands/zrank">Redis Documentation: ZRANK</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrank.html">Redis 命令中文文档:
	 * ZRANK</a>
	 */
	public Mono<Long> rank(String key, V member) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.rank(key, member);
	}

	/**
	 * 移除有序集 key 中的一个或多个成员,不存在的成员将被忽略.
	 * <p>
	 * <b>在 Redis 2.4 版本以前, ZREM 每次只能删除一个元素.</b>
	 * </p>
	 *
	 * @param key key 存在但不是有序集类型时,返回一个错误.
	 * @param members 元素
	 * @return 被成功移除的成员的数量, 不包括被忽略的成员.
	 * @see <a href="https://redis.io/commands/zrem">Redis Documentation: ZREM</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrem.html">Redis 命令中文文档: ZREM</a>
	 */
	public Mono<Long> remove(String key, Object... members) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.remove(key, members);
	}

	/**
	 * 使用 {@code key} 从排序集中删除 {@code start} 和 {@code end} 之间的元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zremrangebyrank">Redis Documentation: ZREMRANGEBYRANK</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zremrangebyrank.html">Redis 命令中文文档: ZREMRANGEBYRANK</a>
	 */
	public Mono<Long> removeRangeByRank(String key, Range<Long> range) {
		return this.zsetOps.removeRange(key, range);
	}

	/**
	 * 使用 {@code key} 从排序集中删除范围内的元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return a {@link Mono} emitting the number or removed elements.
	 * @see <a href="https://redis.io/commands/zremrangebylex">Redis Documentation: ZREMRANGEBYLEX</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zremrangebylex.html">Redis 命令中文文档: ZREMRANGEBYLEX</a>
	 */
	public Mono<Long> removeRangeByLex(String key, Range<String> range) {
		return this.zsetOps.removeRangeByLex(key, range);
	}

	/**
	 * 使用 {@code key} 从排序集中删除分数介于 {@code min} 和 {@code max} 之间的元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zremrangebyscore">Redis Documentation: ZREMRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zremrangebyscore.html">Redis 命令中文文档: ZREMRANGEBYSCORE</a>
	 */
	public Mono<Long> removeRangeByScore(String key, Range<Double> range) {
		return this.zsetOps.removeRangeByScore(key, range);
	}

	/**
	 * 从从高到低排序的有序集中获取从 {@code start} 到 {@code end} 范围内的元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrange.html">Redis 命令中文文档: ZREVRANGE</a>
	 */
	public Flux<V> reverseRange(String key, Range<Long> range) {
		return this.zsetOps.reverseRange(key, range);
	}

	/**
	 * 从从高到低排序的有序集中获取从 {@code start} 到 {@code end} 范围内的 {@link Tuple} 集合.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrange.html">Redis 命令中文文档: ZREVRANGE</a>
	 */
	public Flux<TypedTuple<V>> reverseRangeWithScores(String key, Range<Long> range) {
		return this.zsetOps.reverseRangeWithScores(key, range);
	}

	/**
	 * 从从高到低排序的排序集中获取分数介于 {@code min} 和 {@code max} 之间的元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrangebyscore.html">Redis 命令中文文档: ZREVRANGEBYSCORE</a>
	 */
	public Flux<V> reverseRangeByScore(String key, Range<Double> range) {
		return this.zsetOps.reverseRangeByScore(key, range);
	}

	/**
	 * 从从高到低排序的排序集中获取分数介于 {@code min} 和 {@code max} 之间的 {@link Tuple} 集合.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrangebyscore.html">Redis 命令中文文档: ZREVRANGEBYSCORE</a>
	 */
	public Flux<TypedTuple<V>> reverseRangeByScoreWithScores(String key, Range<Double> range) {
		return this.zsetOps.reverseRangeByScoreWithScores(key, range);
	}


	/**
	 * 获取从 {@code start} 到 {@code end} 范围内的元素，其中分数介于 {@code min} 和 {@code max} 之间，从排序集中按高 -> 低排序.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrangebyscore.html">Redis 命令中文文档: ZREVRANGEBYSCORE</a>
	 */
	public Flux<V> reverseRangeByScore(String key, Range<Double> range, Limit limit) {
		return this.zsetOps.reverseRangeByScore(key, range, limit);
	}

	/**
	 * 获取从 {@code start} 到 {@code end} 范围内的 {@link Tuple} 集合，其中分数介于 {@code min} 和 {@code max} 之间（从排序集中排序高 -> 低）.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrangebyscore.html">Redis 命令中文文档: ZREVRANGEBYSCORE</a>
	 */
	public Flux<TypedTuple<V>> reverseRangeByScoreWithScores(String key, Range<Double> range, Limit limit) {
		return this.zsetOps.reverseRangeByScoreWithScores(key, range, limit);
	}


	/**
	 * 从 {@code key} 处的 {@literal ZSET} 获取具有逆字典顺序的所有元素，
	 * 其值介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param range 不能为 {@literal null}.
	 * @see <a href="https://redis.io/commands/zrevrangebylex">Redis Documentation: ZREVRANGEBYLEX</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrangebylex.html">Redis 命令中文文档: ZREVRANGEBYLEX</a>
	 */
	public Flux<V> reverseRangeByLex(String key, Range<String> range) {
		return this.zsetOps.reverseRangeByLex(key, range);
	}

	/**
	 * 获取所有元素 {@literal n} 个元素，其中 {@literal n = } {@link Limit#getCount()}，从 {@link Limit#getOffset()} 开始，按 {@literal ZSET} 处的
	 * 反向字典顺序排列 @code key} 的值介于 {@link Range#getLowerBound()} 和 {@link Range#getUpperBound()} 之间.
	 *
	 * @param key 不能为 {@literal null}
	 * @param range 不能为 {@literal null}.
	 * @param limit can be {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zrevrangebylex">Redis Documentation: ZREVRANGEBYLEX</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrangebylex.html">Redis 命令中文文档: ZREVRANGEBYLEX</a>
	 */
	public Flux<V> reverseRangeByLex(String key, Range<String> range, Limit limit) {
		return this.zsetOps.reverseRangeByLex(key, range, limit);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名.其中有序集成员按 score 值递减(从大到小)排列的排名.
	 *
	 * @param key key
	 * @param member 元素
	 * @return 如果 member 是有序集 key 的成员,返回 member 的排名.如果 member 不是有序集 key 的成员,返回
	 * {@code null}
	 * @see <a href="https://redis.io/commands/zrevrank">Redis Documentation: ZREVRANK</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zrevrank.html">Redis 命令中文文档: ZREVRANK</a>
	 */
	public Mono<Long> reverseRank(String key, Object member) {
		Assert.notNull(key, "key must not be null.");
		return this.zsetOps.reverseRank(key, member);
	}


	/**
	 * 使用 {@link Flux} 迭代 {@code key} 处排序集中的条目。 由此产生的 {@link Flux} 充当游标并自行发出 {@code ZSCAN} 命令，只要订阅者发出请求信号.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return the {@link Flux} emitting the {@literal values} one by one or an {@link Flux#empty() empty Flux} if none
	 * exist.
	 * @throws IllegalArgumentException when given {@code key} is {@literal null}.
	 * @see <a href="https://redis.io/commands/zscan">Redis Documentation: ZSCAN</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zscan.html">Redis 命令中文文档: ZSCAN</a>
	 */
	public Flux<TypedTuple<V>> zScan(String key) {
		return zScan(key, ScanOptions.NONE);
	}

	/**
	 * 使用 {@link Flux} 在给定 {@link ScanOptions} 的情况下迭代 {@code key} 处排序集中的条目。 由此产生的 {@link Flux} 充当光标，只要订阅者发出需求信号，就会自行发出 {@code ZSCAN} 命令.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param options 不能为 {@literal null}. Use {@link ScanOptions#NONE} instead.
	 * @return the {@link Flux} emitting the {@literal values} one by one or an {@link Flux#empty() empty Flux} if none
	 * exist.
	 * @throws IllegalArgumentException when one of the required arguments is {@literal null}.
	 * @see <a href="https://redis.io/commands/zscan">Redis Documentation: ZSCAN</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zscan.html">Redis 命令中文文档: ZSCAN</a>
	 */
	public Flux<TypedTuple<V>> zScan(String key, ScanOptions options) {
		return this.zsetOps.scan(key, options);
	}

	/**
	 * 从键为 {@code key} 的排序集中获取具有 {@code value} 的元素的分数.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param o the value.
	 * @return /
	 * @see <a href="https://redis.io/commands/zscore">Redis Documentation: ZSCORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zscore.html">Redis 命令中文文档: ZSCORE</a>
	 */
	public Mono<Double> score(String key, Object o) {
		return this.zsetOps.score(key, o);
	}

	/**
	 * 并集排序{@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zscan.html">Redis 命令中文文档: ZSCAN</a>
	 */
	public Flux<V> union(String key, String otherKey) {
		return union(key, Collections.singleton(otherKey));
	}

	/**
	 * 并集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunion.html">Redis 命令中文文档: ZUNION</a>
	 */
	public Flux<V> union(String key, Collection<String> otherKeys) {
		return this.zsetOps.union(key, otherKeys);
	}

	/**
	 * 并集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunion.html">Redis 命令中文文档: ZUNION</a>
	 */
	public Flux<TypedTuple<V>> unionWithScores(String key, String otherKey) {
		return unionWithScores(key, Collections.singleton(otherKey));
	}

	/**
	 * 并集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunion.html">Redis 命令中文文档: ZUNION</a>
	 */
	public Flux<TypedTuple<V>> unionWithScores(String key, Collection<String> otherKeys) {
		return this.zsetOps.unionWithScores(key, otherKeys);
	}

	/**
	 * {@code key} 和 {@code otherKeys} 处的并集排序集 .
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param aggregate 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunion.html">Redis 命令中文文档: ZUNION</a>
	 */
	public Flux<TypedTuple<V>> unionWithScores(String key, Collection<String> otherKeys, Aggregate aggregate) {
		return unionWithScores(key, otherKeys, aggregate, Weights.fromSetCount(1 + otherKeys.size()));
	}

	/**
	 * 并集排序 {@code sets}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param aggregate 不能为 {@literal null}.
	 * @param weights 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunion.html">Redis 命令中文文档: ZUNION</a>
	 */
	public Flux<TypedTuple<V>> unionWithScores(String key, Collection<String> otherKeys, Aggregate aggregate, Weights weights) {
		return this.zsetOps.unionWithScores(key, otherKeys, aggregate, weights);
	}

	/**
	 * 在 {@code key} 和 {@code otherKeys} 并集排序集并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunionstore.html">Redis 命令中文文档: ZUNIONSTORE</a>
	 */
	public Mono<Long> unionStore(String key, String otherKey, String destKey) {
		return this.zsetOps.unionAndStore(key, otherKey, destKey);
	}

	/**
	 * 在 {@code key} 和 {@code otherKeys} 并集排序集并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunionstore.html">Redis 命令中文文档: ZUNIONSTORE</a>
	 */
	public Mono<Long> unionStore(String key, Collection<String> otherKeys, String destKey) {
		return this.zsetOps.unionAndStore(key, otherKeys, destKey);
	}


	/**
	 * 在 {@code key} 和 {@code otherKeys} 并集排序集并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @param aggregate 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunionstore.html">Redis 命令中文文档: ZUNIONSTORE</a>
	 */
	public Mono<Long> unionStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate) {
		return this.unionStore(key, otherKeys, destKey, aggregate, Weights.fromSetCount(1 + otherKeys.size()));
	}

	/**
	 * 在 {@code key} 和 {@code otherKeys} 并集排序集并将结果存储在目标 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @param aggregate 不能为 {@literal null}.
	 * @param weights 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
	 * @see <a href="http://doc.redisfans.com/sorted_set/zunionstore.html">Redis 命令中文文档: ZUNIONSTORE</a>
	 */
	public Mono<Long> unionStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate, Weights weights) {
		return this.zsetOps.unionAndStore(key, otherKeys, destKey, aggregate, weights);
	}

	/**
	 * 删除给定的{@literal key}.
	 *
	 * @param key 不能为 {@literal null}.
	 */
	public Mono<Boolean> delete(String key) {
		return this.zsetOps.delete(key);
	}
}
