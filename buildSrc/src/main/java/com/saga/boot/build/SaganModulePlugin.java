package com.saga.boot.build;

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
 * 描述: .
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2023/4/25 22:35
 * @since 1.0.0
 */
public class SaganModulePlugin implements Plugin<Project> {

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
        Dependency sagaParent = project.getDependencies().enforcedPlatform(project.getDependencies()
                .project(Collections.singletonMap("path", ":saga-boot-projects:saga-boot-dependencies")));
        project.getConfigurations().getByName("dependencyManagement", (dependencyManagement) -> {
            dependencyManagement.getDependencies().add(sagaParent);
        });
    }
}
