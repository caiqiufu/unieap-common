package com.unieap.base.bo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.file.bo.FileBO;
import com.unieap.base.handler.DicHandler;
import com.unieap.base.vo.DicDataVO;
import com.unieap.base.vo.DicGroupVO;
import com.unieap.base.vo.PaginationSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>
 * Description: [业务基类]
 * </p>
 * 
 * @author <a href="mailto: xxx@neusoft.com">蔡秋伏</a>
 * @version $Revision: 1.2 $
 */
@Service
public class BaseBO {
	
	@Autowired
	FileBO fileBO;
	/**
	 * <p>
	 * 描述:
	 * </p>
	 */
	public final Log log = LogFactory.getLog(BaseBO.class);

	public Map<String, String> result(String result, String message) {
		Map<String, String> model = new HashMap<String, String>();
		model.put(result, message);
		return model;
	}

	/**
	 * 针对sql设计，只参与分页数据组装
	 * 
	 * @param className
	 * @param sql
	 * @param totalSql
	 * @param parameters
	 * @param ps
	 * @throws Exception 
	 */
	public void getPaginationDataByMysql(Class<?> className, String sql, String totalSql, Object[] parameters,
			PaginationSupport ps, String dsName) throws Exception {
		sql = sql + " limit " + ps.getStartIndex() + "," + ps.getPageSize();
		log.debug("query sql:"+sql);
		log.debug("totalSql sql:"+totalSql);
		int totalCount = DBManager.getJT().queryForObject(totalSql, parameters, Integer.class);
		ps.setTotalCount(totalCount);
		List<Object> items = DBManager.getJT().query(sql, parameters, new EntityRowMapper(className));
		ps.setItems(items);
		log.debug("JsonString:"+ps.getJsonString());
	}

	public Long getSequence(String dsName, String serialName) {
		return UnieapConstants.getSequence(serialName);
	}

	public String getCurrentTime(String dsName) {
		return UnieapConstants.getCurrentTime();
	}

	public Date getDateTime(String dsName) {
		return UnieapConstants.getDateTime();
	}

	public void setCriteria(DetachedCriteria criteria, Object bean) throws Exception {
		if (bean != null) {
			Map<String, PropertyDescriptor> beanprops = UnieapCacheMgt.getBeanProps(bean.getClass().getName());
			if (beanprops == null) {
				cacheBeanprops(bean);
				beanprops = UnieapCacheMgt.getBeanProps(bean.getClass().getName());
			}
			Iterator<String> iter = beanprops.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (beanprops.containsKey(key)) {
					PropertyDescriptor prop = beanprops.get(key);
					Method getter = prop.getReadMethod();
					Object value = getter.invoke(bean, null);
					if (value != null && !StringUtils.isEmpty(value.toString())) {
						Property pro = Property.forName(key);
						Class<?> retType = getter.getReturnType();
						if (String.class == retType) {
							criteria.add(pro.like(value.toString(), MatchMode.START));
						} else {
							criteria.add(pro.eq(value));
						}
					}
				}
			}
		}
	}

	public Map<String, PropertyDescriptor> cacheBeanprops(Object bean) throws IntrospectionException {
		Map<String, PropertyDescriptor> beanprops = new HashMap<String, PropertyDescriptor>();
		BeanInfo poInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] props = poInfo.getPropertyDescriptors();
		String methodName, desc;
		for (int i = 0; i < props.length; i++) {
			if (!"class".equals(props[i].getName())) {
				Method getter = props[i].getReadMethod();
				if (getter != null) {
					methodName = getter.getName();
					if (methodName.length() > 4) {
						desc = methodName.substring(methodName.length() - 4);
						if (!"Desc".equals(desc)) {
							beanprops.put(props[i].getName(), props[i]);
						}
					} else {
						beanprops.put(props[i].getName(), props[i]);
					}
				}
			}
		}
		UnieapCacheMgt.setBeanProps(bean.getClass().getName(), beanprops);
		return beanprops;
	}
	/**
	 * @param dicType
	 * @param isOptional
	 * @param whereby
	 * @return String
	 * @throws Exception
	 */
	public String getDicData(String dicType,String isOptional,String whereby) throws Exception{
		DicHandler dicHandler = (DicHandler) ApplicationContextProvider.getBean(dicType);
		return dicHandler.getDicList(isOptional, whereby);
	}
	
	public String getCommDicList(String groupCode, String isOptional) throws Exception {
		DicGroupVO group = UnieapConstants.getDicData(groupCode);
		JSONArray ja = new JSONArray();
		if (UnieapConstants.YES.equals(isOptional)) {
			JSONObject jac = new JSONObject();
			jac.put("dicCode", "");
			jac.put("dicName", "Please select...");
			jac.put("parentCode", "");
			ja.add(jac);
		}
		List<DicDataVO> dataList = group.getDataList();
		for (DicDataVO data :dataList) {
			JSONObject jac = new JSONObject();
			jac.put("dicCode", data.getDicCode());
			jac.put("dicName", data.getDicName());
			jac.put("parentCode", data.getGroupCode());
			jac.put("attr1", data.getAttr1());
			jac.put("attr2", data.getAttr2());
			jac.put("attr3", data.getAttr3());
			jac.put("attr4", data.getAttr4());
			jac.put("attr5", data.getAttr5());
			ja.add(jac);
		}
		String dicString = ja.toString();
		return dicString;
	}
	public static String getSortField(String sortOrginialField) {
		StringBuffer sb = new StringBuffer(sortOrginialField);
		for (int i = 1; i < sb.length(); i++) {
			char chr = sb.charAt(i);
			if (Character.isUpperCase(chr)) {
				sb.replace(i, i + 1, "_" + Character.toString(chr).toLowerCase());
			}
		}
		return sb.toString();
	}
	public void createJs(ServletContext servlet, String fileName, String jsStr) throws Exception {
		String shareFolderPath = SYSConfig.getConfig().get("shareFolderPath");
		String mdmCommonPath = SYSConfig.getConfig().get("mdmCommonPath");
		//String CODELISTJSPATH = "unieap/js/common";
		String uploadPath = shareFolderPath+mdmCommonPath;
		fileBO.write(fileName, uploadPath, true, true, jsStr);
	}
}
