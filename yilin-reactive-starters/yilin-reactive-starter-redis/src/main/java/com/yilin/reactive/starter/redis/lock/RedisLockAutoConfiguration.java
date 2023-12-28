package com.yilin.reactive.starter.redis.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import com.yilin.reactive.starter.redis.props.YiLinRedisProperties;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/9/2 14:32
 * @since 2023.0.1
 */
@AutoConfiguration
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(YiLinRedisProperties.class)
@ConditionalOnProperty(value = "yilin.redis.lock.enabled", havingValue = "true")
public class RedisLockAutoConfiguration {

	/**
	 * 创建单机版配置
	 *
	 * @param properties props
	 * @return {@link Config}
	 */
	private static Config singleConfig(YiLinRedisProperties properties) {
		var lockProps = properties.getLock();
		var pool = lockProps.getPool();
		var config = new Config();
		SingleServerConfig serverConfig = config.useSingleServer()
				.setAddress(lockProps.getAddress())
				.setConnectionPoolSize(pool.getSize())
				.setConnectionMinimumIdleSize(pool.getMinIdle())
				.setIdleConnectionTimeout(pool.getConnectionTimeout())
				.setConnectTimeout(pool.getConnectionTimeout())
				.setTimeout(lockProps.getTimeout())
				.setDatabase(lockProps.getDatabase());
		if (StringUtils.hasText(lockProps.getPassword())) {
			serverConfig.setPassword(lockProps.getPassword());
		}
		return config;
	}

	@Bean
	@ConditionalOnMissingBean(RedissonReactiveClient.class)
	public RedissonReactiveClient reactiveClient(YiLinRedisProperties properties) {
		return Redisson.create(singleConfig(properties)).reactive();
	}
}
