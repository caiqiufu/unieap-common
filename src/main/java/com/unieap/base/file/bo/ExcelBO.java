package com.unieap.base.file.bo;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.UnieapConstants;
import com.unieap.base.file.vo.CellVO;
import com.unieap.base.file.vo.ExcelVO;
import com.unieap.base.file.vo.SheetVO;
import com.unieap.base.pojo.MdmFileArchive;

import net.sf.json.JSONObject;

@Service
public class ExcelBO extends FileHandler {
	/**
	 * 
	 * @param parameters
	 * @param handlerName
	 * @return
	 * @throws Exception
	 */
	public MdmFileArchive exportExcel(JSONObject parameters, String handlerName) throws Exception {
		ExcelHandler hanlder = (ExcelHandler) ApplicationContextProvider.getBean(handlerName);
		ExcelVO excelVO = hanlder.getExportData(parameters);
		List<SheetVO> sheets = excelVO.getSheets();
		if (sheets != null && sheets.size() > 0) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			for (int i = 0; i < sheets.size(); i++) {
				XSSFSheet sheet = workbook.createSheet();
				SheetVO svo = sheets.get(i);
				workbook.setSheetName(i, svo.getName());
				List<List<CellVO>> rows = svo.getDatas();
				int startNumber = 0;
				if (svo.getTitle() != null) {
					setHeadTitle(workbook, sheet, svo);
					startNumber = 1;
				}
				if (rows != null && rows.size() > 0) {
					XSSFCellStyle contextStyle = getContextStyle(workbook);
					for (int j = 0; j < rows.size(); j++) {
						XSSFRow row = sheet.createRow(j + startNumber);
						List<CellVO> cells = rows.get(j);
						if (cells != null && cells.size() > 0) {
							for (int k = 0; k < cells.size(); k++) {
								CellVO vo = cells.get(k);
								XSSFCell cell = row.createCell(k);
								cell.setCellStyle(contextStyle);
								cell.setCellValue(vo.getCellValue());
							}
						}
					}
				}

			}
			return this.upload(parameters, workbook, excelVO.getFileName());
		}
		return null;
	}

	public void setHeadTitle(XSSFWorkbook workbook, XSSFSheet sheet, SheetVO svo) {
		XSSFCellStyle headStyle = getHeadStyle(workbook);
		// 构建表头
		XSSFRow headRow = sheet.createRow(0);
		XSSFCell cell = null;
		String[] title = svo.getTitle();
		for (int i = 0; i < title.length; i++) {
			cell = headRow.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(title[i]);
		}
	}

	/**
	 * 设置表头的单元格样式
	 * 
	 * @return
	 */
	public XSSFCellStyle getHeadStyle(XSSFWorkbook workbook) {
		// 创建单元格样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		// 标题居中，没有边框，所以这里没有设置边框，设置标题文字样式
		XSSFFont titleFont = workbook.createFont();
		titleFont.setBold(true);// 加粗
		titleFont.setFontHeight((short) 15);// 文字尺寸
		titleFont.setFontHeightInPoints((short) 15);
		style.setFont(titleFont);
		return style;
	}

	/**
	 * 设置表体的单元格样式
	 * 
	 * @return
	 */
	public XSSFCellStyle getContextStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);// 文本水平居中显示
		style.setVerticalAlignment(VerticalAlignment.CENTER);// 文本竖直居中显示
		style.setWrapText(true);// 文本自动换行

		// 生成Excel表单，需要给文本添加边框样式和颜色
		/*
		 * CellStyle.BORDER_DOUBLE 双边线 CellStyle.BORDER_THIN 细边线 CellStyle.BORDER_MEDIUM
		 * 中等边线 CellStyle.BORDER_DASHED 虚线边线 CellStyle.BORDER_HAIR 小圆点虚线边线
		 * CellStyle.BORDER_THICK 粗边线
		 */
		style.setBorderBottom(BorderStyle.THIN);// 设置文本边框
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		// set border color
		style.setTopBorderColor(new XSSFColor(Color.BLACK));// 设置文本边框颜色
		style.setBottomBorderColor(new XSSFColor(Color.BLACK));
		style.setLeftBorderColor(new XSSFColor(Color.BLACK));
		style.setRightBorderColor(new XSSFColor(Color.BLACK));

		return style;
	}

	/**
	 * 
	 * @param parameters
	 * @param handlerName
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> importExcel(JSONObject parameters, String handlerName) throws Exception {
		ExcelHandler hanlder = (ExcelHandler) ApplicationContextProvider.getBean(handlerName);
		MdmFileArchive fileArchive = this.getFileArchive(Long.parseLong(parameters.getString("fileId")));
		String filePath = fileArchive.getFilePath() + File.separator + fileArchive.getFileName();
		FileInputStream fs = new FileInputStream(new File(filePath));
		XSSFWorkbook workbook = new XSSFWorkbook(fs);
		hanlder.importData(parameters, fileArchive, workbook);
		return this.result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
	}
}
