package com.unieap.base.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.sf.json.JSONObject;

public class PTUtils {
	/***
	 * ping操作
	 * 
	 * @param hostname
	 * @param timeout  in milliseconds
	 * @return
	 */
	public static JSONObject pingResult(String hostname, Integer timeout) {
		JSONObject result = new JSONObject();
		try {
			InetAddress address = InetAddress.getByName(hostname);
			boolean flag = address.isReachable(timeout);
			result.put("flag", flag);
		} catch (IOException e) {
			result.put("message", e.getMessage());
		}
		return result;
	}

	/***
	 * telnet 操作
	 * 
	 * @param hostname
	 * @param timeout  in milliseconds
	 * @return
	 */
	public static JSONObject telnetResult(String hostname, Integer port, Integer timeout) {
		JSONObject result = new JSONObject();
		try {
			Socket server = new Socket();
			InetSocketAddress address = new InetSocketAddress(hostname, port);
			server.connect(address, timeout);
			server.close();
			result.put("flag", 0);
			result.put("message", "telnet:success.");
		} catch (IOException e) {
			result.put("message", e.getMessage());
		}
		return result;
	}
}
