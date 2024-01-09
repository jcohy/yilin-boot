package com.yilin.reactive.utils.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: 同步读取.
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/21 10:36
 * @since 2024.0.1
 */
public class SynchronousFilesReader implements FilesReader {

	@Override
	public void start(File source, Consumer<byte[]> handler) throws IOException {
		try (
				var in = new BufferedInputStream(new FileInputStream(source));
				var out = new ByteArrayOutputStream()
		) {
			var read = -1;
			var bytes = new byte[1024];
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			handler.accept(out.toByteArray());
		}
	}
}
