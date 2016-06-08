package com.zhidian3g.dsp.adPostback.constant;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @描述: 启动Dubbo服务用的MainClass.
 * @版本: 1.0 .
 */
public class SpringTest {
	private static final Log log = LogFactory.getLog(SpringTest.class);
	public static void main(String[] args) {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context.xml");
			context.start();
		} catch (Exception e) {
			log.error("== SpringTest context start error:",e);
		}
		
		synchronized (SpringTest.class) {
			while (true) {
				try {
					SpringTest.class.wait();
				} catch (InterruptedException e) {
					log.error("== synchronized error:",e);
				}
			}
		}
	}
    
}