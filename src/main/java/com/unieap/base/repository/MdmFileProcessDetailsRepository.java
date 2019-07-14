package com.unieap.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unieap.base.pojo.MdmFileProcessDetails;

@Repository
public interface MdmFileProcessDetailsRepository extends JpaRepository<MdmFileProcessDetails, Long> {
}
