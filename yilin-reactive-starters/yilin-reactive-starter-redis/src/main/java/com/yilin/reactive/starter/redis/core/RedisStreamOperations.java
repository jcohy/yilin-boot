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
	 * @param key the stream key.
	 * @param group name of the consumer group.
	 * @param recordIds record Id's to acknowledge.
	 * @return the {@link Mono} emitting the length of acknowledged records.
	 * @see <a href="https://redis.io/commands/xack">Redis Documentation: XACK</a>
	 * @see <a href="http://doc.redisfans.com/key/xack.html">Redis 命令中文文档: XACK</a>
	 */
	public Mono<Long> streamXAcknowledge(String key, String group, String... recordIds) {
		return streamXAcknowledge(key, group, Arrays.stream(recordIds).map(RecordId::of).toArray(RecordId[]::new));
	}

	/**
	 * 确认已处理的一条或多条记录.
	 *
	 * @param key the stream key.
	 * @param group name of the consumer group.
	 * @param recordIds record Id's to acknowledge.
	 * @return the {@link Mono} emitting the length of acknowledged records.
	 * @see <a href="https://redis.io/commands/xack">Redis Documentation: XACK</a>
	 * @see <a href="http://doc.redisfans.com/key/xack.html">Redis 命令中文文档: XACK</a>
	 */
	public Mono<Long> streamXAcknowledge(String key, String group, RecordId... recordIds) {
		return this.streamOps.acknowledge(key, group, recordIds);
	}

	/**
	 * 确认已处理的一条或多条记录.
	 *
	 * @param group name of the consumer group.
	 * @param record the {@link Record} to acknowledge.
	 * @return the {@link Mono} emitting the length of acknowledged records.
	 * @see <a href="https://redis.io/commands/xack">Redis Documentation: XACK</a>
	 * @see <a href="http://doc.redisfans.com/key/xack.html">Redis 命令中文文档: XACK</a>
	 */
	public Mono<Long> streamXAcknowledge(String group, Record<String, ?> record) {
		return streamXAcknowledge(record.getRequiredStream(), group, record.getId());
	}

	/**
	 * 将一条或多条记录添加到 stream {@code key}.
	 *
	 * @param key the stream key.
	 * @param bodyPublisher record body {@link Publisher}.
	 * @return the record Ids.
	 * @see <a href="https://redis.io/commands/xadd">Redis Documentation: XADD</a>
	 * @see <a href="http://doc.redisfans.com/key/xadd.html">Redis 命令中文文档: XADD</a>
	 */
	public Flux<RecordId> streamXAdd(String key, Publisher<? extends Map<? extends K, ? extends V>> bodyPublisher) {
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
	public Mono<RecordId> streamXAdd(String key, Map<? extends K, ? extends V> content) {
		return streamXAdd(StreamRecords.newRecord().in(key).ofMap(content));
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
	public Mono<RecordId> streamXAdd(MapRecord<String, ? extends K, ? extends V> record) {
		return streamXAdd((Record) record);
	}

	/**
	 * 将由给定值支持的记录追加到流中。 该值将被散列和序列化.
	 *
	 * @param record must not be {@literal null}.
	 * @return the {@link Mono} emitting the {@link RecordId}.
	 * @see <a href="https://redis.io/commands/xadd">Redis Documentation: XADD</a>
	 * @see <a href="http://doc.redisfans.com/key/xadd.html">Redis 命令中文文档: XADD</a>
	 * @see MapRecord
	 * @see ObjectRecord
	 */
	public Mono<RecordId> streamXAdd(Record<String, ?> record) {
		return this.streamOps.add(record);
	}

	/**
	 * 从流中删除指定的记录。 返回删除的记录数，如果某些 ID 不存在，则可能与传递的 ID 数不同.
	 *
	 * @param key the stream key.
	 * @param recordIds stream record Id's.
	 * @return the {@link Mono} emitting the number of removed records.
	 * @see <a href="https://redis.io/commands/xdel">Redis Documentation: XDEL</a>
	 * @see <a href="http://doc.redisfans.com/key/xdel.html">Redis 命令中文文档: XDEL</a>
	 */
	public Mono<Long> streamXDelete(String key, String... recordIds) {
		return streamXDelete(key, Arrays.stream(recordIds).map(RecordId::of).toArray(RecordId[]::new));
	}

	/**
	 * 从流中删除给定的 {@link Record}.
	 *
	 * @param record must not be {@literal null}.
	 * @return he {@link Mono} emitting the number of removed records.
	 * @see <a href="https://redis.io/commands/xdel">Redis Documentation: XDEL</a>
	 * @see <a href="http://doc.redisfans.com/key/xdel.html">Redis 命令中文文档: XDEL</a>
	 */
	public Mono<Long> streamXDelete(Record<String, ?> record) {
		return streamXDelete(record.getStream(), record.getId());
	}

	/**
	 * 从流中删除指定的记录。 返回删除的记录数，如果某些 ID 不存在，则可能与传递的 ID 数不同.
	 *
	 * @param key the stream key.
	 * @param recordIds stream record Id's.
	 * @return the {@link Mono} emitting the number of removed records.
	 * @see <a href="https://redis.io/commands/xdel">Redis Documentation: XDEL</a>
	 * @see <a href="http://doc.redisfans.com/key/xdel.html">Redis 命令中文文档: XDEL</a>
	 */
	public Mono<Long> streamXDelete(String key, RecordId... recordIds) {
		return this.streamOps.delete(key, recordIds);
	}

	/**
	 * 在 {@link ReadOffset#latest() 最新偏移量}处创建一个消费者组。 如果流尚不存在，则此命令将创建该流.
	 *
	 * @param key the {@literal key} the stream is stored at.
	 * @param group name of the consumer group.
	 * @return 如果成功，则 {@link Mono} 发出 {@literal OK} .. 在管道/事务中使用时 {@literal null}.
	 * @see <a href="https://redis.io/commands/xgroup-create">Redis Documentation: XGROUP CREATE</a>
	 * @see <a href="http://doc.redisfans.com/key/xgroup-create.html">Redis 命令中文文档: XGROUP CREATE</a>
	 */
	public Mono<String> streamGroupCreate(String key, String group) {
		return streamGroupCreate(key, ReadOffset.latest(), group);
	}

	/**
	 * 创建消费者组。 如果流尚不存在，则此命令将创建该流.
	 *
	 * @param key the {@literal key} the stream is stored at.
	 * @param readOffset the {@link ReadOffset} to apply.
	 * @param group name of the consumer group.
	 * @return the {@link Mono} emitting {@literal OK} if successful.
	 * @see <a href="https://redis.io/commands/xgroup-create">Redis Documentation: XGROUP CREATE</a>
	 * @see <a href="http://doc.redisfans.com/key/xgroup-create.html">Redis 命令中文文档: XGROUP CREATE</a>
	 */
	public Mono<String> streamGroupCreate(String key, ReadOffset readOffset, String group) {
		return this.streamOps.createGroup(key, readOffset, group);
	}

	/**
	 * 从消费者组中删除消费者.
	 *
	 * @param key the stream key.
	 * @param consumer consumer identified by group name and consumer key.
	 * @return 如果成功，则返回 {@link Mono} {@literal OK}。 {@literal null} 在管道/事务中使用时.
	 * @see <a href="https://redis.io/commands/xgroup-delconsumer">Redis Documentation: XGROUP DELCONSUMER</a>
	 * @see <a href="http://doc.redisfans.com/key/xgroup-delconsumer.html">Redis 命令中文文档: XGROUP DELCONSUMER</a>
	 */
	public Mono<String> streamGroupDeleteConsumer(String key, Consumer consumer) {
		return this.streamOps.deleteConsumer(key, consumer);
	}

	/**
	 * 销毁一个消费者组.
	 *
	 * @param key the stream key.
	 * @param group name of the consumer group.
	 * @return 如果成功，则返回 {@link Mono} {@literal OK}。 {@literal null} 在管道/事务中使用时.
	 * @see <a href="https://redis.io/commands/xgroup-destroy">Redis Documentation: XGROUP DESTROY</a>
	 * @see <a href="http://doc.redisfans.com/key/xgroup-destroy.html">Redis 命令中文文档: XGROUP DESTROY</a>
	 */
	public Mono<String> streamGroupDestroy(String key, String group) {
		return this.streamOps.destroyGroup(key, group);
	}

	/**
	 * 获取有关存储在指定 {@literal key} 的流的特定 {@literal 消费者组} 中的每个消费者的信息.
	 *
	 * @param key the {@literal key} the stream is stored at.
	 * @param group name of the {@literal consumer group}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/xinfo-consumers">Redis Documentation: XINFO CONSUMERS</a>
	 * @see <a href="http://doc.redisfans.com/key/xinfo-consumers.html">Redis 命令中文文档: XINFO DESTROY</a>
	 */
	public Flux<XInfoConsumer> streamInfoConsumers(String key, String group) {
		return this.streamOps.consumers(key, group);
	}

	/**
	 * 获取与存储在指定 {@literal key} 处的流关联的 {@literal Consumer groups} 的信息.
	 *
	 * @param key the {@literal key} the stream is stored at.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/xinfo-groups">Redis Documentation: XINFO GROUPS</a>
	 * @see <a href="http://doc.redisfans.com/key/xinfo-groups.html">Redis 命令中文文档: XINFO GROUPS</a>
	 */
	public Flux<XInfoGroup> streamInfoGroups(String key) {
		return this.streamOps.groups(key);
	}

	/**
	 * 获取有关存储在指定 {@literal key} 的流的一般信息.
	 *
	 * @param key the {@literal key} the stream is stored at.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/xinfo-stream">Redis Documentation: XINFO STREAM</a>
	 * @see <a href="http://doc.redisfans.com/key/xinfo-stream.html">Redis 命令中文文档: XINFO STREAM</a>
	 */
	public Mono<XInfoStream> streamInfoStream(String key) {
		return this.streamOps.info(key);
	}

	/**
	 * 获取流的长度.
	 *
	 * @param key the stream key.
	 * @return the {@link Mono} emitting the length of the stream.
	 * @see <a href="https://redis.io/commands/xlen">Redis Documentation: XLEN</a>
	 * @see <a href="http://doc.redisfans.com/key/xlen.html">Redis 命令中文文档: XLEN</a>
	 */
	public Mono<Long> streamLen(String key) {
		return this.streamOps.size(key);
	}

	/**
	 * 获取给定{@literal 消费者组}的{@link PendingMessagesSummary}.
	 *
	 * @param key the {@literal key} the stream is stored at. Must not be {@literal null}.
	 * @param group the name of the {@literal consumer group}. Must not be {@literal null}.
	 * @return 在管道/事务中使用时给定 {@literal Consumer Group} 或 {@literal null} 内待处理消息的摘要。
	 * @see <a href="https://redis.io/commands/xpending">Redis Documentation: xpending</a>
	 * @see <a href="http://doc.redisfans.com/key/xpending.html">Redis 命令中文文档: xpending</a>
	 */
	public Mono<PendingMessagesSummary> streamPending(String key, String group) {
		return this.streamOps.pending(key, group);
	}

	/**
	 * 获取有关给定 {@link Consumer} 的所有待处理消息的详细信息.
	 *
	 * @param key the {@literal key} the stream is stored at. Must not be {@literal null}.
	 * @param consumer the consumer to fetch {@link PendingMessages} for. Must not be {@literal null}.
	 * @return pending messages for the given {@link Consumer} or {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/xpending">Redis Documentation: xpending</a>
	 * @see <a href="http://doc.redisfans.com/key/xpending.html">Redis 命令中文文档: xpending</a>
	 */
	public Mono<PendingMessages> streamPending(String key, Consumer consumer) {
		return streamPending(key, consumer, Range.unbounded(), -1L);
	}

	/**
	 * 获取有关 {@literal Consumer Group}内给定{@link Range} 的待处理 {@link PendingMessages}的详细信息.
	 *
	 * @param key the {@literal key} the stream is stored at. Must not be {@literal null}.
	 * @param group the name of the {@literal consumer group}. Must not be {@literal null}.
	 * @param range the range of messages ids to search within. Must not be {@literal null}.
	 * @param count limit the number of results.
	 * @return 在管道/事务中使用时，给定 {@literal Consumer Group} 或 {@literal null} 的待处理消息.
	 * @see <a href="https://redis.io/commands/xpending">Redis Documentation: xpending</a>
	 * @see <a href="http://doc.redisfans.com/key/xpending.html">Redis 命令中文文档: xpending</a>
	 */
	public Mono<PendingMessages> streamPending(String key, String group, Range<?> range, long count) {
		return this.streamOps.pending(key, group, range, count);
	}

	/**
	 * 获取有关{@literal Consumer Group}内给定{@link Range}和{@link Consumer}的待处理{@link PendingMessages}的详细信息.
	 *
	 * @param key the {@literal key} the stream is stored at. Must not be {@literal null}.
	 * @param consumer the name of the {@link Consumer}. Must not be {@literal null}.
	 * @param range the range of messages ids to search within. Must not be {@literal null}.
	 * @param count limit the number of results.
	 * @return pending messages for the given {@link Consumer} or {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/xpending">Redis Documentation: xpending</a>
	 * @see <a href="http://doc.redisfans.com/key/xpending.html">Redis 命令中文文档: xpending</a>
	 */
	public Mono<PendingMessages> streamPending(String key, Consumer consumer, Range<?> range, long count) {
		return this.streamOps.pending(key, consumer, range, count);
	}


	/**
	 * 从特定{@link Range}内的流中读取记录.
	 *
	 * @param key the stream key.
	 * @param range must not be {@literal null}.
	 * @return the {@link Flux} emitting records one by one.
	 * @see <a href="https://redis.io/commands/xrange">Redis Documentation: XRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrange.html">Redis 命令中文文档: XRANGE</a>
	 */
	public Flux<MapRecord<String, K, V>> streamRange(String key, Range<String> range) {
		return streamRange(key, range, Limit.unlimited());
	}

	/**
	 * 应用 {@link Limit} 从特定 {@link Range} 内的流中读取记录.
	 *
	 * @param key the stream key.
	 * @param range must not be {@literal null}.
	 * @param limit must not be {@literal null}.
	 * @return lthe {@link Flux} emitting records one by one.
	 * @see <a href="https://redis.io/commands/xrange">Redis Documentation: XRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrange.html">Redis 命令中文文档: XRANGE</a>
	 */
	public Flux<MapRecord<String, K, V>> streamRange(String key, Range<String> range, Limit limit) {
		return this.streamOps.range(key, range, limit);
	}

	/**
	 * 读取特定{@link Range}内流中的所有记录.
	 *
	 * @param targetType the target type of the payload.
	 * @param key the stream key.
	 * @param range must not be {@literal null}.
	 * @return the {@link Flux} emitting records one by one.
	 * @see <a href="https://redis.io/commands/xrange">Redis Documentation: XRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrange.html">Redis 命令中文文档: XRANGE</a>
	 */
	public Flux<ObjectRecord<String, V>> streamRange(Class<V> targetType, String key, Range<String> range) {
		return streamRange(targetType, key, range, Limit.unlimited());
	}

	/**
	 * 应用 {@link Limit} 从特定 {@link Range} 内的流中读取记录.
	 *
	 * @param targetType the target type of the payload.
	 * @param key the stream key.
	 * @param range must not be {@literal null}.
	 * @param limit must not be {@literal null}.
	 * @return the {@link Flux} emitting records one by one.
	 * @see <a href="https://redis.io/commands/xrange">Redis Documentation: XRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrange.html">Redis 命令中文文档: XRANGE</a>
	 */
	public Flux<ObjectRecord<String, V>> streamRange(Class<V> targetType, String key, Range<String> range, Limit limit) {

		Assert.notNull(targetType, "Target type must not be null");

		return streamRange(key, range, limit).map(it -> map(it, targetType));
	}

	/**
	 * 从 {@link StreamOffset} 读取记录作为 {@link ObjectRecord}.
	 *
	 * @param stream the stream to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param targetType the target type of the payload.
	 * @param stream the stream to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param streams the streams to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param targetType the target type of the payload.
	 * @param streams the streams to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param readOptions read arguments.
	 * @param streams the streams to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param targetType the target type of the payload.
	 * @param readOptions read arguments.
	 * @param streams the streams to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param streams the streams to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param targetType the target type of the payload.
	 * @param consumer consumer/group.
	 * @param streams the streams to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param readOptions read arguments.
	 * @param streams the streams to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param targetType the target type of the payload.
	 * @param consumer consumer/group.
	 * @param readOptions read arguments.
	 * @param streams the streams to read from.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param key the stream key.
	 * @param range must not be {@literal null}.
	 * @return the {@link Flux} emitting records one by one.
	 * @see <a href="https://redis.io/commands/xrevrange">Redis Documentation: XREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrevrange.html">Redis 命令中文文档: XREVRANGE</a>
	 */
	public Flux<MapRecord<String, K, V>> reverseRange(String key, Range<String> range) {
		return reverseRange(key, range, Limit.unlimited());
	}

	/**
	 * 以相反顺序应用 {@link Limit} 从特定 {@link Range} 内的流中读取记录.
	 *
	 * @param key the stream key.
	 * @param range must not be {@literal null}.
	 * @param limit must not be {@literal null}.
	 * @return the {@link Flux} emitting records one by one.
	 * @see <a href="https://redis.io/commands/xrevrange">Redis Documentation: XREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrevrange.html">Redis 命令中文文档: XREVRANGE</a>
	 */
	public Flux<MapRecord<String, K, V>> reverseRange(String key, Range<String> range, Limit limit) {
		return this.streamOps.reverseRange(key, range, limit);
	}

	/**
	 * 以相反的顺序从特定 {@link Range} 内的流中读取记录，如 {@link ObjectRecord}.
	 *
	 * @param targetType the target type of the payload.
	 * @param key the stream key.
	 * @param range must not be {@literal null}.
	 * @return the {@link Flux} emitting records one by one.
	 * @see <a href="https://redis.io/commands/xrevrange">Redis Documentation: XREVRANGE</a>
	 * @see <a href="http://doc.redisfans.com/key/xrevrange.html">Redis 命令中文文档: XREVRANGE</a>
	 */
	public Flux<ObjectRecord<String, V>> reverseRange(Class<V> targetType, String key, Range<String> range) {
		return reverseRange(targetType, key, range, Limit.unlimited());
	}

	/**
	 * 从特定 {@link Range} 内的流中读取记录，以相反的顺序应用 {@link Limit} 作为 {@link ObjectRecord}.
	 *
	 * @param targetType the target type of the payload.
	 * @param key the stream key.
	 * @param range must not be {@literal null}.
	 * @param limit must not be {@literal null}.
	 * @return the {@link Flux} emitting records one by one.
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
	 * @param key the stream key.
	 * @param count length of the stream.
	 * @return number of removed entries.
	 * @see <a href="https://redis.io/commands/xtrim">Redis Documentation: XTRIM</a>
	 * @see <a href="http://doc.redisfans.com/key/xtrim.html">Redis 命令中文文档: XTRIM</a>
	 */
	public Mono<Long> trim(String key, long count) {
		return this.streamOps.trim(key, count);
	}

	/**
	 * 将流修剪为 {@code count} 元素.
	 *
	 * @param key the stream key.
	 * @param count length of the stream.
	 * @param approximateTrimming the trimming must be performed in a approximated way in order to maximize performances.
	 * @return number of removed entries.
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
	 * @param targetType the target type of the payload.
	 * @return the mapped {@link ObjectRecord}.
	 */
	public ObjectRecord<String, V> map(MapRecord<String, K, V> record, Class<V> targetType) {
		return this.streamOps.map(record, targetType);
	}

	/**
	 * 使用配置的序列化上下文将 {@link ByteBufferRecord} 反序列化为 {@link MapRecord}.
	 *
	 * @param record the stream record to map.
	 * @return deserialized {@link MapRecord}.
	 * @since 2.x
	 */
	public MapRecord<String, K, V> deserializeRecord(ByteBufferRecord record) {
		return this.streamOps.deserializeRecord(record);
	}
}
