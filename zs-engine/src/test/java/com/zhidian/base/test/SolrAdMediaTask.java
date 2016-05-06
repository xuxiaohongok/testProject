package com.zhidian.base.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SolrAdMediaTask extends RecursiveTask<List<Integer>> {

    /**
	 */
	private static final long serialVersionUID = 1L;
    public SolrAdMediaTask(Object object) {
    	
    }

	@Override
	protected List<Integer> compute() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		return list;
	}
	
	public static void main(String[] args) {
		new SolrAdMediaTask(null).fork();
	}

}

