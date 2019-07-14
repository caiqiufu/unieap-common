package com.unieap.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unieap.base.pojo.MdmFileProcessResult;

@Repository
public interface MdmFileProcessRepository extends JpaRepository<MdmFileProcessResult, Long> {
}
