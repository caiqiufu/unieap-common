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
import com.unieap.base.utils.JSONUtils;

@Service("txtBO")
public class TxtBO extends FileBO {

	public Map<String, String> importTxt(String parameters, String handlerName, List<MdmFileArchive> files)
			throws Exception {
		Map paras = JSONUtils.jsonToMap(parameters);
		if (files != null && files.size() > 0) {
			TxtHandler hanlder = (TxtHandler) ApplicationContextProvider.getBean(handlerName);
			for (int i = 0; i < files.size(); i++) {
				MdmFileArchive fileArchive = this.getFileArchive(files.get(i).getId());
				String filePath = fileArchive.getFilePath() + File.separator + fileArchive.getFileName();
				List<String> records = readSmallTxtFile(filePath);
				hanlder.importData(paras,fileArchive, records);
			}
		}
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
		File f = new File(dir, fileName);
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
