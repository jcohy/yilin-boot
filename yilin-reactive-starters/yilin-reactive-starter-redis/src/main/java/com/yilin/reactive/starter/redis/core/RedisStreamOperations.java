package com.yilin.reactive.starter.redis.core;

import java.util.Arrays;
import java.util.Map;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.stream.ByteBufferRecord;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.connection.stream.PendingMessagesSummary;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamInfo.XInfoConsumer;
import org.springframework.data.redis.connection.stream.StreamInfo.XInfoGroup;
import org.springframework.data.redis.connection.stream.StreamInfo.XInfoStream;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStreamOperations;
import org.springframework.util.Assert;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/16:16:25
 * @since 2023.0.1
 */
public class RedisStreamOperations<K, V> {

	private final ReactiveStreamOperations<String, K, V> streamOps;

	public RedisStreamOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		this.streamOps = reactiveRedisTemplate.opsForStream();
	}

	/**
	 * 确认已处理的一条或多条记录.
	 *
	 * @param key stream key.
	 * @param group 消费者组名.
	 * @param recordIds records 要确认的 ID.
	 * @return the {@link Mono} 已确认发出 records 的长度 .
	 * @see <a href="https://redis.io/commands/xack">Redis Documentation: XACK</a>
	 * @see <a href="http://doc.redisfans.com/key/xack.html">Redis 命令中文文档: XACK</a>
	 */
	public Mono<Long> acknowledge(String key, String group, String... recordIds) {
		return acknowledge(key, group, Arrays.stream(recordIds).map(RecordId::of).toArray(RecordId[]::new));
	}

	/**
	 * 确认已处理的一条或多条记录.
	 *
	 * @param key stream key.
	 * @param group 消费者组名.
	 * @param recordIds records 要确认的 ID.
	 * @return the {@link Mono} 已确认发出 records 的长度.
	 * @see <a href="https://redis.io/commands/xack">Redis Documentation: XACK</a>
	 * @see <a href="http://doc.redisfans.com/key/xack.html">Redis 命令中文文档: XACK</a>
	 */
	public Mono<Long> acknowledge(String key, String group, RecordId... recordIds) {
		return this.streamOps.acknowledge(key, group, recordIds);
	}

	/**
	 * 确认已处理的一条或多条记录.
	 *
	 * @param group 消费者组名.
	 * @param record 需要确认的 {@link Record}.
	 * @return the {@link Mono} 已确认发出 records 的长度.
	 * @see <a href="https://redis.io/commands/xack">Redis Documentation: XACK</a>
	 * @see <a href="http://doc.redisfans.com/key/xack.html">Redis 命令中文文档: XACK</a>
	 */
	public Mono<Long> acknowledge(String group, Record<String, ?> record) {
		return acknowledge(record.getRequiredStream(), group, record.getId());
	}

	/**
	 * 将一条或多条 record 添加到 stream {@code key}.
	 *
	 * @param key stream key.
	 * @param bodyPublisher record body {@link Publisher}.
	 * @return the record Ids.
	 * @see <a href="https://redis.io/commands/xadd">Redis Documentation: XADD</a>
	 * @see <a href="http://doc.redisfans.com/key/xadd.html">Redis 命令中文文档: XADD</a>
	 */
	public Flux<RecordId> add(String key, Publisher<? extends Map<? extends K, ? extends V>> bodyPublisher) {
		return this.streamOps.add(key, bodyPublisher);
	}

	/**
	 * 将一条或多条记录添加到 stream {@code key}.
	 *
	 * @param key the stream key.
	 * @param content record content as Map.
	 * @return the {@link Mono} emitting the {@link RecordId}.
	 * @see <a href="https://redis.io/commands/xadd">Redis Documentation: XADD</a>
	 * @see <a href="http://doc.redisfans.com/key/xadd.html">Redis 命令中文文档: XADD</a>
	 */
	public Mono<RecordId> add(String key, Map<? extends K, ? extends V> content) {
		return add(StreamRecords.newRecord().in(key).ofMap(content));
	}

	/**
	 * 将由保存字段/值对的 {@link Map} 支持的记录附加到流中.
	 *
	 * @param record the record to append.
	 * @return the {@link Mono} emitting the {@link RecordId}.
	 * @see <a href="https://redis.io/commands/xadd">Redis Documentation: XADD</a>
	 * @see <a href="http://doc.redisfans.com/key/xadd.html">Redis 命令中文文档: XADD</a>
	 */
	@SuppressWarnings("unchecked")
	public Mono<RecordId> add(MapRecord<String, ? extends K, ? extends V> record) {
		return add((Record) record);
	}

	/**
	 * 将由给定值支持的记录追加到流中。 该值将被散列和序列化.
	 *
	 * @param record 不能为 {@literal null}.
	 * @return the {@link Mono} emitting the {@link RecordId}.
	 * @see <a href="https://redis.io/commands/xadd">Redis Documentation: XADD</a>
	 * @see <a href="http://doc.redisfans.com/key/xadd.html">Redis 命令中文文档: XADD</a>
	 * @see MapRecord
	 * @see ObjectRecord
	 */
	public Mono<RecordId> add(Record<String, ?> record) {
		return this.streamOps.add(record);
	}

	/**
	 * 从流中删除指定的记录。 返回删除的记录数，如果某些 ID 不存在，则可能与传递的 ID 数不同.
	 *
	 * @param key stream key.
	 * @param recordIds stream record Id's.
	 * @return {@link Mono} 已经删除的 record 的数量.
	 * @see <a href="https://redis.io/commands/xdel">Redis Documentation: XDEL</a>
	 * @see <a href="http://doc.redisfans.com/key/xdel.html">Redis 命令中文文档: XDEL</a>
	 */
	public Mono<Long> delete(String key, String... recordIds) {
		return delete(key, Arrays.stream(recordIds).map(RecordId::of).toArray(RecordId[]::new));
	}

	/**
	 * 从流中删除给定的 {@link Record}.
	 *
	 * @param record 不能为 {@literal null}.
	 * @return {@link Mono} 已经删除的 record 的数量.
	 * @see <a href="https://redis.io/commands/xdel">Redis Documentation: XDEL</a>
	 * @see <a href="http://doc.redisfans.com/key/xdel.html">Redis 命令中文文档: XDEL</a>
	 */
	public Mono<Long> delete(Record<String, ?> record) {
		return delete(record.getStream(), record.getId());
	}

	/**
	 * 从流中删除指定的记录。 返回删除的记录数，如果某些 ID 不存在，则可能与传递的 ID 数不同.
	 *
	 * @param key the stream key.
	 * @param recordIds stream record Id's.
	 * @return {@link Mono} 已经删除的 record 的数量.
	 * @see <a href="https://redis.io/commands/xdel">Redis Documentation: XDEL</a>
	 * @see <a href="http://doc.redisfans.com/key/xdel.html">Redis 命令中文文档: XDEL</a>
	 */
	public Mono<Long> delete(String key, RecordId... recordIds) {
		return this.streamOps.delete(key, recordIds);
	}

	/**
	 * 在 {@link ReadOffset#latest() 最新偏移量}处创建一个消费者组。 如果流尚不存在，则此命令将创建该流.
	 *
	 * @param key 流存储的 {@literal key}.
	 * @param group 消费者组名.
	 * @return 如果成功，则 {@link Mono} 发出 {@literal OK}. 在管道/事务中使用时 {@literal null}.
	 * @see <a href="https://redis.io/commands/xgroup-create">Redis Documentation: XGROUP CREATE</a>
	 * @see <a href="http://doc.redisfans.com/key/xgroup-create.html">Redis 命令中文文档: XGROUP CREATE</a>
	 */
	public Mono<String> groupCreate(String key, String group) {
		return groupCreate(key, ReadOffset.latest(), group);
	}

	/**
	 * 创建消费者组。 如果流尚不存在，则此命令将创建该流.
	 *
	 * @param key 流存储的 {@literal key}.
	 * @param readOffset 使用 {@link ReadOffset}.
	 * @param group 消费者组名.
	 * @return 如果成功，则 {@link Mono} 发出 {@literal OK}.
	 * @see <a href="https://redis.io/commands/xgroup-create">Redis Documentation: XGROUP CREATE</a>
	 * @see <a href="http://doc.redisfans.com/key/xgroup-create.html">Redis 命令中文文档: XGROUP CREATE</a>
	 */
	public Mono<String> groupCreate(String key, ReadOffset readOffset, String group) {
		return this.streamOps.createGroup(key, readOffset, group);
	}

	/**
	 * 从消费者组中删除消费者.
	 *
	 * @param key stream key.
	 * @param consumer 通过组名和消费者 key 标识消费者.
	 * @return 如果成功，则返回 {@link Mono} {@literal OK}。 {@literal null} 在管道/事务中使用时.
	 * @see <a href="https://redis.io/commands/xgroup-delconsumer">Redis Documentation: XGROUP DELCONSUMER</a>
	 * @see <a href="http://doc.redisfans.com/key/xgroup-delconsumer.html">Redis 命令中文文档: XGROUP DELCONSUMER</a>
	 */
	public Mono<String> groupDeleteConsumer(String key, Consumer consumer) {
		return this.streamOps.deleteConsumer(key, consumer);
	}

	/**
	 * 销毁一个消费者组.
	 *
	 * @param key stream key.
	 * @param group 消费者组名.
	 * @return 如果成功，则返回 {@link Mono} {@literal OK}。 {@literal null} 在管道/事务中使用时.
	 * @see <a href="https://redis.io/commands/xgroup-destroy">Redis Documentation: XGROUP DESTROY</a>
	 * @see <a href="http://doc.redisfans.com/key/xgroup-destroy.html">Redis 命令中文文档: XGROUP DESTROY</a>
	 */
	public Mono<String> groupDestroy(String key, String group) {
		return this.streamOps.destroyGroup(key, group);
	}

	/**
	 * 获取有关存储在指定 {@literal key} 的流的特定 {@literal 消费者组} 中的每个消费者的信息.
	 *
	 * @param key key.
	 * @param group {@literal consumer group} 名.
	 * @return 在管道/事务中使用时为 {@literal null} .
	 * @see <a href="https://redis.io/commands/xinfo-consumers">Redis Documentation: XINFO CONSUMERS</a>
	 * @see <a href="http://doc.redisfans.com/key/xinfo-consumers.html">Redis 命令中文文档: XINFO DESTROY</a>
	 */
	public Flux<XInfoConsumer> infoConsumers(String key, String group) {
		return this.streamOps.consumers(key, group);
	}

	/**
	 * 获取与存储在指定 {@literal key} 处的流关联的 {@literal Consumer groups} 的信息.
	 *
	 * @param key key.
	 * @return 在管道/事务中使用时为 {@literal null}.
	 * @see <a href="https://redis.io/commands/xinfo-groups">Redis Documentation: XINFO GROUPS</a>
	 * @see <a href="http://doc.redisfans.com/key/xinfo-groups.html">Redis 命令中文文档: XINFO GROUPS</a>
	 */
	public Flux<XInfoGroup> infoGroups(String key) {
		return this.streamOps.groups(key);
	}

	/**
	 * 获取有关存储在指定 {@literal key} 的流的一般信息.
	 *
	 * @param key key.
	 * @return 在管道/事务中使用时为 {@literal null}.
	 * @see <a href="https://redis.io/commands/xinfo-stream">Redis Documentation: XINFO STREAM</a>
	 * @see <a href="http://doc.redisfans.com/key/xinfo-stream.html">Redis 命令中文文档: XINFO STREAM</a>
	 */
	public Mono<XInfoStream> infoStream(String key) {
		return this.streamOps.info(key);
	}

	/**
	 * 获取流的长度.
	 *
	 * @param key key.
	 * @return {@link Mono} 发出 stream 长度.
	 * @see <a href="https://redis.io/commands/xlen">Redis Documentation: XLEN</a>
	 * @see <a href="http://doc.redisfans.com/key/xlen.html">Redis 命令中文文档: XLEN</a>
	 */
	public Mono<Long> length(String key) {
		return this.streamOps.size(key);
	}

	/**
	 * 获取给定{@literal 消费者组}的{@link PendingMessagesSummary}.
	 *
	 * @param key 存储流的 {@literal key} . 不能为 {@literal null}.
	 * @param group {@literal consumer group} 名 不能为 {@literal null}.
	 * @return 在管道/事务中使用时给定 {@literal Consumer Group} 或 {@literal null} 内待处理消息的摘要。
	 * @see <a href="https://redis.io/commands/xpending">Redis Documentation: xpending</a>
	 * @see <a href="http://doc.redisfans.com/key/xpending.html">Redis 命令中文文档: xpending</a>
	 */
	public Mono<PendingMessagesSummary> pending(String key, String group) {
		return this.streamOps.pending(key, group);
	}

	/**
	 * 获取有关给定 {@link Consumer} 的所有待处理消息的详细信息.
	 *
	 * @param key 存储流的 {@literal key} . 不能为 {@literal null}.
	 * @param consumer 消费者获取{@link PendingMessages}. 不能为 {@literal null}.
	 * @return 在管道/事务中使用时给定 {@link Consumer} 或 {@literal null} 的待处理消息.
	 * @see <a href="https://redis.io/commands/xpending">Redis Documentation: xpending</a>
	 * @see <a href="http://doc.redisfans.com/key/xpending.html">Redis 命令中文文档: xpending</a>
	 */
	public Mono<PendingMessages> pending(String key, Consumer consumer) {
		return pending(key, consumer, Range.unbounded(), -1L);
	}

	/**
	 * 获取有关 {@literal Consumer Group}内给定{@link Range} 的待处理 {@link PendingMessages}的详细信息.
	 *
	 * @param key 存储流的 {@literal key} . 不能为 {@literal null}.
	 * @param group {@literal consumer group} 名. 不能为 {@literal null}.
	 * @param range 要搜索的消息 ID 范围。 不得为 {@literal null}.
	 * @param count 限制结果数量.
	 * @return 在管道/事务中使用时，给定 {@literal Consumer Group} 或 {@literal null} 的待处理消息.
	 * @see <a href="https://redis.io/commands/xpending">Redis Documentation: xpending</a>
	 * @see <a href="http://doc.redisfans.com/key/xpending.html">Redis 命令中文文档: xpending</a>
	 */
	public Mono<PendingMessages> pending(String key, String group, Range<?> range, long count) {
		return this.streamOps.pending(key, group, range, count);
	}

	/**
	 * 获取有关{@literal Consumer Group}内给定{@link Range}和{@link Consumer}的待处理{@link PendingMessages}的详细信息.
	 *
	 * @param key key. 不能为 {@literal null}.
	 * @param consumer {@link Consumer} 名. 不能为 {@literal null}.
	 * @param range 要搜索的消息 ID 范围. 不能为 {@literal null}.
	 * @param count 限制结果数量.
	 * @return pending messages for the given {@link Consumer} or {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/xpending">Redis Documentation: xpending</a>
	 * @see <a href="http://doc.redisfans.com/key/xpending.html">Redis 命令中文文档: xpending</a>
	 */
	public Mono<PendingMessages> pending(String key, Consumer consumer, Range<?> range, long count) {
		return this.streamOps.pending(key, consumer, range, count);
	}


	/**
	 * 从特定{@link Range}内的流中读取记录.
	 *
	 * @param key stream key.
	 * @param range 不能为 {@literal null}.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xrange">Redis Documentation: XRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrange.html">Redis 命令中文文档: XRANGE</a>
	 */
	public Flux<MapRecord<String, K, V>> range(String key, Range<String> range) {
		return range(key, range, Limit.unlimited());
	}

	/**
	 * 应用 {@link Limit} 从特定 {@link Range} 内的流中读取记录.
	 *
	 * @param key stream key.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xrange">Redis Documentation: XRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrange.html">Redis 命令中文文档: XRANGE</a>
	 */
	public Flux<MapRecord<String, K, V>> range(String key, Range<String> range, Limit limit) {
		return this.streamOps.range(key, range, limit);
	}

	/**
	 * 读取特定{@link Range}内流中的所有记录.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param key stream key.
	 * @param range 不能为 {@literal null}.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xrange">Redis Documentation: XRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrange.html">Redis 命令中文文档: XRANGE</a>
	 */
	public Flux<ObjectRecord<String, V>> range(Class<V> targetType, String key, Range<String> range) {
		return range(targetType, key, range, Limit.unlimited());
	}

	/**
	 * 应用 {@link Limit} 从特定 {@link Range} 内的流中读取记录.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param key stream key.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xrange">Redis Documentation: XRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrange.html">Redis 命令中文文档: XRANGE</a>
	 */
	public Flux<ObjectRecord<String, V>> range(Class<V> targetType, String key, Range<String> range, Limit limit) {

		Assert.notNull(targetType, "Target type must not be null");

		return range(key, range, limit).map(it -> map(it, targetType));
	}

	/**
	 * 从 {@link StreamOffset} 读取记录作为 {@link ObjectRecord}.
	 *
	 * @param stream 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xread">Redis Documentation: XREAD</a>
	 * @see <a href="http://doc.redisfans.com/key/xread.html">Redis 命令中文文档: XREAD</a>
	 */
	@SuppressWarnings("unchecked")
	public Flux<MapRecord<String, K, V>> read(StreamOffset<K> stream) {
		Assert.notNull(stream, "StreamOffset must not be null");
		return read(StreamReadOptions.empty(), new StreamOffset[] { stream });
	}

	/**
	 * 从 {@link StreamOffset} 读取记录作为 {@link ObjectRecord}.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param stream 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xread">Redis Documentation: XREAD</a>
	 * @see <a href="http://doc.redisfans.com/key/xread.html">Redis 命令中文文档: XREAD</a>
	 */
	public final Flux<ObjectRecord<String, V>> read(Class<V> targetType, StreamOffset<String> stream) {

		Assert.notNull(stream, "StreamOffset must not be null");

		return read(targetType, StreamReadOptions.empty(), stream);
	}

	/**
	 * 从一个或多个 {@link StreamOffset} 读取记录.
	 *
	 * @param streams 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xread">Redis Documentation: XREAD</a>
	 * @see <a href="http://doc.redisfans.com/key/xread.html">Redis 命令中文文档: XREAD</a>
	 */
	@SafeVarargs
	public final Flux<MapRecord<String, K, V>> read(StreamOffset<String>... streams) {
		return read(StreamReadOptions.empty(), streams);
	}

	/**
	 * 从一个或多个 {@link StreamOffset} 读取记录作为 {@link ObjectRecord}.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param streams 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xread">Redis Documentation: XREAD</a>
	 * @see <a href="http://doc.redisfans.com/key/xread.html">Redis 命令中文文档: XREAD</a>
	 */
	@SafeVarargs
	public final Flux<ObjectRecord<String, V>> read(Class<V> targetType, StreamOffset<String>... streams) {
		return read(targetType, StreamReadOptions.empty(), streams);
	}

	/**
	 * 从一个或多个 {@link StreamOffset} 读取记录.
	 *
	 * @param readOptions 读取选项.
	 * @param streams 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xread">Redis Documentation: XREAD</a>
	 * @see <a href="http://doc.redisfans.com/key/xread.html">Redis 命令中文文档: XREAD</a>
	 */
	@SafeVarargs
	public final Flux<MapRecord<String, K, V>> read(StreamReadOptions readOptions, StreamOffset<String>... streams) {
		return this.streamOps.read(readOptions, streams);
	}

	/**
	 * 从一个或多个 {@link StreamOffset} 读取记录作为 {@link ObjectRecord}.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param readOptions 读取选项.
	 * @param streams 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xread">Redis Documentation: XREAD</a>
	 * @see <a href="http://doc.redisfans.com/key/xread.html">Redis 命令中文文档: XREAD</a>
	 */
	@SafeVarargs
	public final Flux<ObjectRecord<String, V>> read(Class<V> targetType, StreamReadOptions readOptions,
			StreamOffset<String>... streams) {

		Assert.notNull(targetType, "Target type must not be null");

		return read(readOptions, streams).map(it -> map(it, targetType));
	}

	/**
	 * 使用消费者组从一个或多个 {@link StreamOffset} 读取记录.
	 *
	 * @param consumer consumer/group.
	 * @param streams 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xreadgroup">Redis Documentation: XREADGROUP</a>
	 * @see <a href="http://doc.redisfans.com/key/xreadgroup.html">Redis 命令中文文档: XREADGROUP</a>
	 */
	@SafeVarargs
	public final Flux<MapRecord<String, K, V>> read(Consumer consumer, StreamOffset<String>... streams) {
		return read(consumer, StreamReadOptions.empty(), streams);
	}

	/**
	 * 使用消费者组作为 {@link ObjectRecord} 从一个或多个 {@link StreamOffset} 读取记录.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param consumer consumer/group.
	 * @param streams 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xreadgroup">Redis Documentation: XREADGROUP</a>
	 * @see <a href="http://doc.redisfans.com/key/xreadgroup.html">Redis 命令中文文档: XREADGROUP</a>
	 */
	@SafeVarargs
	public final Flux<ObjectRecord<String, V>> read(Class<V> targetType, Consumer consumer, StreamOffset<String>... streams) {
		return read(targetType, consumer, StreamReadOptions.empty(), streams);
	}

	/**
	 * 使用消费者组从一个或多个 {@link StreamOffset} 读取记录.
	 *
	 * @param consumer consumer/group.
	 * @param readOptions 读取选项.
	 * @param streams 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xreadgroup">Redis Documentation: XREADGROUP</a>
	 * @see <a href="http://doc.redisfans.com/key/xreadgroup.html">Redis 命令中文文档: XREADGROUP</a>
	 */
	@SafeVarargs
	public final Flux<MapRecord<String, K, V>> read(Consumer consumer, StreamReadOptions readOptions, StreamOffset<String>... streams) {
		return this.streamOps.read(consumer, readOptions, streams);
	}

	/**
	 * 使用消费者组作为 {@link ObjectRecord} 从一个或多个 {@link StreamOffset} 读取记录.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param consumer consumer/group.
	 * @param readOptions 读取选项.
	 * @param streams 要读取的流.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xreadgroup">Redis Documentation: XREADGROUP</a>
	 * @see <a href="http://doc.redisfans.com/key/xreadgroup.html">Redis 命令中文文档: XREADGROUP</a>
	 */
	@SuppressWarnings("unchecked")
	public Flux<ObjectRecord<String, V>> read(Class<V> targetType, Consumer consumer, StreamReadOptions readOptions,
			StreamOffset<String>... streams) {

		Assert.notNull(targetType, "Target type must not be null");

		return read(consumer, readOptions, streams).map(it -> map(it, targetType));
	}

	/**
	 * 以相反顺序从特定{@link Range}内的流中读取记录.
	 *
	 * @param key stream key.
	 * @param range 不能为 {@literal null}.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xrevrange">Redis Documentation: XREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrevrange.html">Redis 命令中文文档: XREVRANGE</a>
	 */
	public Flux<MapRecord<String, K, V>> reverseRange(String key, Range<String> range) {
		return reverseRange(key, range, Limit.unlimited());
	}

	/**
	 * 以相反顺序应用 {@link Limit} 从特定 {@link Range} 内的流中读取记录.
	 *
	 * @param key stream key.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xrevrange">Redis Documentation: XREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrevrange.html">Redis 命令中文文档: XREVRANGE</a>
	 */
	public Flux<MapRecord<String, K, V>> reverseRange(String key, Range<String> range, Limit limit) {
		return this.streamOps.reverseRange(key, range, limit);
	}

	/**
	 * 以相反的顺序从特定 {@link Range} 内的流中读取记录，如 {@link ObjectRecord}.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param key stream key.
	 * @param range 不能为 {@literal null}.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xrevrange">Redis Documentation: XREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrevrange.html">Redis 命令中文文档: XREVRANGE</a>
	 */
	public Flux<ObjectRecord<String, V>> reverseRange(Class<V> targetType, String key, Range<String> range) {
		return reverseRange(targetType, key, range, Limit.unlimited());
	}

	/**
	 * 从特定 {@link Range} 内的流中读取记录，以相反的顺序应用 {@link Limit} 作为 {@link ObjectRecord}.
	 *
	 * @param targetType 有效负载的目标类型.
	 * @param key the stream key.
	 * @param range 不能为 {@literal null}.
	 * @param limit 不能为 {@literal null}.
	 * @return {@link Flux} 发出的 record 一一对应.
	 * @see <a href="https://redis.io/commands/xrevrange">Redis Documentation: XREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrevrange.html">Redis 命令中文文档: XREVRANGE</a>
	 */
	public Flux<ObjectRecord<String, V>> reverseRange(Class<V> targetType, String key, Range<String> range, Limit limit) {

		Assert.notNull(targetType, "Target type must not be null");

		return reverseRange(key, range, limit).map(it -> map(it, targetType));
	}

	/**
	 * 将流修剪为 {@code count} 元素.
	 *
	 * @param key stream key.
	 * @param count stream 长度.
	 * @return 删除的条目数.
	 * @see <a href="https://redis.io/commands/xtrim">Redis Documentation: XTRIM</a>
	 * @see <a href="http://doc.redisfans.com/key/xtrim.html">Redis 命令中文文档: XTRIM</a>
	 */
	public Mono<Long> trim(String key, long count) {
		return this.streamOps.trim(key, count);
	}

	/**
	 * 将流修剪为 {@code count} 元素.
	 *
	 * @param key stream key.
	 * @param count stream 长度.
	 * @param approximateTrimming 为了最大限度地提高性能，必须以近似的方式进行修剪.
	 * @return 删除的条目数.
	 * @see <a href="https://redis.io/commands/xtrim">Redis Documentation: XTRIM</a>
	 * @see <a href="http://doc.redisfans.com/key/xtrim.html">Redis 命令中文文档: XTRIM</a>
	 */
	public Mono<Long> trim(String key, long count, boolean approximateTrimming) {
		return this.streamOps.trim(key, count, approximateTrimming);
	}

	/**
	 * 将记录从 {@link MapRecord} 映射到 {@link ObjectRecord}.
	 *
	 * @param record the stream records to map.
	 * @param targetType 有效负载的目标类型.
	 * @return 映射的 {@link ObjectRecord}.
	 */
	public ObjectRecord<String, V> map(MapRecord<String, K, V> record, Class<V> targetType) {
		return this.streamOps.map(record, targetType);
	}

	/**
	 * 使用配置的序列化上下文将 {@link ByteBufferRecord} 反序列化为 {@link MapRecord}.
	 *
	 * @param record the stream record to map.
	 * @return 反序列化 {@link MapRecord}.
	 */
	public MapRecord<String, K, V> deserializeRecord(ByteBufferRecord record) {
		return this.streamOps.deserializeRecord(record);
	}
}
