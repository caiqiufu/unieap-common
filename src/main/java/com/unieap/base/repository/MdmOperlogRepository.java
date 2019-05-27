package com.unieap.base.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unieap.base.pojo.MdmOperlog;

@Repository
public interface MdmOperlogRepository extends JpaRepository<MdmOperlog, Long> {
	default void saveAll(List<Object> datas) {
		if (datas != null && datas.size() > 0) {
			// auto generate Id
			List<MdmOperlog> logs = new ArrayList<MdmOperlog>();
			for (Object data : datas) {
				MdmOperlog log = (MdmOperlog) data;
				logs.add(log);
			}
			this.saveAll(logs);
			/*Long batchStartNo = UnieapConstants.getBatchSequence(datas.size()) - datas.size() + 1;*/
		}
	}

}
