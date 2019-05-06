package com.unieap.base.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.unieap.base.bo.SaveOperlog;

@Configuration
@EnableScheduling
public class TaskSaveMdmOperlog {

	@Autowired
	private SaveOperlog saveOperlog;

	@Scheduled(cron = "0/2 * * * * *")
	public void saveOperlog() {
		saveOperlog.executeAsyncTask();
	}
}
