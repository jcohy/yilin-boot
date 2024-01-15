package com.yilin;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2024.0.1 2024/1/8 17:33
 * @since 2024.0.1
 */
public class BaseDomain extends IdDomain {

	@CreatedBy
	private Long createBy;

	@CreatedDate
	private Instant createTime;

	@LastModifiedBy
	private Long updateBy;

	@LastModifiedDate
	private Instant updateTime;


	private Instant deleted;

	@LastModifiedDate
	private Long deleteTime;

	public Long getCreateBy() {
		return createBy;
	}

	public BaseDomain setCreateBy(Long createBy) {
		this.createBy = createBy;
		return this;
	}

	public Instant getCreateTime() {
		return createTime;
	}

	public BaseDomain setCreateTime(Instant createTime) {
		this.createTime = createTime;
		return this;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public BaseDomain setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
		return this;
	}

	public Instant getUpdateTime() {
		return updateTime;
	}

	public BaseDomain setUpdateTime(Instant updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Instant getDeleted() {
		return deleted;
	}

	public BaseDomain setDeleted(Instant deleted) {
		this.deleted = deleted;
		return this;
	}

	public Long getDeleteTime() {
		return deleteTime;
	}

	public BaseDomain setDeleteTime(Long deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BaseDomain that)) return false;
		if (!super.equals(o)) return false;

		if (getCreateBy() != null ? !getCreateBy().equals(that.getCreateBy()) : that.getCreateBy() != null)
			return false;
		if (getCreateTime() != null ? !getCreateTime().equals(that.getCreateTime()) : that.getCreateTime() != null)
			return false;
		if (getUpdateBy() != null ? !getUpdateBy().equals(that.getUpdateBy()) : that.getUpdateBy() != null)
			return false;
		if (getUpdateTime() != null ? !getUpdateTime().equals(that.getUpdateTime()) : that.getUpdateTime() != null)
			return false;
		if (getDeleted() != null ? !getDeleted().equals(that.getDeleted()) : that.getDeleted() != null)
			return false;
		return getDeleteTime() != null ? getDeleteTime().equals(that.getDeleteTime()) : that.getDeleteTime() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getCreateBy() != null ? getCreateBy().hashCode() : 0);
		result = 31 * result + (getCreateTime() != null ? getCreateTime().hashCode() : 0);
		result = 31 * result + (getUpdateBy() != null ? getUpdateBy().hashCode() : 0);
		result = 31 * result + (getUpdateTime() != null ? getUpdateTime().hashCode() : 0);
		result = 31 * result + (getDeleted() != null ? getDeleted().hashCode() : 0);
		result = 31 * result + (getDeleteTime() != null ? getDeleteTime().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "BaseDomain{" +
				"createBy=" + createBy +
				", createTime=" + createTime +
				", updateBy=" + updateBy +
				", updateTime=" + updateTime +
				", deleted=" + deleted +
				", deleteTime=" + deleteTime +
				'}';
	}
}
