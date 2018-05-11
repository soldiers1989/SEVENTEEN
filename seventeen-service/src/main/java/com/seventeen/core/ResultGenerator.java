package com.seventeen.core;


/**
 * 响应结果生成工具
 */
public class ResultGenerator {
	public static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";


	/**
	 * 产生调用成功消息
	 *
	 * @param result
	 *            数据内容
	 * @return
	 */
	public static <T> Result<T> genSuccessResult(Result result) {
		return new Result<T>().setPageInfo(result.getPageInfo()).setResultCode(result.getResultCode()).setMessage(result.getMessage()).setData((T) result.getData());
	}


	/**
	 * 产生调用成功消息
	 *
	 * @param data
	 *            数据内容
	 * @return
	 */
	public static <T> Result<T> genSuccessResult(T data) {
		return new Result<T>().setResultCode(ResultCode.SUCCESS.getCode()).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(data);
	}

	/**
	 * 产生调用失败结果
	 *
	 *            错误码，请调用ResultCode里面所定义的错误码
	 * @param message
	 *            失败原因
	 * @return
	 */
	public static <T> Result<T> genSuccessResult(String message,int code) {
		return new Result<T>().setResultCode(code).setMessage(message);
	}

	/**
	 * 产生调用失败结果
	 *
	 *            错误码，请调用ResultCode里面所定义的错误码
	 * @param message
	 *            失败原因
	 * @return
	 */
	public static <T> Result<T> genFailResult(String message) {
		return new Result<T>().setResultCode(ResultCode.FAIL.getCode()).setMessage(message);
	}
}
