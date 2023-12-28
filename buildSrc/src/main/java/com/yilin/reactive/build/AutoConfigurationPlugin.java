package com.yilin.reactive.build;

import java.util.Collections;

import io.github.jcohy.gradle.JcohyVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: 引入自动配置处理器
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/3 22:36
 * @since 2023.0.1
 */
public class AutoConfigurationPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
			project.getDependencies().add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, "org.springframework.boot:spring-boot-autoconfigure-processor:" + JcohyVersion.getSpringBootVersion());
			project.getDependencies().add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, "org.springframework.boot:spring-boot-configuration-processor:" + JcohyVersion.getSpringBootVersion());
			project.getDependencies().add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, project.getDependencies().project(Collections.singletonMap("path",
					":yilin-reactive-projects:yilin-reactive-configuration-processor")));
		});
	}
}
