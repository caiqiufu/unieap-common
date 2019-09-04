package com.unieap.base;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.ws.Endpoint;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import com.unieap.base.inf.vo.BizConfigVO;
import com.unieap.base.inf.vo.BizFieldVO;
import com.unieap.base.inf.vo.BizMessageVO;
import com.unieap.base.inf.vo.InfConfigVO;
import com.unieap.base.inf.vo.InfFieldVO;
import com.unieap.base.inf.vo.InfSQLConfigVO;
import com.unieap.base.vo.DicDataVO;
import com.unieap.base.vo.DicGroupVO;
import com.unieap.base.vo.MessageVO;
import com.unieap.base.vo.UserVO;

/**
 * Feb 19, 2011 缓存管理
 */
public final class UnieapCacheMgt {

	public static Map<String, UserVO> userList = new HashMap<String, UserVO>();

	public static Map<String, DicGroupVO> dicDataList = new HashMap<String, DicGroupVO>();
	public static Map<String, List<String>> resRoleList = new HashMap<String, List<String>>();

	public static Map<String, MessageVO> messageList = new HashMap<String, MessageVO>();

	public static Map<String, Endpoint> endpointList = new HashMap<String, Endpoint>();

	public static Map<String, BizConfigVO> bizHandlerList = new HashMap<String, BizConfigVO>();
	public static Map<String, InfConfigVO> infHandlerList = new HashMap<String, InfConfigVO>();

	private static Map<String, List<Object>> persistenceDataList = new HashMap<String, List<Object>>();
	private static Map<String, Map<String, PropertyDescriptor>> beanProps = new HashMap<String, Map<String, PropertyDescriptor>>();

	public static Map<String, Object> cacheObejctList = new HashMap<String, Object>();

	public static Map<String, Map<String, Object>> esbServerConfig = new HashMap<String, Map<String, Object>>();

	public static Map<String, String> heartCheckResult = new HashMap<String, String>();

	public static Map<String, BizMessageVO> bizMessageList = new HashMap<String, BizMessageVO>();

	public static Map<String, BizFieldVO> bizFieldList = new HashMap<String, BizFieldVO>();
	public static Map<String, InfFieldVO> infFieldList = new HashMap<String, InfFieldVO>();
	
	public static Map<String, InfSQLConfigVO> sqlBizList = new HashMap<String, InfSQLConfigVO>();
	
	public static Map<String, DataSource> dsList = new HashMap<String, DataSource>();
	public static Map<String, JdbcTemplate> jtList = new HashMap<String, JdbcTemplate>();

	public static Map<String, Endpoint> getEndpointList() {
		return endpointList;
	}

	public static void setEndpointList(Map<String, Endpoint> endpointList) {
		UnieapCacheMgt.endpointList = endpointList;
	}

	public static DicGroupVO getDicDataList(String groupCode) {
		return dicDataList.get(groupCode);
	}

	public static void setDicDataList(Map<String, DicGroupVO> dicDataList) {
		UnieapCacheMgt.dicDataList = dicDataList;
	}

	public static Map<String, DicDataVO> getDicData(String groupCode) {
		return dicDataList.get(groupCode).getDataMap();
	}

	public static DicDataVO getDicData(String groupCode, String dicCode) {
		if (StringUtils.isEmpty(groupCode) || StringUtils.isEmpty(dicCode)) {
			return null;
		}
		if (dicDataList.get(groupCode) == null) {
			return null;
		} else {
			DicGroupVO group = dicDataList.get(groupCode);
			if (group.getDataMap().get(dicCode) != null) {
				return group.getDataMap().get(dicCode);
			} else {
				return null;
			}
		}
	}

	public static String getDicName(String groupCode, String dicCode) {
		if (StringUtils.isEmpty(groupCode) || StringUtils.isEmpty(dicCode)) {
			return dicCode;
		}
		if (dicDataList.get(groupCode) == null) {
			return dicCode;
		} else {
			DicGroupVO group = dicDataList.get(groupCode);
			if (group.getDataMap().get(dicCode) != null) {
				return group.getDataMap().get(dicCode).getDicName();
			} else {
				return dicCode;
			}
		}
	}

	public static UserVO getUser(String userCode) {
		return userList.get(userCode);
	}

	public static MessageVO getMessage(String messageName) {
		return messageList.get(messageName);
	}

	public static void setUserList(Map<String, UserVO> userList) {
		UnieapCacheMgt.userList = userList;
	}

	public static List<String> getUrlAccessRoles(String url) {
		return resRoleList.get(url);
	}

	public static Map<String, List<String>> getResRoleList() {
		return resRoleList;
	}

	public static void setResRoleList(Map<String, List<String>> resRoleList) {
		UnieapCacheMgt.resRoleList = resRoleList;
	}

	public static Map<String, MessageVO> getMessageList() {
		return messageList;
	}

	public static void setMessageList(Map<String, MessageVO> messageList) {
		UnieapCacheMgt.messageList = messageList;
	}

	public static Map<String, UserVO> getUserList() {
		return userList;
	}

