package com.unieap.base.file.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.UnieapConstants;
import com.unieap.base.pojo.MdmFileArchive;

import net.sf.json.JSONObject;

@Service
public class TxtBO extends FileHandler {

	/**
	 * 
	 * @param parameters
	 * @param handlerName
	 * @return
	 * @throws Exception
	 */
	public MdmFileArchive exportTxt(JSONObject parameters, String handlerName) throws Exception {
		return null;
	}

	/**
	 * 
	 * @param parameters
	 * @param handlerName
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> importTxt(JSONObject parameters, String handlerName) throws Exception {
		TxtHandler hanlder = (TxtHandler) ApplicationContextProvider.getBean(handlerName);
		MdmFileArchive fileArchive = this.getFileArchive(Long.parseLong(parameters.getString("fileId")));
		String filePath = fileArchive.getFilePath() + File.separator + fileArchive.getFileName();
		List<String> records = readSmallTxtFile(filePath);
		hanlder.importData(parameters, fileArchive, records);
		return this.result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
	}

	public List<String> readSmallTxtFile(String filePath) throws Exception {
		List<String> results = new ArrayList<String>();
		FileInputStream fs = new FileInputStream(new File(filePath));
		InputStreamReader reader = new InputStreamReader(fs);
		BufferedReader bufferedReader = new BufferedReader(reader);
		try {
			String read = null;
			while ((read = bufferedReader.readLine()) != null) {
				results.add(read);
			}
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return results;
	}

	public boolean writeTxtFile(String content, String dir, String fileName) throws Exception {
		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(fileName);
			o.write(content.getBytes("UTF-8"));
			o.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
		return flag;
	}
}
