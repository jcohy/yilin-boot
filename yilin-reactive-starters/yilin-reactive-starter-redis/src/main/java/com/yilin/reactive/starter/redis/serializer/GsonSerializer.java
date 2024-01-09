package com.yilin.reactive.starter.redis.serializer;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/11 15:11
 * @since 2024.0.1
 */
public class GsonSerializer<T> implements RedisSerializer<T> {

	private final Gson gson;

	private final Class<T> clazz;

	public GsonSerializer(Class<T> clazz) {
		this.clazz = clazz;
		gson = new GsonBuilder()
				// 禁止转义 HTML 标签
				.disableHtmlEscaping()
				.registerTypeAdapter(Date.class, new DateSerializer())
				// 格式化输出
				.setPrettyPrinting()
				.create();
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		return gson.toJson(t).getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		String str = new String(bytes, StandardCharsets.UTF_8);
		return gson.fromJson(str, this.clazz);
	}

	/**
	 * 解决 Gson 反序列化时间是时间戳转为 Date 类型问题。
	 */
	private static class DateSerializer implements JsonDeserializer<Date> {
		public Date deserialize(JsonElement json, Type typeOfSrc, JsonDeserializationContext context) {
			return new Date(json.getAsJsonPrimitive().getAsLong());
		}
	}

}
