package com.yilin.reactive.starter.redis.serializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson2.JSON;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/11 15:31
 * @since 2024.0.1
 */
public class StringRedisSerializer implements RedisSerializer<String> {

	private final Charset charset;

	public StringRedisSerializer() {
		this(StandardCharsets.UTF_8);
	}

	private StringRedisSerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
	}

	@Override
	public String deserialize(byte[] bytes) {
		return (bytes != null) ? new String(bytes, this.charset) : null;
	}

	@Override
	public byte[] serialize(String object) {
		String string = JSON.toJSONString(object);
		if (!StringUtils.hasText(string)) {
			return null;
		}
		string = string.replace("\"", "");
		return string.getBytes(this.charset);
	}
}
