package com.yilin.reactive.r2dbc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.yilin.reactive.persistent.enums.DeleteStatus;
import com.yilin.reactive.persistent.enums.ServiceStatus;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/31:10:31
 * @since 2023.0.1
 */
@Table("person")
public class Person extends BaseDomain {

	@Id
	Long id;

	String name;

	Integer age;

	Long version;

	Integer deleted;

	Integer status;

	public Person(Long id, String name, Integer age, Long version, Integer deleted, Integer status) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.version = version;
		this.deleted = deleted;
		this.status = status;
	}

	public Person(Long id, String name, Integer age, Long version) {
		this(id, name, age, version, DeleteStatus.NORMAL.getStatus(), ServiceStatus.ENABLE.getStatus());
	}
}
