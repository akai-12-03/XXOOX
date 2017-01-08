package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowTender;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowCollectionDao extends IbatisBaseDaoImpl<BorrowCollection, Long>{

    private static final String NAME_SPACE_BORROWCOLLECTION = "BorrowCollection";
    
    /**
     * 获取投标记录的待收列表
     * @Title: queryTenderListByTender 
     * @Description: TODO
     * @param @param tid
     * @param @return 设定文件 
     * @return List<BorrowCollection> 返回类型 
     * @throws
     */
    public List<BorrowCollection> queryCollectionListByTender(long tid) {
        
        return getObjList(NAME_SPACE_BORROWCOLLECTION, tid, "TENDER");
        
    }
    
    /**
     * 还款时查询当期的待收记录
     * @Title: queryCollectionForRepayment 
     * @Description: TODO
     * @param @param bid
     * @param @param order
     * @param @return 设定文件 
     * @return List<BorrowCollection> 返回类型 
     * @throws
     */
    public List<BorrowCollection> queryCollectionForRepayment(long bid,int order){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("bid", String.valueOf(bid));
        params.put("order", String.valueOf(order));
        
        return getObjList(NAME_SPACE_BORROWCOLLECTION, params, "BORROW_FOR_REPAYMENT");
    }
     
    
    /**
     * 查询未还款的待收记录当前期
     * @Title: queryPeriodByStatus 
     * @Description: TODO
     * @param @param borrowid
     * @param @param status
     * @param @return 设定文件 
     * @return BorrowCollection 返回类型 
     * @throws
     */
    public BorrowCollection queryPeriodByStatus(long borrowid, int status){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("bid", String.valueOf(borrowid));
        params.put("status", String.valueOf(status));
        
        return getObj(NAME_SPACE_BORROWCOLLECTION, params, "BORROW_FOR_PERIOD");
    }
    
    public List<BorrowCollection> getBorrowCollectionDataByBorrowId(long borrowId) {
		// TODO Auto-generated method stub
		return getObjList(NAME_SPACE_BORROWCOLLECTION, borrowId, "DATABYBORROWID");
	}
}
