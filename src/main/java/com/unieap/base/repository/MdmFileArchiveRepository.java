package com.unieap.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unieap.base.pojo.MdmFileArchive;

@Repository
public interface MdmFileArchiveRepository extends JpaRepository<MdmFileArchive, Long> {
	

}
