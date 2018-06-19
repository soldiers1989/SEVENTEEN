package com.seventeen.bean.core;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@ApiModel(description = "系统用户")
public class SysUser implements UserDetails {

	private static final long serialVersionUID = -7988589094699415970L;

	@Id
	@ApiModelProperty(value = "用户编号，23位主键，自动生成，不用传递", hidden = true)
	private String id;

	@ApiModelProperty(value = "账号，不能为空，长度不能超过64个字符", required = true)
	private String username;

	@ApiModelProperty(value = "密码，不能为空", required = true)
	private String password;

	@ApiModelProperty(value = "描述")
	private String description;

	@ApiModelProperty(value = "电话")
	private String phone;

	@ApiModelProperty(value = "年龄")
	private String age;

	@ApiModelProperty(value = "性别")
	private String sex;

	@ApiModelProperty(value = "unionid")
	private String unionid;

	@ApiModelProperty(value = "openid")
	private String openid;

	@ApiModelProperty(value = "最后订单时间")
	private String lastOrderTime;
	@ApiModelProperty(value = "最后登录时间")
	private String lastLoginTime;

	@ApiModelProperty(value = "创建时间", dataType = "Date", hidden = true)
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;

	@ApiModelProperty(value = "修改时间", dataType = "Date", hidden = true)
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyDate;

	@ApiModelProperty(value = "密码最后修改时间", dataType = "Date", hidden = true)
	private Date lastPasswordResetDate;

	@ApiModelProperty(value = "用户权限，只用作spring-security编码使用，不作为接口用。", hidden = true)
	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@ApiModelProperty(value = "角色列表，修改时不传此参数则维持不变，空列表则清空角色", dataType = "List")
	@Transient
	private List<String> roleIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastOrderTime() {
		return lastOrderTime;
	}

	public void setLastOrderTime(String lastOrderTime) {
		this.lastOrderTime = lastOrderTime;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonDeserialize
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@JsonIgnore
	public List<String> getRoleIds() {
		return roleIds;
	}

	@JsonDeserialize
	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	// 这个是自定义的，返回上次密码重置日期
	@JsonIgnore
	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	@JsonDeserialize
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	@JsonIgnore
	// 账户是否未过期
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	// 账户是否未锁定
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	// 密码是否未过期
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	// 账户是否激活
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return (String) JSON.toJSON(this);
	}

}
