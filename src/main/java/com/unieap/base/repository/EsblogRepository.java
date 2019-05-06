package com.unieap.base.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unieap.base.UnieapConstants;
import com.unieap.base.pojo.Esblog;

@Repository
public interface EsblogRepository extends JpaRepository<Esblog, Long> {

	default void saveAll(List<Object> datas) {
		if (datas != null && datas.size() > 0) {
			Long batchStartNo = UnieapConstants.getBatchSequence(datas.size()) - datas.size() + 1;
			List<Esblog> logs = new ArrayList<Esblog>();
			Esblog log = null;
			//Date date = UnieapConstants.getDateTime();
			//java.sql.Timestamp date = new java.sql.Timestamp(new Date().getTime());
			for (Object data : datas) {
				log = (Esblog) data;
				log.setId(batchStartNo);
				log.setCreateDate(new java.sql.Timestamp(new Date().getTime()));
				logs.add(log);
				batchStartNo = batchStartNo + 1;
			}
			this.saveAll(logs);
		}
	}
}
