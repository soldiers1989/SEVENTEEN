package com.seventeen.service.impl;

import com.seventeen.bean.core.SysUser;
import com.seventeen.config.JwtTokenConfig;
import com.seventeen.filter.JwtAuthenticationTokenFilter;
import com.seventeen.mapper.SysUserMapper;
import com.seventeen.service.AuthService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtTokenConfig jwtTokenConfig;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserDetailsServiceImpl sysUserDetailsServiceImpl;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public String register(SysUser userToAdd) {
		final String openid = userToAdd.getOpenid();
		SysUser userDetail=(SysUser)sysUserDetailsServiceImpl.loadUserByOpenId(openid);
		if(userDetail!=null) {
			String id = userDetail.getId();
			BeanUtils.copyProperties(userToAdd,userDetail);
			userDetail.setLastLoginTime(DateUtil.nowDate());
			userDetail.setId(id);
			sysUserService.update(userDetail);
			return jwtTokenConfig.generateToken(userDetail);
		}

		userToAdd.setId(IDGenerator.getId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		final String rawPassword = userToAdd.getPassword();
		userToAdd.setPassword(encoder.encode(rawPassword));
		userToAdd.setLastPasswordResetDate(new Date());
		userToAdd.setLastLoginTime(DateUtil.nowDate());
		userToAdd.setRoleIds(Arrays.asList("20170802115412744304269"));
		sysUserService.insert(userToAdd);
		return jwtTokenConfig.generateToken(userToAdd);
	}

	@Override
	public String login(String username, String password) {
		UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
		// Perform the security
		final Authentication authentication = authenticationManager.authenticate(upToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// Reload password post-security so we can generate token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		final String token = jwtTokenConfig.generateToken(userDetails);
		return token;
	}

	@Override
	public String refresh(String oldToken) {
		final String token = oldToken.substring(JwtAuthenticationTokenFilter.TOKEN_HEAD.length());
		String username = jwtTokenConfig.getUsernameFromToken(token);
		SysUser user = (SysUser) userDetailsService.loadUserByUsername(username);
		if (jwtTokenConfig.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			return jwtTokenConfig.refreshToken(token);
		}
		return null;
	}
}