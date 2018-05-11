package com.seventeen.bean.core;

import io.swagger.annotations.ApiModel;

import javax.persistence.Table;

@ApiModel("用户角色")
@Table(name = "sys_user_role")
public class SysUserRole {
	private String userId;
	private String roleId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
