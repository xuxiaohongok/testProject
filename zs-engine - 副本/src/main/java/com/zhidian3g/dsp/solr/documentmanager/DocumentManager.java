package com.zhidian3g.dsp.solr.documentmanager;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.util.SolrUtil;
import com.zhidian3g.common.util.CommonLoggerUtil;

public abstract class DocumentManager<T> {
	protected SolrServer solrServer;
	
	protected Class<T> clazz;
	
	@SuppressWarnings("unchecked")
	public DocumentManager(SolrServer solrServer) {
		this.solrServer = solrServer;
        @SuppressWarnings("rawtypes")
        Class clazz = getClass();
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.clazz = (Class<T>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
    
	}
	
	/**
	 * 添加单个索引文档
	 * @param t
	 */
	public boolean createDocumentIndex(T t) {
		if(t == null) {
			CommonLoggerUtil.addBaseLog("查询不到相应的广告文档！");
			return false;
		}
		
		boolean isScuessful = true;
		try {
			solrServer.addBean(t);
			solrServer.commit();
		} catch (IOException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		} catch (SolrServerException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		}
		CommonLoggerUtil.addBaseLog("==索引添加完毕====");
		return isScuessful;
	}
	
	/**
	 * 批量添加索引文档
	 * @param indexListDocument
	 */
	public boolean createDocumentListIndex(Collection<T> indexListDocument) {
		if(indexListDocument == null || indexListDocument.size() == 0) {
			CommonLoggerUtil.addBaseLog("批量添加索引成功失败 传过来的对象为空！");
			return false;
		}
		
		boolean isScuessful = true;
		try {
			solrServer.addBeans(indexListDocument);
			solrServer.optimize();
			solrServer.commit();
			CommonLoggerUtil.addBaseLog("批量添加索引成功=" + indexListDocument);
			return isScuessful;
		} catch (SolrServerException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		} catch (IOException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		}
		
		CommonLoggerUtil.addBaseLog("批量添加索引成功失败 !");
		return isScuessful;
	}
	
	/**
	 * 
	 * 获取总的文档数
	 * @return
	 */
	public List<T> getAllDocument() {
		List<T> adDocumentList = null;
		try {
			SolrQuery query = new SolrQuery();// 查询全部
			query.setRows(DspConstant.DEFAULT_ROW);
			query.setQuery("*:*");
			QueryResponse response = solrServer.query(query);
			adDocumentList = response.getBeans(clazz);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return adDocumentList;
	}
	
	/**
	 * 
	 * 获取总的文档数
	 * @return
	 */
	public long getDocumentNums() {
		try {
			SolrQuery query = new SolrQuery();// 查询全部
			query.setRows(DspConstant.DEFAULT_ROW);
			query.setQuery("*:*");
			QueryResponse response = solrServer.query(query);
			return response.getResults().getNumFound();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	/**
	 * 根据id删除对应索引文档
	 * @param adId
	 */
	public boolean deleteDocumentById(String adId) {
		boolean isScuessful = true;
		try {
			solrServer.deleteById(adId);
			solrServer.commit();
			CommonLoggerUtil.addBaseLog("刪除索引adid=" + adId);
		} catch (SolrServerException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		} catch (IOException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		}
		return isScuessful;
	}

	/**
	 * 根据id集合删除对应索引文档
	 * @param ids
	 */
	public boolean deleteDocumentByListId(List<String> ids) {
		boolean isScuessful = true;
		try {
			solrServer.deleteById(ids);
			solrServer.commit();
			CommonLoggerUtil.addBaseLog("批量刪除索引=" + ids);
		} catch (SolrServerException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		} catch (IOException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		}
		
		return isScuessful;
	}
	
	/**
	 * 删除所有的索引文档
	 */
	public boolean deleteAllDocument() {
		CommonLoggerUtil.addBaseLog("***********删除所有索引*********");
		boolean isScuessful = true;
		try {
			solrServer.deleteByQuery("*:*");
			solrServer.commit();
			CommonLoggerUtil.addBaseLog("***********所有索引删除成功*********");
		} catch (SolrServerException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		} catch (IOException e) {
			isScuessful = false;
			SolrUtil.saveExceptionLogAndSendEmail(e);
		} 
		return isScuessful;
	}

	/**
	 * 获取solrServer
	 * @return
	 */
	public SolrServer getSolrServer() {
		return solrServer;
	}
	
}
