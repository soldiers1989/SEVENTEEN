package com.seventeen.exception;

import com.seventeen.core.ResultCode;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -4297901785346290206L;
	private ResultCode error;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(ResultCode error, String message) {
		super(message);
		this.error = error;
	}

	public ServiceException(ResultCode error, String message, Throwable cause) {
		super(message, cause);
		this.error = error;
	}

	public ResultCode getError() {
		return error;
	}

	public void setError(ResultCode error) {
		this.error = error;
	}

}
