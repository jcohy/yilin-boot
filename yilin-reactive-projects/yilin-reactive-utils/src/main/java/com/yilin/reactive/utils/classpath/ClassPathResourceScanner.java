package com.yilin.reactive.utils.classpath;

import java.net.URL;
import java.util.Set;

/**
 * 描述: 查找路径下的资源.
 * <p>
 * Copyright © 2023
 * <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jcohy
 * @version 2023.0.1 2023/3/7 10:21
 * @since 2023.0.1
 */
public interface ClassPathResourceScanner {

	/**
	 * 查找 locationUrl 类路径下的资源名称
	 * @param location 类路径上与系统无关的位置
	 * @param locationUrl 特定于系统的物理位置 URL.
	 * @return 类路径上资源的名称
	 */
	Set<String> findResourceNames(String location, URL locationUrl);

}
