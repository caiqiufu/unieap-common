package com.unieap.base.file.bo;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import com.unieap.base.file.vo.ExcelVO;
import com.unieap.base.pojo.MdmFileArchive;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public abstract class ExcelHandler extends FileHandler {
	public static String FILE_NAME = "0";
	public static String SHEETS = "1";

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public ExcelVO getExportData(JSONObject parameters) throws Exception {
		return null;
	}

	/**
	 * 
	 * @param parameters
	 * @param fileArchive
	 * @param workbook
	 * @return
	 */
	public Map<String, Object> importData(JSONObject parameters, MdmFileArchive fileArchive, XSSFWorkbook workbook) throws Exception {
		return null;
	}

	public JSONArray readExcelToJson(InputStream inputStream, String sheetName) {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(inputStream);
		} catch (Exception e) {
			System.out.println("Excel data file cannot be found!");
		}
		return readExcelToJson(workbook, sheetName);
	}

	public JSONArray readExcelToJson(XSSFWorkbook workbook, String sheetName) {
		XSSFSheet xssfSheet;
		if (StringUtils.isEmpty(sheetName)) {
			xssfSheet = workbook.getSheetAt(0);
		} else {
			xssfSheet = workbook.getSheet(sheetName);
		}
		JSONArray ja = new JSONArray();
		XSSFRow titleRow = xssfSheet.getRow(0);
		for (int rowIndex = 1; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
			if (xssfRow == null) {
				continue;
			}
			JSONObject jsonObj = new JSONObject();
			for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
				XSSFCell titleCell = titleRow.getCell(cellIndex);
				XSSFCell xssfCell = xssfRow.getCell(cellIndex);
				jsonObj.put(getString(titleCell), getString(xssfCell));
			}
			ja.add(jsonObj);
		}
		return ja;
	}

	/**
	 * get cell data
	 * 
	 * @param xssfCell 单元格
	 * @return 字符串
	 */
	public String getString(XSSFCell xssfCell) {
		if (xssfCell == null) {
			return "";
		}
		if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
			BigDecimal bigDecimal = new BigDecimal(xssfCell.getNumericCellValue());// 创建BigDecimal对象，把科学计数转成数字
			return bigDecimal.toPlainString();// 转成最终要的数字字符串
		} else if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else {
			return xssfCell.getStringCellValue();
		}
	}
}
