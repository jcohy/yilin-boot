package com.yilin.reactive.persistent.enums;

/**
 * 描述: 删除状态.
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/13 14:46
 * @since 2023.0.1
 */
public enum DeleteStatus {

	/**
	 * 正常状态.
	 */
	NORMAL(1),

	/**
	 * 删除状态.
	 */
	DELETED(0);

	private final int status;

	DeleteStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return this.status;
	}
}
