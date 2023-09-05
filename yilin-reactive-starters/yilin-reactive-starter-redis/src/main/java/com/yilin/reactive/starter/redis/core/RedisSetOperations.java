package com.yilin.reactive.starter.redis.core;

import java.util.Collection;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.core.ScanOptions;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/29:09:49
 * @since 2023.0.1
 */
@SuppressWarnings({ "varargs", "unchecked" })
public class RedisSetOperations<V> extends RedisGenericOperations<V> {

	private final ReactiveSetOperations<String, V> setOps;

	public RedisSetOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		super(reactiveRedisTemplate);
		this.setOps = reactiveRedisTemplate.opsForSet();
	}


	/**
	 * 将一个或多个 member 元素加入到集合 key 当中,已经存在于集合的 member 元素将被忽略. 假如 key 不存在,则创建一个只包含 member
	 * 元素作成员的集合. 当 key 不是集合类型时,返回一个错误.
	 *
	 * @param key 键
	 * @param members 值 可以是多个
	 * @return 成功个数
	 * @see <a href="https://redis.io/commands/sadd">Redis Documentation: SADD</a>
	 * @see <a href="http://doc.redisfans.com/set/sadd.html">Redis 命令中文文档: SADD</a>
	 */
	@SafeVarargs
	public final Mono<Long> setAdd(String key, V... members) {
		return this.setOps.add(key, members);
	}

	/**
	 * 返回集合 key 的个数(集合中元素的数量).
	 *
	 * @param key 键
	 * @return 集合的个数.当 key 不存在时,返回 0 .
	 * @see <a href="https://redis.io/commands/scard">Redis Documentation: SCARD</a>
	 * @see <a href="http://doc.redisfans.com/set/scard.html">Redis 命令中文文档: SCARD</a>
	 */
	public Mono<Long> setCard(String key) {
		return this.setOps.size(key);
	}

	/**
	 * 区分给定 {@code key} 和 {@code otherKey} 的所有集合.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
	 * @see <a href="http://doc.redisfans.com/set/sdiff.html">Redis 命令中文文档: SDIFF</a>
	 */
	public Flux<V> setDiff(String key, String otherKey) {
		return this.setOps.difference(key, otherKey);
	}

	/**
	 * 区分给定 {@code key} 和 {@code otherKeys} 的所有集合.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
	 * @see <a href="http://doc.redisfans.com/set/sdiff.html">Redis 命令中文文档: SDIFF</a>
	 */
	public Flux<V> setDiff(String key, Collection<String> otherKeys) {
		return this.setOps.difference(key, otherKeys);
	}

	/**
	 * 区分给定 {@code key} 的所有集合.
	 *
	 * @param keys 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
	 * @see <a href="http://doc.redisfans.com/set/sdiff.html">Redis 命令中文文档: SDIFF</a>
	 */
	public Flux<V> setDiff(Collection<String> keys) {
		return this.setOps.difference(keys);
	}

	/**
	 * 比较给定 {@code key} 和 {@code otherKey} 的所有集合并将结果存储在 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sdiffstore.html">Redis 命令中文文档: SDIFFSTORE</a>
	 */
	public Mono<Long> setDiffStore(String key, String otherKey, String destKey) {
		return this.setOps.differenceAndStore(key, otherKey, destKey);
	}

	/**
	 * 比较给定 {@code key} 的所有集合并将结果存储在 {@code destKey} 中.
	 *
	 * @param keys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sdiffstore.html">Redis 命令中文文档: SDIFFSTORE</a>
	 */
	public Mono<Long> setDiffStore(Collection<String> keys, String destKey) {
		return this.setOps.differenceAndStore(keys, destKey);
	}

	/**
	 * 比较给定 {@code key} 和 {@code otherKeys} 的所有集合并将结果存储在 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sdiffstore.html">Redis 命令中文文档: SDIFFSTORE</a>
	 */
	public Mono<Long> setDiffStore(String key, Collection<String> otherKeys, String destKey) {
		return this.setOps.differenceAndStore(key, otherKeys, destKey);
	}


	/**
	 * 返回在 {@code key} 和 {@code otherKey} 处与所有给定集合相交的成员.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
	 * @see <a href="http://doc.redisfans.com/set/sinter.html">Redis 命令中文文档: SINTER</a>
	 */
	public Flux<V> setIntersect(String key, String otherKey) {
		return this.setOps.intersect(key, otherKey);
	}

	/**
	 * 返回在 {@code key} 和 {@code otherKeys} 处与所有给定集合相交的成员.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
	 * @see <a href="http://doc.redisfans.com/set/sinter.html">Redis 命令中文文档: SINTER</a>
	 */
	public Flux<V> setIntersect(String key, Collection<String> otherKeys) {
		return this.setOps.intersect(key, otherKeys);
	}

	/**
	 * 返回与所有给定集合在 {@code keys} 处相交的成员.
	 *
	 * @param keys 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
	 * @see <a href="http://doc.redisfans.com/set/sinter.html">Redis 命令中文文档: SINTER</a>
	 */
	public Flux<V> setIntersect(Collection<String> keys) {
		return this.setOps.intersect(keys);
	}

	/**
	 * 将所有给定集合在 {@code key} 和 {@code otherKey} 处相交，并将结果存储在 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sinterstore.html">Redis 命令中文文档: SINTERSTORE</a>
	 */
	public Mono<Long> setIntersectStore(String key, String otherKey, String destKey) {
		return this.setOps.intersectAndStore(key, otherKey, destKey);
	}


	/**
	 * 在 {@code key} 和 {@code otherKeys} 处将所有给定集合相交，并将结果存储在 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sinterstore.html">Redis 命令中文文档: SINTERSTORE</a>
	 */
	public Mono<Long> setIntersectStore(String key, Collection<String> otherKeys, String destKey) {
		return this.setOps.intersectAndStore(key, otherKeys, destKey);
	}

	/**
	 * 在 {@code keys} 处与所有给定集合相交并将结果存储在 {@code destKey} 中.
	 *
	 * @param keys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sinterstore.html">Redis 命令中文文档: SINTERSTORE</a>
	 */
	public Mono<Long> setIntersectStore(Collection<String> keys, String destKey) {
		return this.setOps.intersectAndStore(keys, destKey);
	}

	/**
	 * 判断 member 元素是否集合 key 的成员.
	 *
	 * @param key 键
	 * @param member 值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/sismember">Redis Documentation:
	 * SISMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/set/sismember.html">Redis 命令中文文档: SISMEMBER</a>
	 */
	public Mono<Boolean> setIsMember(String key, Object member) {
		return this.setOps.isMember(key, member);
	}

	/**
	 * 检查 {@code key} 处的设置是否包含一个或多个 {@code value}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param objects /
	 * @see <a href="https://redis.io/commands/smismember">Redis Documentation: SMISMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/set/smismember.html">Redis 命令中文文档: SMISMEMBER</a>
	 */
	public Mono<Map<Object, Boolean>> setIsMember(String key, Object... objects) {
		return this.setOps.isMember(key, objects);
	}

	/**
	 * 返回集合 key 中的所有成员.
	 *
	 * @param key 键
	 * @return 集合中的所有成员.
	 * @see <a href="https://redis.io/commands/smembers">Redis Documentation: SMEMBERS</a>
	 * @see <a href="http://doc.redisfans.com/set/smembers.html">Redis 命令中文文档:
	 * SMEMBERS</a>
	 */
	public Flux<V> setMembers(String key) {
		return this.setOps.members(key);
	}

	/**
	 * 将 {@code value} 从 {@code key} 移动到 {@code destKey}.
	 *
	 * @param sourceKey 不能为 {@literal null}.
	 * @param value
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/smove">Redis Documentation: SMOVE</a>
	 * @see <a href="http://doc.redisfans.com/set/smove.html">Redis Documentation: SMOVE</a>
	 */
	public Mono<Boolean> setMove(String sourceKey, V value, String destKey) {
		return this.setOps.move(sourceKey, value, destKey);
	}

	/**
	 * 从 {@code key} 处的集合中删除并返回随机成员.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/spop">Redis Documentation: SPOP</a>
	 * @see <a href="http://doc.redisfans.com/set/spop.html">Redis Documentation: SPOP</a>
	 */
	public Mono<V> setPop(String key) {
		return this.setOps.pop(key);
	}


	/**
	 * 从 {@code key} 处的集合中删除并返回 {@code count} 个随机成员.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of random members to pop from the set.
	 * @return {@link Flux} emitting random members.
	 * @see <a href="https://redis.io/commands/spop">Redis Documentation: SPOP</a>
	 * @see <a href="http://doc.redisfans.com/set/spop.html">Redis Documentation: SPOP</a>
	 */
	public Flux<V> setPop(String key, long count) {
		return this.setOps.pop(key, count);
	}


	/**
	 * 从 {@code key} 处的集合中获取随机元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/set/srandmember.html">Redis Documentation: SRANDMEMBER</a>
	 */
	public Mono<V> setRandomMember(String key) {
		return this.setOps.randomMember(key);
	}

	/**
	 * 从 {@code key} 处的集合中获取 {@code count} 个不同的随机元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of members to return.
	 * @return
	 * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/set/srandmember.html">Redis Documentation: SRANDMEMBER</a>
	 */
	public Flux<V> setDistinctRandomMembers(String key, long count) {
		return this.setOps.distinctRandomMembers(key, count);
	}

	/**
	 * 从 {@code key} 处的集合中获取 {@code count} 个随机元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param count number of members to return.
	 * @return
	 * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/set/srandmember.html">Redis Documentation: SRANDMEMBER</a>
	 */
	public Flux<V> setRandomMember(String key, long count) {
		return this.setOps.randomMembers(key, count);
	}

	/**
	 * 移除集合 key 中的一个或多个 member 元素,不存在的 member 元素会被忽略.
	 *
	 * @param key 键
	 * @param member 值 可以是多个
	 * @return 移除的个数
	 * @see <a href="https://redis.io/commands/srem">Redis Documentation: SREM</a>
	 * @see <a href="http://doc.redisfans.com/set/srem.html">Redis 命令中文文档: SREM</a>
	 */
	public Mono<Long> setRem(String key, Object... member) {
		return this.setOps.remove(key, member);
	}

	/**
	 * 使用 {@link Flux} 迭代 {@code key} 处集合中的条目。 由此产生的 {@link Flux} 充当游标并自行发出 {@code SSCAN} 命令，只要订阅者发出请求信号.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return {@link Flux} 逐一发出 {@literal 值} 或 {@link Flux#empty() 空 Flux}（如果不存在）.
	 * @throws IllegalArgumentException 当给定的 {@code key} 为 {@literal null} 时.
	 * @see <a href="https://redis.io/commands/sscan">Redis Documentation: SSCAN</a>
	 * @see <a href="http://doc.redisfans.com/set/sscan.html">Redis Documentation: SSCAN</a>
	 */
	public Flux<V> setScan(String key) {
		return this.setOps.scan(key, ScanOptions.NONE);
	}

	/**
	 * 使用 {@link Flux} 在给定 {@link ScanOptions} 的情况下迭代 {@code key} 处的集合中的条目。 由此产生的 {@link Flux} 充当游标并自行发出 {@code SSCAN} 命令，只要订阅者发出请求信号.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param options 不得为 {@literal null}。 使用 {@link ScanOptions#NONE} 代替.
	 * @return {@link Flux} 逐一发出 {@literal value} 或 {@link Flux#empty() 空 Flux}（如果键不存在）.
	 * @throws IllegalArgumentException 当必需参数之一是 {@literal null} 时.
	 * @see <a href="https://redis.io/commands/sscan">Redis Documentation: SSCAN</a>
	 * @see <a href="http://doc.redisfans.com/set/sscan.html">Redis Documentation: SSCAN</a>
	 */
	public Flux<V> setScan(String key, ScanOptions options) {
		return this.setOps.scan(key, options);
	}

	/**
	 * 联合给定 {@code keys} 和 {@code otherKey} 处的所有集合.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
	 * @see <a href="http://doc.redisfans.com/set/sunion.html">Redis Documentation: SUNION</a>
	 */
	public Flux<V> setUnion(String key, String otherKey) {
		return this.setOps.union(key, otherKey);
	}

	/**
	 * 联合给定 {@code keys} 和 {@code otherKeys} 处的所有集合.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
	 * @see <a href="http://doc.redisfans.com/set/sunion.html">Redis Documentation: SUNION</a>
	 */
	public Flux<V> setUnion(String key, Collection<String> otherKeys) {
		return this.setOps.union(key, otherKeys);
	}

	/**
	 * 合并给定 {@code key} 处的所有集合.
	 *
	 * @param keys 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
	 * @see <a href="http://doc.redisfans.com/set/sunion.html">Redis Documentation: SUNION</a>
	 */
	public Flux<V> setUnion(Collection<String> keys) {
		return this.setOps.union(keys);
	}

	/**
	 * 联合给定 {@code key} 和 {@code otherKey} 处的所有集合并将结果存储在 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKey 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sunionstore.html">Redis Documentation: SUNIONSTORE</a>
	 */
	public Mono<Long> setUnionStore(String key, String otherKey, String destKey) {
		return this.setOps.unionAndStore(key, otherKey, destKey);
	}

	/**
	 * 联合给定 {@code key} 和 {@code otherKeys} 处的所有集合并将结果存储在 {@code destKey} 中.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param otherKeys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sunionstore.html">Redis Documentation: SUNIONSTORE</a>
	 */
	public Mono<Long> setUnionStore(String key, Collection<String> otherKeys, String destKey) {
		return this.setOps.unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * 合并给定 {@code key} 处的所有集合并将结果存储在 {@code destKey} 中.
	 *
	 * @param keys 不能为 {@literal null}.
	 * @param destKey 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
	 * @see <a href="http://doc.redisfans.com/set/sunionstore.html">Redis Documentation: SUNIONSTORE</a>
	 */
	public Mono<Long> setUnionStore(Collection<String> keys, String destKey) {
		return this.setOps.unionAndStore(keys, destKey);
	}

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中,已经存在于集合的 member 元素将被忽略.并设置时间.
	 *
	 * @param key 键
	 * @param time 时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 * @see <a href="https://redis.io/commands/sadd">Redis Documentation: SADD</a>
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @see <a href="http://doc.redisfans.com/set/sadd.html">Redis 命令中文文档: SADD</a>
	 * @see <a href="http://doc.redisfans.com/key/expire.html">Redis 命令中文文档: EXPIRE</a>
	 */
	@SafeVarargs
	public final Mono<Boolean> setAddAndExpire(String key, long time, V... values) {
		return this.setOps.add(key, values)
				.then(this.expire(key, time));
	}


	/**
	 * 删除给定的{@literal key}.
	 *
	 * @param key 不能为 {@literal null}.
	 */
	public Mono<Boolean> setDelete(String key) {
		return this.setOps.delete(key);
	}

}
