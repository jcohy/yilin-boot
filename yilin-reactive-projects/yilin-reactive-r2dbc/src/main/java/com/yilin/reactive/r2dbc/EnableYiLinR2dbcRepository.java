package com.yilin.reactive.r2dbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactoryBean;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

import com.yilin.reactive.r2dbc.config.ReactiveR2dbcConfiguration;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2024.0.1 2024/1/15 14:17
 * @since 2024.0.1
 */
@Import(ReactiveR2dbcConfiguration.class)
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableYiLinR2dbcRepository {

	/**
	 * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
	 * {@code @EnableR2dbcRepositories("org.my.pkg")} instead of
	 * {@code @EnableR2dbcRepositories(basePackages="org.my.pkg")}.
	 */
	@AliasFor(annotation = EnableR2dbcRepositories.class,value = "value")
	String[] value() default { "com.yilin" };

	/**
	 * Base packages to scan for annotated components. {@link #value()} is an alias for (and mutually exclusive with) this
	 * attribute. Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
	 */
	@AliasFor(annotation = EnableR2dbcRepositories.class,value = "basePackages")
	String[] basePackages() default {};

	/**
	 * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components. The
	 * package of each class specified will be scanned. Consider creating a special no-op marker class or interface in
	 * each package that serves no purpose other than being referenced by this attribute.
	 */
	@AliasFor(annotation = EnableR2dbcRepositories.class,value = "basePackages")
	Class<?>[] basePackageClasses() default {};


	/**
	 * Configures whether nested repository-interfaces (e.g. defined as inner classes) should be discovered by the
	 * repositories infrastructure.
	 */
	@AliasFor(annotation = EnableR2dbcRepositories.class,value = "considerNestedRepositories")
	boolean considerNestedRepositories() default false;

}
