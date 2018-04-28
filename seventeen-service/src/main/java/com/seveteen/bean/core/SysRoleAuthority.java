package com.seveteen.bean.core;

import io.swagger.annotations.ApiModel;

import javax.persistence.Table;

@ApiModel("角色权限")
@Table(name = "sys_role_authority")
public class SysRoleAuthority {
	private String roleId;
	private String authorityId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

}
