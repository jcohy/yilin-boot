package com.yilin.reactive.build;

import java.util.HashMap;
import java.util.Map;

import io.github.jcohy.gradle.asciidoctor.AsciidoctorConventionsPlugin;
import io.github.jcohy.gradle.conventions.ConventionsPlugin;
import io.github.jcohy.gradle.deployed.DeployedPlugin;
import org.asciidoctor.gradle.jvm.AbstractAsciidoctorTask;
import org.asciidoctor.gradle.jvm.AsciidoctorJPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: 文档构建
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/3 22:35
 * @since 2024.0.1
 */
public class AsciidoctorDocsPlugins implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		PluginContainer plugins = project.getPlugins();
		plugins.apply(AsciidoctorJPlugin.class);
		plugins.apply(AsciidoctorConventionsPlugin.class);
		plugins.apply(ConventionsPlugin.class);
		plugins.apply(DeployedPlugin.class);
		plugins.withType(AsciidoctorJPlugin.class, (asciidoctorPlugin) -> {
			project.getTasks().withType(AbstractAsciidoctorTask.class, (asciidoctorTask) -> {
				configureAsciidoctorTask(project, asciidoctorTask);
			});
		});
	}

	private void configureAsciidoctorTask(Project project, AbstractAsciidoctorTask asciidoctorTask) {
		configureCommonAttributes(project, asciidoctorTask);
	}

	private void configureCommonAttributes(Project project, AbstractAsciidoctorTask asciidoctorTask) {
		Map<String, Object> attributes = new HashMap<>();
		addAsciidoctorTaskAttributes(project, attributes);
		asciidoctorTask.attributes(attributes);
	}

	private void addAsciidoctorTaskAttributes(Project project, Map<String, Object> attributes) {
		attributes.put("rootProject", project.getRootProject().getProjectDir());
	}
}
