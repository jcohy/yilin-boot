package com.yilin.boot.configuration.processor;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.yilin.boot.configuration.processor.utils.Constants;
import com.yilin.boot.configuration.processor.utils.Elements;
import com.yilin.boot.configuration.processor.utils.MultiMapSet;
import com.yilin.boot.configuration.processor.value.ValueExtractor;

/**
 * Copyright: Copyright (c) 2022
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description: 处理 @YiLinAutoService 注解，并将其作为 {@link java.util.ServiceLoader#load(Class)}
 * 服务类文件.
 *
 * @author jiac
 * @version 2023.0.1 2023/7/3:23:20
 * @since 2023.0.1
 */
@SupportedAnnotationTypes({ "com.yilin.boot.configuration.processor.annotations.YiLinAutoService" })
public class YiLinAutoServiceProcessor extends AbstractConfigureAnnotationProcessor {

	/**
	 * 待处理的注解集合.
	 */
	private final Map<String, String> annotations;

	/**
	 * 属性提取器.
	 */
	private final Map<String, ValueExtractor> valueExtractors;

	/**
	 * spi 服务集合，key 接口 -> value 实现列表.
	 */
	private final MultiMapSet<String, String> providers = new MultiMapSet<>();

	public YiLinAutoServiceProcessor() {
		Map<String, String> annotations = new LinkedHashMap<>();
		addAnnotations(annotations);
		this.annotations = Collections.unmodifiableMap(annotations);
		Map<String, ValueExtractor> valueExtractors = new LinkedHashMap<>();
		addValueExtractors(valueExtractors);
		this.valueExtractors = Collections.unmodifiableMap(valueExtractors);
	}

	private void addAnnotations(Map<String, String> annotations) {
		annotations.put("YiLinAutoService", "com.yilin.boot.configuration.processor.annotations.YiLinAutoService");
	}

	private void addValueExtractors(Map<String, ValueExtractor> attributes) {
		attributes.put("YiLinAutoService", ValueExtractor.allFrom("value"));
	}

	@Override
	protected boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Map.Entry<String, String> entry : this.annotations.entrySet()) {
			processImpl(roundEnv, entry.getKey(), entry.getValue());
		}
		if (roundEnv.processingOver()) {
			try {
				writeProperties();
			}
			catch (Exception ex) {
				throw new IllegalStateException("Failed to write metadata", ex);
			}
		}
		return false;
	}

	private void processImpl(RoundEnvironment roundEnv, String propertyKey, String annotationName) {
		TypeElement annotationType = this.processingEnv.getElementUtils().getTypeElement(annotationName);
		if (annotationType != null) {
			for (Element element : roundEnv.getElementsAnnotatedWith(annotationType)) {
				processElement(element, propertyKey, annotationName);
			}
		}
	}

	private void processElement(Element element, String propertyKey, String annotationName) {
		try {
			String qualifiedName = Elements.getQualifiedName(element);
			AnnotationMirror annotation = getAnnotation(element, annotationName);
			if (qualifiedName != null && annotation != null) {
				List<Object> interfaces = getValues(propertyKey, annotation);
				log("provider interface: " + interfaces);
				log("provider implementer: " + qualifiedName);
				this.providers.put(interfaces.get(0).toString(), qualifiedName);
			}
		}
		catch (Exception ex) {
			throw new IllegalStateException("Error processing configuration meta-data on " + element, ex);
		}
	}

	private AnnotationMirror getAnnotation(Element element, String type) {
		if (element != null) {
			for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
				if (type.equals(annotation.getAnnotationType().toString())) {
					return annotation;
				}
			}
		}
		return null;
	}

	private List<Object> getValues(String propertyKey, AnnotationMirror annotation) {
		ValueExtractor extractor = this.valueExtractors.get(propertyKey);
		if (extractor == null) {
			return Collections.emptyList();
		}
		return extractor.getValues(annotation);
	}

	/**
	 * 输出文件.
	 */
	private void writeProperties() {
		this.providers.keySet().forEach(providerInterface -> {
			String resourceFile = Constants.SERVICE_RESOURCE_LOCATION + providerInterface;
			writeResourcesFile(resourceFile, this.providers.get(providerInterface));
		});
	}

}
