package com.yilin.reactive.utils.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/21:11:45
 * @since 2023.0.1
 */
public class NetworkFileSyncTest {

	@Test
	void ioNetReaderTest() {
		var nfs = new IoNetworkFileSync();
		nfs.start(8888, new FileSystemPersistingByteConsumer("io"));
	}

	@Test
	void nioNetReaderTest() throws Exception {
		var nfs = new NioNetworkFileSync();
		nfs.start(8888, new FileSystemPersistingByteConsumer("nio"));
	}

	@Test
	void nettyNetReaderTest() throws IOException, InterruptedException {
		var nfs = new NettyNetworkFileSync();
		nfs.start(8888, new FileSystemPersistingByteConsumer("netty"));
	}

	record FileSystemPersistingByteConsumer(String prefix) implements Consumer<byte[]> {

		private static final Logger log = LoggerFactory.getLogger(FileSystemPersistingByteConsumer.class);

		public void accept(byte[] bytes) {
			log.info("the bytes length is " + bytes.length);
			var outputDirectory = new File(new File(System.getenv("HOME"), "Desktop"), "output");
			Assert.isTrue(outputDirectory.mkdirs() || outputDirectory.exists(),
					() -> "the folder " + outputDirectory.getAbsolutePath() + " does not exist");
			var file = new File(outputDirectory, prefix + ".download");
			try {
				FileCopyUtils.copy(bytes, new FileOutputStream(file));
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
