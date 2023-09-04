package com.yilin.reactive.starter.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoSearchCommandArgs;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoSearchStoreCommandArgs;
import org.springframework.data.redis.core.ReactiveGeoOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.domain.geo.BoundingBox;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.domain.geo.GeoShape;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:Geo 类型的获取与设置
 *
 * @author jiac
 * @version 2023.0.1 2023/8/29:09:40
 * @since 2023.0.1
 */
public class RedisGeoOperations<V> extends RedisGenericOperations<V> {

	private final ReactiveGeoOperations<String, V> geoOps;

	public RedisGeoOperations(ReactiveRedisTemplate<String, V> reactiveRedisTemplate) {
		super(reactiveRedisTemplate);
		this.geoOps = reactiveRedisTemplate.opsForGeo();
	}

	/**
	 * 添加经纬度.
	 *
	 * @param key key
	 * @param lng 经度
	 * @param lat 纬度
	 * @param member 元素
	 * @return 添加的元素数量.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @see <a href="http://doc.redisfans.com/string/geoadd.html">Redis 命令中文文档: GEOADD</a>
	 */
	public Mono<Long> add(String key, double lng, double lat, V member) {
		return this.geoOps.add(key, new Point(lng, lat), member);
	}

	/**
	 * 将具有给定成员 {@literal name} 的 {@link Point} 添加到 {@literal key}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param point 不能为 {@literal null}.
	 * @param member 不能为 {@literal null}.
	 * @return 添加的元素数量.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @see <a href="http://doc.redisfans.com/string/geoadd.html">Redis 命令中文文档: GEOADD</a>
	 */
	public Mono<Long> add(String key, Point point, V member) {
		return this.geoOps.add(key, point, member);
	}

	/**
	 * 将 {@link GeoLocation} 添加到 {@literal key}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param location 不能为 {@literal null}.
	 * @return 添加的元素数量.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @see <a href="http://doc.redisfans.com/string/geoadd.html">Redis 命令中文文档: GEOADD</a>
	 */
	public Mono<Long> add(String key, GeoLocation<V> location) {
		return this.geoOps.add(key, location);
	}

	/**
	 * 将 成员/{@link Point}对 的{@link Map}添加到{@literal key}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param memberCoordinateMap 不能为 {@literal null}.
	 * @return 添加的元素数量.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @see <a href="http://doc.redisfans.com/string/geoadd.html">Redis 命令中文文档: GEOADD</a>
	 */
	public Mono<Long> add(String key, Map<V, Point> memberCoordinateMap) {
		return this.geoOps.add(key, memberCoordinateMap);
	}

	/**
	 * 将 {@link GeoLocation}s 添加到 {@literal key}
	 *
	 * @param key 不能为 {@literal null}.
	 * @param locations 不能为 {@literal null}.
	 * @return 添加的元素数量.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @see <a href="http://doc.redisfans.com/string/geoadd.html">Redis 命令中文文档: GEOADD</a>
	 */
	public Mono<Long> add(String key, Iterable<GeoLocation<V>> locations) {
		return this.geoOps.add(key, locations);
	}

	/**
	 * 将 {@link GeoLocation}s 添加到 {@literal key}
	 *
	 * @param key 不能为 {@literal null}.
	 * @param locations 不能为 {@literal null}.
	 * @return 添加的元素数量.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @see <a href="http://doc.redisfans.com/string/geoadd.html">Redis 命令中文文档: GEOADD</a>
	 */
	public Flux<Long> add(String key, Publisher<? extends Collection<GeoLocation<V>>> locations) {
		return this.geoOps.add(key, locations);
	}

	/**
	 * 获取 {@literal member1} 和 {@literal member2} 之间的 {@link Distance}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param member1 不能为 {@literal null}.
	 * @param member2 不能为 {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 * @see <a href="http://doc.redisfans.com/string/geodist.html">Redis 命令中文文档: GEODIST</a>
	 */
	public Mono<Distance> distance(String key, V member1, V member2) {
		return this.geoOps.distance(key, member1, member2);
	}

	/**
	 * 获取给定 {@link Metric} 中 {@literal member1} 和 {@literal member2} 之间的 {@link Distance}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param member1 不能为 {@literal null}.
	 * @param member2 不能为 {@literal null}.
	 * @param metric 不能为 {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 * @see <a href="http://doc.redisfans.com/string/geodist.html">Redis 命令中文文档: GEODIST</a>
	 */
	public Mono<Distance> distance(String key, V member1, V member2, Metric metric) {
		return this.geoOps.distance(key, member1, member2, metric);
	}

	/**
	 * 获取一个或多个 {@literal member} 位置的 Geohash 表示形式.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param member 不能为 {@literal null}.
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
	 * @see <a href="http://doc.redisfans.com/string/geohash.html">Redis 命令中文文档: GEOHASH</a>
	 */
	public Mono<String> hash(String key, V member) {
		return this.geoOps.hash(key, member);
	}

