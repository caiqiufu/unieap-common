package com.unieap.base.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.repository.MdmOperlogRepository;

@Service
public class SaveOperlog {
	Logger logger = LoggerFactory.getLogger(SaveOperlog.class);
	@Autowired
	private MdmOperlogRepository mdmOperlogRepository;

	@Async // 通过@Async注解表明该方法是一个异步方法，如果注解在类级别，表明该类下所有方法都是异步方法，而这里的方法自动被注入使用ThreadPoolTaskExecutor
			// 作为 TaskExecutor
	public void executeAsyncTask() {
		saveOperlog();
	}

	public void saveOperlog() {
		List<Object> datas = UnieapCacheMgt.getPersistenceData("MDM_OPERLOG");
		if (datas != null && datas.size() > 0) {
			Date startTime = new Date();
			logger.debug("SaveOperlog start..." + startTime);
			List<Object> copyDatas = new ArrayList<Object>();
			if (!UnieapCacheMgt.persistenceListLock) {
				UnieapCacheMgt.persistenceListLock = true;
				copyDatas.addAll(datas);
				UnieapCacheMgt.getPersistenceData("MDM_OPERLOG").clear();
				datas.clear();
				UnieapCacheMgt.persistenceListLock = false;
				logger.debug("Copy esblist:" + copyDatas.size());
			}
			if (copyDatas.size() > 0) {
				asyncInvokeWithParameter(copyDatas);
				logger.debug("Save list:" + copyDatas.size());
			}
			long timeDuring = new Date().getTime() - startTime.getTime();
			logger.debug("Process size:" + copyDatas.size() + ",time during:" + timeDuring + ",avg(records/s):"
					+ (copyDatas.size() * 1000 / timeDuring));
			Date endTime = new Date();
			logger.debug("SaveOperlog end..." + endTime);
		}
	}

	@Async
	public void asyncInvokeWithParameter(List<Object> datas) {
		mdmOperlogRepository.saveAll(datas);
	}
}