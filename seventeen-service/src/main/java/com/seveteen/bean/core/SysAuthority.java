package com.seveteen.bean.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "系统权限")
public class SysAuthority implements Serializable {
	private static final long serialVersionUID = -5604640071174433644L;

	@Id
	@ApiModelProperty(value = "用户编号，23位主键，自动生成，不用传递", hidden = true)
	private String id;

	@ApiModelProperty(value = "权限code，不能为空，长度不能超过64个字符", required = true)
	private String code;

	@ApiModelProperty(value = "描述")
	private String description;

	@ApiModelProperty(value = "创建时间", dataType = "java.util.Date", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;

	@ApiModelProperty(value = "修改时间", dataType = "java.util.Date", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyDate;

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

	@Override
	public String toString() {
		return "SysAuthority [id=" + id + ", code=" + code + ", description=" + description + ", createDate="
				+ createDate + ", modifyDate=" + modifyDate + "]";
	}

}
