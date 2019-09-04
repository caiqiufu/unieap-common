package com.unieap.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.unieap.base.dos.StreamManage;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		Process p;
		try {
			/*List<String> cmdList = new ArrayList<String>();
			cmdList.add("cmd");
            cmdList.add("/c");
            cmdList.add("start unieap-esb-20190721-1.0-SNAPSHOT-8100 -Xms128m -Xmx128m -jar E:\\OneDrive\\13-deploy\\winsw-winsw-2.2.0\\unieap-esb-20190721-1.0-SNAPSHOT.jar --server.port=8100");
            //cmdList.add("start javawunieapesb201907218100 -Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -server -Xms128m -Xmx128m -jar E:\\OneDrive\\13-deploy\\winsw-winsw-2.2.0\\unieap-esb-20190721-1.0-SNAPSHOT.jar --server.port=8100");
			ProcessBuilder pb = new ProcessBuilder();
			pb.command(cmdList);
			p = pb.start();

			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			String javawFilePath = "";
			while ((line = reader.readLine()) != null) {
				javawFilePath = line+File.separator+"bin"+File.separator+"javaw.exe";
			}
			System.out.println("javawFilePath="+javawFilePath);*/
			Process proc = Runtime.getRuntime().exec("cmd /k start esb-start.bat",null,new File("E:\\OneDrive\\13-deploy\\winsw-winsw-2.2.0"));
			StreamManage errorStream = new StreamManage(proc.getErrorStream(), "Error");
			StreamManage outputStream  = new StreamManage(proc.getInputStream(), "Output");
			errorStream.start();
			outputStream.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
