package com.unieap.base.task;

import java.util.Iterator;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.unitls.SoapCallUtils;

@Configuration
@EnableScheduling
public class TaskHeartCheck {

	@Scheduled(cron = "0/2 * * * * *")
	public void heartCheck() throws Exception {
		Map<String, Map<String, Object>> esbServerConfig = UnieapCacheMgt.esbServerConfig;
		Iterator<?> iter = esbServerConfig.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			Map<String, Object> data = esbServerConfig.get(key);
			if (data != null && data.get("connect_app") != null) {
				String[] connect_app = data.get("connect_app").toString().split(",");
				for (String app : connect_app) {
					String url = esbServerConfig.get(app).get("url").toString();
					/*if (SoapCallUtils.heardCheck(url)) {
						UnieapCacheMgt.heartCheckResult.put(app, UnieapConstants.YES);
					} else {
						UnieapCacheMgt.heartCheckResult.put(app, UnieapConstants.NO);
					}*/
				}
			}
		}

	}
}
