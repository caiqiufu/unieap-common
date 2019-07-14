package com.unieap.base.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.unieap.base.utils.DBUtils;
import com.unieap.base.vo.PaginationSupport;

@NoRepositoryBean
public interface UnieapRepository<T, Long> extends JpaSpecificationExecutor<T>, JpaRepository<T, Long> {

	/**
	 * 
	 * @param page
	 * @param pojo
	 * @throws Exception
	 */
	default void getPaginationDataByMysql(PaginationSupport page, Object pojo) throws Exception {
		getPaginationDataByMysql(page, pojo, true);
	}

	/**
	 * 
	 * @param page
	 * @param pojo
	 * @throws Exception
	 */
	default void getPaginationDataByMysql(PaginationSupport page, Object pojo, boolean filter) throws Exception {
		Sort sort = null;
		if (page.ASC.equals(page.getDir())) {
			sort = new Sort(Sort.Direction.ASC, page.getSort());
		} else {
			sort = new Sort(Sort.Direction.DESC, page.getSort());
		}
		Pageable pageable = PageRequest.of(page.getCurrentPage()-1, page.getPageSize(), sort);
		if (filter) {
			Criteria<T> criteria = new Criteria<>();
			DBUtils.setCriteria(criteria, pojo);
			Page<T> datas = this.findAll(criteria, pageable);
			page.setTotalCount((int) datas.getTotalElements());
			page.setItems(datas.getContent());
		} else {
			Page<T> datas = this.findAll(pageable);
			page.setTotalCount((int) datas.getTotalElements());
			page.setItems(datas.getContent());
		}
	}

	default void getPaginationDataByMysql(PaginationSupport page, Object pojo, Criteria<T> criteria) {
		Sort sort = null;
		if (page.ASC.equals(page.getSort())) {

			sort = new Sort(Sort.Direction.ASC, page.getSortStr());
		} else {
			sort = new Sort(Sort.Direction.DESC, page.getSortStr());
		}
		Pageable pageable = PageRequest.of(page.getStartIndex(), page.getPageSize(), sort);
		Page<T> datas = this.findAll(criteria, pageable);
		page.setTotalCount((int) datas.getTotalElements());
		page.setItems(datas.getContent());
	}
}
