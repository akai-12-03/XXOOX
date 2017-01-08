package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.PlanSetting;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class PlanSettingDao extends IbatisBaseDaoImpl<PlanSetting, Long>{

	public static final String NAME_SPACE_PLANSETTING = "PlanSetting";

	/**
	 * 查找所有的配资设定
	 * @Title: queryAllPlanSetting 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<PlanSetting> 返回类型 
	 * @throws
	 */
	public Page<PlanSetting> queryAllPlanSetting(PageRequest<Map<String, String>> pageRequest) {
	    
	    return pageQuery(NAME_SPACE_PLANSETTING, pageRequest, "ALL");
	    
	}
}
