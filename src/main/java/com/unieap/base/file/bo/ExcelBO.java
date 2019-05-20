package com.unieap.base.file.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
import com.unieap.base.utils.JSONUtils;

@Service
public class ExcelBO extends FileHandler {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> exportExcel(String parameters, String handlerName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map paras = JSONUtils.jsonToMap(parameters);
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExcelHandler hanlder = (ExcelHandler) ApplicationContextProvider.getBean(handlerName);
		ExcelVO excelVO = hanlder.getExportData(paras);
		String fileName = excelVO.getFileName();
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
		List<SheetVO> sheets = excelVO.getSheets();
		if (sheets != null && sheets.size() > 0) {
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
					XSSFCellStyle bodyStyle = getBodyStyle(workbook);
					for (int j = 0; j < rows.size(); j++) {
						XSSFRow row = sheet.createRow(j + startNumber);
						List<CellVO> cells = rows.get(j);
						if (cells != null && cells.size() > 0) {
							for (int k = 0; k < cells.size(); k++) {
								CellVO vo = cells.get(k);
								XSSFCell cell = row.createCell(k);
								cell.setCellStyle(bodyStyle);
								cell.setCellType(XSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(vo.getCellValue());
							}
						}
					}
				}

			}
			OutputStream os = null;
			try {
				os = response.getOutputStream();
				workbook.write(os);
				os.flush();
				os.close();
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		return result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
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
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		return cellStyle;
	}

	/**
	 * 设置表体的单元格样式
	 * 
	 * @return
	 */
	public XSSFCellStyle getBodyStyle(XSSFWorkbook workbook) {
		// 创建单元格样式
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		return cellStyle;
	}

	/**
	 * only support office 2007
	 * 
	 * @param parameters
	 * @param handlerConfig
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> importExcel(String parameters, String handlerName, List<MdmFileArchive> files)
			throws Exception {
		Map paras = JSONUtils.jsonToMap(parameters);
		if (files != null && files.size() > 0) {
			ExcelHandler hanlder = (ExcelHandler) ApplicationContextProvider.getBean(handlerName);
			for (int i = 0; i < files.size(); i++) {
				MdmFileArchive fileArchive = this.getFileArchive(files.get(i).getId());
				String filePath = fileArchive.getFilePath() + File.separator + fileArchive.getFileName();
				FileInputStream fs = new FileInputStream(new File(filePath));
				XSSFWorkbook workbook = new XSSFWorkbook(fs);
				hanlder.importData(paras, fileArchive, workbook);
			}
		}
		return this.result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
		// XSSFWorkbook workbook = new
		// XSSFWorkbook(vo.getFile().getInputStream());
		/*
		 * try { book = new XSSFWorkbook(vo.getFile().getInputStream()); } catch
		 * (Exception ex) { book = new
		 * HSSFWorkbook(vo.getFile().getInputStream()); }
		 */
		// HSSFWorkbook workbook = new
		// HSSFWorkbook(vo.getFile().getInputStream());
		// Map exDatas = hanlder.importData(paras, workbook);
		// exDatas.put(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
		// return exDatas;
	}
}
