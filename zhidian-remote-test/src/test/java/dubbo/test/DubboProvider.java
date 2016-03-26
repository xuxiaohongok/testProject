package dubbo.test;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.druid.support.json.JSONUtils;
import com.zhidian.remote.service.RemoteService;


/**
 * 
 * @描述: 启动Dubbo服务用的MainClass.
 * @作者: WuShuicheng .
 * @创建时间: 2013-11-5,下午9:47:55 .
 * @版本: 1.0 .
 */
public class DubboProvider {
	
	private static final Log log = LogFactory.getLog(DubboProvider.class);

	public static void main(String[] args) {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
			RemoteService hw = (RemoteService)context.getBean("remoteService");
			hw.getAdMessage(JSONUtils.toJSONString(new HashMap<String, Object>()));
		} catch (Exception e) {
			log.error("== DubboProvider context start error:",e);
		}
		
	}
    
}