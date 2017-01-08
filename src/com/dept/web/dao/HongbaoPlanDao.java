package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.HongbaoPlan;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@SuppressWarnings("all")
@Repository
public class HongbaoPlanDao extends IbatisBaseDaoImpl<HongbaoPlan, Long>{

	private static final String NAME_SPACE_HONGBAOPLAN = "HongbaoPlan";
	
	  public Long getHongbaoPlanHongbaoId(Map map){
	    	
	    	Object o=getObjs(NAME_SPACE_HONGBAOPLAN, map, "BYIDS");
	    	if(o==null){
	    		return 0L;
	    	}else{
	    		return (Long)o;
	    	}
	    		
	    }
}
