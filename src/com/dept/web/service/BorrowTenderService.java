package com.dept.web.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.BorrowCollectionDao;
import com.dept.web.dao.BorrowTenderDao;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowTender;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class BorrowTenderService {
    
    @Autowired
    private BorrowTenderDao borrowTenderDao;
    
    @Autowired
    private BorrowCollectionDao borrowCollectionDao;
    /**
     * 查询投标情况
     * @Title: queryBorrowPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<Borrow> 返回类型 
     * @throws
     */
    public Page<BorrowTender> queryTbqk(PageRequest<Map<String, String>> pageRequest){
        
        return borrowTenderDao.queryTbqk(pageRequest);
    }
    
       
    /**      
     * @desc 用途描述: 根据第三方流水号查标
     * @param loanNo
     * @return 返回说明:
     * @exception 内部异常说明:
     * @throws 抛出异常说明:
     * @author gwx
     * @version 1.0      
     * @created 2016-3-2 下午6:06:04 
     * @mod 修改描述:
     * @modAuthor 修改人:
        
     */
    public BorrowTender queryTenderByLoanNo(String loanNo){
    	return borrowTenderDao.queryTenderByLoanNo(loanNo);
    }
    
    public List<BorrowTender> queryTenderListByBid(long bid){
    	return borrowTenderDao.queryTenderListByBorrow(bid);
    }
    
    public BorrowTender queryTenderById(long tid)
    {
    	return borrowTenderDao.queryTenderById(tid);
    }
    
    public void updateBorrowTender(BorrowTender tender)
    {
    	borrowTenderDao.update(tender);
    }
}
