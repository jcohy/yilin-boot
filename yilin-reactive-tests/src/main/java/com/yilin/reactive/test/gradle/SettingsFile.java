package com.yilin.reactive.test.gradle;

import java.io.File;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/20 15:57
 * @since 2023.0.1
 */
public class SettingsFile {

	private File settingsFile;

	public SettingsFile(File projectDir) {
		this.settingsFile = new File(projectDir, "settings.gradle");
	}
}
