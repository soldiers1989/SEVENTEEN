package com.seventeen.service;

import com.seventeen.util.PageInfo;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Service 层 基础接口，其他Service 接口 请继承该接口
 */
public interface Service<T> {
	/**
	 * 持久化
	 * 
	 * @param model
	 * @return
	 */
	int insert(T model);

	/**
	 * 批量持久化
	 * 
	 * @param models
	 */
	int insert(List<T> models);

	/**
	 * 通过主鍵刪除
	 * 
	 * @param id
	 */
	int deleteById(String id);

	/**
	 * 批量刪除 eg：ids -> “1,2,3,4”
	 * 
	 * @param ids
	 */
	int deleteByIds(String ids);

	/**
	 * 更新
	 * 
	 * @param model
	 */
	int update(T model);

	/**
	 * 通过ID查找
	 * 
	 * @param id
	 * @return
	 */
	T findById(String id);

	/**
	 * 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
	 * 
	 * @param fieldName
	 * @param value
	 * @return
	 * @throws TooManyResultsException
	 */
	T findBy(String fieldName, Object value) throws Exception;

	/**
	 * 通过多个ID查找//eg：ids -> “1,2,3,4”
	 * 
	 * @param ids
	 * @return
	 */
	List<T> findByIds(String ids);

	/**
	 * 根据条件查找
	 * 
	 * @param condition
	 * @return
	 */
	List<T> findByCondition(Example condition);

	/**
	 * 分页根据条件查找
	 * 
	 * @param condition
	 * @return
	 */
	List<T> findByCondition(Example condition, PageInfo pageInfo);

	/**
	 * 获取所有
	 * 
	 * @return
	 */
	List<T> findAll();

	/**
	 * 分页获取所有
	 * 
	 * @param page
	 * @return
	 */
	List<T> findAll(PageInfo pageInfo);
}