package com.yilin.reactive.configuration.processor.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Copyright: Copyright (c) 2022
 * <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p>
 * Description: {@link java.util.ServiceLoader} 中描述的服务提供商的提供的注解一样，此注解处理器可以自动生成
 * 被注解的类的配置文件，然后被 {@link java.util.ServiceLoader#load(Class)} 加载..
 *
 * <p>
 * 被注解的类必须符合服务提供商规范
 * <ul>
 * <li>不能是内部类和匿名类，必须要有确定的名称
 * <li>必须要有公共的，可调用的无参构造函数
 * <li>使用这个注解的类必须要实现 value 参数定义的接口 { @code value()}
 * </ul>
 *
 * @author jiac
 * @version 2023.0.1 2023/7/3:23:18
 * @since 2023.0.1
 */
@Documented
@Retention(java.lang.annotation.RetentionPolicy.SOURCE)
@Target(java.lang.annotation.ElementType.TYPE)
public @interface YiLinAutoService {

	/**
	 * 接口名称.
	 *
	 * @return /
	 */
	Class<?>[] value();

	/**
	 * 接口实现类名称.
	 *
	 * @return /
	 */
	String name() default "";

}
