package com.seventeen.service;

import com.seventeen.bean.core.SysUser;

/**
 * 认证服务
 *
 * @author
 *
 */
public interface AuthService {
	/**
	 * 登录获取token
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	String login(String username, String password);

	/**
	 * 根据旧token刷新token
	 *
	 * @param oldToken
	 * @return
	 */
	String refresh(String oldToken);

	/**
	 * 注册
	 *
	 * @param userToAdd
	 * @return
	 */
	String register(SysUser userToAdd);
}