	/**
	 * 获取一个或多个 {@literal member} 位置的 Geohash 表示形式.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param members 不能为 {@literal null}.
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
	 * @see <a href="http://doc.redisfans.com/string/geohash.html">Redis 命令中文文档: GEOHASH</a>
	 */
	@SafeVarargs
	public final Mono<List<String>> hash(String key, V... members) {
		return this.geoOps.hash(key, members);
	}

	/**
	 * 获取一个或多个 {@literal member} 位置的 {@link Point} 表示.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param member 不能为 {@literal null}.
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
	 * @see <a href="http://doc.redisfans.com/string/geopos.html">Redis 命令中文文档: GEOPOS</a>
	 */
	public Mono<Point> position(String key, V member) {
		return this.geoOps.position(key, member);
	}

	/**
	 * 获取一个或多个 {@literal member} 位置的 {@link Point} 表示.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param members 不能为 {@literal null}.
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
	 * @see <a href="http://doc.redisfans.com/string/geopos.html">Redis 命令中文文档: GEOPOS</a>
	 */
	@SafeVarargs
	public final Mono<List<Point>> position(String key, V... members) {
		return this.geoOps.position(key, members);
	}

	/**
	 * 获取给定 {@link Circle} 边界内的 {@literal member}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param within 不能为 {@literal null}.
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 * @see <a href="http://doc.redisfans.com/string/georadius.html">Redis 命令中文文档: GEORADIUS</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> radius(String key, Circle within) {
		return this.geoOps.radius(key, within);
	}

	/**
	 * 应用 {@link GeoRadiusCommandArgs} 获取给定 {@link Circle} 边界内的 {@literal member}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param within 不能为 {@literal null}.
	 * @param args 不能为 {@literal null}.
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 * @see <a href="http://doc.redisfans.com/string/georadius.html">Redis 命令中文文档: GEORADIUS</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> radius(String key, Circle within, GeoRadiusCommandArgs args) {
		return this.geoOps.radius(key, within, args);
	}

	/**
	 * 获取由 {@literal member} 坐标定义的圆内的 {@literal member} {@literal radius}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param member 不能为 {@literal null}.
	 * @param radius 半径
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/string/georadiusbymember.html">Redis 命令中文文档: GEORADIUSBYMEMBER</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> radiusByMember(String key, V member, double radius) {
		return this.geoOps.radius(key, member, radius);
	}

	/**
	 * 获取由 {@literal member} 坐标定义的圆内的 {@literal member} {@literal radius} 应用 {@link Metric}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param member 不能为 {@literal null}.
	 * @param distance 不能为 {@literal null}.
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/string/georadiusbymember.html">Redis 命令中文文档: GEORADIUSBYMEMBER</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> radiusByMember(String key, V member, Distance distance) {
		return this.geoOps.radius(key, member, distance);
	}

	/**
	 * 获取由 {@literal member} 坐标定义的圆内的 {@literal member}  {@literal radius} 应用 {@link Metric} 和 {@link GeoRadiusCommandArgs}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param member 不能为 {@literal null}.
	 * @param distance 不能为 {@literal null}.
	 * @param args 不能为 {@literal null}.
	 * @return never {@literal null}.
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 * @see <a href="http://doc.redisfans.com/string/georadiusbymember.html">Redis 命令中文文档: GEORADIUSBYMEMBER</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> radiusByMember(String key, V member, Distance distance, GeoRadiusCommandArgs args) {
		return this.geoOps.radius(key, member, distance, args);
	}

	/**
	 * 移除 key 中的 {@literal member}s.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param members 不能为 {@literal null}.
	 * @return Number of elements removed.
	 * @see RedisSortedSetOperations#zSetRem(String, Object...)
	 */
	@SafeVarargs
	public final Mono<Long> remove(String key, V... members) {
		return this.geoOps.remove(key, members);
	}

	/**
	 * 移除 {@literal key}.
	 *
	 * @param key 不能为 {@literal null}.
	 */
	public Mono<Boolean> delete(String key) {
		return this.geoOps.delete(key);
	}

