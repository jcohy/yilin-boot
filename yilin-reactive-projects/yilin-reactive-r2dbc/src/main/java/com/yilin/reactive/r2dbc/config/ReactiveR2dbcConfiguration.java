package com.yilin.reactive.r2dbc.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import com.yilin.reactive.r2dbc.repository.support.YiLinR2dbcRepositoryFactoryBean;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/31 10:10
 * @since 2024.0.1
 */
@AutoConfiguration
@EnableR2dbcRepositories(considerNestedRepositories = true, basePackages = { "com.yilin" },
		repositoryFactoryBeanClass = YiLinR2dbcRepositoryFactoryBean.class)
public class ReactiveR2dbcConfiguration {


}
