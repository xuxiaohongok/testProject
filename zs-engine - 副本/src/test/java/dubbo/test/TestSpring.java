package dubbo.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhidian.IpFinder;

/**
 * 
 * @描述: 启动Dubbo服务用的MainClass.
 * @版本: 1.0 .-
 */
public class TestSpring {
	
	private static final Log log = LogFactory.getLog(TestSpring.class);

	public static void main(String[] args) {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
			context.start();
		} catch (Exception e) {
			log.error("== DubboProvider context start error:",e);
		}
	}
    
}