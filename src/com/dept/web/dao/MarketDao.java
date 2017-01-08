package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Market;
import com.dept.web.general.util.DateUtils;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class MarketDao extends IbatisBaseDaoImpl<Market, Long>{

	public static final String NAME_SPACE_MARKET = "Market";
	
	/**
	 * 每日需计算的转让标
	 * @Title: searchMarketForDayComputer 
	 * @Description: TODO
	 * @param @return 设定文件 
	 * @return List<Market> 返回类型 
	 * @throws
	 */
    public List<Market> searchMarketForDayComputer(){
        
        long status = 0;
        
        return getObjList(NAME_SPACE_MARKET, status, "COMPUTER");
    }
    
    
    /**
	 * 每日需计算的转让标
	 * @Title: searchMarketForDayComputer 
	 * @Description: TODO
	 * @param @return 设定文件 
	 * @return List<Market> 返回类型 
	 * @throws
	 */
    public List<Market> findMarketByBorrowIdAndStatus(long borrowId,int status){
        
    	 Map<String,String> params = new HashMap<String,String>();
         
         params.put("borrowId", String.valueOf(borrowId));
         params.put("status", String.valueOf(status));
        
        return getObjList(NAME_SPACE_MARKET, params, "BORROWIDANDSTATUS");
    }
    
    public void delMarketByMarketId(long id)
    {
    	
    	delete(NAME_SPACE_MARKET, id, "MARKETID");
    }
}
