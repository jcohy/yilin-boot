package com.yilin.reactive.starter.redis.serializer;

import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.support.config.FastJsonConfig;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/11 14:59
 * @since 2024.0.1
 */
public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

	private final Class<T> clazz;

	private FastJsonConfig config = new FastJsonConfig();

	public FastJson2RedisSerializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
		this.config = fastJsonConfig;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		return JSON.toJSONString(t, config.getDateFormat(), config.getWriterFilters(), config.getWriterFeatures()).getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		String str = new String(bytes, StandardCharsets.UTF_8);
		return JSON.parseObject(str, this.clazz);
	}
}
