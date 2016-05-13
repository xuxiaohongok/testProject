package com.zhidian.common.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.zhidian.common.repository.support.page.Page;

public abstract interface BaseRepository {
	public abstract <T> int save(T paramT);

	public abstract <T> int delete(Class<T> paramClass,
			Serializable paramSerializable);

	public abstract <T> int deleteList(Class<T> paramClass,
			List<? extends Serializable> paramList);

	public abstract <T> int delete(Class<T> paramClass, String paramString,
			Object paramObject);

	public abstract <T> int update(T paramT);

	public abstract <T> int update(Class<T> paramClass, String paramString,
			Object paramObject);

	public abstract <T> T findById(Class<T> paramClass,
			Serializable paramSerializable);

	public abstract <T> List<T> findAll(Class<T> paramClass);

	public abstract <E, T> Page<E> findPage(Class<T> paramClass, int paramInt1,
			int paramInt2);

	public abstract <E, T> Page<E> findPage(Class<T> paramClass,
			String paramString, Map<String, Object> paramMap, int paramInt1,
			int paramInt2);

	public abstract <E, T> List<E> findList(Class<T> paramClass,
			String paramString, Object paramObject);

	public abstract <E, T> E findOne(Class<T> paramClass, String paramString,
			Object paramObject);

	public abstract <T> int isExist(Class<T> paramClass, String paramString,
			Object paramObject);

	public abstract <T> int insert(Class<T> paramClass, String paramString,
			Object paramObject);
	
	public void batchUpdate(final String statementName, final List list)  throws DataAccessException;
	
	public void batchInsert(final String statementName, final List list)  throws DataAccessException;
	
	public void batchDelete(final String statementName, final List<Object> list)  throws DataAccessException;
}