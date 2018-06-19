package com.seventeen.mapper;

import com.seventeen.bean.core.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper extends CoreMapper<SysRole> {

	/**
	 * 根据用户id查询角色列表
	 * 
	 * @param usrerId
	 * @return
	 */
	@Select("select r.* from sys_role r inner join sys_user_role ur on r.id = ur.role_id where ur.user_id = #{userId}")
	List<SysRole> findByUserId(@Param("userId") String userId);
}
