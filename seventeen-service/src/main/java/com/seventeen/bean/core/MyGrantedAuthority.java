package com.seventeen.bean.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * 自定义简单权限类
 * 
 * @author
 *
 */
public class MyGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 8465708992019885599L;
	private String role;

	public MyGrantedAuthority() {

	}

	public MyGrantedAuthority(String role) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	@JsonIgnore
	public String getAuthority() {
		return role;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof MyGrantedAuthority) {
			return role.equals(((MyGrantedAuthority) obj).role);
		}

		return false;
	}

	public int hashCode() {
		return this.role.hashCode();
	}

	public String toString() {
		return this.role;
	}

}