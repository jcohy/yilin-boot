package com.yilin.boot.configuration.processor.spi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.processing.SupportedAnnotationTypes;

import com.yilin.boot.configuration.processor.YiLinAutoServiceProcessor;
import com.yilin.boot.configuration.processor.utils.Constants;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/4:17:49
 * @since 2023.0.1
 */
@SupportedAnnotationTypes({ "com.yilin.boot.configuration.processor.annotations.YiLinAutoService" })
public class TestYiLinAutoServiceProcessor extends YiLinAutoServiceProcessor {

	private final File outputLocation;

	public TestYiLinAutoServiceProcessor(File outputLocation) {
		this.outputLocation = outputLocation;
	}

	public static Properties getWrittenProperties(File file) {
		if (!file.exists()) {
			return null;
		}
		try (FileInputStream inputStream = new FileInputStream(file)) {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public File getWrittenFile() {
		return new File(this.outputLocation, Constants.SERVICE_RESOURCE_LOCATION);
	}
}
