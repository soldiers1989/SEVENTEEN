package com.seventeen.service.impl;

import com.seventeen.bean.core.MyGrantedAuthority;
import com.seventeen.bean.core.SysAuthority;
import com.seventeen.bean.core.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 从数据库中获取用户信息及权限
 * 
 * @author
 *
 */
@Service
@CacheConfig(cacheNames = { "sysUserDetails" })
public class SysUserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysAuthorityService sysAuthorityService;

	@Override
	@Cacheable
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = sysUserService.findBy("username", username);
		if (sysUser == null) {
			throw new UsernameNotFoundException(String.format("用户【%s】不存在。", username));
		} else {
			List<SysAuthority> sysAuthorities = sysAuthorityService.findByUserId(sysUser.getId());
			// 设置权限
			sysUser.setAuthorities(mapToGrantedAuthorities(sysAuthorities));
			return sysUser;
		}
	}

	/**
	 * 权限集合转换成需要的集合对象
	 * 
	 * @param authorities
	 * @return
	 */
	private static List<GrantedAuthority> mapToGrantedAuthorities(List<SysAuthority> authorities) {
		return authorities.stream().map(SysAuthority::getCode).map(MyGrantedAuthority::new)
				.collect(Collectors.toList());
	}

}
