package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowTender;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowTenderDao extends IbatisBaseDaoImpl<BorrowTender, Long>{

    private static final String NAME_SPACE_BORROWTENDER = "BorrowTender";
    
    /**
     * 获取借款标的投标记录
     * @Title: queryTenderListByBorrow 
     * @Description: TODO
     * @param @param bid
     * @param @return 设定文件 
     * @return List<BorrowTender> 返回类型 
     * @throws
     */
    public List<BorrowTender> queryTenderListByBorrow(long bid) {
        
        return getObjList(NAME_SPACE_BORROWTENDER, bid, "BORROW");
        
    }
    
    /**
     * ID查找投标记录
     * @Title: queryTenderById 
     * @Description: TODO
     * @param @param tid
     * @param @return 设定文件 
     * @return BorrowTender 返回类型 
     * @throws
     */
    public BorrowTender queryTenderById(long tid){
        
        return getObj(NAME_SPACE_BORROWTENDER, tid, "ID");
    }
    
    /**
     * 第三方流水号查找投标记录
     * @Title: queryTenderByLoanNo 
     * @Description: TODO
     * @param @param tid
     * @param @return 设定文件 
     * @return BorrowTender 返回类型 
     * @throws
     */
    public BorrowTender queryTenderByLoanNo(String loanNo){
        
        return getObj(NAME_SPACE_BORROWTENDER, loanNo, "LOANNO");
    }
    
    /**
     * 用户投资详情
     * @param pageRequest
     * @return
     */
    public Page<BorrowTender> queryBorrowTender(PageRequest<Map<String, String>> pageRequest) {
	    
	    return pageQuery(NAME_SPACE_BORROWTENDER, pageRequest, "STATUS_USER_BORROWTENDER");
	    
	}
    
    /**
	 * 投标情况
	 * @Title: queryUserRecharge 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<UserRecharge> 返回类型 
	 * @throws
	 */
    public Page<BorrowTender> queryTbqk(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_BORROWTENDER, pageRequest, "TBQK");
        
    }
    /**
     * 还款时更新投标记录
     * @Title: modifyTender 
     * @Description: TODO
     * @param @param capital
     * @param @param interest
     * @param @param id
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int modifyTender(double capital, double interest, long id){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("money", String.valueOf(capital));
        params.put("interest", String.valueOf(interest));
        params.put("id", String.valueOf(id));
        
        return update(NAME_SPACE_BORROWTENDER, params, "REPAY_TENDER");
    }
    
    /**
     * 更改投标记录的状态
     * @Title: modifyTenderStatus 
     * @Description: TODO
     * @param @param status
     * @param @param id
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int modifyTenderStatus(int status, long id){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("status", String.valueOf(status));
        params.put("id", String.valueOf(id));
        
        return update(NAME_SPACE_BORROWTENDER, params, "TENDER_STATUS");
    }
    
}
