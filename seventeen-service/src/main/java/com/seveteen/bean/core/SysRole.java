package com.seveteen.bean.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(description = "系统角色")
public class SysRole implements Serializable {

	private static final long serialVersionUID = -8537481261850428534L;

	@Id
	@ApiModelProperty(value = "用户编号，23位主键，自动生成，不用传递。", hidden = true)
	private String id;

	@ApiModelProperty(value = "角色code，不能为空，长度不能超过64个字符", required = true)
	private String code;

	@ApiModelProperty(value = "描述")
	private String description;

	@ApiModelProperty(value = "创建时间", dataType = "java.util.Date", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;

	@ApiModelProperty(value = "修改时间", dataType = "java.util.Date", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyDate;

	@ApiModelProperty(value = "权限列表，修改时不传此参数则维持不变，空列表则清空权限", dataType = "List")
	@Transient
	private List<String> authorityIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	public List<String> getAuthorityIds() {
		return authorityIds;
	}

	@JsonDeserialize
	public void setAuthorityIds(List<String> authorityIds) {
		this.authorityIds = authorityIds;
	}

	@Override
	public String toString() {
		return "SysRole [id=" + id + ", code=" + code + ", description=" + description + ", createDate=" + createDate
				+ ", modifyDate=" + modifyDate + "]";
	}

}
