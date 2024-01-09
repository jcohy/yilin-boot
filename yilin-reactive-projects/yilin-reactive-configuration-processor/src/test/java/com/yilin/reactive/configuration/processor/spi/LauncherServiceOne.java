package com.yilin.reactive.configuration.processor.spi;

import com.yilin.reactive.configuration.processor.annotations.YiLinAutoService;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/4 17:48
 * @since 2024.0.1
 */
@YiLinAutoService(value = LauncherService.class, name = "LauncherServiceOne")
public class LauncherServiceOne implements LauncherService {

	@Override
	public void launcher() {

	}

}
