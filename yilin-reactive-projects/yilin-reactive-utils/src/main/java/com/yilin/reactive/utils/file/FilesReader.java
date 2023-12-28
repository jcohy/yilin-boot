package com.yilin.reactive.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: 文件读取接口.
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/21 10:35
 * @since 2023.0.1
 */
public interface FilesReader {
	void start(File source, Consumer<byte[]> handler) throws IOException;
}
