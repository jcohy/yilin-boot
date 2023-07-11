package com.yilin.boot.configuration.processor;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.springframework.stereotype.Service;

import com.yilin.boot.configuration.processor.autoconfiguration.SpringAutoConfigurationOne;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringAutoConfigurationTwo;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringComponentOne;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringComponentTwo;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringConfiguration;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringControllerOne;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringControllerTwo;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringRepositoryOne;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringRepositoryTwo;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringServiceOne;
import com.yilin.boot.configuration.processor.autoconfiguration.SpringServiceTwo;
import com.yilin.boot.configuration.processor.autoconfiguration.TestSpringComponentProcessor;

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
@Service
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
				.ofNullable(compile(SpringAutoConfigurationOne.class,
						SpringAutoConfigurationTwo.class,
						SpringComponentOne.class,
						SpringComponentTwo.class,
						SpringConfiguration.class,
						SpringControllerOne.class,
						SpringControllerTwo.class,
						SpringRepositoryOne.class,
						SpringRepositoryTwo.class,
						SpringServiceOne.class,
						SpringServiceTwo.class));
		Assertions.assertTrue(file.isPresent());
		Properties properties = TestSpringComponentProcessor.getWrittenProperties(file.get());
		Assertions.assertEquals(properties.size(), 10);
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringAutoConfigurationOne"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringAutoConfigurationTwo"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringComponentOne"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringComponentTwo"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringControllerOne"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringControllerTwo"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringRepositoryOne"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringRepositoryTwo"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringServiceOne"));
		Assertions.assertTrue(
				properties.containsKey("com.yilin.boot.configuration.processor.autoconfiguration.SpringServiceTwo"));
	}

	private File compile(Class<?>... types) throws IOException {
		return process(types).getWrittenFile();
	}

	private TestSpringComponentProcessor process(Class<?>... types) {
		TestSpringComponentProcessor processor = new TestSpringComponentProcessor(
				this.compiler.getOutputLocation());
		this.compiler.getTask(types).call(processor);
		return processor;
	}

}
