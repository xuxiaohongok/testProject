package com.zhidian3g.dsp.adPostback.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static long decryptWinPrice(String enc_price, String secretKey,
			String signKey) {
		byte[] bytes = Decrypter.decryptSafeWebStringToByte(enc_price,
				secretKey, signKey);
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				bytes));
		long value = 0;
		try {
			value = dis.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void main(String args[]) throws Exception {
		
		Scanner input = new Scanner(System.in);
		System.out.print("密钥：");
		String encKeyStr = input.next();
		System.out.print("积分签名：");
		String signKeyStr = input.next();
        
        System.out.print("加密后的价格为：");
        String price1 = input.next();
		long price2 = decryptWinPrice(price1,encKeyStr, signKeyStr);
		System.out.println("解密后的价格为：" + price2);
	}
}
