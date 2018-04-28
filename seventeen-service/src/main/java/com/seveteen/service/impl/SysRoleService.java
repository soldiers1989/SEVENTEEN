package com.seveteen.service.impl;

import com.seveteen.bean.core.SysRole;
import com.seveteen.bean.core.SysRoleAuthority;
import com.seveteen.core.AbstractService;
import com.seveteen.mapper.SysRoleAuthorityMapper;
import com.seveteen.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SysRoleService extends AbstractService<SysRole> {
	@Autowired
	private SysRoleAuthorityMapper sysRoleAuthorityMapper;

	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	@Transactional
	public int insert(SysRole model) {
		String roleId = model.getId();
		List<String> authorityIds = model.getAuthorityIds();
		// 保存角色信息
		int count = super.insert(model);
		if (authorityIds != null) {
			// 保存角色权限信息
			for (String authorityId : authorityIds) {
				SysRoleAuthority sysRoleAuthority = new SysRoleAuthority();
				sysRoleAuthority.setRoleId(roleId);
				sysRoleAuthority.setAuthorityId(authorityId);
				sysRoleAuthorityMapper.insert(sysRoleAuthority);
			}
		}
		return count;
	}

	@Override
	@Transactional
	public int update(SysRole model) {
		String roleId = model.getId();
		List<String> authorityIds = model.getAuthorityIds();
		if (authorityIds != null) {
			Example condition = new Example(SysRoleAuthority.class);
			condition.createCriteria().andCondition("role_id=", roleId);
			// 删除角色权限信息
			sysRoleAuthorityMapper.deleteByExample(condition);
			// 保存角色权限信息
			for (String authorityId : authorityIds) {
				SysRoleAuthority sysRoleAuthority = new SysRoleAuthority();
				sysRoleAuthority.setRoleId(roleId);
				sysRoleAuthority.setAuthorityId(authorityId);
				sysRoleAuthorityMapper.insert(sysRoleAuthority);
			}
		}
		// 修改角色信息
		return super.update(model);
	}

	/**
	 * 根据用户id查询角色列表
	 * 
	 * @param usrerId
	 * @return
	 */
	public List<SysRole> findByUserId(String userId) {
		return sysRoleMapper.findByUserId(userId);
	}
}
