package com.unieap.base.file.bo;

import java.util.Date;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.unieap.base.file.vo.ExcelVO;
import com.unieap.base.pojo.MdmFileArchive;


public abstract class ExcelHandler {
	public static String FILE_NAME = "0";
	public static String SHEETS = "1";
	public ExcelVO getExportData(Map<String,Object> parameters){
		return null;
	}
	public Map<String,Object> importData(Map<String,Object> parameters,MdmFileArchive fileArchive,XSSFWorkbook workbook){
		return null;
	}
	@SuppressWarnings("deprecation")
	public String getFileName(){
		String filename = new Date().toLocaleString().replace(" ","_")+".xlsx";
		return filename;
	}
}
