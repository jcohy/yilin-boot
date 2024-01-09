package com.yilin.reactive.configuration.processor.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/4 17:35
 * @since 2024.0.1
 */
public class NamedValuesExtractor extends AbstractValueExtractor {

	private final Set<String> names;

	NamedValuesExtractor(String... names) {
		this.names = new HashSet<>(Arrays.asList(names));
	}

	@Override
	public List<Object> getValues(AnnotationMirror annotation) {
		List<Object> result = new ArrayList<>();
		annotation.getElementValues().forEach((key, value) -> {
			if (this.names.contains(key.getSimpleName().toString())) {
				extractValues(value).forEach(result::add);
			}
		});
		return result;
	}

}
