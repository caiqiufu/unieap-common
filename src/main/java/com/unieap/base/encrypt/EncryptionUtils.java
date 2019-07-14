package com.unieap.base.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

public class EncryptionUtils {
	public static void main(String[] args) throws Exception {
		String sKey = "1234567890111110";
		String sSrc = "Abc1234%";
		String enString = encryptSunJCE(sSrc, sKey);
		System.out.println("enSunJCEString=" + enString);
		String deString = decryptSunJCE(enString, sKey);
		System.out.println("deSunJCEString=" + deString);

		String pinSrc = "11122345678877765554";

		String mKey = "1234567890543210";
		String menString = encryptSun(pinSrc, mKey);
		System.out.println("enSunString=" + menString);
		String mdeString = decryptSun(menString, mKey);
		System.out.println("deSunString=" + mdeString);
	}

	public static String encryptSunJCE(String sSrc, String sKey) throws Exception {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		if (StringUtils.isEmpty(sKey)) {
			return null;
		}
		if (sKey.length() != 16) {
			return null;
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return new Base64().encodeToString(encrypted);

	}

	public static String decryptSunJCE(String sSrc, String sKey) throws Exception {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		if (sKey == null) {
			return null;
		}
		if (sKey.length() != 16) {
			return null;
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
		byte[] encrypted64 = new Base64().decode(sSrc);
		byte[] original = cipher.doFinal(encrypted64);
		String originalString = new String(original, "utf-8");
		return originalString;

	}

	public static String encryptSun(String sSrc, String sKey) throws Exception {
		Security.addProvider(new sun.security.provider.Sun());
		if (StringUtils.isEmpty(sKey)) {
			return null;
		}
		if (sKey.length() != 16) {
			return null;
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return new Base64().encodeToString(encrypted);

	}

	public static String decryptSun(String sSrc, String sKey) throws Exception {
		Security.addProvider(new sun.security.provider.Sun());
		if (sKey == null) {
			return null;
		}
		if (sKey.length() != 16) {
			return null;
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
		byte[] encrypted64 = new Base64().decode(sSrc);
		byte[] original = cipher.doFinal(encrypted64);
		String originalString = new String(original, "utf-8");
		return originalString;

	}

	/**
	 * generate SHA String
	 * @param decript
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static String SHA1(String decript) throws NoSuchAlgorithmException {
		if(StringUtils.isEmpty(decript)){
			return null;
		}
		MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
		digest.update(decript.getBytes());
		byte messageDigest[] = digest.digest();
		// Create Hex String
		StringBuffer hexString = new StringBuffer();
		// 字节数组转换为 十六进制 数
		for (int i = 0; i < messageDigest.length; i++) {
			String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		return hexString.toString();
	}

	public static String SHA(String decript) throws NoSuchAlgorithmException {
		if(StringUtils.isEmpty(decript)){
			return null;
		}
		MessageDigest digest = java.security.MessageDigest.getInstance("SHA");
		digest.update(decript.getBytes());
		byte messageDigest[] = digest.digest();
		// Create Hex String
		StringBuffer hexString = new StringBuffer();
		// 字节数组转换为 十六进制 数
		for (int i = 0; i < messageDigest.length; i++) {
			String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		return hexString.toString();
	}

	public static String MD5(String input) throws NoSuchAlgorithmException {
		if(StringUtils.isEmpty(input)){
			return null;
		}
		// 获得MD5摘要算法的 MessageDigest 对象
		MessageDigest mdInst = MessageDigest.getInstance("MD5");
		// 使用指定的字节更新摘要
		mdInst.update(input.getBytes());
		// 获得密文
		byte[] md = mdInst.digest();
		// 把密文转换成十六进制的字符串形式
		StringBuffer hexString = new StringBuffer();
		// 字节数组转换为 十六进制 数
		for (int i = 0; i < md.length; i++) {
			String shaHex = Integer.toHexString(md[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		return hexString.toString();
	}
}
