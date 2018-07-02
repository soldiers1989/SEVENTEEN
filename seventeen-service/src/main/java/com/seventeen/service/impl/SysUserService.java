package com.seventeen.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.core.SysUser;
import com.seventeen.bean.core.SysUserRole;
import com.seventeen.core.AbstractService;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SysUserRoleMapper;
import com.seventeen.util.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
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

	private final Logger logger = LoggerFactory.getLogger(SysUserService.class);


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
		// 修改用户信息
		return super.update(model);
	}

	public Result<List<SysUser>> findList(String status, PageInfo pageInfo) {
		Result<List<SysUser>> result = new Result<>();

		try {
			Page page = PageHelper.startPage(pageInfo.getPageNum(),
					pageInfo.getPageSize(), true);
			ArrayList<SysUser> sysUsers = sysUserRoleMapper.findList(status);
			pageInfo.setTotal(page.getTotal());
			result.setData(sysUsers, pageInfo);
		} catch (Exception e) {
			logger.error("error",e);
			throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return result;
	}
}
