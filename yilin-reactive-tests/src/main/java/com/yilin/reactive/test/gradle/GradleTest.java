package com.yilin.reactive.test.gradle;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/20 15:29
 * @since 2023.0.1
 */
public class GradleTest {

	private File projectDir;

	private BuildFile buildFile;

	private SettingsFile settingsFile;

	private Map<String, String> environment;

	private List<String> args;

	public GradleTest projectDir(File projectDir) {
		this.projectDir = projectDir;
		return this;
	}

	public GradleTest buildFile(Consumer<BuildFile> buildFile) {
		this.buildFile = new BuildFile(this.projectDir);
		buildFile.accept(this.buildFile);
		return this;
	}

	public GradleTest settingsFile(Consumer<SettingsFile> buildFile) {
		this.settingsFile = new SettingsFile(this.projectDir);
		buildFile.accept(this.settingsFile);
		return this;
	}

	public GradleTest environment(Map<String, String> environment) {
		this.environment = environment;
		return this;
	}

	public GradleTest args(List<String> args) {
		this.args = args;
		return this;
	}

	public BuildResult runGradle() {
		return GradleRunner.create()
				.withGradleVersion("7.6")
				.withProjectDir(this.projectDir)
//				.withEnvironment(this.environment)
				.withArguments(this.args)
				.withPluginClasspath()
				.build();
	}
}
