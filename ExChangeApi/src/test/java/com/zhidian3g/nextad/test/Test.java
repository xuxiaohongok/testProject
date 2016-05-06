package com.zhidian3g.nextad.test;


public class Test {
	 private int currentIndex = 0;// 上一次选择的服务器
	 public int getIndex(int size) {
		 currentIndex = (currentIndex + 1) % size;
		 return currentIndex;
	 }
	 
	 public static void main(String[] args) {
		 Test test = new Test();
	}
}
