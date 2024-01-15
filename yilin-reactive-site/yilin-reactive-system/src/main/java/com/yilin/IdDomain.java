package com.yilin;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import com.yilin.reactive.commons.YiLinReactiveVersion;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2024.0.1 2024/1/8 17:21
 * @since 2024.0.1
 */
public class IdDomain implements Serializable, Persistable<Long> {

	@Serial
	private static final long serialVersionUID = YiLinReactiveVersion.SERIAL_VERSION_UID;

	@Id
	private Long id;

	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}

	public IdDomain setId(Long id) {
		this.id = id;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IdDomain idDomain)) return false;

		return getId() != null ? getId().equals(idDomain.getId()) : idDomain.getId() == null;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}

	@Override
	public String toString() {
		return "IdDomain{" +
				"id=" + id +
				'}';
	}


}
