package com.yilin.reactive.configuration.processor.value;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/4 17:34
 * @since 2024.0.1
 */

@FunctionalInterface
public interface ValueExtractor {

	/**
	 * 指定注解的属性列表，后续会从此属性中提取值.
	 *
	 * @param names 属性列表
	 * @return valueExtractor
	 */
	static ValueExtractor allFrom(String... names) {
		return new NamedValuesExtractor(names);
	}

	/**
	 * 获取注解的值.
	 *
	 * @param annotation 注解
	 * @return 值
	 */
	List<Object> getValues(AnnotationMirror annotation);

}
