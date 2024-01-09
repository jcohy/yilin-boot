package com.yilin.reactive.configuration.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.yilin.reactive.configuration.processor.utils.Constants;
import com.yilin.reactive.configuration.processor.utils.Elements;

/**
 * Copyright: Copyright (c) 2022
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description: 主要用来加载 Spring 的组件，将指定注解的组件写入 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 文件中，也就是自动装配.
 *
 * 主要有对包含有以下注解的类进行自动装配：
 *
 * <ul>
 *     <li>@AutoConfiguration: 在 Spring Boot 3.0.0 版本中，不建议使用 @Configuration </li>
 *     <li>@Component </li>
 *     <li>@Service </li>
 *     <li>@Controller </li>
 *     <li>@Repository </li>
 * </ul>
 * @author jcohy
 * @version 2024.0.1 2023/7/3 23:20
 * @since 2024.0.1
 */
@SupportedAnnotationTypes({
		"org.springframework.boot.autoconfigure.AutoConfiguration",
		"org.springframework.stereotype.Service",
		"org.springframework.stereotype.Component",
		"org.springframework.stereotype.Controller",
		"org.springframework.stereotype.Repository"})
public class SpringComponentProcessor extends AbstractConfigureAnnotationProcessor {

	/**
	 * 待处理的注解.
	 */
	private final List<String> annotations = List.of("org.springframework.boot.autoconfigure.AutoConfiguration",
			"org.springframework.stereotype.Service",
			"org.springframework.stereotype.Component",
			"org.springframework.stereotype.Controller",
			"org.springframework.stereotype.Repository");

	/**
	 * 使用此注解的组件集合.
	 */
	private final Set<String> autoComponents = new HashSet<>();



	@Override
	protected boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		this.annotations.forEach(annotation -> {
			TypeElement annotationType = this.processingEnv.getElementUtils().getTypeElement(annotation);
			if (annotationType != null) {
				for (Element element : roundEnv.getElementsAnnotatedWith(annotationType)) {
					processElement(annotation,element);
				}
			}
		});
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

	private void processElement(String annotationName,Element element) {
		try {
			String qualifiedName = Elements.getQualifiedName(element);
			AnnotationMirror annotation = getAnnotation(annotationName,element);
			if (qualifiedName != null && annotation != null) {
				log("auto configuration component: " + qualifiedName);
				this.autoComponents.add(qualifiedName);
			}
		}
		catch (Exception ex) {
			throw new IllegalStateException("Error processing configuration meta-data on " + element, ex);
		}
	}

	private AnnotationMirror getAnnotation(String annotationName,Element element) {
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
