package com.zhidian3g.nextad.test;

import org.junit.Test;

import com.zhidian3g.common.utils.GZIP;

public class GzipTest {

	@Test
	public void testGip() {
		try {
			String ok = "sadgasdgjlj哦了空间哦爱上见到过=圣诞歌加速的改扩建========";
			String ok1 = GZIP.compress(ok);
			System.out.println("ok=" + GZIP.unCompress(ok1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
