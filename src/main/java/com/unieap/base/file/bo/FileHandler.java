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
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapConstants;
import com.unieap.base.bo.BaseBO;
import com.unieap.base.file.ImageUtils;
import com.unieap.base.pojo.MdmFileArchive;
import com.unieap.base.pojo.MdmFileProcessDetails;
import com.unieap.base.pojo.MdmFileProcessResult;
import com.unieap.base.repository.MdmFileArchiveRepository;
import com.unieap.base.repository.MdmFileProcessDetailsRepository;
import com.unieap.base.repository.MdmFileProcessRepository;

import net.sf.json.JSONObject;

public abstract class FileHandler extends BaseBO {

	@Autowired
	MdmFileProcessRepository mdmFileProcessRepository;
	@Autowired
	MdmFileProcessDetailsRepository mdmFileProcessDetailsRepository;

	public Map<String, String> fileHandle(JSONObject parameters, String handlerName, List<MdmFileArchive> files)
			throws Exception {
		return null;
	}

	public List<FileItem> getFileItems(HttpServletRequest request) {
		request.getParameterMap().keySet();
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
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;

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
			String fileType = StringUtils.split(fileName, ".")[1];
			MdmFileArchive fileArchive = new MdmFileArchive();
			fileArchive.setArchiveDate(UnieapConstants.getDateTime());
			fileArchive.setExtKey(fileName);
			fileArchive.setFileCategory("backup");
			fileArchive.setFileName(newFileName);
			fileArchive.setFilePath(folderPath);
			fileArchive.setFileSize(newfile.length());
			fileArchive.setFileType(fileType);
			fileArchive.setId(getSequence(null, null));
			url = url + File.separator + newFileName;
			fileArchive.setUrl(url);
			mdmFileArchiveRepository.save(fileArchive);
			return fileArchive;
		}
	}

	/**
	 * 如果html页面上的路径不以/开头，则认为是相对路径，默认会自动加上上个页面请求的路径(默认加上controller前缀，导致404错误)
	 * 
	 * @param       parameters:fileCategory,appName,extKey
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> upload(Map<String, String> parameters, List<FileItem> files) throws Exception {
		String fileCategory = "common";
		if (parameters.containsKey("fileCategory")) {
			fileCategory = parameters.get("fileCategory").toString();
		}
		String appName = UnieapConstants.UNIEAP;
		if (parameters.containsKey("appName")) {
			appName = parameters.get("appName").toString();
		}
		String extKey = "-9999";
		if (parameters.containsKey("extKey")) {
			extKey = parameters.get("extKey").toString();
		}
		String shareFolderPath = SYSConfig.getConfig().get("shareFolderPath") + File.separator + appName;
		String urlUploadPath = SYSConfig.getConfig().get("url.path") + File.separator + appName;
		return upload(fileCategory, extKey, files, shareFolderPath, urlUploadPath);
	}

	/**
	 * 
	 * @param parameters
	 * @param fileMap
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> upload(JSONObject parameters, Map<String, MultipartFile> fileMap) throws Exception {
		String fileCategory = "common";
		if (parameters.containsKey("fileCategory")) {
			fileCategory = parameters.get("fileCategory").toString();
		}
		String appName = UnieapConstants.UNIEAP;
		if (parameters.containsKey("appName")) {
			appName = parameters.get("appName").toString();
		}
		String extKey = "-9999";
		if (parameters.containsKey("extKey")) {
			extKey = parameters.get("extKey").toString();
		}
		String shareFolderPath = SYSConfig.getConfig().get("shareFolderPath") + File.separator + appName;
		String urlUploadPath = SYSConfig.getConfig().get("url.path") + File.separator + appName;
		return upload(fileCategory, extKey, fileMap, shareFolderPath, urlUploadPath);
	}

	/**
	 * 
	 * @param parameters
	 * @param workbook
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public MdmFileArchive upload(JSONObject parameters, XSSFWorkbook workbook, String fileName) throws Exception {
		String fileCategory = "common";
		if (parameters.containsKey("fileCategory")) {
			fileCategory = parameters.get("fileCategory").toString();
		}
		String appName = UnieapConstants.UNIEAP;
		if (parameters.containsKey("appName")) {
			appName = parameters.get("appName").toString();
		}
		String extKey = "-9999";
		if (parameters.containsKey("extKey")) {
			extKey = parameters.get("extKey").toString();
		}
		String shareFolderPath = SYSConfig.getConfig().get("shareFolderPath") + File.separator + appName;
		String urlUploadPath = SYSConfig.getConfig().get("url.path") + File.separator + appName;
		return upload(fileCategory, extKey, workbook, shareFolderPath, urlUploadPath, fileName);
	}

	/**
	 * 
	 * @param fileCategory
	 * @param extKey
	 * @param files
	 * @param uploadPath   物理存储地址
	 * @param url          应用访问地址
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> upload(String fileCategory, String extKey, List<FileItem> files, String uploadPath,
			String url) throws Exception {
		List<MdmFileArchive> fileArchiveList = new ArrayList<MdmFileArchive>();
		if (files != null && files.size() > 0) {
			for (int i = 0; i < files.size(); i++) {
				FileItem fileItem = files.get(i);
				String fileName = fileItem.getName();
				if (!StringUtils.isEmpty(fileName)) {
					String fileType = StringUtils.split(fileName, ".")[1];
					String finialFileName = getFinialFileName(StringUtils.split(fileName, ".")[0]) + "." + fileType;
					this.createDir(uploadPath);
					File savedFile = new File(uploadPath, finialFileName);
					fileItem.write(savedFile);
					MdmFileArchive fileArchive = new MdmFileArchive();
					fileArchiveList.add(fileArchive);
					fileArchive.setArchiveDate(UnieapConstants.getDateTime());
					fileArchive.setExtKey(extKey);
					fileArchive.setFileCategory(fileCategory);
					fileArchive.setFileName(finialFileName);
					fileArchive.setFilePath(uploadPath);
					fileArchive.setFileSize(fileItem.getSize());
					fileArchive.setFileType(fileType);
					fileArchive.setId(getSequence(null, null));
					url = url + File.separator + finialFileName;
					fileArchive.setUrl(url);
					fileArchive.setTenantId(UnieapConstants.getUser().getTenantId());
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
	 * @param workbook
	 * @param uploadPath
	 * @param url
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public MdmFileArchive upload(String fileCategory, String extKey, XSSFWorkbook workbook, String uploadPath,
			String url, String fileName) throws Exception {
		if (workbook != null && !StringUtils.isEmpty(fileName)) {
			String fileType = StringUtils.split(fileName, ".")[1];
			String finialFileName = getFinialFileName(StringUtils.split(fileName, ".")[0]) + "." + fileType;
			createDir(uploadPath);
			FileOutputStream output = new FileOutputStream(uploadPath + File.separator + finialFileName);
			workbook.write(output);
			output.close();
			File file = new File(uploadPath + File.separator + finialFileName);
			MdmFileArchive fileArchive = new MdmFileArchive();
			fileArchive.setArchiveDate(UnieapConstants.getDateTime());
			fileArchive.setExtKey(extKey);
			fileArchive.setFileCategory(fileCategory);
			fileArchive.setFileName(finialFileName);
			fileArchive.setFilePath(uploadPath);
			fileArchive.setFileSize(file.length());
			fileArchive.setFileType(fileType);
			fileArchive.setId(getSequence());
			url = url + File.separator + finialFileName;
			fileArchive.setUrl(url);
			fileArchive.setTenantId(UnieapConstants.getUser().getTenantId());
			mdmFileArchiveRepository.save(fileArchive);
			return fileArchive;
		}
		return null;
	}

	/**
	 * 
	 * @param fileCategory
	 * @param extKey
	 * @param fileMap
	 * @param uploadPath
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> upload(String fileCategory, String extKey, Map<String, MultipartFile> fileMap,
			String uploadPath, String url) throws Exception {
		List<MdmFileArchive> fileArchiveList = new ArrayList<MdmFileArchive>();
		if (fileMap != null && fileMap.size() > 0) {
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile fileItem = entity.getValue();
				String fileName = fileItem.getOriginalFilename();
				if (!StringUtils.isEmpty(fileName)) {
					String fileType = StringUtils.split(fileName, ".")[1];
					String finialFileName = getFinialFileName(StringUtils.split(fileName, ".")[0]) + "." + fileType;
					this.createDir(uploadPath);
					File savedFile = new File(uploadPath, finialFileName);
					fileItem.transferTo(savedFile);
					MdmFileArchive fileArchive = new MdmFileArchive();
					fileArchiveList.add(fileArchive);
					fileArchive.setArchiveDate(UnieapConstants.getDateTime());
					fileArchive.setExtKey(extKey);
					fileArchive.setFileCategory(fileCategory);
					fileArchive.setFileName(finialFileName);
					fileArchive.setFilePath(uploadPath);
					fileArchive.setFileSize(fileItem.getSize());
					fileArchive.setFileType(fileType);
					fileArchive.setId(getSequence());
					url = url + File.separator + finialFileName;
					fileArchive.setUrl(url);
					fileArchive.setTenantId(UnieapConstants.getUser().getTenantId());
					mdmFileArchiveRepository.save(fileArchive);
				}
			}
		}
		return fileArchiveList;
	}

	public String getFinialFileName(String originalFileName) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateString = formatter.format(currentTime);
		return originalFileName + "_" + dateString;
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
			for (int i = 0; i < files.size(); i++) {
				FileItem fileItem = files.get(i);
				String fileName = fileItem.getName();
				String fileType = StringUtils.split(fileName, ".")[1];
				String finialFileName = getFinialFileName(StringUtils.split(fileName, ".")[0]) + "." + fileType;
				FileOutputStream fos = new FileOutputStream(uploadPath + File.separator + finialFileName);
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
					if (finialFileName.indexOf(".") > -1) {
						suffix = finialFileName.substring(finialFileName.lastIndexOf(".") + 1);
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
					MdmFileArchive fileArchive = new MdmFileArchive();
					fileArchiveList.add(fileArchive);
					fileArchive.setArchiveDate(UnieapConstants.getDateTime());
					fileArchive.setExtKey(extKey);
					fileArchive.setFileCategory(fileCategory);
					fileArchive.setFileName(finialFileName);
					fileArchive.setFilePath(uploadPath);
					fileArchive.setFileSize(fileItem.getSize());
					fileArchive.setFileType(fileType);
					fileArchive.setId(getSequence());
					fileArchive.setTenantId(UnieapConstants.getUser().getTenantId());
					url = url + File.separator + finialFileName;
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

	/**
	 * 
	 * @param fileCategory
	 * @param extKey
	 * @param fileMap
	 * @param uploadPath
	 * @param url
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> uploadByThumbnailImage(String fileCategory, String extKey,
			Map<String, MultipartFile> fileMap, String uploadPath, String url, int x, int y, int width, int height)
			throws Exception {
		if (fileMap != null && fileMap.size() > 0) {
			List<MdmFileArchive> fileArchiveList = new ArrayList<MdmFileArchive>();
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile fileItem = entity.getValue();
				String fileName = fileItem.getOriginalFilename();
				String fileType = StringUtils.split(fileName, ".")[1];
				String finialFileName = getFinialFileName(StringUtils.split(fileName, ".")[0]) + "." + fileType;
				FileOutputStream fos = new FileOutputStream(uploadPath + File.separator + finialFileName);
				File savedFile = new File(uploadPath, finialFileName);
				fileItem.transferTo(savedFile);
				MdmFileArchive fileArchive = ImageUtils.getInstance().thumbnailImage(savedFile, fos, width, height,
						finialFileName, true);
				if (fileArchive != null) {
					fileArchiveList.add(fileArchive);
					fileArchiveList.add(fileArchive);
					fileArchive.setExtKey(extKey);
					fileArchive.setFileCategory(fileCategory);
					fileArchive.setFileName(finialFileName);
					fileArchive.setFilePath(uploadPath);
					fileArchive.setFileSize(fileItem.getSize());
					fileArchive.setFileType(fileType);
					fileArchive.setId(getSequence());
					fileArchive.setTenantId(UnieapConstants.getUser().getTenantId());
					url = url + File.separator + finialFileName;
					fileArchive.setUrl(url);
					mdmFileArchiveRepository.save(fileArchive);
				}

			}
			return fileArchiveList;
		}
		return null;
	}

	/**
	 * 
	 * @param fileCategory
	 * @param extKey
	 * @param fileMap
	 * @param uploadPath
	 * @param url
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public List<MdmFileArchive> uploadByCutImage(String fileCategory, String extKey, Map<String, MultipartFile> fileMap,
			String uploadPath, String url, int x, int y, int width, int height) throws Exception {
		if (fileMap != null && fileMap.size() > 0) {
			List<MdmFileArchive> fileArchiveList = new ArrayList<MdmFileArchive>();
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile fileItem = entity.getValue();
				String fileName = fileItem.getOriginalFilename();
				String fileType = StringUtils.split(fileName, ".")[1];
				String finialFileName = getFinialFileName(StringUtils.split(fileName, ".")[0]) + "." + fileType;
				FileOutputStream fos = new FileOutputStream(uploadPath + File.separator + finialFileName);
				File savedFile = new File(uploadPath, finialFileName);
				fileItem.transferTo(savedFile);
				MdmFileArchive fileArchive = ImageUtils.getInstance().cutImage(savedFile, fos, x, y, width, height);
				if (fileArchive != null) {
					fileArchiveList.add(fileArchive);
					fileArchiveList.add(fileArchive);
					fileArchive.setExtKey(extKey);
					fileArchive.setFileCategory(fileCategory);
					fileArchive.setFileName(finialFileName);
					fileArchive.setFilePath(uploadPath);
					fileArchive.setFileSize(fileItem.getSize());
					fileArchive.setFileType(fileType);
					fileArchive.setId(getSequence());
					fileArchive.setTenantId(UnieapConstants.getUser().getTenantId());
					url = url + File.separator + finialFileName;
					fileArchive.setUrl(url);
					mdmFileArchiveRepository.save(fileArchive);
				}

			}
			return fileArchiveList;
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

	/**
	 * 
	 * @param url
	 * @param fileName
	 * @param fileSize
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
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
	 * 
	 * @param fileName
	 * @param path
	 * @param isCover
	 * @param isEnter
	 * @param str
	 * @throws Exception
	 */
	public void write(String fileName, String path, boolean isCover, boolean isEnter, String str) throws Exception {
		create(path, fileName, isCover);
		File file = new File(path, fileName);
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
	 * 
	 * @param fileName
	 * @param path
	 * @param isCover
	 * @param strs
	 * @throws Exception
	 */
	public void write(String fileName, String path, boolean isCover, String[] strs) throws Exception {
		create(path, fileName, isCover);
		File file = new File(path, fileName);
		OutputStreamWriter out = null;
		StringBuffer feed = new StringBuffer();
		for (String str : strs) {
			feed.append(str).append('\n');
		}
		try {
			out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			out.write(feed.toString());
			out.flush();
		} catch (IOException e) {
			throw new Exception("write file [" + fileName + "] failure!", e);
		} finally {
			out.close();
		}
	}

	/**
	 * 
	 * @param dir
	 * @param fileName
	 * @param isCover
	 * @return
	 * @throws Exception
	 */
	public boolean create(String dir, String fileName, boolean isCover) throws Exception {
		File f = new File(dir, fileName);
		if (f.exists() && !isCover) {
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

	public MdmFileProcessResult saveFileProcess(Long id, Long fileId, Long totalNumber, String resultCode,
			String resultDesc) {
		MdmFileProcessResult mdmFileProcessResult = new MdmFileProcessResult();
		mdmFileProcessResult.setId(id);
		mdmFileProcessResult.setFileId(fileId);
		mdmFileProcessResult.setCreateBy(UnieapConstants.UNIEAP);
		mdmFileProcessResult.setCreateDate(UnieapConstants.getDateTime());
		mdmFileProcessResult.setResultCode(resultCode);
		mdmFileProcessResult.setResultDesc(resultDesc);
		mdmFileProcessResult.setTotalNumber(totalNumber);
		return mdmFileProcessRepository.save(mdmFileProcessResult);
	}

	public void saveFileProcessDetail(Long processId, Long rowNumber, String originalContent, String resultCode,
			String resultDesc) {
		MdmFileProcessDetails mdmFileProcessDetails = new MdmFileProcessDetails();
		mdmFileProcessDetails.setCreateBy(UnieapConstants.UNIEAP);
		mdmFileProcessDetails.setCreateDate(UnieapConstants.getDateTime());
		mdmFileProcessDetails.setOriginalContent(originalContent);
		mdmFileProcessDetails.setProcessId(processId);
		mdmFileProcessDetails.setResultCode(resultCode);
		mdmFileProcessDetails.setResultDesc(resultDesc);
		mdmFileProcessDetails.setRowNumber(rowNumber);
		mdmFileProcessDetailsRepository.save(mdmFileProcessDetails);
	}

	public void copyFileUsingFileChannels(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			fileInputStream = new FileInputStream(source);
			inputChannel = fileInputStream.getChannel();
			fileOutputStream = new FileOutputStream(dest);
			outputChannel = fileOutputStream.getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}

	/**
	 * 删除文件，可以是文件或文件夹
	 *
	 * @param fileName 要删除的文件名
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("删除文件失败:" + fileName + "不存在！");
			return false;
		} else {
			if (file.isFile())
				return deleteFile(fileName);
			else
				return deleteDirectory(fileName);
		}
	}

	/**
	 * 删除单个文件
	 *
	 * @param fileName 要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				System.out.println("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			System.out.println("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}

	/**
	 * 删除目录及目录下的文件
	 *
	 * @param dir 要删除的目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			System.out.println("删除目录失败：" + dir + "不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			System.out.println("删除目录失败！");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			System.out.println("删除目录" + dir + "成功！");
			return true;
		} else {
			return false;
		}
	}
}
