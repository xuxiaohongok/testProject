package com.zhidian.common.util;

import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.beans.BeanCopier;

public class CachedBeanCopier {
	static final Map<String, BeanCopier> BEAN_COPIERS = new HashMap<String, BeanCopier>();

	public static void copy(Object srcObj, Object destObj) {
		String key = genKey(srcObj.getClass(), destObj.getClass());
		BeanCopier copier = null;
		if (!BEAN_COPIERS.containsKey(key)) {
			copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
			BEAN_COPIERS.put(key, copier);
		} else {
			copier = BEAN_COPIERS.get(key);
		}
		copier.copy(srcObj, destObj, null);
	}

	private static String genKey(Class<?> srcClazz, Class<?> destClazz) {
		return srcClazz.getName() + destClazz.getName();
	}
}
