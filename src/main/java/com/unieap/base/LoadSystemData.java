package com.unieap.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.unieap.base.db.DBManager;
import com.unieap.base.handler.ConfigHandler;

@Service
public class LoadSystemData {
	private final Log logger = LogFactory.getLog(LoadSystemData.class);

	public void loadSystemConfigure() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT name,value FROM mdm_sys_config where activate_flag ='Y'");
		List<Map<String, Object>> datas = DBManager.getJT().queryForList(sql.toString());
		if (datas != null & !datas.isEmpty()) {
			Map<String, String> config = new HashMap<String, String>();
			String name, value = "";
			Map<String, Object> data;
			for (int i = 0; i < datas.size(); i++) {
				data = datas.get(i);
				if (data.get("name") != null) {
					name = data.get("name").toString();
					if (datas.get(i).get("value") != null) {
						value = data.get("value").toString();
					}
					config.put(name, value);
				}
			}
			config.put("bss.encryption.key", "1234567890111110");
			config.put("mcare.encryption.key", "1234567890543210");
			SYSConfig.setConfig(config);
		}
	}
	public void loadSystemHandler(String handlerType) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM mdm_handler_config where handler_type = ? and activate_flag=? order by seq asc");
		List<Map<String, Object>> datas = DBManager.getJT().queryForList(sql.toString(),
				new Object[] { handlerType, UnieapConstants.YES });
		if (datas != null & !datas.isEmpty()) {
			Map<String, Object> data;
			for (int i = 0; i < datas.size(); i++) {
				data = datas.get(i);
				if (data.get("handler_name") != null) {
					String handlerName = data.get("handler_name").toString();
					ConfigHandler configHandler = (ConfigHandler) ApplicationContextProvider.getBean(handlerName);
					if (configHandler != null) {
						try {
							configHandler.deal(null);
							logger.info("============================================================================");
							logger.info("***            handler["+handlerName+"] process done                     ***");
							logger.info("============================================================================");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
