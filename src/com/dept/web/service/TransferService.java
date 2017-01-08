package com.dept.web.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.DebtTransferDao;
/*import com.dept.web.dao.MarketDao;
import com.dept.web.dao.model.Market;*/

@Service
@Transactional(rollbackFor=Exception.class)
public class TransferService {
	
/*	@Autowired
	private MarketDao marketDao;*/
	
	@Autowired
	private DebtTransferDao debtTransferDao;
	
	
/*	*//**
	 * 转让市场列表
	 * @Title: searchMarketList 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<Market> 返回类型 
	 * @throws
	 *//*
    public List<Market> searchMarketByDay(){
        
		return marketDao.searchMarketForDayComputer();
	}
	*/
    
   /* *//**
     * 更新转让市场
     * @Title: updateMark 
     * @Description: TODO
     * @param @param market
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     *//*
    public int updateMark(Market market){
        
        return marketDao.update(market);
    }
    */
}
