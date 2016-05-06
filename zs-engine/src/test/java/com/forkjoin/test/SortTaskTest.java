package com.forkjoin.test;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;

/**
 * Author: zhongxin
 * Created at: 2010-4-14 14:13:12
 */
public class SortTaskTest {
    private static final int SIZE = 10000;

    @Test
    public void run() throws InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Random rnd = new Random();
        long[] array = new long[SIZE];
        for (int i = 0; i < SIZE; i++) {
            array[i] = rnd.nextInt();
        }
        forkJoinPool.submit(new SortTask(array));

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1000, TimeUnit.SECONDS);

        for (int i = 1; i < SIZE; i++) {
            assertTrue(array[i - 1] < array[i]);
        }
    }

}
