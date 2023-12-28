package com.yilin.reactive.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/8/21 10:18
 * @since 2023.0.1
 */
public class Maps {

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(Object... keysValues) {
		var length = keysValues.length;
		if (length % 2 != 0) {
			throw new IllegalArgumentException("the keysValues number must be odd!");
		}
		Map<K, V> maps = new HashMap<>(length);

		for (var i = length - 2; i >= 0; i -= 2) {
			var key = keysValues[i];
			var value = keysValues[i + 1];
			maps.put((K) key, (V) value);
		}
		return maps;
	}
}
