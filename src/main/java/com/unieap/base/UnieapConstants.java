package com.unieap.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import com.unieap.base.db.DBManager;
import com.unieap.base.vo.DicDataVO;
import com.unieap.base.vo.DicGroupVO;
import com.unieap.base.vo.MessageVO;
import com.unieap.base.vo.UserVO;

/**
 * Dec 14, 2010 全局变量,包含WEB层与Action层的数据交互定义
 */
public class UnieapConstants {
	/**
	 * <p>
	 * 描述:保存类型:UPDATE,NEW,DELETE
	 * </p>
	 */
	public final static String ADD = "Add";
	public final static String MODIFY = "Modify";
	public final static String DELETE = "Delete";
	public final static String CHECKEXIST = "checkExist";
	public final static String SEARCH = "Search";
	public final static String EXPORT = "Export";
	public final static String ISSUCCESS = "isSuccess";
	public final static String RETURN_MESSAGE = "message";
	public final static String SUCCESS = "success";
	public final static String FAILED = "failed";
	// public final static String JSON = "json";
	public final static boolean TRUE = true;
	public final static boolean FALSE = false;
	public final static String YES = "Y";
	public final static String NO = "N";
	public static String C99999 = "999999";

	public final static String ESB = "unieap-esb";
	public final static String MDM = "unieap-mdm";
	public final static String SUCCESS_CODE = "SUCCESSCODE";
	public final static String ERRORCODE_IGNORE = "ERRORCODEIGNORE";

	public final static String DATEFORMAT = "yyyy-MM-dd";
	public final static String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String TIMEFORMAT2 = "yyyyMMddHHmmss";

	public final static String TRANSFORMTYPE = "TRANSFORM_TYPE";
	public final static String TRANSFORM2EXISTING = "TE";
	public final static String TRANSFORM2STANDARD = "TS";
	// esb interface begin
	public final static String INFCONFIG = "INFCONFIG";
	public final static String BIZCONFIG = "BIZCONFIG";
	public final static String BIZMESSAGEVO = "BIZMESSAGEVO";
	public final static String REQUESTINFO = "REQUESTINFO";
	// esb interface end

	public final static String PAYLOAD = "payload";
	public final static String REQUEST_MESSAGE = "REQUEST_MESSAGE";
	public final static String RESPONSE_MESSAGE = "RESPONSE_MESSAGE";

	public final static String BUTTON_PERMISSION = "button_permission";
	public final static String DIC_PERMISSION = "dic_permission";
	
	public final static String BATCH_UPLOAD = "BU";
	public final static String DOWNLOAD = "DL";
	
	public final static String DYNAMIC_PROGRESS = "DYNAMIC_PROGRESS";

	/**
	 * success
	 */
	public static String C0 = "0";
	/**
	 * failed
	 */
	public static String C1 = "1";

	public final static UserVO getUser() {
		org.springframework.security.core.userdetails.User u;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
			u = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
		} else if (auth.getDetails() instanceof org.springframework.security.core.userdetails.User) {
			u = (org.springframework.security.core.userdetails.User) auth.getDetails();
		} else {
			throw new AccessDeniedException("User not properly authenticated.");
		}
		return UnieapCacheMgt.getUser(u.getUsername());
	}

	public final static Long getTenantId() {
		return Long.valueOf(SYSConfig.getConfig().get("tenantId"));
	}

	public final static String getCurrentTime() {
		//Map<String, Object> obj = DBManager.getJT().queryForMap("SELECT CURRENT_TIMESTAMP() CURRENTTIME");
		//Date data = (Date) obj.get("CURRENTTIME");
		Date data = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dt = sdf.format(data);
		return dt;
	}

	public final static Date getDateTime() {
		Map<String, Object> obj = DBManager.getJT().queryForMap("SELECT CURRENT_TIMESTAMP() CURRENTTIME");
		Date data = (Date) obj.get("CURRENTTIME");
		return data;
	}

	/**
	 * 
	 * @param groupCode
	 * @param dicCode
	 * @return
	 */
	public static String getDicName(String groupCode, String dicCode) {
		if (StringUtils.isEmpty(dicCode)) {
			return "";
		}
		DicGroupVO group = UnieapCacheMgt.getDicDataList(groupCode);
		if (group == null) {
			return dicCode;
		} else {
			DicDataVO dicDataVO = group.getDataMap().get(dicCode);
			return dicDataVO.getDicName();
		}
	}

	public static DicGroupVO getDicData(String groupCode) {
		return UnieapCacheMgt.getDicDataList(groupCode);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public final static String getMessage(String messageName) {
		String language = SYSConfig.getConfig().get("language");
		String key = messageName + "_" + language;
		MessageVO message = UnieapCacheMgt.getMessage(key);
		if (message != null) {
			return message.getValue();
		} else {
			return "No message configured:[" + messageName + "]";
		}

	}

	/**
	 * 
	 * @param code
	 * @param args
	 * @return
	 */
	public static String getMessage(String messageName, String[] args) {
		String language = SYSConfig.getConfig().get("language");
		String key = messageName + "_" + language;
		MessageVO message = UnieapCacheMgt.getMessage(key);
		if (message == null) {
			return "No message configured:[" + messageName + "]";
		}
		String value = message.getValue();
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				value = StringUtils.replace(value, "{" + i + "}", args[i]);
			}
		}
		return value;
	}

	public final static String getMessage(String messageName, String language) {
		String key = messageName + "_" + language;
		MessageVO message = UnieapCacheMgt.getMessage(key);
		if (message != null) {
			return message.getValue();
		} else {
			return "No message configured:[" + messageName + "]";
		}

	}

	public static String getMessage(String messageName, String language, String[] args) {
		String key = messageName + "_" + language;
		MessageVO message = UnieapCacheMgt.getMessage(key);
		if (message == null) {
			return "No message configured:[" + messageName + "]";
		}
		String value = message.getValue();
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				value = StringUtils.replace(value, "{" + i + "}", args[i]);
			}
		}
		return value;
	}

	public final static String UNIEAP = "unieap";

	public synchronized final static Long getSequence(String serialName) {
		if (StringUtils.isEmpty(serialName)) {
			serialName = UNIEAP;
		}
		int batchNo = 1;
		return getBatchSequence(serialName, batchNo);
	}

	public synchronized final static Long getSequence() {
		return getSequence(null);
	}

	/**
	 * get batch sequence
	 * 
	 * @param serialName
	 * @param batchLength
	 * @return
	 */
	public synchronized final static Long getBatchSequence(String serialName, int batchLength) {
		if (StringUtils.isEmpty(serialName)) {
			serialName = UNIEAP;
		}
		return DBManager.getJT().queryForObject("SELECT unieap.NEXTVAL('" + serialName + "'," + batchLength + ") SEQ",
				Long.class);
	}

	/**
	 * get batch sequence
	 * 
	 * @param batchLength
	 * @return
	 */
	public synchronized final static Long getBatchSequence(int batchLength) {
		return getBatchSequence(null, batchLength);
	}

	public interface PERSISTENCE_TYPE {
		String ESB = "ESB";
		String UNIEAP = "UNIEAP";
	}
}
