package com.yilin.boot.configuration.processor.autoconfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.annotation.processing.SupportedAnnotationTypes;

import com.yilin.boot.configuration.processor.SpringAutoConfigurationProcessor;
import com.yilin.boot.configuration.processor.utils.Constants;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/7:10:11
 * @since 2023.0.1
 */
@SupportedAnnotationTypes({ "org.springframework.boot.autoconfigure.AutoConfiguration" })
public class TestSpringAutoConfigurationProcessor extends SpringAutoConfigurationProcessor {

	private final File outputLocation;

	public TestSpringAutoConfigurationProcessor(File outputLocation) {
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
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public File getWrittenFile() {
		return new File(this.outputLocation, Constants.SPRING_AUTO_CONFIGURATION_RESOURCE_LOCATION);
	}

}
