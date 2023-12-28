package com.yilin.reactive.utils.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: 文件异步读取.
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/21 10:37
 * @since 2023.0.1
 */
public class AsynchronousFilesReader implements FilesReader {

	@Override
	public void start(File source, Consumer<byte[]> handler) throws IOException {
		var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		var fileChannel = AsynchronousFileChannel.open(source.toPath(), Collections.singleton(StandardOpenOption.READ),
				executorService);

		var completionHandler = new AsynchronousFileCompletionHandler(executorService, handler, source, fileChannel);

		var attachment = new AsynchronousReadAttachment(source, ByteBuffer.allocate(1024), new ByteArrayOutputStream(),
				0);
		fileChannel.read(attachment.buffer(), attachment.position(), attachment, completionHandler);
	}
}

class AsynchronousFileCompletionHandler implements CompletionHandler<Integer, AsynchronousReadAttachment> {

	private static final Logger log = LoggerFactory.getLogger(AsynchronousFileCompletionHandler.class);

	private final ExecutorService executorService;

	private final Consumer<byte[]> handler;

	private final File source;

	private final AsynchronousFileChannel fileChannel;

	public AsynchronousFileCompletionHandler(ExecutorService executorService, Consumer<byte[]> handler,
			File source, AsynchronousFileChannel fileChannel) {
		this.executorService = executorService;
		this.handler = handler;
		this.source = source;
		this.fileChannel = fileChannel;
	}

	private static void error(Throwable throwable, AsynchronousReadAttachment attachment) {
		log.error("error reading file '" + attachment.source().getAbsolutePath() + "'!", throwable);
	}

	@Override
	public void completed(Integer result, AsynchronousReadAttachment attachment) {
		var byteArrayOutputStream = attachment.byteArrayOutputStream();
		if (!result.equals(-1)) {
			var buffer = attachment.buffer();
			buffer.flip();
			var storage = new byte[buffer.limit()];
			buffer.get(storage);
			try {
				byteArrayOutputStream.write(storage);
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
			attachment.buffer().clear();
			var ra = new AsynchronousReadAttachment(source, attachment.buffer(), //
					byteArrayOutputStream, //
					attachment.position() + attachment.buffer().limit() //
			);
			fileChannel.read(attachment.buffer(), ra.position(), ra, this);
		} //
		else { // it's -1
			var all = byteArrayOutputStream.toByteArray();
			try {
				byteArrayOutputStream.close();
				executorService.shutdown();
			} //
			catch (Exception e) {
				error(e, attachment);
			}
			handler.accept(all);

		}
	}

	@Override
	public void failed(Throwable throwable, AsynchronousReadAttachment attachment) {
		error(throwable, attachment);
	}

}

record AsynchronousReadAttachment(File source,
								  ByteBuffer buffer,
								  ByteArrayOutputStream byteArrayOutputStream,
								  long position) {
}
