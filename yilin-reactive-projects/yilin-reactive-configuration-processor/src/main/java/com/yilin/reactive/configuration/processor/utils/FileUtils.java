package com.yilin.reactive.configuration.processor.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/7 10:00
 * @since 2023.0.1
 */
public class FileUtils {

	public static void withPrintWriter(File file, Consumer<PrintWriter> consumer) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(file, true), true)) {
			consumer.accept(writer);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
