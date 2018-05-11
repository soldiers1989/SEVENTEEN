package com.seventeen.service.impl;


import com.seventeen.bean.core.SysUser;
import com.seventeen.bean.core.SysUserRole;
import com.seventeen.core.AbstractService;
import com.seventeen.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 用户管理服务类
 * 
 * @author
 *
 */
@Service
public class SysUserService extends AbstractService<SysUser> {
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

	@Override
	@Transactional
	public int insert(SysUser model) {
		// 保存用户信息
		int count = super.insert(model);
		String userId = model.getId();
		List<String> roleIds = model.getRoleIds();
		if (roleIds != null) {
			// 保存用户角色信息
			for (String roleId : roleIds) {
				SysUserRole sysUserRole = new SysUserRole();
				sysUserRole.setUserId(userId);
				sysUserRole.setRoleId(roleId);
				sysUserRoleMapper.insert(sysUserRole);
			}
		}
		return count;
	}

	@Override
	@Transactional
	public int update(SysUser model) {
		String userId = model.getId();
		List<String> roleIds = model.getRoleIds();
		if (roleIds != null) {
			Example condition = new Example(SysUserRole.class);
			condition.createCriteria().andCondition("user_id=", userId);
			// 删除用户角色信息
			sysUserRoleMapper.deleteByExample(condition);
			// 保存用户角色信息
			for (String roleId : roleIds) {
				SysUserRole sysUserRole = new SysUserRole();
				sysUserRole.setUserId(userId);
				sysUserRole.setRoleId(roleId);
				sysUserRoleMapper.insert(sysUserRole);
			}
		}
		// 修改用户信息
		return super.update(model);
	}

}
