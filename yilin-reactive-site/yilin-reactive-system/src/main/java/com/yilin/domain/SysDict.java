package com.yilin.domain;

import java.sql.Timestamp;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2024.0.1 2024/1/9 9:07
 * @since 2024.0.1
 */
public class SysDict {
	private Long id;

	private Long parentId;

	private String code;

	private String name;

	private String paramName;

	private String paramValue;

	private String valueType;

	private String paramValueSub;

	private Byte type;

	private Long orders;

	private String remark;

	private Integer sealed;

	private Long createBy;

	private Timestamp createTime;

	private Long updateBy;

	private Timestamp updateTime;

	private Byte deleted;

	private Timestamp deleteTime;

	public Long getId() {
		return id;
	}


}
