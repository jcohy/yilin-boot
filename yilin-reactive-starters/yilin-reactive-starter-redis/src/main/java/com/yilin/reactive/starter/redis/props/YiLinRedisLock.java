package com.yilin.reactive.starter.redis.props;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/11:11:32
 * @since 2023.0.1
 */
public class YiLinRedisLock {

	/**
	 * 是否开启：默认为：false，便于生成配置提示.
	 */
	private Boolean enabled = Boolean.FALSE;

	/**
	 * 单机配置：redis 服务地址.
	 */
	private String address = "redis://127.0.0.1:6379";

	/**
	 * 密码配置.
	 */
	private String password;

	/**
	 * db.
	 */
	private Integer database = 0;

	/**
	 * 连接池大小.
	 */
	private Integer poolSize = 20;

	/**
	 * 最小空闲连接数.
	 */
	private Integer idleSize = 5;

	/**
	 * 连接空闲超时，单位：毫秒.
	 */
	private Integer idleTimeout = 60000;

	/**
	 * 连接超时，单位：毫秒.
	 */
	private Integer connectionTimeout = 3000;

	/**
	 * 命令等待超时，单位：毫秒.
	 */
	private Integer timeout = 10000;

	/**
	 * 集群模式，单机：single，主从：master，哨兵模式：sentinel，集群模式：cluster.
	 */
	private Mode mode = Mode.SINGLE;

	/**
	 * 主从模式，主地址.
	 */
	private String masterAddress;

	/**
	 * 主从模式，从地址.
	 */
	private String[] slaveAddress;

	/**
	 * 哨兵模式：主名称.
	 */
	private String masterName;

	/**
	 * 哨兵模式地址.
	 */
	private String[] sentinelAddress;

	/**
	 * 集群模式节点地址.
	 */
	private String[] nodeAddress;

	public Boolean getEnabled() {
		return enabled;
	}

	public YiLinRedisLock setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public YiLinRedisLock setAddress(String address) {
		this.address = address;
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

	public Integer getPoolSize() {
		return poolSize;
	}

	public YiLinRedisLock setPoolSize(Integer poolSize) {
		this.poolSize = poolSize;
		return this;
	}

	public Integer getIdleSize() {
		return idleSize;
	}

	public YiLinRedisLock setIdleSize(Integer idleSize) {
		this.idleSize = idleSize;
		return this;
	}

	public Integer getIdleTimeout() {
		return idleTimeout;
	}

	public YiLinRedisLock setIdleTimeout(Integer idleTimeout) {
		this.idleTimeout = idleTimeout;
		return this;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public YiLinRedisLock setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public YiLinRedisLock setTimeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}

	public Mode getMode() {
		return mode;
	}

	public YiLinRedisLock setMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public String getMasterAddress() {
		return masterAddress;
	}

	public YiLinRedisLock setMasterAddress(String masterAddress) {
		this.masterAddress = masterAddress;
		return this;
	}

	public String[] getSlaveAddress() {
		return slaveAddress;
	}

	public YiLinRedisLock setSlaveAddress(String[] slaveAddress) {
		this.slaveAddress = slaveAddress;
		return this;
	}

	public String getMasterName() {
		return masterName;
	}

	public YiLinRedisLock setMasterName(String masterName) {
		this.masterName = masterName;
		return this;
	}

	public String[] getSentinelAddress() {
		return sentinelAddress;
	}

	public YiLinRedisLock setSentinelAddress(String[] sentinelAddress) {
		this.sentinelAddress = sentinelAddress;
		return this;
	}

	public String[] getNodeAddress() {
		return nodeAddress;
	}

	public YiLinRedisLock setNodeAddress(String[] nodeAddress) {
		this.nodeAddress = nodeAddress;
		return this;
	}

	public enum Mode {

		/**
		 * 集群模式，单机：single，主从：master，哨兵模式：sentinel，集群模式：cluster.
		 */
		SINGLE, MASTER, SENTINEL, CLUSTER

	}
}
