package com.yilin.reactive.starter.redis.core;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: 通用操作
 *
 * @author jiac
 * @version 2023.0.1 2023/8/29:10:04
 * @since 2023.0.1
 */
public class RedisGenericOperations<V> {

	private final ReactiveRedisTemplate<String, V> reactiveRedisTemplate;

	public RedisGenericOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		this.reactiveRedisTemplate = reactiveRedisTemplate;
	}

	/**
	 * 此命令将 sourceKey 中存储的值复制到 targetKey.
	 *
	 * @param sourceKey sourceKey
	 * @param targetKey targetKey
	 * @param replace 是否替换
	 * @return {@code true} 复制成功.
	 * @see <a href="https://redis.io/commands/copy">Redis Documentation: COPY</a>
	 * @see <a href="http://doc.redisfans.com/key/del.html">Redis 命令中文文档: DEL</a>
	 * @since Redis 6.2.0
	 */
	public Mono<Boolean> copy(String sourceKey, String targetKey, boolean replace) {
		return this.reactiveRedisTemplate.copy(sourceKey, targetKey, replace);
	}

	/**
	 * 删除给定的一个或多个 key .
	 *
	 * @param keys 可以传一个值 或多个
	 * @return 返回个数
	 * @see <a href="https://redis.io/commands/del">Redis Documentation: DEL</a>
	 * @see <a href="http://doc.redisfans.com/key/del.html">Redis 命令中文文档: DEL</a>
	 * @since Redis 1.0.0
	 */
	public Mono<Long> del(String... keys) {
		return this.reactiveRedisTemplate.delete(keys);
	}

	/**
	 * 删除给定的一个或多个 key .
	 *
	 * @param keys 可以传一个值 或多个
	 * @return 返回个数
	 * @see <a href="https://redis.io/commands/del">Redis Documentation: DEL</a>
	 * @see <a href="http://doc.redisfans.com/key/del.html">Redis 命令中文文档: DEL</a>
	 * @since Redis 1.0.0
	 */
	public Mono<Long> del(Collection<String> keys) {
		return this.reactiveRedisTemplate.delete(Flux.fromIterable(keys));
	}

	/**
	 * 检查给定 key 是否存在.
	 *
	 * @param key 键
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/exists">Redis Documentation: EXISTS</a>
	 * @see <a href="http://doc.redisfans.com/key/exists.html">Redis 命令中文文档: EXISTS</a>
	 * @since Redis 1.0.0
	 */
	public Mono<Boolean> exists(String key) {
		try {
			return this.reactiveRedisTemplate.hasKey(key);
		}
		catch (Exception ex) {
			return Mono.just(false);
		}
	}

	/**
	 * 为给定 key 设置生存时间,当 key 过期时(生存时间为 0 ),它会被自动删除.
	 *
	 * @param key 键
	 * @param second 时间(秒)
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @see <a href="http://doc.redisfans.com/key/expire.html">Redis 命令中文文档: EXPIRE</a>
	 */
	public Mono<Boolean> expire(String key, Long second) {
		try {
			if (second > 0) {
				return this.expire(key, Duration.ofSeconds(second));
			}
		}
		catch (Exception ex) {
			return Mono.just(false);
		}
		return Mono.just(false);
	}

	/**
	 * 为给定 key 设置生存时间,当 key 过期时(生存时间为 0 ),它会被自动删除.
	 *
	 * @param key 键
	 * @param duration 时间
	 * @return {@code true} 成功, {@code false} 失败
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @see <a href="http://doc.redisfans.com/key/expire.html">Redis 命令中文文档: EXPIRE</a>
	 */
	public Mono<Boolean> expire(String key, Duration duration) {
		return this.reactiveRedisTemplate.expire(key, duration);
	}

	/**
	 * EXPIREAT 的作用和 EXPIRE 类似,都用于为 key 设置生存时间. 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix
	 * timestamp).
	 *
	 * @param key key
	 * @param instant instant
	 * @return 如果生存时间设置成功, 返回 {@code 1} .当 key 不存在或没办法设置生存时间,返回 {@code 0} .
	 * @see <a href="https://redis.io/commands/expireat">Redis Documentation: EXPIREAT</a>
	 * @see <a href="http://doc.redisfans.com/key/expireat.html">Redis 命令中文文档:
	 * EXPIREAT</a>
	 */
	public Mono<Boolean> expireAt(String key, Instant instant) {
		return this.reactiveRedisTemplate.expireAt(key, instant);
	}

	/**
	 * 查找所有符合给定模式 pattern 的 key .
	 *
	 * @param pattern 模式
	 * <ul>
	 * <li>KEYS * 匹配数据库中所有 key 。</li>
	 * <li>KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。</li>
	 * <li>KEYS h*llo 匹配 hllo 和 heeeeello 等。</li>
	 * <li>KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。</li>
	 * </ul>
	 * 特殊符号用 \ 隔开
	 * <p>
	 * KEYS 的速度非常快，但在一个大的数据库中使用它仍然可能造成性能问题，如果你需要从一个数据集中查找特定的 key ，你最好还是用 Redis
	 * 的集合结构(set)来代替。
	 * </p>
	 * @return 符合给定模式的 key 列表。
	 * @see <a href="https://redis.io/commands/keys">Redis Documentation: KEYS</a>
	 * @see <a href="http://doc.redisfans.com/key/keys.html">Redis 命令中文文档: KEYS</a>
	 */
	public Flux<String> keys(String pattern) {
		return this.reactiveRedisTemplate.keys(pattern);
	}

	/**
	 * 将当前数据库的 key 移动到给定的数据库 db 当中. 如果当前数据库(源数据库)和给定数据库(目标数据库)有相同名字的给定 key ,或者 key
	 * 不存在于当前数据库,那么 MOVE 没有任何效果. 因此,也可以利用这一特性,将 MOVE 当作锁(locking)原语(primitive).
	 *
	 * @param key key
	 * @param dbIndex 数据库索引
	 * @return 移动成功返回 {@code 1} ,失败则返回 {@code 0} .
	 * @see <a href="https://redis.io/commands/move">Redis Documentation: MOVE</a>
	 * @see <a href="http://doc.redisfans.com/key/move.html">Redis 命令中文文档: MOVE</a>
	 */
	public Mono<Boolean> move(String key, int dbIndex) {
		return this.reactiveRedisTemplate.move(key, dbIndex);
	}

	/**
	 * 移除给定 key 的生存时间,将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key ).
	 *
	 * @param key key
	 * @return 当生存时间移除成功时, 返回 {@code 1} .如果 key 不存在或 key 没有设置生存时间,返回 {@code 0} .
	 * @see <a href="https://redis.io/commands/persist">Redis Documentation: PERSIST</a>
	 * @see <a href="http://doc.redisfans.com/key/persist.html">Redis 命令中文文档: PERSIST</a>
	 */
	public Mono<Boolean> persist(String key) {
		return this.reactiveRedisTemplate.persist(key);
	}

	/**
	 * 以毫秒为单位,返回给定 key 的剩余生存时间(TTL, time to live).
	 *
	 * @param key 键 不能为 null
	 * @return 时间(毫秒) 返回 0 代表为永久有效
	 * @see <a href="https://redis.io/commands/pttl">Redis Documentation: PTTL</a>
	 * @see <a href="http://doc.redisfans.com/key/pttl.html">Redis 命令中文文档: PTTL</a>
	 */
	public Mono<Long> getExpireMillis(String key) {
		return this.reactiveRedisTemplate.getExpire(key).map(Duration::toMillis);
	}

	/**
	 * 以秒为单位,返回给定 key 的剩余生存时间(TTL, time to live).
	 *
	 * @param key 键 不能为 null
	 * @return 时间(秒) 返回 0 代表为永久有效
	 * @see <a href="https://redis.io/commands/ttl">Redis Documentation: TTL</a>
	 * @see <a href="http://doc.redisfans.com/key/ttl.html">Redis 命令中文文档: TTL</a>
	 */
	public Mono<Long> getExpireSecond(String key) {
		return this.reactiveRedisTemplate.getExpire(key).map(Duration::toSeconds);
	}

	/**
	 * 从当前选择的数据库中返回一个随机 key.
	 *
	 * @return 当数据库不为空时，返回一个 key,当数据库为空时，返回 nil。
	 * @see <a href="https://redis.io/commands/randomkey/">Redis Documentation: RANDOMKEY</a>
	 * @see <a href="http://doc.redisfans.com/key/randomkey.html">Redis 命令中文文档: RANDOMKEY</a>
	 */
	public Mono<String> randomKey() {
		return this.reactiveRedisTemplate.randomKey();
	}

	/**
	 * 将 key 改名为 newkey . 当 key 不存在时,返回一个错误. 当 newkey 已经存在时, RENAME 命令将覆盖旧值.
	 * >= 3.2.0 版本：当 key 和 newkey 相同，该命令不再返回错误。
	 *
	 * @param key 旧 key
	 * @param newkey 新 key
	 * @see <a href="https://redis.io/commands/rename">Redis Documentation: RENAME</a>
	 * @see <a href="http://doc.redisfans.com/key/rename.html">Redis 命令中文文档: RENAME</a>
	 */
	public Mono<Boolean> rename(String key, String newkey) {
		return this.reactiveRedisTemplate.rename(key, newkey);
	}

	/**
	 * 如果 `newkey` 不存在，则将 key 重命名为 `newkey`。当 key 不存在时，它会返回一个错误.
	 * >= 3.2.0 版本：当 key 和 newkey 相同，该命令不再返回错误。
	 *
	 * @param key 旧 key
	 * @param newkey 新 key
	 * @see <a href="https://redis.io/commands/renamenx">Redis Documentation: RENAMENX</a>
	 * @see <a href="http://doc.redisfans.com/key/renamenx.html">Redis 命令中文文档: RENAMENX</a>
	 */
	public Mono<Boolean> renameNx(String key, String newkey) {
		return this.reactiveRedisTemplate.renameIfAbsent(key, newkey);
	}

	/**
	 * 查找匹配 key.
	 *
	 * @param pattern key
	 * @return 数据列表
	 * @see <a href="https://redis.io/commands/scan">Redis Documentation: SCAN</a>
	 * @see <a href="http://doc.redisfans.com/key/scan.html">Redis 命令中文文档: SCAN</a>
	 */
	public Flux<String> scan(String pattern) {
		ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
		return this.reactiveRedisTemplate.scan(options);
	}

	/**
	 * 返回 key 所储存的值的类型.
	 *
	 * @param key key
	 * @return {@code none} (key不存在),{@code string} (字符串),{@code list} (列表),{@code set}
	 * (集合),{@code zset} (有序集),{@code hash} (哈希表)
	 * @see <a href="https://redis.io/commands/type">Redis Documentation: TYPE</a>
	 * @see <a href="http://doc.redisfans.com/key/type.html">Redis 命令中文文档: TYPE</a>
	 */
	public Mono<String> type(String key) {
		return this.reactiveRedisTemplate.type(key).map(DataType::code);
	}

	/**
	 * 该命令与 DEL 非常相似：它删除指定的 key 。 就像 DEL 一样，如果 key 不存在，则会被忽略,然而，该命令在不同的线程中执行实际的内存回收，因此它不是阻塞的，而 DEL 是阻塞的.
	 *
	 * @param keys keys
	 * @return <a href="https://redis.io/docs/reference/protocol-spec/#resp-integers">[整数]:</a> 未链接的 key 的数量.
	 * @see <a href="https://redis.io/commands/type">Redis Documentation: TYPE</a>
	 * @see <a href="http://doc.redisfans.com/key/type.html">Redis 命令中文文档: TYPE</a>
	 */
	public Mono<Long> unlink(String... keys) {
		return this.reactiveRedisTemplate.unlink(keys);
	}

	//	/**
