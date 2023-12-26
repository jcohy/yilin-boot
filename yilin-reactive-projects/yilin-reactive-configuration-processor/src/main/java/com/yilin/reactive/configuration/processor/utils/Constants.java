package com.yilin.reactive.configuration.processor.utils;

/**
 * Copyright: Copyright (c) 2023
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/4 17:41
 * @since 2023.0.1
 */
public class Constants {

    /**
     * Java SPI 加载路径. META-INF/services/.
     */
    public static final String SERVICE_RESOURCE_LOCATION = "META-INF/services/";

    /**
     * Spring 自动配置资源路径.
     * META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports.
     */
    public static final String SPRING_AUTO_CONFIGURATION_RESOURCE_LOCATION = "META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";

    /**
     * Spring 热部署资源路径. META-INF/spring-devtools.properties.
     */
    public static final String DEVTOOLS_RESOURCE_LOCATION = "META-INF/spring-devtools.properties";

}
