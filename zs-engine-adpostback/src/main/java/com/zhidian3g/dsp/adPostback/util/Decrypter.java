package com.zhidian3g.dsp.adPostback.util;

// Copyright 2009 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// Decrypter is sample code showing the steps to decrypt and verify 64-bit
// values. It uses the Base 64 decoder from the Apache commons project
// (http://commons.apache.org).

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * An Exception thrown by Decrypter if the ciphertext cannot successfully be
 * decrypted.
 */
class DecrypterException extends Exception {

	private static final long serialVersionUID = 7232230637730180806L;

	public DecrypterException(String message) {
		super(message);
	}
}

/**
 * Java language sample code for 64 bit value decryption
 */
public class Decrypter {
	/** The length of the initialization vector */
	private static final int INITIALIZATION_VECTOR_SIZE = 16;
	/** The length of the signature */
	private static final int SIGNATURE_SIZE = 4;
	/** The fixed block size for the block cipher */
	private static final int BLOCK_SIZE = 20;

	/**
	 * Converts from unpadded web safe base 64 encoding (RFC 3548) to standard
	 * base 64 encoding (RFC 2045) and pads the result.
	 */
	public static String unWebSafeAndPad(String webSafe) {
		String pad = "";
		if ((webSafe.length() % 4) == 2) {
			pad = "==";
		} else if ((webSafe.length() % 4) == 3) {
			pad = "=";
		}
		return webSafe.replace('-', '+').replace('_', '/') + pad;
	}

	public static String decryptSafeWebString(String webString,
			String secretKey, String signKey) {
		byte[] bytes = decryptSafeWebStringToByte(webString, secretKey, signKey);
		String retStr = null;
		try {
			retStr = new String(bytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retStr;
	}

	public static byte[] decryptSafeWebStringToByte(String webString,
			String secretKey, String signKey) {
		if (32 != secretKey.getBytes().length)
			return null;
		if (32 != signKey.getBytes().length)
			return null;

		SecretKey encryptionKey = new SecretKeySpec(secretKey.getBytes(),
				"HmacSHA1");
		SecretKey integrityKey = new SecretKeySpec(signKey.getBytes(),
				"HmacSHA1");
		byte[] retStr = null;
		try {
			retStr = decryptSafeWebString(webString, encryptionKey,
					integrityKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retStr;
	}

	/**
	 * 解密Base64编码的安全字符串
	 * 
	 * @param webString
	 * @param encryptionKey
	 *            加密KEY
	 * @param integrityKey
	 *            签名KEY
	 * @return
	 * @throws DecrypterException
	 */
	public static byte[] decryptSafeWebString(String webString,
			SecretKey encryptionKey, SecretKey integrityKey)
			throws DecrypterException {
		String unSafeString = unWebSafeAndPad(webString);
		byte[] ciphertext = {};
		try {
			ciphertext = Base64.decodeBase64(unSafeString.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return decrypt(ciphertext, encryptionKey, integrityKey);
	}

	/**
	 * Performs the decryption algorithm.
	 *
	 * This method decrypts the ciphertext using the encryption key and verifies
	 * the integrity bits with the integrity key. The encrypted format is:
	 * {initialization_vector (16 bytes)}{ciphertext}{integrity (4 bytes)}
	 * https://developers.google.com/ad-exchange/rtb/response-guide/decrypt-
	 * hyperlocal,
	 * https://developers.google.com/ad-exchange/rtb/response-guide/decrypt
	 * -price and https://support.google.com/adxbuyer/answer/3221407?hl=en have
	 * more details about the encrypted format of hyperlocal, winning price,
	 * IDFA, hashed IDFA and Android Advertiser ID.
	 */
	public static byte[] decrypt(byte[] ciphertext, SecretKey encryptionKey,
			SecretKey integrityKey) throws DecrypterException {
		try {
			// Step 1. find the length of initialization vector and clear text.
			final int plaintext_length = ciphertext.length
					- INITIALIZATION_VECTOR_SIZE - SIGNATURE_SIZE;
			if (plaintext_length < 0) {
				throw new RuntimeException(
						"The plain text length can't be negative.");
			}

			byte[] iv = Arrays.copyOf(ciphertext, INITIALIZATION_VECTOR_SIZE);

			// Step 2. recover clear text
			final Mac hmacer = Mac.getInstance("HmacSHA1");
			hmacer.init(encryptionKey);

			final int ciphertext_end = INITIALIZATION_VECTOR_SIZE
					+ plaintext_length;
			final byte[] plaintext = new byte[plaintext_length];
			boolean add_iv_counter_byte = true;
			for (int ciphertext_begin = INITIALIZATION_VECTOR_SIZE, plaintext_begin = 0; ciphertext_begin < ciphertext_end;) {
				hmacer.reset();
				hmacer.init(encryptionKey);
				final byte[] pad = hmacer.doFinal(iv);

				int i = 0;
				while (i < BLOCK_SIZE && ciphertext_begin != ciphertext_end) {
					plaintext[plaintext_begin++] = (byte) (ciphertext[ciphertext_begin++] ^ pad[i++]);
				}

				if (!add_iv_counter_byte) {
					final int index = iv.length - 1;
					add_iv_counter_byte = ++iv[index] == 0;
				}

				if (add_iv_counter_byte) {
					add_iv_counter_byte = false;
					iv = Arrays.copyOf(iv, iv.length + 1);
				}
			}

			// Step 3. Compute integrity hash. The input to the HMAC is
			// clear_text
			// followed by initialization vector, which is stored in the 1st
			// section
			// or ciphertext.
			hmacer.reset();
			hmacer.init(integrityKey);
			hmacer.update(plaintext);
			hmacer.update(Arrays.copyOf(ciphertext, INITIALIZATION_VECTOR_SIZE));
			final byte[] computedSignature = Arrays.copyOf(hmacer.doFinal(),
					SIGNATURE_SIZE);
			final byte[] signature = Arrays.copyOfRange(ciphertext,
					ciphertext_end, ciphertext_end + SIGNATURE_SIZE);
			if (!Arrays.equals(signature, computedSignature)) {
				throw new DecrypterException("Signature mismatch.");
			}
			return plaintext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("HmacSHA1 not supported.", e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Key is invalid for this purpose.", e);
		}
	}

}
