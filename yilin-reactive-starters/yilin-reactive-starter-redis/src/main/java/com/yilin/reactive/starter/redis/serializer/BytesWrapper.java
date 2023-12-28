package com.yilin.reactive.starter.redis.serializer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description: redis 序列化辅助类.单纯的泛型无法定义通用schema，原因是无法通过泛型 T 得到 Class.
 *
 * @author jcohy
 * @version 2023.0.1 2023/8/11 14:40
 * @since 2023.0.1
 */
public class BytesWrapper<T> {

	private T value;

	public BytesWrapper() {
	}

	public BytesWrapper(T value) {
		this.value = value;
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BytesWrapper<T> clone() {
		try {
			return (BytesWrapper) super.clone();
		}
		catch (CloneNotSupportedException ex) {
			return new BytesWrapper<>();
		}
	}
}
