package com.yilin.reactive.r2dbc.domain;

import java.util.Objects;

import com.yilin.reactive.r2dbc.annotations.LogicDelete;
import com.yilin.reactive.r2dbc.annotations.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

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
public class Person {

	@Id
	Long id;

	String name;

	Integer age;

	Long version;

	@LogicDelete
	Integer deleted;

	Integer status;

	@TenantId
	String tenantId;
	public Person() {
	}

	public Person(Long id, String name, Integer age, Long version, Integer deleted, Integer status,String tenantId) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.version = version;
		this.deleted = deleted;
		this.status = status;
	}

	public Person(Long id, String name, Integer age, Long version, Integer deleted, Integer status) {
		this(id, name, age, version, deleted, status,"000000");
	}

	public Person(Long id, String name, Integer age, Long version) {
		this(id, name, age, version, DeleteStatus.NORMAL.getStatus(), ServiceStatus.ENABLE.getStatus(),"000000");
	}

	public Long getId() {
		return id;
	}

	public Person setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Person setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public Person setAge(Integer age) {
		this.age = age;
		return this;
	}

	public Long getVersion() {
		return version;
	}

	public Person setVersion(Long version) {
		this.version = version;
		return this;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public Person setDeleted(Integer deleted) {
		this.deleted = deleted;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public Person setStatus(Integer status) {
		this.status = status;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return Objects.equals(id, person.id) && Objects.equals(name, person.name) && Objects.equals(age, person.age) && Objects.equals(version, person.version) && Objects.equals(deleted, person.deleted) && Objects.equals(status, person.status);
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, age, version, deleted, status,tenantId);
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", version=" + version +
				", deleted=" + deleted +
				", status=" + status +
				", tenantId=" + tenantId +
				'}';
	}
}
