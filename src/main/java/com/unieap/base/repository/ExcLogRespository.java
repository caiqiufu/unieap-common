package com.unieap.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unieap.base.pojo.MdmExclog;
@Repository
public interface ExcLogRespository extends JpaRepository<MdmExclog, Long>{

}
