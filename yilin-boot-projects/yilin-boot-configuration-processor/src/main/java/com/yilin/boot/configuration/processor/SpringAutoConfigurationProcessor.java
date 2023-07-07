package com.yilin.boot.configuration.processor;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.yilin.boot.configuration.processor.utils.Constants;
import com.yilin.boot.configuration.processor.utils.Elements;

/**
 * Copyright: Copyright (c) 2022
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description: 主要用来处理 Spring Boot 的 @AutoConfiguration 注解.
 *
 * @author jiac
 * @version 2023.0.1 2023/7/3:23:20
 * @since 2023.0.1
 */
@SupportedAnnotationTypes({ "org.springframework.boot.autoconfigure.AutoConfiguration" })
public class SpringAutoConfigurationProcessor extends AbstractConfigureAnnotationProcessor {

	/**
	 * 待处理的注解.
	 */
	private final String annotationName = "org.springframework.boot.autoconfigure.AutoConfiguration";

	/**
	 * 使用此注解的组件集合.
	 */
	private final Set<String> autoComponents = new HashSet<>();

	@Override
	protected boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		TypeElement annotationType = this.processingEnv.getElementUtils().getTypeElement(annotationName);
		if (annotationType != null) {
			for (Element element : roundEnv.getElementsAnnotatedWith(annotationType)) {
				processElement(element);
			}
		}

		if (roundEnv.processingOver()) {
			try {
				writeResourcesFile(Constants.SPRING_AUTO_CONFIGURATION_RESOURCE_LOCATION, this.autoComponents);
			}
			catch (Exception ex) {
				throw new IllegalStateException("Failed to write metadata", ex);
			}
		}
		return false;
	}

	private void processElement(Element element) {
		try {
			String qualifiedName = Elements.getQualifiedName(element);
			AnnotationMirror annotation = getAnnotation(element);
			if (qualifiedName != null && annotation != null) {
				log("auto configuration component: " + qualifiedName);
				this.autoComponents.add(qualifiedName);
			}
		}
		catch (Exception ex) {
			throw new IllegalStateException("Error processing configuration meta-data on " + element, ex);
		}
	}

	private AnnotationMirror getAnnotation(Element element) {
		if (element != null) {
			for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
				if (annotationName.equals(annotation.getAnnotationType().toString())) {
					return annotation;
				}
			}
		}
		return null;
	}

}
