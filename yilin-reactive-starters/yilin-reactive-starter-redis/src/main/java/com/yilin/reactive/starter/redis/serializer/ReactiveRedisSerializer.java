package com.yilin.reactive.starter.redis.serializer;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.yilin.reactive.starter.redis.props.YiLinRedisProperties;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/11:12:09
 * @since 2023.0.1
 */
public interface ReactiveRedisSerializer {


	/**
	 * 序列化类型字段.
	 */
	String TYPE_NAME = "@class";

	/**
	 * 默认的序列化方式.
	 * @param properties 配置
	 * @return redisSerializer
	 */
	static RedisSerializer<Object> defaultRedisSerializer(YiLinRedisProperties properties) {
		YiLinRedisProperties.SerializerType serializerType = properties.getSerializerType();

		if (YiLinRedisProperties.SerializerType.JDK == serializerType) {
			/*
			  SpringBoot 扩展了 ClassLoader，进行分离打包的时候，使用到 JdkSerializationRedisSerializer 的地方
			  会因为 ClassLoader 的不同导致加载不到 Class 指定使用项目的 ClassLoader
			  JdkSerializationRedisSerializer
			  默认使用 {@link sun.misc.Launcher.AppClassLoader}
			  SpringBoot 默认使用 {@link org.springframework.boot.loader.LaunchedURLClassLoader}
			 */
			ClassLoader classLoader = ReactiveRedisSerializer.class.getClassLoader();
			return new JdkSerializationRedisSerializer(classLoader);
		}
		return new GenericJackson2JsonRedisSerializer(TYPE_NAME);
	}

	/**
	 * 序列化接口.
	 * @param properties 配置
	 * @return redisSerializer
	 */
	RedisSerializer<Object> redisSerializer(YiLinRedisProperties properties);
}
