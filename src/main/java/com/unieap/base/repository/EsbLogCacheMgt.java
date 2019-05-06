package com.unieap.base.repository;

import java.util.ArrayList;
import java.util.List;

import com.unieap.base.pojo.Esblog;

/**
 * Feb 19, 2011 缓存管理
 */
public final class EsbLogCacheMgt {

	private static List<Esblog> esbPersistenceList = new ArrayList<Esblog>();

	public static List<Esblog> getEsbPersistenceList() {
		return esbPersistenceList;
	}

	public static boolean esbLogListLock = false;

	public static void setEsbLogVO(Esblog log) {
		esbPersistenceList.add(log);
	}

	public static void setEsbPersistenceList(List<Esblog> esbPersistenceList) {
		EsbLogCacheMgt.esbPersistenceList = esbPersistenceList;
	}

}
