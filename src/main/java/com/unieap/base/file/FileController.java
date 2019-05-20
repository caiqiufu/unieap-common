package com.unieap.base.file;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
import com.unieap.base.file.bo.ExcelBO;
import com.unieap.base.file.bo.FileBO;
import com.unieap.base.file.bo.TxtBO;
import com.unieap.base.pojo.MdmExclog;
import com.unieap.base.pojo.MdmFileArchive;
import com.unieap.base.repository.ExcLogRespository;

@Controller
@RequestMapping("fileController.do")
public class FileController {
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


	@RequestMapping(params = "method=upload")
	public @ResponseBody Map<String, String> upload(String handlerName, String parameters, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<FileItem> items = fileBO.getFileItems(request);
		List<MdmFileArchive> files = fileBO.upload(parameters, items);
		return fileBO.fileHandle(parameters, handlerName, files);
	}

	@RequestMapping(params = "method=download")
	public @ResponseBody Map<String, String> download(String fileId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return fileBO.downLoad(new Long(fileId), request, response);
	}

	@RequestMapping(params = "method=uploadExcel")
	public @ResponseBody Map<String, String> uploadExcel(String parameters, String handlerName,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FileItem> items = fileBO.getFileItems(request);
		List<MdmFileArchive> files = fileBO.upload(parameters, items);
		ExcelBO excelBO = (ExcelBO) ApplicationContextProvider.getBean("excelBO");
		Map<String, String> model = excelBO.importExcel(parameters, handlerName, files);
		model.put(UnieapConstants.SUCCESS, UnieapConstants.SUCCESS);
		return model;
	}

	@RequestMapping(params = "method=uploadTxt")
	public @ResponseBody Map<String, String> uploadTxt(String parameters, String handlerName,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		TxtBO txtBO = (TxtBO) ApplicationContextProvider.getBean("txtBO");
		List<FileItem> items = fileBO.getFileItems(request);
		List<MdmFileArchive> files = fileBO.upload(parameters, items);
		Map<String, String> model = txtBO.importTxt(parameters, handlerName, files);
		model.put(UnieapConstants.SUCCESS, UnieapConstants.SUCCESS);
		return model;
	}

	@RequestMapping(params = "method=exportExcel")
	public @ResponseBody Map<String, String> exportExcel(String parameters, String handlerName,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ExcelBO excelBO = (ExcelBO) ApplicationContextProvider.getBean("excelBO");
		return excelBO.exportExcel(parameters, handlerName, request, response);
	}

	@RequestMapping(params = "method=svgServer")
	public @ResponseBody void svgServer(String svg, String type, String handlerName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String filename = new Date().toLocaleString().replace(" ", "_") + "." + type.substring(6);
		response.setContentType(type);
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
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
