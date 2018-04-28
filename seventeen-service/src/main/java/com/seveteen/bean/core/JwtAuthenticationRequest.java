package com.seveteen.bean.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = " 认证登录信息")
public class JwtAuthenticationRequest implements Serializable {
	
	private static final long serialVersionUID = 37073251282323746L;

	@ApiModelProperty(value = "登录名")
	private String username;

	@ApiModelProperty(value = "密码")
	private String password;

	public JwtAuthenticationRequest() {
		super();
	}

	public JwtAuthenticationRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}