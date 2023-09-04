package com.yilin.reactive.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.FileCopyUtils;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/21:10:46
 * @since 2023.0.1
 */
@Disabled
class FileReaderTest {


	private static final Logger log = LoggerFactory.getLogger(FileReaderTest.class);

	File file;

	@BeforeEach
	void setUp() throws IOException {
		file = Files.createTempFile("io-content-data", ".txt").toFile();
		file.deleteOnExit();
		try (var in = FileReaderTest.class.getResourceAsStream("/content");
			 var out = new FileOutputStream(file)) {
			if (in != null) {
				FileCopyUtils.copy(in, out);
			}
		}
	}


	@Test
	void syncFileReader() throws IOException {
		long start = System.currentTimeMillis();
		var fss = new SynchronousFilesReader();
		fss.start(file, new BytesConsumer(file, "SynchronousFilesReader"));
		long end = System.currentTimeMillis();
		log.info("SynchronousFilesReader cost {}", (end - start));
	}

	@Test
	void aSyncFileReader() throws IOException {
		long start = System.currentTimeMillis();
		var fss = new AsynchronousFilesReader();
		fss.start(file, new BytesConsumer(file, "AsynchronousFileReader"));
		long end = System.currentTimeMillis();
		log.info("AsynchronousFileReader cost {}", (end - start));
	}

	static class BytesConsumer implements Consumer<byte[]> {

		private static final Logger log = LoggerFactory.getLogger(BytesConsumer.class);

		private final File source;

		private final String prefix;

		public BytesConsumer(File source, String prefix) {
			this.source = source;
			this.prefix = prefix;
		}

		@Override
		public void accept(byte[] bytes) {
			log.info(prefix + ':' + bytes.length + ':' + source.getAbsolutePath());
		}
	}
}