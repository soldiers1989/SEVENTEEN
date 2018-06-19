package com.seventeen.mapper;


import com.seventeen.bean.core.SysAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysAuthorityMapper extends CoreMapper<SysAuthority> {
	/**
	 * 根据用户id查询权限列表
	 *
	 * @param userId
	 * @return
	 */
	@Select("select distinct a.* from sys_authority a inner join sys_role_authority ra on a.id = ra.authority_id"
			+ " inner join sys_role r on r.id = ra.role_id inner join sys_user_role ur on r.id = ur.role_id"
			+ " where ur.user_id = #{userId}")
	public List<SysAuthority> findByUserId(@Param("userId") String userId);

	/**
	 * 根据角色id查询权限列表
	 *
	 * @param roleId
	 * @return
	 */
	@Select("select distinct a.* from sys_authority a inner join sys_role_authority ra on a.id = ra.authority_id"
			+ " where ra.role_id = #{roleId}")
	public List<SysAuthority> findByRoleId(@Param("roleId") String roleId);
}
