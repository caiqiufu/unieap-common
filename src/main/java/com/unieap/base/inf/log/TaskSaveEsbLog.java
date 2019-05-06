package com.unieap.base.inf.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class TaskSaveEsbLog {

	@Autowired
	private SaveEsbLog saveEsbLog;

	@Scheduled(cron = "0/2 * * * * *")
	public void saveEsbLog() {
		saveEsbLog.executeAsyncTask();
	}
}
