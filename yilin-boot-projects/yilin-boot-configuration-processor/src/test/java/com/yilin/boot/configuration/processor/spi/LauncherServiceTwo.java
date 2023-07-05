package com.yilin.boot.configuration.processor.spi;

import com.yilin.boot.configuration.processor.annotations.YiLinAutoService;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/4:17:52
 * @since 2023.0.1
 */
@YiLinAutoService(value = LauncherService.class, name = "LauncherServiceTwo")
public class LauncherServiceTwo implements LauncherService {

	@Override
	public void launcher() {

	}

}
