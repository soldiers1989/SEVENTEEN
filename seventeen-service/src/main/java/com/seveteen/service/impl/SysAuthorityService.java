package com.seveteen.service.impl;


import com.seveteen.bean.core.SysAuthority;
import com.seveteen.core.AbstractService;
import com.seveteen.mapper.SysAuthorityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAuthorityService extends AbstractService<SysAuthority> {
	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;

	/**
	 * 根据用户id查询权限列表
	 * 
	 * @param usrerId
	 * @return
	 */
	public List<SysAuthority> findByUserId(String userId) {
		return sysAuthorityMapper.findByUserId(userId);
	}

	/**
	 * 根据角色id查询权限列表
	 * 
	 * @param roleId
	 * @return
	 */
	public List<SysAuthority> findByRoleId(String roleId) {
		return sysAuthorityMapper.findByRoleId(roleId);
	}

}