	/**
	 * 获取给定 {@link Circle} 边界内的 {@literal member}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param within 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearch.html">Redis 命令中文文档: GEOSEARCH</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> search(String key, Circle within) {
		return search(key, GeoReference.fromCircle(within), GeoShape.byRadius(within.getRadius()),
				GeoSearchCommandArgs.newGeoSearchArgs());
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member} {@link Distance}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param radius 不能为 {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearch.html">Redis 命令中文文档: GEOSEARCH</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> search(String key, GeoReference<V> reference, Distance radius) {
		return search(key, reference, radius, GeoSearchCommandArgs.newGeoSearchArgs());
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member}  {@link Distance} 应用 {@link GeoRadiusCommandArgs}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param radius 不能为 {@literal null}.
	 * @param args 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearch.html">Redis 命令中文文档: GEOSEARCH</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> search(String key, GeoReference<V> reference, Distance radius,
			GeoSearchCommandArgs args) {
		return search(key, reference, GeoShape.byRadius(radius), args);
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member}  边界框.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param boundingBox 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearch.html">Redis 命令中文文档: GEOSEARCH</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> search(String key, GeoReference<V> reference,
			BoundingBox boundingBox) {
		return search(key, reference, boundingBox, GeoSearchCommandArgs.newGeoSearchArgs());
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member}  应用 {@link GeoRadiusCommandArgs} 的边界框.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param boundingBox 不能为 {@literal null}.
	 * @param args 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearch.html">Redis 命令中文文档: GEOSEARCH</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> search(String key, GeoReference<V> reference,
			BoundingBox boundingBox, GeoSearchCommandArgs args) {
		return search(key, reference, GeoShape.byBox(boundingBox), args);
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member}  {@link GeoShape 谓词} 应用 {@link GeoRadiusCommandArgs}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param geoPredicate 不能为 {@literal null}.
	 * @param args 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearch.html">Redis 命令中文文档: GEOSEARCH</a>
	 */
	public Flux<GeoResult<GeoLocation<V>>> search(String key, GeoReference<V> reference, GeoShape geoPredicate,
			GeoSearchCommandArgs args) {
		return this.geoOps.search(key, reference, geoPredicate, args);
	}

	/**
	 * 获取给定 {@link Circle} 边界内的 {@literal member} 并将结果存储在 {@code destKey}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param within 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearchstore.html">Redis 命令中文文档: GEOSEARCHSTORE</a>
	 */
	public Mono<Long> searchStore(String key, String destKey, Circle within) {
		return searchStore(key, destKey, GeoReference.fromCircle(within), GeoShape.byRadius(within.getRadius()),
				GeoSearchStoreCommandArgs.newGeoSearchStoreArgs());
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member} {@link Distance} 并将结果存储在 {@code destKey}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param radius 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearchstore.html">Redis 命令中文文档: GEOSEARCHSTORE</a>
	 */
	public Mono<Long> searchStore(String key, String destKey, GeoReference<V> reference, Distance radius) {
		return searchStore(key, destKey, reference, radius, GeoSearchStoreCommandArgs.newGeoSearchStoreArgs());
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member}  {@link Distance radius} 应用 {@link GeoRadiusCommandArgs} 并将结果存储在 {@code destKey}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param radius 不能为 {@literal null}.
	 * @param args 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearchstore.html">Redis 命令中文文档: GEOSEARCHSTORE</a>
	 */
	public Mono<Long> searchStore(String key, String destKey, GeoReference<V> reference, Distance radius,
			GeoSearchStoreCommandArgs args) {
		return searchStore(key, destKey, reference, GeoShape.byRadius(radius), args);
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member} 边界框并将结果存储在{@code destKey}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param boundingBox 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearchstore.html">Redis 命令中文文档: GEOSEARCHSTORE</a>
	 */
	public Mono<Long> searchStore(String key, String destKey, GeoReference<V> reference,
			BoundingBox boundingBox) {
		return searchStore(key, destKey, reference, boundingBox, GeoSearchStoreCommandArgs.newGeoSearchStoreArgs());
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member} 应用 {@link GeoRadiusCommandArgs} 的边界框并将结果存储在 {@code destKey}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param boundingBox 不能为 {@literal null}.
	 * @param args 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearchstore.html">Redis 命令中文文档: GEOSEARCHSTORE</a>
	 */
	public Mono<Long> searchStore(String key, String destKey, GeoReference<V> reference,
			BoundingBox boundingBox, GeoSearchStoreCommandArgs args) {
		return searchStore(key, destKey, reference, GeoShape.byBox(boundingBox), args);
	}

	/**
	 * 使用 {@link GeoReference} 作为给定边界内查询的中心获取 {@literal member} {@link GeoShape predicate} 应用 {@link GeoRadiusCommandArgs} 并将结果存储在 {@code destKey}.
	 *
	 * @param key 不能为 {@literal null}.
	 * @param reference 不能为 {@literal null}.
	 * @param geoPredicate 不能为 {@literal null}.
	 * @param args 不能为 {@literal null}.
	 * @return /
	 * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
	 * @see <a href="http://doc.redisfans.com/string/geosearchstore.html">Redis 命令中文文档: GEOSEARCHSTORE</a>
	 */
	public Mono<Long> searchStore(String key, String destKey, GeoReference<V> reference, GeoShape geoPredicate,
			GeoSearchStoreCommandArgs args) {
		return this.geoOps.searchAndStore(key, destKey, reference, geoPredicate, args);
	}

}
