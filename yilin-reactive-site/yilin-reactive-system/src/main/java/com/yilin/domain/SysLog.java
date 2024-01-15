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

public class SysLog {
	private Long id;

	
	private String tenantId;

	private String serviceId;

	private String serverHost;

	private String serverIp;

	private String env;

	private String type;

	private String title;

	private String method;

	private String requestUri;

	private String userAgent;

	private String remoteIp;

	private String methodClass;

	private String methodName;

	private String params;

	private String time;

	private String createBy;

	private Timestamp createTime;
	
}
