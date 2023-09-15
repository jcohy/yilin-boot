package com.yilin.reactive.starter.redis.lock;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import org.redisson.api.RLockReactive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/8:16:42
 * @since 2023.0.1
 */
public interface GenericLockClient {

	Logger log = LoggerFactory.getLogger(GenericLockClient.class);


	RLockReactive getReactiveLock();

	<T> Mono<T> synchronize(Supplier<Mono<T>> executableSupplier);

	default <T> Mono<T> synchronize(RLockReactive lock, String name, RedisLockConfigurer configurer, Supplier<Mono<T>> executableSupplier) {
		var threadId = ThreadLocalRandom.current().nextLong();
		return lock.tryLock(configurer.waitTime(), configurer.leaseTime(), configurer.timeUnit(), threadId)
				.doOnSubscribe(subscription -> log.info("Locked Resource, name = {}, threadId = {}", name, threadId))
				.flatMap(isLocked -> {
					if (isLocked) {
						return executableSupplier.get();
					}
					else {
						return Mono.error(new RuntimeException("Couldn't get redis lock"));
					}
				})
				.doFinally(signalType -> {
					Mono.fromRunnable(() -> log.info("Unlocked Resource, name = {}, threadId = {}", name, threadId))
							.then(lock.unlock(threadId))
							.subscribe();
				});
	}
}
