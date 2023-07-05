package com.yilin.boot.configuration.processor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


import com.yilin.boot.configuration.processor.spi.LauncherServiceOne;
import com.yilin.boot.configuration.processor.spi.LauncherServiceTwo;
import com.yilin.boot.configuration.processor.spi.TestYiLinAutoServiceProcessor;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/4:17:46
 * @since 2023.0.1
 */
class YiLinAutoServiceProcessorTest {

	@TempDir
	File tempDir;

	private TestCompiler compiler;

	@BeforeEach
	void createCompiler() throws IOException {
		this.compiler = new TestCompiler(tempDir);
	}

	@Test
	void annotatedClass() throws IOException {
		File file = compile(LauncherServiceOne.class, LauncherServiceTwo.class);
		File[] files = Optional.ofNullable(file.listFiles())
				.orElseThrow();

		Assertions.assertTrue(file.exists());
		Assertions.assertEquals(files.length,1);
		Arrays.stream(files)
				.findFirst()
				.map(TestYiLinAutoServiceProcessor::getWrittenProperties)
				.ifPresent((properties) -> {
					Assertions.assertEquals(properties.size(),2);
					Assertions.assertTrue(properties.containsKey("com.yilin.boot.configuration.processor.spi.LauncherServiceOne"));
					Assertions.assertTrue(properties.containsKey("com.yilin.boot.configuration.processor.spi.LauncherServiceTwo"));
				});
	}

	private File compile(Class<?>... types) throws IOException {
		return process(types).getWrittenFile();
	}

	private TestYiLinAutoServiceProcessor process(Class<?>... types) {
		TestYiLinAutoServiceProcessor processor = new TestYiLinAutoServiceProcessor(this.compiler.getOutputLocation());
		this.compiler.getTask(types).call(processor);
		return processor;
	}

}