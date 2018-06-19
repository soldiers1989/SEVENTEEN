package com.seventeen.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
	/**
	 * 成功
	 */
	SUCCESS(200),
	/**
	 * 失败
	 */
	FAIL(-200),
	/**
	 * 未认证（签名错误）
	 */
	UNAUTHORIZED(401),
	/**
	 * 接口不存在
	 */
	NOT_FOUND(404),
	/**
	 * 服务器内部错误
	 */
	INTERNAL_SERVER_ERROR(500),
	/**
	 * 服务器内部错误(重复的列)
	 */
	INTERNAL_SERVER_ERROR_UNIQUE(500001),
	/**
	 * 服务器内部错误(外键约束)
	 */
	INTERNAL_SERVER_ERROR_FOREIGN_KEY(500002),
	/**
	 * 图片或文件路径不能为空
	 */
	FILE_PATH_NOT_NULL(2001),
	/**
	 * 图片或文件路径不正确，请检查
	 */
	FILE_PATH_IS_WRONG(2002),
	/**
	 * 文件太大
	 */
	FILE_SIZE_TOO_LONG(2003),
	/**
	 * 文件不存在
	 */
	FILE_NOT_EXISTS(2004),
	/**
	 * 手机号码已注册，请确认!
	 */
	MOBILE_REGISTERED(3008),
	/**
	 * 该手机号码不是学校预留的联系号码!
	 */
	MOBILE_NOT_RESERVED_BY_SCHOOL(3009),
	/**
	 * 找不到相关记录
	 */
	NO_RECORDS_FOUND(1001),
	/**
	 * 操作码有误!
	 */
	CODE_IS_WRONG(1005),
	/**
	 * 操作失败
	 */
	OPERATION_FAILED(1006),
	/**
	 * 找不到对应教师(已离职或者不存在)
	 */
	NO_TEACHER(1007),
	/**
	 * 小孩ID不能为空
	 */
	CHILD_ID_CANNOT_EMPTY(1008),
	/**
	 * 激活码失效
	 */
	ACTIVATION_CODE_FAILURE(1009),
	/**
	 * 该教师已开启屏蔽功能
	 */
	TEACHER_SHIELD(1010),
	/**
	 * 用户的账号[%s]不存在!
	 */
	ACCOUNT_NOT_EXISTS(3000),
	/**
	 * 用户的密码错误
	 */
	PASSWORD_WRONG(3001),
	/**
	 * 登录未知异常!
	 */
	LOGIN_UNKNOW_ERROR(3002),
	/**
	 * 手机号码[%s]格式有误!
	 */
	MOBILE_FORMAT_WRONG(3003),
	/**
	 * 您输入的验证码有误或过期，验证失败!
	 */
	CODE_INVALID(3004),
	/**
	 * 注册失败
	 */
	REGISTER_FAIL(3005),
	/**
	 * 验证码过期，短信验证出错，请重新获取验证码验证!
	 */
	CODE_EXPIRE(3006),
	/**
	 * 旧密码输入错误，请重新输入!
	 */
	OLD_PASSWORD_WRONG(3007);

	public Integer code;

	ResultCode(Integer code) {
		this.code = code;
	}

	@JsonCreator
	public static ResultCode getItem(Integer code) {
		for (ResultCode item : values()) {
			if (item.getCode().intValue() == code.intValue()) {
				return item;
			}
		}
		throw new IllegalArgumentException("No element matches [" + code + "]");
	}

	@JsonValue
	public Integer getCode() {
		return code;
	}
}
