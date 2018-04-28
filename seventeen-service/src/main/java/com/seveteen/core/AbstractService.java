package com.seveteen.core;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seveteen.exception.ServiceException;
import com.seveteen.mapper.CoreMapper;
import com.seveteen.service.Service;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T> implements Service<T> {

	@Autowired
	protected CoreMapper<T> mapper;

	private Class<T> modelClass; // 当前泛型真实类型的Class

	@SuppressWarnings("unchecked")
	public AbstractService() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		modelClass = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@Override
	public int insert(T model) {
		return mapper.insertSelective(model);
	}

	@Override
	public int insert(List<T> models) {
		return mapper.insertList(models);
	}

	@Override
	public int deleteById(String id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int deleteByIds(String ids) {
		return mapper.deleteByIds(ids);
	}

	@Override
	public int update(T model) {
		return mapper.updateByPrimaryKeySelective(model);
	}

	@Override
	public T findById(String id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public T findBy(String fieldName, Object value) throws TooManyResultsException {
		try {
			T model = modelClass.newInstance();
			Field field = modelClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(model, value);
			return mapper.selectOne(model);
		} catch (ReflectiveOperationException e) {
			throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public List<T> findByIds(String ids) {
		return mapper.selectByIds(ids);
	}

	@Override
	public List<T> findByCondition(Example condition) {
		return mapper.selectByExample(condition);
	}

	@Override
	public List<T> findByCondition(Example condition, PageInfo pageInfo) {
		Page<T> page = PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
		List<T> list = mapper.selectByExample(condition);
		pageInfo.setTotal(page.getTotal());
		return list;
	}

	@Override
	public List<T> findAll() {
		return mapper.selectAll();
	}

	@Override
	public List<T> findAll(PageInfo pageInfo) {
		Page<T> page = PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
		List<T> list = mapper.selectAll();
		pageInfo.setTotal(page.getTotal());
		return list;
	}
}