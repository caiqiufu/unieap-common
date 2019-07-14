package com.unieap.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unieap.base.pojo.MdmFileTask;

@Repository
public interface MdmFileTaskRepository extends JpaRepository<MdmFileTask, Long> {
	@Query(value = "select e from MdmFileTask e where e.executeStatus =?1", nativeQuery = false)
	public List<MdmFileTask> getProcessTaskList(String executeStatus);
}
