package com.unieap.base.bo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.pojo.MdmOperlog;
import com.unieap.base.repository.MdmOperlogRepository;

@Service
public class OperlogBO extends BaseBO {
	@Autowired
	MdmOperlogRepository mdmOperlogRepository;

	/**
	 * save change log
	 * 
	 * @param vo
	 * @return
	 */
	public Map<String, String> save(MdmOperlog vo) {
		vo.setId(getSequence(null, null));
		vo.setOperDate(UnieapConstants.getDateTime());
		vo.setOperatorName(UnieapConstants.getUser().getUserName());
		vo.setTenantId(UnieapConstants.getTenantId());
		UnieapCacheMgt.getPersistenceData("mdm_operlog").add(vo);
		return result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
	}

	/**
	 * 
	 * @param app
	 * @param moduleName
	 * @param bizCode
	 * @param bizDesc
	 * @param operObj
	 * @param operType
	 * @param operDesc
	 * @param recordId
	 * @param fieldName
	 * @param displayName
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public Map<String, String> save(String dsName,String appName, String moduleName, String bizCode, String bizDesc, String operObj,
			String operType, String operDesc, String recordId, String fieldName, String displayName, String oldValue,
			String newValue) {
		MdmOperlog vo = new MdmOperlog();
		vo.setAppName(appName);
		vo.setId(getSequence(null, null));
		vo.setModuleName(moduleName);
		vo.setBizCode(bizCode);
		vo.setBizDesc(bizDesc);
		vo.setDisplayName(displayName);
		vo.setFieldName(fieldName);
		vo.setId(UnieapConstants.getSequence());
		vo.setModuleName(moduleName);
		vo.setNewValue(newValue);
		vo.setOldValue(oldValue);
		vo.setOperatorId(UnieapConstants.getUser().getUserCode());
		vo.setOperatorName(UnieapConstants.getUser().getUserName());
		vo.setOperDate(UnieapConstants.getDateTime());
		vo.setOperDesc(operDesc);
		vo.setOperObj(operObj);
		vo.setOperType(operType);
		vo.setRecordId(recordId);
		vo.setTenantId(UnieapConstants.getTenantId());
		UnieapCacheMgt.getPersistenceData("mdm_operlog").add(vo);
		return result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
	}

	/**
	 * 
	 * @param app
	 * @param moduleName
	 * @param bizCode
	 * @param bizDesc
	 * @param operObj
	 * @param operType
	 * @param operDesc
	 * @param id
	 * @param newObj
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Map<String, String> save(String dsName,String appName, String moduleName, String bizCode, String bizDesc, String operObj,
			String operType, String operDesc, Long id, Object newObj)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object oldObj = mdmOperlogRepository.getOne(id);
		Map<String, PropertyDescriptor> beanprops = UnieapCacheMgt.getBeanProps(newObj.getClass().getName());
		if (beanprops == null) {
			beanprops = cacheBeanprops(newObj);
		}
		Iterator<String> iter = beanprops.keySet().iterator();
		String snewValue = "", soldValue = "";
		Integer inewValue, ioldValue;
		while (iter.hasNext()) {
			String fieldName = iter.next();
			if (StringUtils.equals(fieldName, "createDate") || StringUtils.equals(fieldName, "modifyDate")
					|| StringUtils.equals(fieldName, "createBy") || StringUtils.equals(fieldName, "modifyBy")) {
				continue;
			} else {
				if (beanprops.containsKey(fieldName)) {
					PropertyDescriptor prop = beanprops.get(fieldName);
					Method getter = prop.getReadMethod();
					Object newValue = getter.invoke(newObj, null);
					Object oldValue = getter.invoke(oldObj, null);
					if (newValue != null || oldValue != null) {
						Class<?> retType = getter.getReturnType();
						if (String.class == retType) {
							if (newValue != null) {
								snewValue = newValue.toString();
							}
							if (oldValue != null) {
								soldValue = oldValue.toString();
							}
							if (!StringUtils.equals(snewValue, soldValue)) {
								save(dsName,appName, moduleName, bizCode, bizDesc, operObj, operType, operDesc,
										Long.toString(id), fieldName, fieldName, soldValue, snewValue);
							}
						} else if (Integer.class == retType) {
							if (newValue != null && oldValue != null) {
								inewValue = (Integer) newValue;
								ioldValue = (Integer) oldValue;
								if (inewValue.intValue() != ioldValue.intValue()) {
									save(dsName,appName, moduleName, bizCode, bizDesc, operObj, operType, operDesc,
											Long.toString(id), fieldName, fieldName, ioldValue.toString(),
											inewValue.toString());
								}
							} else {
								if (newValue == null) {
									snewValue = "";
								} else {
									snewValue = newValue.toString();
								}
								if (oldValue == null) {
									soldValue = "";
								} else {
									soldValue = oldValue.toString();
								}
								save(dsName,appName, moduleName, bizCode, bizDesc, operObj, operType, operDesc,
										Long.toString(id), fieldName, fieldName, snewValue, soldValue);
							}
						}
					}
				}
			}
		}
		return result(UnieapConstants.ISSUCCESS, UnieapConstants.SUCCESS);
	}
}
