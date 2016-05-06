package com.forkjoin.test;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertEquals;

/**
 * Author: zhongxin
 * Created at: 2010-4-14 13:40:58
 */
public class CalculatorTest { 
	private int count = 1000000;
	
    @Test
    public void run() throws Exception{
    	ForkJoinPool forkJoinPool = new ForkJoinPool();
    	long startTime = System.currentTimeMillis();
        for(int i=0;i<100;i++) {
        	Future<Integer> result = forkJoinPool.submit(new Calculator(0, count));
        	System.out.println(result.get());
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
    
    @Test
    public void run1() throws Exception{
    	long startTime = System.currentTimeMillis();
        for(int i=0;i<100;i++) {
        	 int sum = 0;
             for(int j = 0; j < count;j++){
                 sum += j;
             }
             System.out.println(sum);
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }


}




