package com.zhidian.base.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SolrAdBaseTask extends RecursiveTask<List<Long>> {

    /**
	 */
	private static final long serialVersionUID = 1L;
	private Object object;
    public SolrAdBaseTask(Object object) {
    	
    }

	@Override
	protected List<Long> compute() {
		List<Long> list = new ArrayList<Long>();
		list.add(1l);
		return list;
	}

}

