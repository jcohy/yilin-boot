package com.yilin.reactive.test.gradle;

import java.io.File;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/20:15:56
 * @since 2023.0.1
 */
public class BuildFile {

	File buildFile;

	public BuildFile(File projectDir) {
		this.buildFile = new File(projectDir, "build.gradle");
	}
}
