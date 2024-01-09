package com.yilin.reactive.utils.net;

import java.util.function.Consumer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: 网络读取.
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/21 11:30
 * @since 2024.0.1
 */
public interface NetworkFileSync {

	void start(int port, Consumer<byte[]> byteHandler) throws Exception;
}
