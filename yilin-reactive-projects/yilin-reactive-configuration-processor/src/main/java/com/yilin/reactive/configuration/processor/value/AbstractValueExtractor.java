package com.yilin.reactive.configuration.processor.value;

import java.util.List;
import java.util.stream.Stream;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.DeclaredType;

import com.yilin.reactive.configuration.processor.utils.Elements;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/4 17:35
 * @since 2023.0.1
 */
public abstract class AbstractValueExtractor implements ValueExtractor {

	/**
	 * 提取值.
	 *
	 * @param annotationValue 表示注解类型元素的值。
	 * @return /
	 */
	@SuppressWarnings("unchecked")
	protected Stream<Object> extractValues(AnnotationValue annotationValue) {
		if (annotationValue == null) {
			return Stream.empty();
		}

		Object value = annotationValue.getValue();

		if (value instanceof List) {
			return ((List<AnnotationValue>) value).stream().map((annotation) -> extractValue(annotation.getValue()));
		}

		return Stream.of(value);
	}

	private Object extractValue(Object value) {
		if (value instanceof DeclaredType) {
			return Elements.getQualifiedName(((DeclaredType) value).asElement());
		}
		return value;
	}

}
