package com.yilin.boot.build;

import java.util.Collections;

import io.github.jcohy.gradle.conventions.ConventionsPlugin;
import io.github.jcohy.gradle.deployed.DeployedPlugin;
import io.github.jcohy.gradle.optional.OptionalDependenciesPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.PluginContainer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: 为所有的项目模块添加约定
 *
 * @author jiac
 * @version 2023.0.1 2023/7/3:22:36
 * @since 2023.0.1
 */
public class YiLinModulePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		PluginContainer plugins = project.getPlugins();
		plugins.apply(JavaLibraryPlugin.class);
		plugins.apply(ConventionsPlugin.class);
		plugins.apply(DeployedPlugin.class);
		plugins.apply(OptionalDependenciesPlugin.class);
		configureDependencyManagement(project);
	}

	private void configureDependencyManagement(Project project) {
		Dependency parent = project.getDependencies().enforcedPlatform(project.getDependencies()
				.project(Collections.singletonMap("path", ":yilin-boot-projects:yilin-boot-dependencies")));
		project.getConfigurations().getByName("dependencyManagement", (dependencyManagement) -> {
			dependencyManagement.getDependencies().add(parent);
		});
	}
}
