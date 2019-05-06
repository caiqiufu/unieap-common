package com.unieap.base.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unieap.base.UnieapConstants;
import com.unieap.base.pojo.MdmOperlog;

@Repository
public interface MdmOperlogRepository extends JpaRepository<MdmOperlog, Long> {
	default void saveAll(List<Object> datas) {
		if (datas != null && datas.size() > 0) {
			Long batchStartNo = UnieapConstants.getBatchSequence(datas.size()) - datas.size() + 1;
			List<MdmOperlog> logs = new ArrayList<MdmOperlog>();
			MdmOperlog log = null;
			for (Object data : datas) {
				log = (MdmOperlog) data;
				log.setId(batchStartNo);
				logs.add(log);
				batchStartNo = batchStartNo + 1;
			}
			this.saveAll(logs);
		}
	}

}
