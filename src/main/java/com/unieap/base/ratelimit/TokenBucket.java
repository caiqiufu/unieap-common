package com.unieap.base.ratelimit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import com.unieap.base.SYSConfig;

@Configuration
@EnableScheduling
public class TokenBucket {
	public static Map<String, Integer> tokens = new HashMap<String, Integer>();

	@Scheduled(cron = "0/1 * * * * *")
	public void addTokens() {
		refreshInfLimitationTokens();
	}

	public static void refreshInfLimitationTokens() {
		String limitationLicence = SYSConfig.getConfig().get("licence.controller.inf.limitation");
		if (StringUtils.isEmpty(limitationLicence)) {
			limitationLicence = "5";
		}
		tokens.put("licence.controller.inf.limitation", Integer.valueOf(limitationLicence));
	}

	public static void decreaseToken() {
		int limitationCurrent = tokens.get("licence.controller.inf.limitation").intValue();
		tokens.put("licence.controller.inf.limitation", Integer.valueOf(limitationCurrent - 1));
	}
}
