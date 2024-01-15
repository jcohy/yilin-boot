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

public class SysNotice {
	private Long id;

	
	private String tenantId;

	
	private String title;

	
	private Integer category;

	
	private Timestamp releaseTime;

	
	private String content;

	
	private Long createUser;

	
	private Long createDept;

	
	private Timestamp createTime;

	
	private Long updateUser;

	
	private Timestamp updateTime;

	
	private Integer status;

	
	private Integer isDeleted;
	
}
