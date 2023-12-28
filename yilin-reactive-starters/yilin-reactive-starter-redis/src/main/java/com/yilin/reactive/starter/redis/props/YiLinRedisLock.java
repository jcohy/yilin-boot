package com.yilin.reactive.starter.redis.props;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/8/11 11:32
 * @since 2023.0.1
 */
public class YiLinRedisLock {

	/**
	 * 是否开启：默认为：false，便于生成配置提示.
	 */
	private Boolean enabled = Boolean.FALSE;

	/**
	 * 密码配置.
	 */
	private String password;

	/**
	 * db.
	 */
	private Integer database = 8;

	/**
	 * 命令等待超时，单位：毫秒.
	 */
	private Integer timeout = 10000;

	/**
	 * 单机版或分布式版本.
	 */
	private LockModel mode = LockModel.SINGLE;

	/**
	 * 当 model 设置为 SINGLE 时需要设置此配置
	 */
	private String address = "redis://127.0.0.1:6379";

	/**
	 * 当 model 设置为 MULTI 时需要设置此配置.
	 */
	private String[] masterNodes;

	/**
	 * 连接池配置
	 */
	private Pool pool;

	public Boolean getEnabled() {
		return enabled;
	}

	public YiLinRedisLock setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public YiLinRedisLock setPassword(String password) {
		this.password = password;
		return this;
	}

	public Integer getDatabase() {
		return database;
	}

	public YiLinRedisLock setDatabase(Integer database) {
		this.database = database;
		return this;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public YiLinRedisLock setTimeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}


	public Pool getPool() {
		return this.pool;
	}

	public YiLinRedisLock setPool(Pool pool) {
		this.pool = pool;
		return this;
	}

	public LockModel getMode() {
		return mode;
	}

	public YiLinRedisLock setMode(LockModel mode) {
		this.mode = mode;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public YiLinRedisLock setAddress(String address) {
		this.address = address;
		return this;
	}

	public String[] getMasterNodes() {
		return masterNodes;
	}

	public YiLinRedisLock setMasterNodes(String[] masterNodes) {
		this.masterNodes = masterNodes;
		return this;
	}

	public enum LockModel {
		SINGLE,MULTI
	}

	/**
	 * 连接池配置.
	 */
	public static class Pool {

		/**
		 * 连接池大小.
		 */
		private Integer size = 20;

		/**
		 * 最小空闲连接数.
		 */
		private Integer minIdle = 5;

		/**
		 * 连接超时，单位：毫秒.
		 */
		private Integer connectionTimeout = 3000;

		public Integer getSize() {
			return size;
		}

		public Pool setSize(Integer size) {
			this.size = size;
			return this;
		}

		public Integer getMinIdle() {
			return minIdle;
		}

		public Pool setMinIdle(Integer minIdle) {
			this.minIdle = minIdle;
			return this;
		}

		public Integer getConnectionTimeout() {
			return connectionTimeout;
		}

		public Pool setConnectionTimeout(Integer connectionTimeout) {
			this.connectionTimeout = connectionTimeout;
			return this;
		}
	}
}
