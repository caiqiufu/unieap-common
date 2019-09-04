package com.unieap.base.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;

import javax.xml.bind.JAXBElement;

import org.springframework.util.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * <p>
 * Description: [用于JSON格式的对象和POJO对象及集合对象之间的转换]
 * </p>
 * 
 * @version $Revision: 1.5 $
 */
public final class JSONUtils {
	/**
	 * <p>
	 * Discription:[把数据对象转换成JSON对象,List中可以包含Map,Bean]
	 * </p>
	 * 
	 * @param list
	 * @return JSONObject
	 * @throws Exception
	 */
	/*
	 * public static JSONObject getJsonObject(List<Object> list) throws Exception {
	 * if (list != null && !list.isEmpty()) { JSONObject jsonResult = new
	 * JSONObject(); JSONArray ja = new JSONArray(); JSONObject jsonObj = null; for
	 * (Object value : list) { if (value instanceof Map) { jsonObj =
	 * mapToJSON((Map<String, Object>) value); } else { jsonObj =
	 * convertBean2JSON(value); } ja.add(jsonObj); } jsonResult.put("data", ja);
	 * return jsonResult; } else { return null; } }
	 */

	/**
	 * @param list
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getJSONArray(List<?> list) throws Exception {
		JSONArray ja = new JSONArray();
		if (list != null && !list.isEmpty()) {
			JSONObject jsonObj = null;
			for (Object value : list) {
				if (value instanceof Map) {
					jsonObj = mapToJSON((Map<String, Object>) value);
				} else {
					jsonObj = convertBean2JSON(value);
				}
				ja.add(jsonObj);
			}
		}
		return ja;
	}

	/**
	 * @param map
	 * @return String
	 * @throws Exception
	 */
	public static String getMap2JsonString(Map<String, String> map) throws Exception {
		if (map != null) {
			JSONArray ja = new JSONArray();
			JSONObject jsonObj = new JSONObject();
			Iterator<?> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				jsonObj.put(key, value);
			}
			ja.add(jsonObj);
			return ja.toString();
		} else {
			return "";
		}
	}

	/**
	 * @param list
	 * @return String
	 * @throws Exception
	 */
	/*
	 * public static String getList2JsonString(List<Object> list) throws Exception {
	 * JSONObject jo = getJsonObject(list); if (jo == null) { return ""; } else {
	 * return getJsonObject(list).toString(); } }
	 */

	/**
	 * @param list
	 * @param totalCount
	 * @return String
	 * @throws Exception
	 */
	/*
	 * public static String getJsonString(List<Object> list, String totalCount)
	 * throws Exception { JSONObject jsonResult; jsonResult = getJsonObject(list);
	 * if (jsonResult == null) { jsonResult = new JSONObject(); }
	 * jsonResult.put("totalCount", totalCount); return jsonResult.toString(); }
	 */

	/**
	 * 
	 * <p>
	 * Discription:[把符合sun javabean标准的POJO类转换为json对象]
	 * </p>
	 * 
	 * @param bean
	 * @return JSONObject
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject convertBean2JSON(Object bean) throws Exception {
		JSONObject json = new JSONObject();
		if (bean == null) {
			return json;
		}
		// Method[] getters = getGetters(clazz);
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			Method getter = props[i].getReadMethod();
			String property = props[i].getName();
			if ("class".equals(property)) {
				continue;
			}
			if (getter == null) {
				continue;
			}
			Class<?> retType = getter.getReturnType();
			try {
				Object value = getter.invoke(bean, null);
				if (isPrimitive(retType)) {
					json.put(property, value);
				} else if (isPrimitiveArray(retType)) {
					JSONArray jarr = priArray(value);
					json.put(property, jarr);
				} else if (isDate(value)) {
					// json.put(property, BeanUtils.getTime(value));
					json.put(property, formatDate((Date) value, "yyyy-MM-dd HH:mm:ss"));
				} else if (isPrimitiveCollection(retType)) {
					json.put(property, collectionToJson(value));
				} else if (retType.getName().startsWith("[")) {
					json.put(property, arrayToJarr(value));
				} else if (value instanceof Map) {
					json.put(property, mapToJSON((Map<String, Object>) value));
				} else if (value instanceof JAXBElement) {
					json.put(property, value == null ? null : ((JAXBElement<?>) value).getValue());
				} else {
					json.put(property, value == null ? null : convertBean2JSON(value));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error to get value,error:" + e.getMessage());
			}
		}

		return json;
	}

	/**
	 * <p>
	 * Discription:[转换成json的时候把null转换成""]
	 * </p>
	 * 
	 * @param bean
	 * @param haveNull
	 * @return JSONObject
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject convertBean2JSON(Object bean, boolean haveNull) throws Exception {
		JSONObject json = new JSONObject();
		if (bean == null) {
			return json;
		}
		// Method[] getters = getGetters(clazz);
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			Method getter = props[i].getReadMethod();
			String property = props[i].getName();
			if ("class".equals(property)) {
				continue;
			}
			if (getter == null) {
				continue;
			}
			Class<?> retType = getter.getReturnType();
			try {
				Object value = getter.invoke(bean, null);
				if (haveNull) {
					if (value == null) {
						value = "";
					}
				}
				if (isPrimitive(retType))
					json.put(property, value);
				else if (isPrimitiveArray(retType)) {
					JSONArray jarr = priArray(value);
					json.put(property, jarr);
				} else if (isDate(value)) {
					json.put(property, getTime(value));
				} else if (value instanceof Collection) {
					json.put(property, collectionToJson(value));
				} else if (retType.getName().startsWith("[")) {
					json.put(property, arrayToJarr(value));
				} else if (value instanceof Map) {
					json.put(property, mapToJSON((Map<String, Object>) value));
				} else {
					json.put(property, value == null ? null : convertBean2JSON(value));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error to get value");
			}
		}

		return json;
	}

	public static JSONArray priArray(Object value) {
		JSONArray jarr = new JSONArray();
		if (value == null)
			return jarr;
		int arLen = Array.getLength(value);
		for (int j = 0; j < arLen; j++) {
			jarr.add(Array.get(value, j));
		}

		return jarr;
	}

	public static JSONArray arrayToJarr(Object value) throws Exception {
		if (value == null) {
			return new JSONArray();
		}
		Object[] arr = (Object[]) value;
		Collection<Object> c = new ArrayList<Object>();
		for (int i = 0; i < arr.length; i++) {
			c.add(arr[i]);
		}
		return collectionToJson(c);
	}

	/**
	 * @param value
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray collectionToJson(Object value) throws Exception {
		JSONArray jarr = new JSONArray();
		if (value == null) {
			return jarr;
		}
		Collection<?> c = (Collection<?>) value;
		Iterator<?> iter = c.iterator();
		Object o = null;
		while (iter.hasNext()) {
			o = iter.next();
			if (o == null) {
				jarr.add(null);
				continue;
			}
			Class<?> type = o.getClass();
			if (isPrimitive(type)) {
				jarr.add(o);
			} else if (isPrimitiveArray(type)) {
				jarr.add(priArray(o));
			} else if (isDate(o)) {
				jarr.add(getTime(o));
			} else if (o instanceof Collection) {
				jarr.add(collectionToJson(o));
			} else if (type.getName().startsWith("[")) {
				jarr.add(arrayToJarr(o));
			} else if (o instanceof Map) {
				jarr.add(mapToJSON((Map) o));
			} else {
				jarr.add(convertBean2JSON(o));
			}
		}
		return jarr;
	}

	/**
	 * <p>
	 * Discription:[把对象转换成JSONArray]
	 * </p>
	 * 
	 * @param value
	 * @param haveNull
	 * @return
	 * @throws Exception
	 */
	public static JSONArray collectionToJson(Object value, boolean haveNull) throws Exception {
		JSONArray jarr = new JSONArray();
		if (value == null) {
			return jarr;
		}
		Collection<?> c = (Collection<?>) value;
		Iterator<?> iter = c.iterator();
		Object o = null;
		while (iter.hasNext()) {
			o = iter.next();
			if (o == null) {
				jarr.add(null);
				continue;
			}
			Class<?> type = o.getClass();
			if (isPrimitive(type)) {
				jarr.add(o);
			} else if (isPrimitiveArray(type)) {
				jarr.add(priArray(o));
			} else if (isDate(o)) {
				jarr.add(getTime(o));
			} else if (o instanceof Collection) {
				jarr.add(collectionToJson(o, haveNull));
			} else if (type.getName().startsWith("[")) {
				jarr.add(arrayToJarr(o));
			} else if (o instanceof Map) {
				// jarr.add(new JSONObject((Map<?, ?>) o));
				jarr.add(mapToJSON((Map) o));
			} else {
				jarr.add(convertBean2JSON(o, haveNull));
			}
		}
		return jarr;
	}

	/**
	 * 
	 * <p>
	 * Discription:[将一个Map对象转换为一个JSON对象]
	 * </p>
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject mapToJSON(Map<String, Object> map) throws Exception {
		if (map == null) {
			return null;
		}
		JSONObject jso = new JSONObject();
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Object val = map.get(key);
			if (val == null) {
				jso.put(key, null);
				continue;
			}
			Class<?> type = val.getClass();
			if (isPrimitive(type))
				jso.put(key, val);
			else if (isPrimitiveArray(type)) {
				JSONArray ja = priArray(val);
				jso.put(key, ja);
			} else if (isDate(val)) {
				// jso.put(key,BeanUtils.getTime(val));
				jso.put(key, formatDate((Date) val, "yyyy-MM-dd HH:mm:ss"));
			} else if (val instanceof Collection) {
				JSONArray ja2 = collectionToJson(val);
				jso.put(key, ja2);
			} else if (type.getName().startsWith("[")) {
				JSONArray ja3 = arrayToJarr(val);
				jso.put(key, ja3);
			} else if (val instanceof Map) {
				jso.put(key, mapToJSON((Map<String, Object>) val));
			} else
				jso.put(key, convertBean2JSON(val));
		}
		return jso;
	}

	public static final String typeKey = "___private_par_key__";

	/**
	 * 
	 * @param json
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static Object convertJSON2Bean(JSONObject json, Class<?> beanClass) throws Exception {
		return convertJSON2Bean(json, beanClass, null);
	}

	/**
	 * 
	 * <p>
	 * Discription:[将JSON对象转换为POJO对象]
	 * </p>
	 * 
	 * @param json                     输入的json对象
	 * @param beanClass                转换成的javabean的class对象
	 * @param collectionComponentTypes 标示集合类的属性中所包含的Class类型信息.属性名为key放入Map中
	 * @return
	 * @throws Exception
	 */
	public static Object convertJSON2Bean(JSONObject json, Class<?> beanClass,
			Map<String, Object> collectionComponentTypes) throws Exception {
		Object bean = beanClass.newInstance();
		if (json == null || json.size() == 0) {
			return null;
		}
		if (collectionComponentTypes == null) {
			collectionComponentTypes = new HashMap<String, Object>();
		}
		String parKey = (String) collectionComponentTypes.get(typeKey);
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		Iterator<?> keys = json.keys();
		String key = null;
		while (keys.hasNext()) {
			key = (String) keys.next();
			Method setter = getMethod(props, key);
			if (setter == null) {
				continue;
			}
			// String property = props[i].getName();
			Class<?> paramType = setter.getParameterTypes()[0];// set参数
			if (isPrimitive(paramType)) {
				setter.invoke(bean, new Object[] { convertJSONNull(json.get(key)) });
			} else if (isPrimitiveArray(paramType)) {// 数组类型
				JSONArray jarr = json.getJSONArray(key);
				Object arr = null;
				if (jarr != null) {
					arr = Array.newInstance(getType(paramType), jarr.size());
					for (int j = 0; j < jarr.size(); j++)
						Array.set(arr, j, convertJSONNull(jarr.get(j)));
				}
				setter.invoke(bean, new Object[] { arr });
			} else if (paramType == java.util.Date.class) {
				JSONObject obj = (JSONObject) convertJSONNull(json.get(key));
				setter.invoke(bean, new Object[] { obj != null ? new java.util.Date(obj.getLong(key)) : null });
			} else if (paramType == java.sql.Date.class) {
				JSONObject obj = (JSONObject) convertJSONNull(json.get(key));
				setter.invoke(bean, new Object[] { obj != null ? new java.sql.Date(json.getLong(key)) : null });
			} else if (paramType == java.sql.Time.class) {
				JSONObject obj = (JSONObject) convertJSONNull(json.get(key));
				setter.invoke(bean, new Object[] { obj != null ? new java.sql.Time(json.getLong(key)) : null });
			} else if (paramType == java.sql.Timestamp.class) {
				JSONObject obj = (JSONObject) convertJSONNull(json.get(key));
				setter.invoke(bean, new Object[] { obj != null ? new java.sql.Timestamp(json.getLong(key)) : null });
			} else if (paramType == java.util.Calendar.class) {
				JSONObject obj = (JSONObject) convertJSONNull(json.get(key));
				if (obj != null) {
					java.util.Calendar c = java.util.Calendar.getInstance();
					c.setTimeInMillis(json.getLong(key));
					setter.invoke(bean, new Object[] { c });
				} else
					setter.invoke(bean, new Object[] { (java.util.Calendar) null });

			} else if (paramType == java.util.Set.class) {
				String nowKey = parKey.concat(key);
				Class<?> componentType = (Class<?>) collectionComponentTypes.get(nowKey);
				collectionComponentTypes.put(typeKey, nowKey);
				JSONArray jarr = json.getJSONArray(key);
				java.util.Set<Object> s = null;
				if (jarr != null && jarr != null) {
					s = new java.util.HashSet<Object>();
					initCollection(s, json.getJSONArray(key), componentType, collectionComponentTypes);
				}
				setter.invoke(bean, new Object[] { s });
			} else if (paramType == java.util.List.class || paramType == java.util.Collection.class) {
				String nowKey = parKey.concat(key);
				Class componentType = (Class) collectionComponentTypes.get(nowKey);
				collectionComponentTypes.put(typeKey, nowKey);
				JSONArray jarr = json.getJSONArray(key);
				java.util.ArrayList<Object> l = null;
				if (jarr != null && jarr != null) {
					l = new java.util.ArrayList<Object>();
					initCollection(l, json.getJSONArray(key), componentType, collectionComponentTypes);
				}
				setter.invoke(bean, new Object[] { l });
			} else if (paramType.getName().startsWith("[")) {
				Object[] arr = toObjectArray(paramType, json.getJSONArray(key), collectionComponentTypes);
				setter.invoke(bean, arr);// 参数为Object[]类型
			} else {
				JSONObject obj = (JSONObject) convertJSONNull(json.get(key));
				String nowKey = parKey.concat(key);
				collectionComponentTypes.put(typeKey, nowKey);
				Object cobj = convertJSON2Bean(obj, paramType, collectionComponentTypes);
				if (cobj != null) {
					setter.invoke(bean, new Object[] { cobj });
				}
			}
		}
		return bean;
	}

	public static Object convertJSONNull(Object o) {
		if (o == null)
			return null;
		else
			return o;
	}

	public static Method getMethod(PropertyDescriptor[] props, String prop) {
		for (int i = 0; i < props.length; i++) {
			if (prop.equals(props[i].getName()))
				return props[i].getWriteMethod();
		}
		return null;
	}

	/**
	 * 
	 * <p>
	 * Discription:[把jsonObject数组转换为对象数组]
	 * </p>
	 * 
	 * @param paramType
	 * @param jarr
	 * @param collectionComponentTypes
	 * @return Object[]
	 * @throws Exception
	 */
	public static Object[] toObjectArray(Class<?> paramType, JSONArray jarr,
			Map<String, Object> collectionComponentTypes) throws Exception {
		if (jarr == null || jarr == null)
			return (Object[]) null;
		Object[] objs = new Object[jarr.size()];
		Class<?> beanClass = Class.forName(paramType.getName().substring(2));
		for (int i = 0; i < objs.length; i++) {
			Object obj = convertJSON2Bean(jarr.getJSONObject(i), beanClass, collectionComponentTypes);
			if (obj != null) {
				objs[i] = obj;
			}
		}
		return objs;
	}

	/**
	 * 
	 * <p>
	 * Discription:[jsonArray转换为List]
	 * </p>
	 * 
	 * @param paramType
	 * @param jarr
	 * @param collectionComponentTypes
	 * @return
	 * @throws Exception
	 */
	public static List<Object> jsonArrayToList(Class<?> paramType, JSONArray jarr,
			Map<String, Object> collectionComponentTypes) throws Exception {
		List<Object> list = new ArrayList<Object>(jarr.size());
		Class<?> beanClass = Class.forName(paramType.getName());
		for (int i = 0; i < jarr.size(); i++) {
			Object obj = convertJSON2Bean(jarr.getJSONObject(i), beanClass, collectionComponentTypes);
			if (obj != null) {
				list.add(obj);
			}
		}
		return list;
	}

	// 把jsArray转换为制定Class类型的集合
	public static void initCollection(Collection<Object> c, JSONArray jarr, Class componentType,
			Map<String, Object> collectionComponentTypes) throws Exception {
		// component of collection is primitive
		if (componentType == null || isPrimitive(componentType)) {
			for (int i = 0; i < jarr.size(); i++)
				c.add(jarr.get(i));
		} else if (componentType == java.util.Date.class) {
			for (int i = 0; i < jarr.size(); i++)
				c.add(new java.util.Date((long) jarr.getDouble(i)));
		} else if (componentType == java.sql.Date.class) {
			for (int i = 0; i < jarr.size(); i++)
				c.add(new java.sql.Date((long) jarr.getDouble(i)));
		} else if (componentType == java.sql.Time.class) {
			for (int i = 0; i < jarr.size(); i++)
				c.add(new java.sql.Time((long) jarr.getDouble(i)));
		} else if (componentType == java.sql.Timestamp.class) {
			for (int i = 0; i < jarr.size(); i++)
				c.add(new java.sql.Timestamp((long) jarr.getDouble(i)));
		} else if (componentType == java.util.Calendar.class) {
			for (int i = 0; i < jarr.size(); i++) {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.setTimeInMillis((long) jarr.getDouble(i));
				c.add(cal);
			}
		} else if (componentType != null) {
			for (int i = 0; i < jarr.size(); i++) {
				Object obj = convertJSON2Bean(jarr.getJSONObject(i), componentType, collectionComponentTypes);
				if (obj != null) {
					c.add(obj);
				}
			}
		}
	}

	public static String[] jsonArrToStrArr(JSONArray jarr) throws Exception {
		if (jarr == null || jarr == null)
			return new String[0];
		String[] s = new String[jarr.size()];
		for (int j = 0; j < jarr.size(); j++) {
			if (jarr.get(j) == null)
				s[j] = null;
			else
				s[j] = jarr.getString(j);
		}
		return s;
	}

	public static boolean isPrimitive(Class<?> clazz) {
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == clazz)
				return true;
		}

		return false;
	}

	/**
	 * 判断类是否是基础数组类
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isPrimitiveArray(Class<?> clazz) {
		for (int i = 0; i < arrayClasses.length; i++) {
			if (arrayClasses[i] == clazz)
				return true;
		}

		return false;
	}

	/**
	 * 判断类是否是时间类集合
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isPrimitiveTime(Class<?> clazz) {
		for (int i = 0; i < timeClasses.length; i++) {
			if (timeClasses[i] == clazz)
				return true;
		}
		return false;
	}

	/**
	 * 判断类是否是collection类集合
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isPrimitiveCollection(Class<?> clazz) {
		for (int i = 0; i < collectionClasses.length; i++) {
			if (collectionClasses[i] == clazz)
				return true;
		}
		return false;
	}

	/**
	 * 判断类是否是map类集合
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isPrimitiveMap(Class<?> clazz) {
		for (int i = 0; i < mapClasses.length; i++) {
			if (mapClasses[i] == clazz)
				return true;
		}
		return false;
	}

	/**
	 * 判断类是否是基础类或者是基础集合类
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isSpecificClass(Class<?> clazz) {
		return isPrimitive(clazz) || isPrimitiveArray(clazz) || isPrimitiveTime(clazz);
	}

	/**
	 * 基本数据类型
	 */
	public static Class[] classes = { byte.class, short.class, int.class, long.class, float.class, double.class,
			boolean.class, char.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
			Boolean.class, Character.class, String.class, BigDecimal.class };

	public static Class[] arrayClasses = { byte[].class, short[].class, int[].class, long[].class, float[].class,
			double[].class, boolean[].class, char[].class, Byte[].class, Short[].class, Integer[].class, Long[].class,
			Float[].class, Double[].class, Boolean[].class, Character[].class, String[].class, BigDecimal[].class };

	/**
	 * 时间类
	 */
	public static Class[] timeClasses = { java.util.Date.class, java.sql.Date.class, java.sql.Time.class,
			java.sql.Timestamp.class, java.util.Calendar.class, java.util.Date[].class, java.sql.Date[].class,
			java.sql.Time[].class, java.sql.Timestamp[].class, java.util.Calendar[].class };

	/**
	 * collection集合类
	 */
	public static Class[] collectionClasses = { Collection.class, List.class, ArrayList.class, LinkedList.class,
			Vector.class, Stack.class, Set.class, HashSet.class, LinkedHashSet.class, SortedSet.class, TreeSet.class };

	/**
	 * map集合类
	 */
	public static Class[] mapClasses = { Map.class, HashMap.class, Hashtable.class, TreeMap.class, WeakHashMap.class };

	/**
	 * 获得类
	 * 
	 * @param paramType
	 * @return
	 */
	public static Class getType(Class paramType) {
		Class type = null;
		for (int i = 0; i < arrayClasses.length; i++) {
			if (paramType == arrayClasses[i]) {
				type = classes[i];
			}
		}
		return type;
	}

	/**
	 * 判断是否是时间
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isDate(Object value) {
		return value instanceof Date || value instanceof Calendar;
	}

	/**
	 * 得到时间数据
	 * 
	 * @param value
	 * @return
	 */
	public static Object getTime(Object value) {
		if (value != null && value instanceof Date)
			return new Long(((Date) value).getTime());
		else if (value != null)
			return new Long(((Calendar) value).getTimeInMillis());
		else
			return null;
	}

	public static String formatDate(Date date, String dateFormat) {
		if (StringUtils.isEmpty(dateFormat)) {
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat objSDateFormat = new SimpleDateFormat(dateFormat);
		return objSDateFormat.format(date);
	}

	/**
	 * JSON后台传到前台为中文的地方
	 * 
	 * @param str
	 * @return
	 */
	public static String transformJava2json(String str) {
		return str.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"").replaceAll("\t", "\\\\t")
				.replaceAll("\n", "\\\\n").replaceAll("\f", "\\\\f").replaceAll("\r", "\\\\r")
				.replaceAll("\b", "\\\\b");
	}

	public static Map<String, String> jsonToMap(String json) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isEmpty(json)) {
			return map;
		}
		JSONObject jsonObj = JSONObject.fromObject(json);
		Iterator<String> iter = jsonObj.keys();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			String val = jsonObj.getString(key);
			map.put(key, "null".equals(val) || "NULL".equals(val) ? "" : val.toString());
		}
		return map;
	}
}