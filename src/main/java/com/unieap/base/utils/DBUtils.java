package com.unieap.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

public class DBUtils {
	/**
	 * 将blob转化为byte[],可以转化二进制流的
	 * 
	 * @param blob
	 * @return
	 */
	public static byte[] blobToBytes(Blob blob) {
		InputStream is = null;
		byte[] b = null;
		try {
			is = blob.getBinaryStream();
			b = new byte[(int) blob.length()];
			is.read(b);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}
}
