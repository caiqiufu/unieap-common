package com.unieap.base.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.unieap.base.UnieapConstants;
import com.unieap.base.file.bo.ExcelBO;
import com.unieap.base.file.bo.FileBO;
import com.unieap.base.file.bo.TxtBO;
import com.unieap.base.pojo.MdmFileArchive;
import com.unieap.base.pojo.MdmFileTask;
import com.unieap.base.repository.MdmFileTaskRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Configuration
@EnableScheduling
public class TaskFileProcess {

	@Autowired
	private MdmFileTaskRepository mdmFileTaskRepository;

	@Autowired
	private FileBO fileBO;
	@Autowired
	private ExcelBO excelBO;
	@Autowired
	private TxtBO txtBO;

	@Scheduled(cron = "0/2 * * * * *")
	public void fileProcess() throws Exception {
		List<MdmFileTask> tasks = mdmFileTaskRepository.getProcessTaskList("0");
		if (tasks != null && tasks.size() > 0) {
			for (MdmFileTask task : tasks) {
				JSONArray ja = JSONArray.fromObject(task.getFileIds());
				JSONObject jsonObj = JSONObject.fromObject(task.getParameters());
				for (int i = 0; i < ja.size(); i++) {
					String fileId = ja.getString(i);
					jsonObj.put("fileId", fileId);
					MdmFileArchive mdmFileArchive = fileBO.getFileArchive(Long.parseLong(fileId));
					if ("xlsx".equals(mdmFileArchive.getFileType())) {
						if("BU".equals(task.getTaskType())) {
							excelBO.importExcel(jsonObj, task.getHandlerName());
						}
						if("DL".equals(task.getTaskType())) {
							excelBO.exportExcel(jsonObj, task.getHandlerName());
						}
					}
					if ("txt".equals(mdmFileArchive.getFileType())) {
						if("BU".equals(task.getTaskType())) {
							txtBO.importTxt(jsonObj, task.getHandlerName());
						}
						if("DL".equals(task.getTaskType())) {
							txtBO.exportTxt(jsonObj, task.getHandlerName());
						}
					}
				}
				task.setExecuteStatus("1");
				task.setExecuteDate(UnieapConstants.getDateTime());
				mdmFileTaskRepository.save(task);
			}
		}
	}
}
