package com.unieap.base.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.Criteria;
import com.unieap.base.db.Restrictions;

public class DBUtils {
	/**
	 * 将blob转化为byte[],可以转化二进制流的
	 * 
	 * @param blob
	 * @return
	 */
	public static byte[] blobToBytes(Blob blob) {
		InputStream is = null;
		byte[] b = null;
		try {
			is = blob.getBinaryStream();
			b = new byte[(int) blob.length()];
			is.read(b);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}
	public static Map<String,PropertyDescriptor> cacheBeanprops(Object bean) throws IntrospectionException{
		Map<String,PropertyDescriptor> beanprops = new HashMap<String,PropertyDescriptor>();
		BeanInfo poInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] props = poInfo.getPropertyDescriptors();
		String methodName,desc;
		for (int i = 0; i < props.length; i++){
			if (!"class".equals(props[i].getName())){
				Method getter = props[i].getReadMethod();
				if(getter!=null){
					methodName = getter.getName();
					if(methodName.length()>4){
						desc = StringUtils.substring(methodName, methodName.length()-4);
						if(!"Desc".equals(desc)){
							beanprops.put(props[i].getName(), props[i]);
						}
					}else{
						beanprops.put(props[i].getName(), props[i]);
					}
				}
			}
		}
		UnieapCacheMgt.setBeanProps(bean.getClass().getName(), beanprops);
		return beanprops;
	}
	public static <T> void setCriteria(Criteria<T> criteria,Object bean) throws Exception{
		if(bean!=null){
			Map<String,PropertyDescriptor> beanprops = UnieapCacheMgt.getBeanProps(bean.getClass().getName());
			if(beanprops==null){
				cacheBeanprops(bean);
				beanprops = UnieapCacheMgt.getBeanProps(bean.getClass().getName());
			}
			Iterator<String> iter = beanprops.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				if(beanprops.containsKey(key)){
					PropertyDescriptor prop = beanprops.get(key);
					Method getter = prop.getReadMethod();
					Object value = getter.invoke(bean, null);
					if (value!=null&&StringUtils.isNotEmpty(value.toString())){
						Class<?> retType = getter.getReturnType();
						if(String.class==retType){
							criteria.add(Restrictions.like(key,value.toString(), true));
						}else{
							criteria.add(Restrictions.eq(key,value, true));
						}
					}
				}
			}
		}
	}
}
