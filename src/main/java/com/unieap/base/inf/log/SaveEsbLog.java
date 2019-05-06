package com.unieap.base.inf.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.unieap.base.pojo.Esblog;
import com.unieap.base.repository.EsbLogCacheMgt;
import com.unieap.base.repository.EsblogRepository;

@Service
public class SaveEsbLog {
	Logger logger = LoggerFactory.getLogger(SaveEsbLog.class);
	@Autowired
	private EsblogRepository esblogRepository;

	@Async // 通过@Async注解表明该方法是一个异步方法，如果注解在类级别，表明该类下所有方法都是异步方法，而这里的方法自动被注入使用ThreadPoolTaskExecutor
			// 作为 TaskExecutor
	public void executeAsyncTask() {
		saveEsbLog();
	}

	public void saveEsbLog() {
		List<Esblog> datas = EsbLogCacheMgt.getEsbPersistenceList();
		if (datas != null && datas.size() > 0) {
			Date startTime = new Date();
			logger.debug("TaskSaveEsbLog start..." + startTime);
			List<Object> copyDatas = new ArrayList<Object>();
			if (!EsbLogCacheMgt.esbLogListLock) {
				EsbLogCacheMgt.esbLogListLock = true;
				copyDatas.addAll(datas);
				EsbLogCacheMgt.getEsbPersistenceList().clear();
				EsbLogCacheMgt.esbLogListLock = false;
				logger.debug("Copy esblist:" + copyDatas.size());
			}
			if (copyDatas.size() > 0) {
				asyncInvokeWithParameter(copyDatas);
				logger.debug("Save esblist:" + copyDatas.size());
			}
			long timeDuring = new Date().getTime() - startTime.getTime();
			logger.debug("Process size:" + copyDatas.size() + ",time during:"
					+timeDuring+",avg(records/s):"+(copyDatas.size()*1000/timeDuring));
			Date endTime = new Date();
			logger.debug("TaskSaveEsbLog end..." + endTime);
		}
	}

	@Async
	public void asyncInvokeWithParameter(List<Object> datas) {
		esblogRepository.saveAll(datas);
	}
}