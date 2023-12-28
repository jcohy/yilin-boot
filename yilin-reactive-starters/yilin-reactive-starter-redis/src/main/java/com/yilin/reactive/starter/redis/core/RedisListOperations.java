package com.yilin.reactive.starter.redis.core;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.redis.connection.ReactiveListCommands.Direction;
import org.springframework.data.redis.core.ListOperations.MoveFrom;
import org.springframework.data.redis.core.ListOperations.MoveTo;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: List 命令操作
 *
 * @author jcohy
 * @version 2023.0.1 2023/8/29 09:38
 * @since 2023.0.1
 */
@SuppressWarnings({ "varargs", "unchecked" })
public class RedisListOperations<V> extends RedisGenericOperations<V> {

	private final ReactiveListOperations<String, V> listOps;

	public RedisListOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		super(reactiveRedisTemplate);
		this.listOps = reactiveRedisTemplate.opsForList();
	}

	/**
	 * 原子地返回并删除存储在 {@code sourceKey} 的列表的第一个/最后一个元素（头/尾取决于 {@code from} 参数），
	 * 并将元素推送到第一个/最后一个元素（头/尾取决于 {@code from} 参数） 存储在 {@code destinationKey} 的列表的 {@code to} 参数）.
	 * <p>
	 * <b>阻塞连接</b> 直到元素可用或达到 {@code timeout}.
	 *
	 * @param sourceKey 不能为 {@literal null}.
	 * @param from 不能为 {@literal null}.
	 * @param destinationKey 不能为 {@literal null}.
	 * @param to 不能为 {@literal null}.
	 * @param timeout 超时
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/blmove">Redis Documentation: BLMOVE</a>
	 * @see <a href="http://doc.redisfans.com/list/blmove.html">Redis 命令中文文档: BLMOVE</a>
	 */
	public Mono<V> listBlockLeftMove(String sourceKey, Direction from, String destinationKey, Direction to, Duration timeout) {
		return this.listOps.move(sourceKey, from, destinationKey, to, timeout);
	}

	/**
	 * 原子地返回并删除存储在 {@code sourceKey} 的列表的第一个/最后一个元素（头/尾取决于 {@code from} 参数），
	 * 并将元素推送到第一个/最后一个元素（头/尾取决于 {@code from} 参数） 存储在 {@code destinationKey} 的列表的 {@code to} 参数）.
	 * <p>
	 * <b>阻塞连接</b> 直到元素可用或达到 {@code timeout}.
	 *
	 * @param from 不能为 {@literal null}.
	 * @param to 不能为 {@literal null}.
	 * @param timeout 超时
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/blmove">Redis Documentation: BLMOVE</a>
	 * @see <a href="http://doc.redisfans.com/list/blmove.html">Redis 命令中文文档: BLMOVE</a>
	 */
	public Mono<V> listBlockLeftMove(MoveFrom<String> from, MoveTo<String> to, Duration timeout) {
		return this.listOps.move(from, to, timeout);
	}

	/**
	 * 原子地返回并删除存储在 {@code sourceKey} 的列表的第一个/最后一个元素（头/尾取决于 {@code from} 参数），
	 * 并将元素推送到第一个/最后一个元素（头/尾取决于 {@code from} 参数） 存储在 {@code destinationKey} 的列表的 {@code to} 参数）.
	 *
	 * @param from 不能为 {@literal null}.
	 * @param to 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/lmove">Redis Documentation: LMOVE</a>
	 * @see <a href="http://doc.redisfans.com/list/lmove.html">Redis 命令中文文档: LMOVE</a>
	 */
	public Mono<V> listLeftMove(MoveFrom<String> from, MoveTo<String> to) {
		return this.listOps.move(from, to);
	}

	/**
	 * 原子地返回并删除存储在 {@code sourceKey} 的列表的第一个/最后一个元素（头/尾取决于 {@code from} 参数），
	 * 并将元素推送到第一个/最后一个元素（头/尾取决于 {@code from} 参数） 存储在 {@code destinationKey} 的列表的 {@code to} 参数）.
	 *
	 * @param sourceKey 不能为 {@literal null}.
	 * @param from 不能为 {@literal null}.
	 * @param destinationKey 不能为 {@literal null}.
	 * @param to 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/lmove">Redis Documentation: LMOVE</a>
	 * @see <a href="http://doc.redisfans.com/list/lmove.html">Redis 命令中文文档: LMOVE</a>
	 */
	public Mono<V> listLeftMove(String sourceKey, Direction from, String destinationKey, Direction to) {
		return this.listOps.move(sourceKey, from, destinationKey, to);
	}

	/**
	 * 从存储在 {@code key} 的列表中删除并返回第一个元素。 <br> <b>一旦元素可用或达到 {@code timeout}，结果就会返回。</b>.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param timeout 等待列表中 {@code key} 处的 entry 可用的最长持续时间。 必须为 {@link Duration#ZERO} 或更大的 {@link 1 秒}，不能为 {@literal null}。 超时为零可用于无限期等待。 不支持零到一秒之间的持续时间。
	 * @return
	 * @see <a href="https://redis.io/commands/blpop">Redis Documentation: BLPOP</a>
	 * @see <a href="http://doc.redisfans.com/list/blpop.html">Redis 命令中文文档: BLPOP</a>
	 */
	public Mono<V> listBlockLeftPop(String key, Duration timeout) {
		return this.listOps.leftPop(key, timeout);
	}

	/**
	 * 删除并返回存储在 {@code key} 的列表中的第一个元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/lpop">Redis Documentation: LPOP</a>
	 * @see <a href="http://doc.redisfans.com/list/lpop.html">Redis 命令中文文档: LPOP</a>
	 */
	public Mono<V> listLeftPop(String key) {
		return this.listOps.leftPop(key);
	}

	/**
	 * 删除并返回存储在 {@code key} 的列表中的最后一个元素. <br>
	 * <b>一旦元素可用或达到{@code timeout}，结果就会返回.</b>
	 *
	 * @param key 不能为 {@literal null}.
	 * @param timeout 等待列表中 {@code key} 处的 entry 可用的最长持续时间。 必须为 {@link Duration#ZERO} 或更大的 {@link 1 秒}，不能为 {@literal null}.  超时为零可用于无限期等待。 不支持零到一秒之间的持续时间。
	 * @return
	 * @see <a href="https://redis.io/commands/brpop">Redis Documentation: BRPOP</a>
	 * @see <a href="http://doc.redisfans.com/list/brpop.html">Redis 命令中文文档: BRPOP</a>
	 */
	public Mono<V> listBlockRightPop(String key, Duration timeout) {
		return this.listOps.rightPop(key, timeout);
	}

	/**
	 * 删除并返回存储在 {@code key} 的列表中的最后一个元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/rpop">Redis Documentation: RPOP</a>
	 * @see <a href="http://doc.redisfans.com/list/rpop.html">Redis 命令中文文档: RPOP</a>
	 */
	public Mono<V> listRightPop(String key) {
		return this.listOps.rightPop(key);
	}

	/**
	 * 从 {@code srcKey} 处的列表中删除最后一个元素，将其附加到 {@code dstKey} 并返回其值.
	 * <b>一旦元素可用或达到 {@code timeout}，结果就会返回。</b>
	 *
	 * @param sourceKey 源 key
	 * @param destinationKey 目标 key
	 * @param timeout 等待列表中 {@code key} 处的 entry 可用的最长持续时间。 必须为 {@link Duration#ZERO} 或更大的 {@link 1 秒}，不能为 {@literal null}.  超时为零可用于无限期等待。 不支持零到一秒之间的持续时间。
	 * @return 被弹出的元素.
	 * @see <a href="https://redis.io/commands/brpoplpush">Redis Documentation: BRPOPLPUSH</a>
	 * @see <a href="http://doc.redisfans.com/list/brpoplpush.html">Redis 命令中文文档: BRPOPLPUSH</a>
	 */
	public Mono<V> listBlockRightPopLeftPush(String sourceKey, String destinationKey, Duration timeout) {
		return this.listOps.rightPopAndLeftPush(sourceKey, destinationKey, timeout);
	}

	/**
	 * 命令 RPOPLPUSH 在一个原子时间内,执行以下两个动作：
	 * <ul>
	 * <li>将列表 source 中的最后一个元素(尾元素)弹出,并返回给客户端.</li>
	 * <li>将 source 弹出的元素插入到列表 destination ,作为 destination 列表的的头元素.</li>
	 * </ul>
	 * 如果 source 不存在,值 {@code null} 被返回,并且不执行其他动作. 如果 source 和 destination
	 * 相同,则列表中的表尾元素被移动到表头,并返回该元素,可以把这种特殊情况视作列表的旋转(rotation)操作.
	 *
	 * @param sourceKey 源 key
	 * @param destinationKey 目标 key
	 * @return 被弹出的元素.
	 * @see <a href="https://redis.io/commands/rpoplpush">Redis Documentation: RPOPLPUSH</a>
	 * @see <a href="http://doc.redisfans.com/list/rpoplpush.html">Redis 命令中文文档: RPOPLPUSH</a>
	 */
	public Mono<V> listRightPopLeftPush(String sourceKey, String destinationKey) {
		return this.listOps.rightPopAndLeftPush(sourceKey, destinationKey);
	}

	/**
	 * 返回列表 key 中,下标为 index 的元素.
	 *
	 * @param key 键
	 * @param index 索引 {@code index >= 0 } 时, 0 表头,1
	 * 第二个元素,依次类推;{@code index<0}时,-1,表尾,-2倒数第二个元素,依次类推
	 * @return 列表中下标为 index 的元素.如果 index 参数的值不在列表的区间范围内(out of range),返回 {@code null} .
	 * @see <a href="https://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
	 * @see <a href="http://doc.redisfans.com/list/lindex.html">Redis 命令中文文档: LINDEX</a>
	 */
	public Mono<V> listIndex(String key, long index) {
		return this.listOps.index(key, index);
	}

	/**
	 * 用于把 value 插入到列表 key 中参考值 pivot 的前面或后面。.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param pivot 不能为 {@literal null}.
	 * @param value 不能为 {@literal null}.
	 * @return 执行操作后的列表长度，列表中 pivot 参考值不存在的时候返回 -1
	 * @see <a href="https://redis.io/commands/linsert">Redis Documentation: LINSERT</a>
	 * @see <a href="http://doc.redisfans.com/list/linsert.html">Redis 命令中文文档: LINSERT</a>
	 */
	public Mono<Long> listInsert(String key, V pivot, V value) {
		return this.listOps.leftPush(key, pivot, value);
	}

	/**
	 * 返回列表 key 的长度.如果 key 不存在,则 key 被解释为一个空列表,返回 0 .如果 key 不是列表类型,返回一个错误.
	 *
	 * @param key 键
	 * @return 列表 key 的长度.
	 * @see <a href="https://redis.io/commands/llen">Redis Documentation: LLEN</a>
	 * @see <a href="http://doc.redisfans.com/list/llen.html">Redis 命令中文文档: LLEN</a>
	 */
	public Mono<Long> listLen(String key) {
		return this.listOps.size(key);
	}

	/**
	 * 返回列表中 {@code key} 处指定值第一次出现的索引。 <br /> 需要 Redis 6.0.6 或更高版本.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param value 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/lpos">Redis Documentation: LPOS</a>
	 * @see <a href="http://doc.redisfans.com/list/lpos.html">Redis 命令中文文档: LPOS</a>
	 */
	public Mono<Long> listLeftPos(String key, V value) {
		return this.listOps.indexOf(key, value);
	}

	/**
	 * 将 {@code value} 添加到 {@code key} 之前.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param value
	 * @return
	 * @see <a href="https://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 * @see <a href="http://doc.redisfans.com/list/lpush.html">Redis 命令中文文档: LPUSH</a>
	 */
	public Mono<Long> listLeftPush(String key, V value) {
		return this.listOps.leftPush(key, value);
	}

	/**
	 * 将 {@code values} 添加到 {@code key} 之前.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param values 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 * @see <a href="http://doc.redisfans.com/list/lpush.html">Redis 命令中文文档: LPUSH</a>
	 */
	@SafeVarargs
	public final Mono<Long> listLeftPush(String key, V... values) {
		return this.listOps.leftPushAll(key, values);
	}

	/**
	 * 将 {@code values} 添加到 {@code key} 之前.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param values 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 * @see <a href="http://doc.redisfans.com/list/lpush.html">Redis 命令中文文档: LPUSH</a>
	 */
	public Mono<Long> listLeftPush(String key, Collection<V> values) {
		return this.listOps.leftPushAll(key, values);
	}

	/**
	 * 仅当列表存在时才将 {@code value} 添加到 {@code key}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param value
	 * @return
	 * @see <a href="https://redis.io/commands/lpushx">Redis Documentation: LPUSHX</a>
	 * @see <a href="http://doc.redisfans.com/list/lpushx.html">Redis 命令中文文档: LPUSHX</a>
	 */
	public Mono<Long> listLeftPushIfPresent(String key, V value) {
		return this.listOps.leftPushIfPresent(key, value);
	}

	/**
	 * 返回列表 key 中指定区间内的元素,区间以偏移量 start 和 stop 指定. 标(index)参数 {@code index >= 0 } 时, 0
	 * 表示列表的第一个元素,1 表示列表的第二个元素,依次类推;{@code index<0}时, -1 表示列表的最后一个元素, -2
	 * 表示列表的倒数第二个元素,以此类推.
	 *
	 * @param key 键
	 * @param start 开始
	 * @param stop 结束 0 到 -1代表所有值
	 * @return 一个列表, 包含指定区间内的元素.
	 * @see <a href="https://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
	 * @see <a href="http://doc.redisfans.com/list/lrange.html">Redis 命令中文文档: LRANGE</a>
	 */
	public Flux<V> listRange(String key, long start, long stop) {
		return this.listOps.range(key, start, stop);
	}

	/**
	 * 根据参数 count 的值,移除列表中与参数 value 相等的元素. count 的值可以是以下几种：
	 * <ul>
	 * <li>count &gt; 0 : 从表头开始向表尾搜索,移除与 value 相等的元素,数量为 count .</li>
	 * <li>count &lt; 0 : 从表尾开始向表头搜索,移除与 value 相等的元素,数量为 count 的绝对值.</li>
	 * <li>count = 0 : 移除表中所有与 value 相等的值.</li>
	 * </ul>
	 * .
	 *
	 * @param key 键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 被移除元素的数量.因为不存在的 key 被视作空表(empty list),所以当 key 不存在时, LREM 命令总是返回 0 .
	 * @see <a href="https://redis.io/commands/lrem">Redis Documentation: LREM</a>
	 * @see <a href="http://doc.redisfans.com/list/lrem.html">Redis 命令中文文档: LREM</a>
	 */
	public Mono<Long> listRemove(String key, long count, V value) {
		return this.listOps.remove(key, count, value);
	}

	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value . 当 index 参数超出范围,或对一个空列表( key 不存在)进行 LSET
	 * 时,返回一个错误.
	 *
	 * @param key 键
	 * @param index 索引
	 * @param value 值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/lset">Redis Documentation: LSET</a>
	 * @see <a href="http://doc.redisfans.com/list/lset.html">Redis 命令中文文档: LSET</a>
	 */
	public Mono<Boolean> listSet(String key, long index, V value) {
		return this.listOps.set(key, index, value);
	}

	/**
	 * 将 {@code key} 处的列表修剪为 {@code start} 和 {@code end} 之间的元素.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param start
	 * @param end
	 * @see <a href="https://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
	 * @see <a href="http://doc.redisfans.com/list/ltrim.html">Redis 命令中文文档: LTRIM</a>
	 */
	public Mono<Boolean> listLeftTrim(String key, long start, long end) {
		return this.listOps.trim(key, start, end);
	}

	/**
	 * 将一个值 value 插入到列表 key 的表尾(最右边).
	 *
	 * @param key 键
	 * @param value 值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 * @see <a href="http://doc.redisfans.com/list/rpush.html">Redis 命令中文文档: RPUSH</a>
	 */
	public Mono<Long> listRightPush(String key, V value) {
		return this.listOps.rightPush(key, value);
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边).
	 *
	 * @param key 键
	 * @param value 值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 * @see <a href="http://doc.redisfans.com/list/rpush.html">Redis 命令中文文档: RPUSH</a>
	 */
	public Mono<Long> listRightPushAll(String key, List<V> value) {
		return this.listOps.rightPushAll(key, value);
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边).
	 *
	 * @param key 键
	 * @param value 值
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 * @see <a href="http://doc.redisfans.com/list/rpush.html">Redis 命令中文文档: RPUSH</a>
	 */
	@SafeVarargs
	public final Mono<Long> listRightPushAll(String key, V... value) {
		return this.listOps.rightPushAll(key, value);
	}

	/**
	 * 仅当列表存在时才将 {@code value} 附加到 {@code key}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param value
	 * @return
	 * @see <a href="https://redis.io/commands/rpushx">Redis Documentation: RPUSHX</a>
	 * @see <a href="http://doc.redisfans.com/list/rpushx.html">Redis 命令中文文档: RPUSHX</a>
	 */
	public Mono<Long> listRightPushIfPresent(String key, V value) {
		return this.listOps.rightPushIfPresent(key, value);
	}

	/**
	 * 将一个值 value 插入到列表 key 的表尾(最右边).并设置过期时间.
	 *
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @see <a href="http://doc.redisfans.com/list/rpush.html">Redis 命令中文文档: RPUSH</a>
	 * @see <a href="http://doc.redisfans.com/key/expire.html">Redis 命令中文文档: EXPIRE</a>
	 */
	public Mono<Boolean> listRightPushExpire(String key, V value, long time) {
		return this.listRightPush(key, value)
				.then(this.expire(key, time));
	}


	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边).并设置过期时间.
	 *
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @see <a href="http://doc.redisfans.com/hash/rpush.html">Redis 命令中文文档: RPUSH</a>
	 * @see <a href="http://doc.redisfans.com/key/expire.html">Redis 命令中文文档: EXPIRE</a>
	 */
	public Mono<Boolean> listRightPushAllExpire(String key, List<V> value, long time) {
		return this.listRightPushAll(key, value)
				.then(this.expire(key, time));
	}

	/**
	 * 删除 key.
	 *
	 * @param key 键
	 * @return {@code true} 成功, {@code false} 失败
	 */
	public Mono<Boolean> listDelete(String key) {
		return this.listOps.delete(key);
	}

}
