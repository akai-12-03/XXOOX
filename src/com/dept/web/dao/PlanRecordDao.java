package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.PlanRecord;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class PlanRecordDao extends IbatisBaseDaoImpl<PlanRecord, Long>{

	public static final String NAME_SPACE_PLANRECORD = "PlanRecord";

	/**
	 * 查找配资计划
	 * 
	 * @Title: queryPlanByStatus 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<PlanRecord> 返回类型 
	 * @throws
	 */
	public Page<PlanRecord> queryPlanByStatus(PageRequest<Map<String, String>> pageRequest) {
	    
	    return pageQuery(NAME_SPACE_PLANRECORD, pageRequest, "STATUS_FOR_SEARCH");
	    
	}
	
	/**
	 * 
	 * @Title: updatePlanRecord 
	 * @Description: TODO
	 * @param @param pr
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int updatePlanRecord(PlanRecord pr){
	    
	    return update(NAME_SPACE_PLANRECORD, pr, "STATUS_BY_ID");
	}
}
