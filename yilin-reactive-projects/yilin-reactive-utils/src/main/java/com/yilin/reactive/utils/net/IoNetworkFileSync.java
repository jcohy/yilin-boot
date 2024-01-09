package com.yilin.reactive.utils.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/21 11:32
 * @since 2024.0.1
 */
public class IoNetworkFileSync implements NetworkFileSync {

	@Override
	public void start(int port, Consumer<byte[]> consumer) {
		try (var ss = new ServerSocket(port)) {
			while (true) {
				try (var socket = ss.accept();
					 var in = socket.getInputStream();
					 var out = new ByteArrayOutputStream()) {
					var bytes = new byte[1024];
					var read = -1;
					while ((read = in.read(bytes)) != -1)
						out.write(bytes, 0, read);
					consumer.accept(out.toByteArray());
				}

			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
