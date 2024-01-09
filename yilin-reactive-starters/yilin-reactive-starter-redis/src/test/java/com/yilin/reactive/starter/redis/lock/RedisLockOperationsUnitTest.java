package com.yilin.reactive.starter.redis.lock;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import com.yilin.reactive.starter.redis.AbstractRedisReactiveTestSupport;
import com.yilin.reactive.starter.redis.core.RedisLockOperations;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/9/6 14:40
 * @since 2024.0.1
 */
public class RedisLockOperationsUnitTest extends AbstractRedisReactiveTestSupport {

	private static final Logger log = LoggerFactory.getLogger(RedisLockOperationsUnitTest.class);

	private final RedisLockOperations lockOps;

	private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

	private final RedissonReactiveClient reactiveClient;

	@Autowired
	public RedisLockOperationsUnitTest(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
		super(reactiveRedisTemplate);
		this.reactiveClient = getRedissonClient();
		this.reactiveRedisTemplate = reactiveRedisTemplate;
		this.lockOps = new RedisLockOperations(Redisson.create().reactive());
	}

	@Test
	void testReentrant() {
		var client = this.lockOps
				.reentrant("redisLock")
				.apply(RedisLockConfigurer.crete()
						.timeUnit(TimeUnit.SECONDS)
						.leaseTime(200)
						.waitTime(100))
				.using();

		var stock = new AtomicInteger(5000);

		Flux.range(0, 5000)
				.parallel()
				.runOn(Schedulers.parallel())
				.flatMap(i -> client.synchronize(() -> {
					int nextStock = stock.decrementAndGet();
					int currentStock = Math.max(nextStock, 0);
					return Mono.just(currentStock);
				}))
				.as(StepVerifier::create)
				.expectNextCount(4999)
				.expectNext(0)
				.verifyComplete();
	}


	@Test
	void testMultiLock() {
		var client = this.lockOps
				.multiLock(locks ->
						locks.lock((lock) -> lock.reentrant("multi1")
										.apply(RedisLockConfigurer.crete())
										.using()
										.getReactiveLock())
								.lock((lock) -> lock.reentrant("multi2")
										.apply(RedisLockConfigurer.crete())
										.using()
										.getReactiveLock())
								.combine())
				.apply(RedisLockConfigurer.crete()
						.leaseTime(90)
						.waitTime(40)
						.timeUnit(TimeUnit.MINUTES))
				.using();

		var stock = new AtomicInteger(5000);
		Flux.range(0, 5000)
				.parallel()
				.runOn(Schedulers.parallel())
				.flatMap(i -> client.synchronize(() -> {
					int nextStock = stock.decrementAndGet();
					int currentStock = Math.max(nextStock, 0);
					return Mono.just(currentStock);
				}))
				.as(StepVerifier::create)
				.expectNextCount(4999)
				.expectNext(0)
				.verifyComplete();
	}

	@Test
	void readLock() {
		this.lockOps
				.readWriteLock("name")
				.readLock();
	}

	@Test
	public void context() throws InterruptedException {
		var observedContextValues = new ConcurrentHashMap<String, AtomicInteger>();

		var max = 3;
		var key = "key1";
		var cdl = new CountDownLatch(max);

		var context = Context.of(key, "value1");

		var just = Flux.range(0, max)
				.delayElements(Duration.ofMillis(1))
				.doOnEach((Signal<Integer> integerSignal) -> { // <1>
					var currentContext = integerSignal.getContextView();
					if (integerSignal.getType().equals(SignalType.ON_NEXT)) {
						String key1 = context.get(key);
						Assertions.assertNotNull(key1);

						log.info("key = {}, value = {}", key1, observedContextValues.get(key1));
						Assertions.assertEquals(key1, "value1");
						observedContextValues.computeIfAbsent(key1, k -> new AtomicInteger(0)).incrementAndGet();
					}
				})
				.contextWrite(context);

		just.subscribe(integer -> {
			log.info("integer: " + integer);
			cdl.countDown();
		});

		cdl.await();

		Assertions.assertEquals(observedContextValues.get(key), max);
	}

	void test() {

	}
}
