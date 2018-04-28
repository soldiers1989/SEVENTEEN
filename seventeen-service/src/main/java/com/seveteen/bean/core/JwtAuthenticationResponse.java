package com.seveteen.bean.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("token响应")
public class JwtAuthenticationResponse {
	@ApiModelProperty(value = "token值")
	private String token;

	public JwtAuthenticationResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}
}