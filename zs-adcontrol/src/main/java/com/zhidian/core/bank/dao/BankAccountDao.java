/**
 * com.zhidian.bank.dao.BankAccountDao.java
 */
package com.zhidian.core.bank.dao;

import java.util.List;

import com.zhidian.ad.AdBaseMessage;
import com.zhidian.common.core.dao.BaseDao;



/**
 * 
 * @描述: 银行账户信息表，数据访问层接口.
 * @作者: WuShuicheng .
 * @创建时间: 2014-4-15, 下午2:25:18
 */
public interface BankAccountDao extends BaseDao<AdBaseMessage> {
	
	/**
	 * 根据银行账号模糊查找 
	 * @param bankAccountKey .
	 * @param status .
	 * @return List .
	 */
	List<AdBaseMessage> likeBy(String bankAccountKey, Integer status);
}