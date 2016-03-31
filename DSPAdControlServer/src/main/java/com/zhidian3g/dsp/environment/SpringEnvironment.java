package com.zhidian3g.dsp.environment;

import org.springframework.context.ApplicationContext;

public class SpringEnvironment {
	private static ApplicationContext applicationContext;
	public static ApplicationContext getContext() {
		return applicationContext;
	}

	public void setContext(ApplicationContext applicationContext) {
		System.out.println("===========初始化系统环境=======");
		SpringEnvironment.applicationContext = applicationContext;
	}
}
