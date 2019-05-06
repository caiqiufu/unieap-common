package com.unieap.base.file.bo;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapConstants;
import com.unieap.base.bo.BaseBO;
import com.unieap.base.pojo.MdmFileArchive;
import com.unieap.base.repository.MdmFileArchiveRepository;
import com.unieap.base.utils.JSONUtils;

@Service
public class FileBO extends BaseBO {

	public Map<String, String> fileHandle(String parameters, String handlerName, List<MdmFileArchive> files)
			throws Exception {
		return null;
	}

	public List<FileItem> getFileItems(HttpServletRequest request) {
		String tempPath = getRootPath() + File.separator + "buffer";
		File tempPathFile = new File(tempPath);
		if (!tempPathFile.exists()) {
			tempPathFile.mkdirs();
		}
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Set factory constraints
		factory.setSizeThreshold(Integer.parseInt(SYSConfig.getConfig().get("file.maxInMemorySize"))); // 设置缓冲区大小
		factory.setRepository(tempPathFile);// 设置缓冲区目录
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// Set overall request size constraint
		upload.setSizeMax(Integer.parseInt(SYSConfig.getConfig().get("file.maxUploadSize"))); // 设置最大文件尺寸
		List<FileItem> items;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			items = null;
		} // 得到所有的文件
		return items;
	}

	@Autowired
	MdmFileArchiveRepository mdmFileArchiveRepository;

	public MdmFileArchive getFileArchive(Long fileId) throws Exception {
		MdmFileArchive fileArchive = mdmFileArchiveRepository.getOne(fileId);
		if (fileArchive == null) {
			throw new Exception("file id[" + fileId + "] not existing");
		}
		return fileArchive;
	}

	/**
	 * 
	 * @param url
	 * @param folderPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public MdmFileArchive fileArchiveByChangeName(String url, String folderPath, String fileName) throws Exception {
		File oldfile = new File(folderPath + File.separator + fileName);
		String newFileName = fileName + ".bak";
		File newfile = new File(folderPath + File.separator + newFileName);
		if (newfile.exists())// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
			throw new Exception(newfile.getName() + " existing,please check");
		else {
			oldfile.renameTo(newfile);
			DecimalFormat df = new DecimalFormat(".00");
			String fileSize = df.format((double) newfile.length() / 1024);
			String fileType = StringUtils.split(fileName, ".")[1];
			MdmFileArchive fileArchive = new MdmFileArchive();
			fileArchive.setArchiveDate(UnieapConstants.getDateTime());
			fileArchive.setExtKey(fileName);
			fileArchive.setFileCategory("backup");
			fileArchive.setFileName(newFileName);
			fileArchive.setFilePath(folderPath);
			fileArchive.setFileSize(new Double(fileSize));
			fileArchive.setFileType(fileType);
			fileArchive.setId(getSequence(null, null));
			url = url + File.separator + newFileName;
			fileArchive.setUrl(url);
			mdmFileArchiveRepository.save(fileArchive);
			return fileArchive;
		}
	}

	/**
	 * 
	 * @param parameters:fileCategory,appName,extKey
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> upload(String parameters, List<FileItem> files) throws Exception {
		Map<?, ?> paras = JSONUtils.jsonToMap(parameters);
		String fileCategory = "common";
		if (paras.containsKey("fileCategory")) {
			fileCategory = paras.get("fileCategory").toString();
		}
		String appName = UnieapConstants.UNIEAP;
		if (paras.containsKey("appName")) {
			appName = paras.get("appName").toString();
		}
		String extKey = "-9999";
		if (paras.containsKey("extKey")) {
			appName = paras.get("extKey").toString();
		}
		String shareFolderPath = SYSConfig.getConfig().get("shareFolderPath") + File.separator + appName
				+ File.separator + "upload";
		String urlUploadPath = SYSConfig.getConfig().get("url.path") + File.separator + appName + File.separator
				+ "upload";
		return upload(fileCategory, extKey, files, shareFolderPath, urlUploadPath);
	}

	/**
	 * 
	 * @param fileCategory
	 * @param extKey
	 * @param files
	 * @param uploadPath
	 *            物理存储地址
	 * @param url
	 *            应用访问地址
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> upload(String fileCategory, String extKey, List<FileItem> files, String uploadPath,
			String url) throws Exception {
		List<MdmFileArchive> fileArchiveList = new ArrayList<MdmFileArchive>();
		if (files != null && files.size() > 0) {
			DecimalFormat df = new DecimalFormat(".00");
			for (int i = 0; i < files.size(); i++) {
				FileItem fileItem = files.get(i);
				String fileName = fileItem.getName();
				if (!StringUtils.isEmpty(fileName)) {
					String fileSize = df.format((double) fileItem.getSize() / 1024);
					String fileType = StringUtils.split(fileName, ".")[1];
					File uploadFile = new File(uploadPath);
					if (!uploadFile.exists()) {
						uploadFile.mkdirs();
					}
					File fullFile = new File(fileItem.getName());
					File savedFile = new File(uploadPath, fullFile.getName());
					fileItem.write(savedFile);
					MdmFileArchive fileArchive = new MdmFileArchive();
					fileArchiveList.add(fileArchive);
					fileArchive.setArchiveDate(UnieapConstants.getDateTime());
					fileArchive.setExtKey(extKey);
					fileArchive.setFileCategory(fileCategory);
					fileArchive.setFileName(fileName);
					fileArchive.setFilePath(uploadPath);
					fileArchive.setFileSize(new Double(fileSize));
					fileArchive.setFileType(fileType);
					fileArchive.setId(getSequence(null, null));
					url = url + File.separator + fileName;
					fileArchive.setUrl(url);
					fileArchive.setTenantId(UnieapConstants.getUser().getTenantId());
					// fileIds.append(fileArchive.getId().toString()).append(",");
					mdmFileArchiveRepository.save(fileArchive);
				}
			}
		}
		return fileArchiveList;

	}

	/**
	 * 
	 * @param fileCategory
	 * @param extKey
	 * @param fileItem
	 * @param uploadPath
	 * @param url
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> uploadByCutImage(String fileCategory, String extKey, List<FileItem> files,
			String uploadPath, String url, int x, int y, int width, int height) throws Exception {
		Rectangle rect = new java.awt.Rectangle(x, y, width, height);
		if (files != null && files.size() > 0) {
			List<MdmFileArchive> fileArchiveList = new ArrayList<MdmFileArchive>();
			DecimalFormat df = new DecimalFormat(".00");
			for (int i = 0; i < files.size(); i++) {
				FileItem fileItem = files.get(i);
				String fileName = fileItem.getName();
				FileOutputStream fos = new FileOutputStream(uploadPath + File.separator + fileName);
				// java.io.FileInputStream fis = null;
				InputStream is = null;
				ImageInputStream iis = null;
				try {
					is = fileItem.getInputStream();
					// ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg,
					// png, PNG,
					// JPEG, WBMP, GIF, gif]
					String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
					String suffix = null;
					// 获取图片后缀
					if (fileName.indexOf(".") > -1) {
						suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
					} // 类型和图片后缀全部小写，然后判断后缀是否合法
					if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase() + ",") < 0) {
						extracted(types);
					}
					// 将FileInputStream 转换为ImageInputStream
					iis = ImageIO.createImageInputStream(is);
					// 根据图片类型获取该种类型的ImageReader
					ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
					reader.setInput(iis, true);
					ImageReadParam param = reader.getDefaultReadParam();
					param.setSourceRegion(rect);
					BufferedImage bi = reader.read(0, param);
					ImageIO.write(bi, suffix, fos);
					String fileSize = df.format((double) fileItem.getSize() / 1024);
					String fileType = StringUtils.split(fileName, ".")[1];
					MdmFileArchive fileArchive = new MdmFileArchive();
					fileArchiveList.add(fileArchive);
					fileArchive.setArchiveDate(UnieapConstants.getDateTime());
					fileArchive.setExtKey(extKey);
					fileArchive.setFileCategory(fileCategory);
					fileArchive.setFileName(fileName);
					fileArchive.setFilePath(uploadPath);
					fileArchive.setFileSize(new Double(fileSize));
					fileArchive.setFileType(fileType);
					fileArchive.setId(getSequence(null, null));
					fileArchive.setTenantId(UnieapConstants.getUser().getTenantId());
					url = url + File.separator + fileName;
					fileArchive.setUrl(url);
					mdmFileArchiveRepository.save(fileArchive);
					return fileArchiveList;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (is != null) {
							is.close();
						}
						if (iis != null) {
							iis.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	private void extracted(String types) throws Exception {
		throw new Exception("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
	}

	/**
	 * 
	 * @param fileId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> downLoad(Long fileId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MdmFileArchive fileArchive = getFileArchive(fileId);
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileArchive.getFileName());
		response.setHeader("Content-Length", String.valueOf(fileArchive.getFileSize()));
		String filePath = fileArchive.getFilePath() + File.separator + fileArchive.getFileName();
		FileInputStream fs = null;
		OutputStream os = null;
		try {
			fs = new FileInputStream(new File(filePath));
			os = response.getOutputStream();
			byte[] b = new byte[1024];
			int length;
			while ((length = fs.read(b)) > 0) {
				os.write(b, 0, length);
			}
			os.flush();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fs != null) {
				fs.close();
			}
			if (os != null) {
				os.close();
			}
		}
		return result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
	}

	public Map<String, String> downloadByUrl(String url, String fileName, String fileSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
		response.setHeader("Content-Length", String.valueOf(fileSize));
		FileInputStream fs = null;
		OutputStream os = response.getOutputStream();
		try {
			URL httpUrl = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) httpUrl.openConnection();
			uc.setDoInput(true);// 设置是否要从 URL 连接读取数据,默认为true
			uc.connect();
			InputStream iputstream = uc.getInputStream();
			byte[] buffer = new byte[4 * 1024];
			int byteRead = -1;
			while ((byteRead = (iputstream.read(buffer))) != -1) {
				os.write(buffer, 0, byteRead);
			}
			/*
			 * fs = new FileInputStream(new File(url)); os =
			 * response.getOutputStream(); byte[] b = new byte[1024]; int
			 * length; while ((length = fs.read(b)) > 0) { os.write(b, 0,
			 * length); }
			 */
			os.flush();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fs != null) {
				fs.close();
			}
			if (os != null) {
				os.close();
			}
		}
		return result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
	}

	public String getRootPath() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF"));
		}
		return path;
		// return SYSConfig.rootPath+File.separator;
	}

	/**
	 * <p>
	 * 描述:创建文件
	 * </P>
	 * Jan 26, 2011
	 * 
	 * @param path
	 * @param isCover
	 * @param isEnter
	 * @param str
	 * @throws Exception
	 */
	public void write(String fileName, String uploadPath, boolean isCover, boolean isEnter, String str)
			throws Exception {
		create(uploadPath, fileName);
		File file = new File(uploadPath, fileName);
		OutputStreamWriter out = null;
		String feed = str;
		if (isEnter) {
			feed = feed + '\n';
		}
		try {
			out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			out.write(feed);
			out.flush();
		} catch (IOException e) {
			throw new Exception("write file [" + fileName + "] failure!", e);
		} finally {
			out.close();
		}
	}

	/**
	 * <p>
	 * 描述:创建文件夹
	 * </P>
	 * Jan 26, 2011
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public boolean create(String dir, String fileName) throws Exception {
		File f = new File(dir, fileName);
		if (f.exists()) {
			return true;
		}
		createDir(dir);
		File file = new File(dir, fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new Exception("create file [" + dir + "] failure,message:" + e.getMessage(), e);
		}
		return true;
	}

	/**
	 * <p>
	 * 描述:创建默认
	 * </P>
	 * Jan 26, 2011
	 * 
	 * @param path
	 * @return
	 */
	public boolean createDir(String path) {
		File filepath = new File(path);
		if (!filepath.exists()) {
			return filepath.mkdirs();
		}
		return true;
	}

	public String[] txtFileTrailerVerify(List<String> records) {
		StringBuffer errorInfo = new StringBuffer();
		if (records == null || records.size() < 2) {
			errorInfo = errorInfo.append("header and trailer are mandatory");
			return new String[] { UnieapConstants.NO, errorInfo.toString() };
		}
		if (records == null || records.size() == 2) {
			errorInfo = errorInfo.append("no records");
			return new String[] { UnieapConstants.NO, errorInfo.toString() };
		}
		String trailer = records.get(records.size() - 1);
		String total = trailer.split("\\|")[0];
		if (records.size() - 2 != Integer.parseInt(total)) {
			errorInfo = errorInfo.append("file records unmatch trailer total number");
			return new String[] { UnieapConstants.NO, errorInfo.toString() };
		}
		return new String[] { UnieapConstants.YES, errorInfo.toString() };
	}

	/*
	 * public List<FileVO> uploadFileForImport(String fileNamePrefix) throws
	 * Exception { String shareFolderPath =
	 * SYSConfig.getConfig().get("shareFolderPath"); String mdmCommonUploadPath
	 * = SYSConfig.getConfig().get("mdmCommonUploadPath"); String path =
	 * shareFolderPath + mdmCommonUploadPath; File file = new File(path);
	 * String[] fileNames = file.list(); if (fileNames != null &&
	 * fileNames.length > 0) { List<FileVO> recordList = new
	 * ArrayList<FileVO>(); TxtBO txtBO = (TxtBO) ServiceUtils.getBean("txtBO");
	 * for (int i = 0; i < fileNames.length; i++) { String fileName =
	 * fileNames[i]; if (fileName.startsWith(fileNamePrefix) &&
	 * fileName.endsWith(".txt")) { FileVO fileVO = new FileVO();
	 * recordList.add(fileVO); String filePath = path + File.separator +
	 * fileName; List<String> records = txtBO.readSmallTxtFile(filePath);
	 * fileVO.setFileName(fileName); fileVO.setFilePath(path);
	 * fileVO.setFileType("txt"); fileVO.setRecords(records); } } return
	 * recordList; } else { return null; } }
	 */
}
