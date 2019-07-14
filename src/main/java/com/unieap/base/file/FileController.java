package com.unieap.base.file;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.UnieapConstants;
import com.unieap.base.controller.BaseController;
import com.unieap.base.file.bo.ExcelBO;
import com.unieap.base.file.bo.FileBO;
import com.unieap.base.file.bo.TxtBO;
import com.unieap.base.pojo.MdmExclog;
import com.unieap.base.pojo.MdmFileArchive;
import com.unieap.base.repository.ExcLogRespository;
import com.unieap.base.utils.JSONUtils;

import net.sf.json.JSONObject;

@Controller
public class FileController extends BaseController {
	@Autowired
	ExcLogRespository excLogRespository;

	@ExceptionHandler
	public String exp(HttpServletRequest request, Exception ex) {
		request.setAttribute("ex", ex);
		MdmExclog log = new MdmExclog();
		log.setId(UnieapConstants.getSequence());
		log.setBizModule("file");
		log.setExType("system_exception");
		log.setExCode("");
		log.setExInfo(ex.getLocalizedMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		try {
			log.setExTracking(sw.toString().getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			StringWriter sw1 = new StringWriter();
			PrintWriter pw1 = new PrintWriter(sw);
			e.printStackTrace(pw1);
			log.setExTracking(sw1.toString().getBytes());
		}
		if (UnieapConstants.getUser() != null) {
			log.setOperatorName(UnieapConstants.getUser().getUserCode());
		} else {
			log.setOperatorName("system error");
		}
		log.setOperationDate(UnieapConstants.getDateTime());
		excLogRespository.save(log);
		return "error";
	}

	@Autowired
	public FileBO fileBO;
	@Autowired
	public ExcelBO excelBO;

	@RequestMapping("fileController/imageDisplay")
	public Map<String, String> imageDisplay(String fileId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return fileBO.downLoad(new Long(fileId), request, response);
	}

	@RequestMapping("fileController/upload")
	public @ResponseBody Map<String, String> upload(String handlerName, String parameters, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<FileItem> items = fileBO.getFileItems(request);
		List<MdmFileArchive> files = fileBO.upload(JSONUtils.jsonToMap(parameters), items);
		return fileBO.fileHandle(JSONObject.fromObject(parameters), handlerName, files);
	}

	@RequestMapping("fileController/download")
	public @ResponseBody Map<String, String> download(String fileId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> model = fileBO.downLoad(new Long(fileId), request, response);
		model.put(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
		model.put(UnieapConstants.SUCCESS, UnieapConstants.SUCCESS);
		return model;
	}

	@RequestMapping("fileController/uploadExcel")
	public @ResponseBody Map<String, String> uploadExcel(String parameters, String handlerName,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FileItem> items = fileBO.getFileItems(request);
		List<MdmFileArchive> files = fileBO.upload(JSONUtils.jsonToMap(parameters), items);
		JSONObject para = JSONObject.fromObject(parameters);
		if (files != null) {
			List<String> ids = new ArrayList<String>();
			for (MdmFileArchive f : files) {
				ids.add(f.getId().toString());
			}
			para.put("fileId", ids);
		}
		Map<String, String> model = excelBO.importExcel(para, handlerName);
		model.put(UnieapConstants.SUCCESS, UnieapConstants.SUCCESS);
		return model;
	}

	@RequestMapping("fileController/uploadTxt")
	public @ResponseBody Map<String, String> uploadTxt(String parameters, String handlerName,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		TxtBO txtBO = (TxtBO) ApplicationContextProvider.getBean("txtBO");
		List<FileItem> items = fileBO.getFileItems(request);
		List<MdmFileArchive> files = fileBO.upload(JSONUtils.jsonToMap(parameters), items);
		JSONObject para = JSONObject.fromObject(parameters);
		if (files != null) {
			List<String> ids = new ArrayList<String>();
			for (MdmFileArchive f : files) {
				ids.add(f.getId().toString());
			}
			para.put("fileId", ids);
		}
		Map<String, String> model = txtBO.importTxt(para, handlerName);
		model.put(UnieapConstants.SUCCESS, UnieapConstants.SUCCESS);
		return model;
	}

	@RequestMapping("fileController/exportExcel")
	public @ResponseBody Map<String, String> exportExcel(String parameters, String handlerName,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		MdmFileArchive mdmFileArchive = excelBO.exportExcel(JSONObject.fromObject(parameters), handlerName);
		Map<String, String> model = fileBO.downLoad(mdmFileArchive.getId(), request, response);
		model.put(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
		model.put(UnieapConstants.SUCCESS, UnieapConstants.SUCCESS);
		return model;
	}

	/**
	 * 保存图片
	 * 
	 * @param svg
	 * @param type
	 * @param handlerName
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("fileController/svgServer")
	public @ResponseBody void svgServer(String svg, String type, String handlerName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String fileName = formatter.format(currentTime) + "." + type.substring(6);
		response.setContentType(type);
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		JPEGTranscoder t = new JPEGTranscoder();
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
		TranscoderInput input = new TranscoderInput(new StringReader(svg));
		try {
			TranscoderOutput output = new TranscoderOutput(response.getOutputStream());
			t.transcode(input, output);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			response.getOutputStream().close();
			e.printStackTrace();
		} finally {
			response.getOutputStream().close();
		}
	}
}
