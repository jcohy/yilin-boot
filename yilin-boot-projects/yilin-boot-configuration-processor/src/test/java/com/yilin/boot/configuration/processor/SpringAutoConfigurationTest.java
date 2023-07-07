package com.yilin.boot.configuration.processor;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.yilin.boot.configuration.processor.autoconfiguration.SpringAutoConfigurationOne;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringAutoConfigurationTwo;
import com.yilin.boot.configuration.processor.autoconfiguration.TestSpringAutoConfigurationProcessor;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/7:10:04
 * @since 2023.0.1
 */
public class SpringAutoConfigurationTest {

	@TempDir
	File tempDir;

	private TestCompiler compiler;

	@BeforeEach
	void createCompiler() throws IOException {
		this.compiler = new TestCompiler(tempDir);
	}

	@Test
	void annotatedClass() throws IOException {
		Optional<File> file = Optional
				.ofNullable(compile(SpringAutoConfigurationOne.class, SpringAutoConfigurationTwo.class));
		Assertions.assertTrue(file.isPresent());
		Properties properties = TestSpringAutoConfigurationProcessor.getWrittenProperties(file.get());
		Assertions.assertEquals(properties.size(), 2);
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringAutoConfigurationOne"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringAutoConfigurationTwo"));
	}

	private File compile(Class<?>... types) throws IOException {
		return process(types).getWrittenFile();
	}

	private TestSpringAutoConfigurationProcessor process(Class<?>... types) {
		TestSpringAutoConfigurationProcessor processor = new TestSpringAutoConfigurationProcessor(
				this.compiler.getOutputLocation());
		this.compiler.getTask(types).call(processor);
		return processor;
	}

}
