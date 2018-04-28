package com.seveteen.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("Result(返回结果)")
@Data
public class Result<T> {
	@ApiModelProperty("状态，200表示成功， -100表示失败")
	private int resultCode = 200;
	@ApiModelProperty("提示性信息")
	private String message = "SUCCESS";
	@ApiModelProperty("数据体")
	private T data;
	@ApiModelProperty("分页信息")
	@JsonInclude(Include.NON_NULL)
	private PageInfo pageInfo = null;

	public Result<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public Result<T> setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
		return this;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

	public Result<T> setResultCode(int resultCode) {
		this.resultCode = resultCode;
		return this;
	}

	public Result(T data) {
		this.data = data;
	}

	public Result(T data, PageInfo pageInfo) {
		this.data = data;
		this.pageInfo = pageInfo;
	}

	public Result() {
	}
}