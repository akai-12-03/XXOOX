package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowRepaymentDao extends IbatisBaseDaoImpl<BorrowRepayment, Long>{

	public static final String NAME_SPACE_BORROW_REPAYMENT = "BorrowRepayment";

	/**
	 * 由状态查找还款计划
	 * @Title: queryRepaylistByStatus 
	 * @Description: TODO
	 * @param @param status
	 * @param @return 设定文件 
	 * @return List<BorrowRepayment> 返回类型 
	 * @throws
	 */
	public List<BorrowRepayment> queryRepaylistByStatus(int status){
	    
	    return getObjList(NAME_SPACE_BORROW_REPAYMENT, status, "STATUS");
	}
	
	/**
	 * 判断是否为最后一期
	 * @Title: subBorrowAndRepayment 
	 * @Description: TODO
	 * @param @param borrow_id
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
    public int subBorrowAndRepayment(long borrow_id) {
        
        return getSelectCount(NAME_SPACE_BORROW_REPAYMENT, borrow_id, "SUB_BORROW_AND_REPAYMENT");
    }
    
    /**
     * 
     * @Title: modifyRepaymentYes 
     * @Description: TODO
     * @param @param repay
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int modifyRepaymentYes(BorrowRepayment repay){
        
        return update(NAME_SPACE_BORROW_REPAYMENT, repay, "MODIFY_REPAYMENT_YES");
    }
    
    
    public Page<BorrowRepayment> queryRepaymentList(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_BORROW_REPAYMENT, pageRequest, "REPAYMENTLIST");
        
    }
    
    public List<BorrowRepayment> queryRepaymentListForExcel(Map<String, String> params) {
        
        return getObjList(NAME_SPACE_BORROW_REPAYMENT, params, "REPAYMENTLIST");
        
    }
}
