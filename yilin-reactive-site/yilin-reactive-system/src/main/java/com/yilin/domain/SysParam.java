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

public class SysParam {
	private Long id;

	
	private String paramName;

	
	private String paramKey;

	
	private String paramValue;

	private String remark;

	
	private Long createUser;

	private Long createDept;

	
	private Timestamp createTime;

	
	private Long updateUser;

	
	private Timestamp updateTime;

	
	private Integer status;

	
	private Integer isDeleted;

}