	public static Map<String, DicGroupVO> getDicDataList() {
		return dicDataList;
	}

	public static Map<String, BizConfigVO> getBizHandlerList() {
		return bizHandlerList;
	}

	public static BizConfigVO getBizHandler(String bizCode) {
		return bizHandlerList.get(bizCode);
	}

	public static void setBizHandlerList(Map<String, BizConfigVO> bizHandlerList) {
		UnieapCacheMgt.bizHandlerList = bizHandlerList;
	}

	public static Map<String, List<Object>> getPersistenceDataList() {
		return persistenceDataList;
	}

	public static List<Object> getPersistenceData(String type) {
		return persistenceDataList.get(type);
	}

	public static void setPersistenceDataList(Map<String, List<Object>> persistenceDataList) {
		UnieapCacheMgt.persistenceDataList = persistenceDataList;
	}

	public static boolean persistenceListLock = false;

	public static void setPersistenceData(String type, Object object) {
		List<Object> persistenceData = UnieapCacheMgt.persistenceDataList.get(type);
		if (persistenceData == null) {
			persistenceData = new ArrayList<Object>();
		}
		persistenceData.add(object);
		UnieapCacheMgt.persistenceDataList.put(type, persistenceData);
	}

	public static Map<String, PropertyDescriptor> getBeanProps(String className) {
		return beanProps.get(className);
	}

	public static void setBeanProps(String className, Map<String, PropertyDescriptor> beanProp) {
		beanProps.put(className, beanProp);
	}

	public static Map<String, Object> getCacheObejctList() {
		return cacheObejctList;
	}

	public static void setCacheObejctList(Map<String, Object> cacheObejctList) {
		UnieapCacheMgt.cacheObejctList = cacheObejctList;
	}

	public static InfConfigVO getInfHandler(String infCode) {
		return infHandlerList.get(infCode);
	}

	public static void setInfHandlerList(Map<String, InfConfigVO> infHandlerList) {
		UnieapCacheMgt.infHandlerList = infHandlerList;
	}

	public static Map<String, Map<String, PropertyDescriptor>> getBeanProps() {
		return beanProps;
	}

	public static void setBeanProps(Map<String, Map<String, PropertyDescriptor>> beanProps) {
		UnieapCacheMgt.beanProps = beanProps;
	}

	public static Map<String, Map<String, Object>> getEsbServerConfig() {
		return esbServerConfig;
	}

	public static void setEsbServerConfig(Map<String, Map<String, Object>> esbServerConfig) {
		UnieapCacheMgt.esbServerConfig = esbServerConfig;
	}

	public static Map<String, String> getHeartCheckResult() {
		return heartCheckResult;
	}

	public static void setHeartCheckResult(Map<String, String> heartCheckResult) {
		UnieapCacheMgt.heartCheckResult = heartCheckResult;
	}

	public static boolean isPersistenceListLock() {
		return persistenceListLock;
	}

	public static void setPersistenceListLock(boolean persistenceListLock) {
		UnieapCacheMgt.persistenceListLock = persistenceListLock;
	}

	public static Map<String, InfConfigVO> getInfHandlerList() {
		return infHandlerList;
	}

	public static BizMessageVO getBizMessageVO(String bizCode) {
		return bizMessageList.get(bizCode);
	}

	public static void setBizMessageList(Map<String, BizMessageVO> bizMessageList) {
		UnieapCacheMgt.bizMessageList = bizMessageList;
	}

	public static Map<String, BizFieldVO> getBizFieldList() {
		return bizFieldList;
	}

	public static BizFieldVO getBizFieldVO(String bizFieldId) {
		return bizFieldList.get(bizFieldId);
	}

	public static void setBizFieldList(Map<String, BizFieldVO> bizFieldList) {
		UnieapCacheMgt.bizFieldList = bizFieldList;
	}

	public static Map<String, InfFieldVO> getInfFieldList() {
		return infFieldList;
	}

	public static InfFieldVO getInfFieldVO(String infFieldId) {
		return infFieldList.get(infFieldId);
	}

	public static void setInfFieldList(Map<String, InfFieldVO> infFieldList) {
		UnieapCacheMgt.infFieldList = infFieldList;
	}

	public static Map<String, BizMessageVO> getBizMessageList() {
		return bizMessageList;
	}

	public static Map<String, InfSQLConfigVO> getSqlBizList() {
		return sqlBizList;
	}

	public static void setSqlBizList(Map<String, InfSQLConfigVO> sqlBizList) {
		UnieapCacheMgt.sqlBizList = sqlBizList;
	}

	public static Map<String, DataSource> getDsList() {
		return dsList;
	}

	public static void setDsList(Map<String, DataSource> dsList) {
		UnieapCacheMgt.dsList = dsList;
	}

	public static Map<String, JdbcTemplate> getJtList() {
		return jtList;
	}

	public static void setJtList(Map<String, JdbcTemplate> jtList) {
		UnieapCacheMgt.jtList = jtList;
	}

}
