package com.yilin.reactive.starter.redis.serializer;

import java.util.Objects;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/8/11 14:40
 * @since 2023.0.1
 */
@SuppressWarnings("rawtypes")
public class ProtoStuffSerializer implements RedisSerializer<Object> {

	private final Schema<BytesWrapper> schema;

	public ProtoStuffSerializer() {
		this.schema = RuntimeSchema.getSchema(BytesWrapper.class);
	}

	@Override
	public byte[] serialize(Object object) throws SerializationException {
		if (object == null) {
			return null;
		}
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			return ProtostuffIOUtil.toByteArray(new BytesWrapper<>(object), this.schema, buffer);
		}
		finally {
			buffer.clear();
		}
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (Objects.isNull(bytes)) {
			return null;
		}
		BytesWrapper<Object> wrapper = new BytesWrapper<>();
		ProtostuffIOUtil.mergeFrom(bytes, wrapper, this.schema);
		return wrapper.getValue();
	}

}
