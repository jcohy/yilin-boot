package com.yilin.auth;

import org.springframework.data.relational.core.mapping.Table;

import com.yilin.BaseDomain;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2024.0.1 2024/1/9 9:19
 * @since 2024.0.1
 */
@Table("auth_department")
public class Department extends BaseDomain {

	private String tenantId;

	private String name;

	private String fullName;

	private Long parentId;

	private String contact;

	private String phone;

	private Integer category;

	private Long orders;

	private String remark;

	public String getTenantId() {
		return tenantId;
	}

	public Department setTenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public String getName() {
		return name;
	}

	public Department setName(String name) {
		this.name = name;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public Department setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public Department setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	public String getContact() {
		return contact;
	}

	public Department setContact(String contact) {
		this.contact = contact;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public Department setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public Integer getCategory() {
		return category;
	}

	public Department setCategory(Integer category) {
		this.category = category;
		return this;
	}

	public Long getOrders() {
		return orders;
	}

	public Department setOrders(Long orders) {
		this.orders = orders;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public Department setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Department that)) return false;
		if (!super.equals(o)) return false;

		if (getTenantId() != null ? !getTenantId().equals(that.getTenantId()) : that.getTenantId() != null)
			return false;
		if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
			return false;
		if (getFullName() != null ? !getFullName().equals(that.getFullName()) : that.getFullName() != null)
			return false;
		if (getParentId() != null ? !getParentId().equals(that.getParentId()) : that.getParentId() != null)
			return false;
		if (getContact() != null ? !getContact().equals(that.getContact()) : that.getContact() != null)
			return false;
		if (getPhone() != null ? !getPhone().equals(that.getPhone()) : that.getPhone() != null)
			return false;
		if (getCategory() != null ? !getCategory().equals(that.getCategory()) : that.getCategory() != null)
			return false;
		if (getOrders() != null ? !getOrders().equals(that.getOrders()) : that.getOrders() != null)
			return false;
		return getRemark() != null ? getRemark().equals(that.getRemark()) : that.getRemark() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getTenantId() != null ? getTenantId().hashCode() : 0);
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		result = 31 * result + (getFullName() != null ? getFullName().hashCode() : 0);
		result = 31 * result + (getParentId() != null ? getParentId().hashCode() : 0);
		result = 31 * result + (getContact() != null ? getContact().hashCode() : 0);
		result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
		result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
		result = 31 * result + (getOrders() != null ? getOrders().hashCode() : 0);
		result = 31 * result + (getRemark() != null ? getRemark().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Department{" +
				"tenantId='" + tenantId + '\'' +
				", name='" + name + '\'' +
				", fullName='" + fullName + '\'' +
				", parentId=" + parentId +
				", contact='" + contact + '\'' +
				", phone='" + phone + '\'' +
				", category=" + category +
				", orders=" + orders +
				", remark='" + remark + '\'' +
				'}';
	}
}
