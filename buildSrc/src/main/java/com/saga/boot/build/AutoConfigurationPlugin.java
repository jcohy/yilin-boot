package com.saga.boot.build;

import java.util.Collections;

import io.github.jcohy.gradle.JcohyVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;

/**
 * 描述: .
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2023/4/25 22:37
 * @since 1.0.0
 */
public class AutoConfigurationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            project.getDependencies().add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, "org.springframework.boot:spring-boot-autoconfigure-processor:" + JcohyVersion.getSpringBootVersion());
            project.getDependencies().add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, "org.springframework.boot:spring-boot-configuration-processor:" + JcohyVersion.getSpringBootVersion());
            project.getDependencies().add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, project.getDependencies().project(Collections.singletonMap("path",
                    ":saga-boot-projects:saga-boot-configuration-processor")));
        });
    }
}
