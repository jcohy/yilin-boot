package com.yilin.reactive.starter.redis;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/8/18 16:42
 * @since 2024.0.1
 */
public class RedisContainer extends GenericContainer<RedisContainer> {

	public RedisContainer() {
		super(DockerImageName.parse("redis").withTag("7.0.0"));
		addExposedPorts(6379);
	}

}
