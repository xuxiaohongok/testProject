package com.zhidian.common.repository.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.zhidian.common.repository.BaseRepository;
import com.zhidian.common.repository.support.page.Page;


public class BaseRepositoryImpl extends SqlSessionDaoSupport implements BaseRepository {
	private Logger log4j = LoggerFactory.getLogger(BaseRepositoryImpl.class);
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    
	public <T> int save(T entity) {
		return getSqlSession().insert(entity.getClass().getName() + ".insert",
				entity);
	}

	public <T> int delete(Class<T> entityClass, Serializable id) {
		return getSqlSession().delete(entityClass.getName() + ".delete", id);
	}

	public <T> int deleteList(Class<T> entityClass,
			List<? extends Serializable> list) {
		return getSqlSession().delete(entityClass.getName() + ".deleteList",
				list);
	}
	
	public <T> int delete(Class<T> entityClass, String statement, Object params) {
		return getSqlSession().delete(entityClass.getName() + "." + statement,
				params);
	}

	public <T> int update(T entity) {
		return getSqlSession().update(entity.getClass().getName() + ".update",
				entity);
	}

	public <T> int update(Class<T> entityClass, String statement, Object params) {
		return getSqlSession().update(entityClass.getName() + "." + statement,
				params);
	}

	public <T> T findById(Class<T> entityClass, Serializable id) {
		return getSqlSession().selectOne(entityClass.getName() + ".findById",
				id);
	}

	public <T> List<T> findAll(Class<T> entityClass) {
		return getSqlSession().selectList(entityClass.getName() + ".findAll");
	}
	
	public <E, T> Page<E> findPage(Class<T> entityClass, int pageNo,
			int pageSize) {
		Page page = new Page(pageSize, pageNo);
		Map params = new HashMap();
		params.put("offset", Integer.valueOf(page.getFirstResult()));
		params.put("pageSize", Integer.valueOf(page.getPageSize()));
		List pageList = getSqlSession().selectList(
				entityClass.getName() + ".findByPage", params);
		page.setResult(pageList);
		
		if(page.isAutoCount()) {
			long total = ((Long) getSqlSession().selectOne(
					entityClass.getName() + ".findTotal")).longValue();
			page.setTotalCount(total);
		}
		return page;
	}

	
	public <E, T> Page<E> findPage(Class<T> entityClass, String statement,
			Map<String, Object> params, int pageNo, int pageSize) {
		Page page = new Page(pageSize, pageNo);
		params.put("offset", Integer.valueOf(page.getFirstResult()));
		params.put("pageSize", Integer.valueOf(page.getPageSize()));
		List pageList = getSqlSession().selectList(
				entityClass.getName() + "." + statement, params);
		page.setResult(pageList);
		
		if(page.isAutoCount()) {
			long total = ((Long) getSqlSession().selectOne(
					entityClass.getName() + "." + statement + "Total", params))
					.longValue();
			page.setTotalCount(total);
		}
		
		return page;
	}

	public <E, T> List<E> findList(Class<T> entityClass, String statement,
			Object params) {
		return getSqlSession().selectList(
				entityClass.getName() + "." + statement, params);
	}

	public <E, T> E findOne(Class<T> entityClass, String statement,
			Object params) {
		return getSqlSession().selectOne(
				entityClass.getName() + "." + statement, params);
	}

	public <T> int isExist(Class<T> entityClass, String statement, Object params) {
		return ((Integer) getSqlSession().selectOne(
				entityClass.getName() + "." + statement, params)).intValue();
	}

	public <T> int insert(Class<T> entityClass, String statement, Object params) {
		return getSqlSession().insert(entityClass.getName() + "." + statement,
				params);
	}
	

	 /**
     * 批量更新
     * 方法描述：批量更新（效率没有在配置文件上的高）
     * @param statementName
     * @param list
     * @throws DataAccessException
     * @comment
     */
    public void batchUpdate(final String statementName, final List list)  throws DataAccessException{
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try{
            if(null != list || list.size() > 0){
                int size = 10000;
             
                for (int i = 0, n = list.size(); i < n; i++) {
                	session.update(statementName, list.get(i));
                    if (i % 1000 == 0 || i == size - 1) {
                        //手动每1000个一提交，提交后无法回滚
                        session.commit();
                        //清理缓存，防止溢出
                        session.clearCache();
                    }
                }
            }
        }catch (Exception e){
            session.rollback();
            e.printStackTrace();
            if (log4j.isDebugEnabled()) {
                log4j.debug("batchUpdate error: id [" + statementName + "], parameterObject [" + list + "].  Cause: " + e.getMessage());
            }
        } finally {
            session.close();
        }
    }
     
    /**
     * 批量插入
     * 方法描述：批量插入（效率没有在配置文件上的高）
     * @param statementName
     * @param list
     * @throws DataAccessException
     * @comment
     */
    public void batchInsert(final String statementName, final List list)  throws DataAccessException{
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        int size = 10000;
        try{
            if(null != list || list.size() > 0){
                for (int i = 0, n = list.size(); i < n; i++) {
                	session.insert(statementName, list.get(i));
                    if (i % 1000 == 0 || i == size - 1) {
                        //手动每1000个一提交，提交后无法回滚
                        session.commit();
                        //清理缓存，防止溢出
                        session.clearCache();
                    }
                }
            }
        }catch (Exception e){
            session.rollback();
            if (log4j.isDebugEnabled()) {
                e.printStackTrace();
                log4j.debug("batchInsert error: id [" + statementName + "], parameterObject [" + list + "].  Cause: " + e.getMessage());
            }
        } finally {
            session.close();
        }
    }
 
    /**
     * 批量删除
     * 方法描述：批量删除（效率没有在配置文件上的高）
     * @param statementName
     * @param list
     * @throws DataAccessException
     * @comment
     */
    public void batchDelete(final String statementName, final List<Object> list)  throws DataAccessException{
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        int size = 10000;
        try{
            if(null != list || list.size() > 0){
                for (int i = 0, n = list.size(); i < n; i++) {
                	session.delete(statementName, list.get(i));
                    if (i % 1000 == 0 || i == size - 1) {
                        //手动每1000个一提交，提交后无法回滚
                        session.commit();
                        //清理缓存，防止溢出
                        session.clearCache();
                    }
                }
            }
        }catch (Exception e){
            session.rollback();
            if (log4j.isDebugEnabled()) {
                e.printStackTrace();
                log4j.debug("batchDelete error: id [" + statementName + "], parameterObject [" + list + "].  Cause: " + e.getMessage());
            }
        } finally {
            session.close();
        }
    }
}