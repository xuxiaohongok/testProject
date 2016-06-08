package com.zhidian.dsp.sy.init;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.zhidian.common.util.CommonLoggerUtil;

/**
 * 系统初始化类
 * @author Administrator
 *
 */
public class InitDataListener implements InitializingBean,DisposableBean {
	
	@Override
	public void afterPropertiesSet() throws Exception {
		CommonLoggerUtil.addBaseLog("执行InitDataListener: afterPropertiesSet");   
	}

	public void initMethod() {  
		try {
			CommonLoggerUtil.addBaseLog("==========初始化ip库==============");  
//			IpFinder.load();
//			Class.forName("com.zhidian.CityMap");
			CommonLoggerUtil.addBaseLog("==========ip库初始化完毕==============");  
		} catch (Exception e) {
			e.printStackTrace();
		}
    }  
	
	@Override
	public void destroy() throws Exception {
		CommonLoggerUtil.addBaseLog("==========执行InitDataListener: 销毁======");   
	}
	
	public void destroyMethod() {  
		CommonLoggerUtil.addBaseLog("==========执行InitDataListener destroyMethod: 销毁======");  
    }  

	
	
}