//	 * 分页查询 key.
//	 *
//	 * @param patternKey key
//	 * @param page 页码
//	 * @param size 每页数目
//	 * @return 数据列表
//	 * @see <a href="https://redis.io/commands/scan">Redis Documentation: SCAN</a>
//	 * @see <a href="http://doc.redisfans.com/key/scan.html">Redis 命令中文文档: SCAN</a>
//	 */
//	public List<String> findKeysForPage(String patternKey, int page, int size) {
//		ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
//		this.reactiveRedisTemplate.scan(options, ScanCursor.of())
//				.flatMap(scan -> {
//					scan.get
//				})
//
//		RedisConnectionFactory factory = this.reactiveRedisTemplate.getConnectionFactory();
//		RedisConnection rc = Objects.requireNonNull(factory).getConnection();
//		Cursor<byte[]> cursor = rc.scan(options);
//		List<String> result = new ArrayList<>(size);
//		int tmpIndex = 0;
//		int fromIndex = page * size;
//		int toIndex = page * size + size;
//		while (cursor.hasNext()) {
//			if (tmpIndex >= fromIndex && tmpIndex < toIndex) {
//				result.add(new String(cursor.next()));
//				tmpIndex++;
//				continue;
//			}
//			// 获取到满足条件的数据后,就可以退出了
//			if (tmpIndex >= toIndex) {
//				break;
//			}
//			tmpIndex++;
//			cursor.next();
//		}
//		try {
//			RedisConnectionUtils.releaseConnection(rc, factory);
//		}
//		catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return result;
//	}
}
