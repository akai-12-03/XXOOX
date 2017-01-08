package com.dept.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.context.Constant;
import com.dept.web.dao.BorrowCollectionDao;
import com.dept.web.dao.BorrowDao;
import com.dept.web.dao.BorrowRepaymentDao;
import com.dept.web.dao.BorrowTenderDao;
import com.dept.web.dao.BorrowWishDao;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.UserAccountLogDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.BorrowWish;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.Message;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.StringUtils;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class BorrowService {
	
    @Autowired
    private BorrowDao borrowDao;
    
    @Autowired
    private BorrowRepaymentDao borrowRepaymentDao;
    
    @Autowired
    private BorrowTenderDao borrowTenderDao;
    
    @Autowired
    private BorrowWishDao borrowWishDao;
    
    @Autowired
    private UserAccountDao userAccountDao;
    
    @Autowired
    private UserAccountLogDao userAccountLogDao;
    @Autowired
    private HongbaoDao hongbaoDao;
    
    @Autowired
    private BorrowCollectionDao borrowCollectionDao;
    
    /**
     * 创建新的借款
     * @Title: createBorrow 
     * @Description: TODO
     * @param @param borrow
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createBorrow(Borrow borrow){
        
        return (Long) borrowDao.save(borrow);
    }
    /**
     * 查询发标个数和总金额
     */
    public Borrow getBorrowCountAndMoney(){
    	return borrowDao.getBorrowCountAndMoney();
    }
    
    /**
     * 用户投标明细表
     * @param pageRequest
     * @return
     */
    public Page<BorrowTender> queryBorrowTender(PageRequest<Map<String, String>> pageRequest){
        
        return borrowTenderDao.queryBorrowTender(pageRequest);
      }
    
    /**
     * 根据状态查询标
     * @param status
     * @return
     */
    public List<Borrow> queryBorrowListByStatus(int status){
    	
    	return borrowDao.queryBorrowListByStatus(status);
    }
    
    /**
     * 借款人情况
     * @param pageRequest
     * @return
     */
    public Page<Borrow> queryBorrow(PageRequest<Map<String, String>> pageRequest){
        
        return borrowDao.queryBorrow(pageRequest);
      }
    /**
     * 查询标
     * @Title: queryBorrowPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<Borrow> 返回类型 
     * @throws
     */
    public Page<Borrow> queryBorrowPage(PageRequest<Map<String, String>> pageRequest){
        
        return borrowDao.queryBorrowPage(pageRequest);
    }
    
    
    /**
     * ID获取borrow
     * @Title: queryBorrowById 
     * @Description: TODO
     * @param @param bid
     * @param @return 设定文件 
     * @return Borrow 返回类型 
     * @throws
     */
    public Borrow queryBorrowById(long bid){
        
        return borrowDao.queryBorrowById(bid);
    }
    
    /**
     *  更新标信息
     * @Title: updateBorrow 
     * @Description: TODO
     * @param @param borrow
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateBorrow(Borrow borrow){
        
        return borrowDao.update(borrow);
    }
    
    
    /**
     * 获取复审通过的理财产品
     * @Title: queryReVerifyBorrowList 
     * @Description: TODO
     * @param @param status
     * @param @return 设定文件 
     * @return List<Borrow> 返回类型 
     * @throws
     */
    public List<Borrow> queryReVerifyBorrowList(int status, int opType){
        
        Map<String,Integer> params = new HashMap<String,Integer>();
        params.put("status", status);
        params.put("opType", opType);
        
        return borrowDao.queryReVerifyBorrowList(params);
    }
    
    /**
     * 更新标状态
     * @Title: updateBorrowForStatus 
     * @Description: TODO
     * @param @param bid
     * @param @param status
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateBorrowForStatus(long bid, int status){
        
        Map<String,String> params = new HashMap<String,String>();
        params.put("id", String.valueOf(bid));
        params.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
        params.put("status", String.valueOf(status));
        params.put("ip", "1.1.1.1");
        return borrowDao.updateBorrowForStatus(params);
    }
    
    /**
     * 批量生成还款计划
     * @Title: addBatchRepayment 
     * @Description: TODO
     * @param @param repaylist 设定文件 
     * @return void 返回类型 
     * @throws
     */
    public void addBatchRepayment(List<BorrowRepayment> repaylist){
        
        for (int i = 0; i < repaylist.size(); i++) {
            
            borrowRepaymentDao.save(repaylist.get(i));
            
        }
    }
    
    /**
     * 根据状态查找还款计划
     * 
     * @Title: queryRepaymentForStatus 
     * @Description: TODO
     * @param @param status
     * @param @return 设定文件 
     * @return List<BorrowRepayment> 返回类型 
     * @throws
     */
    public List<BorrowRepayment> queryRepaymentForStatus(int status){
        
        return borrowRepaymentDao.queryRepaylistByStatus(status);
    }
    
    /**
     * 获取标的投标记录
     * @Title: queryTenderByBorrow 
     * @Description: TODO
     * @param @param bid
     * @param @return 设定文件 
     * @return List<BorrowTender> 返回类型 
     * @throws
     */
    public List<BorrowTender> queryTenderByBorrow(long bid){
        
        return borrowTenderDao.queryTenderListByBorrow(bid);
    }
    
    /**
     * 查询过期的标
     * @Title: queryOverBorrow 
     * @Description: TODO
     * @param @return 设定文件 
     * @return List<Borrow> 返回类型 
     * @throws
     */
    public List<Borrow> queryOverBorrow(){
        
        return borrowDao.queryOverBorrow();
    }
    
    public Page<BorrowWish> queryBorrowWishPage(PageRequest<Map<String, String>> pageRequest){
    	return borrowWishDao.queryBorrowWishPage(pageRequest);
    }
    /**
     * 更新标状态
     * @Title: updateBorrowStatus 
     * @Description: TODO
     * @param @param status
     * @param @param id 设定文件 
     * @return void 返回类型 
     * @throws
     */
    public void updateBorrowStatus(int status, long id){
        
        borrowDao.updateBorrowStatus( status, id);
        
    }
    
    public void autoVerifyFullFail(Borrow borrow) {
    	Borrow borrows =borrowDao.queryBorrowById(borrow.getId());
		if (borrows.getStatus()==43) {
			System.out.println("该借款标满标审核未通过!");
			return ;
		}
		List<BorrowTender> tenderList=borrowTenderDao.queryTenderListByBorrow(borrow.getId());
		for(BorrowTender t:tenderList){
			double tenderValue=t.getAccount();
			userAccountDao.updateAccountNotZero(0, tenderValue, -tenderValue, t.getUserId());
			UserAccount act=userAccountDao.queryByUser(t.getUserId());
			
			UserAccountLog accountLog = new UserAccountLog();
			
			accountLog.setUserId(t.getUserId());
			
			accountLog.setMoneyOperate(t.getAccount());
			
			accountLog.setMoneyTotal(act.getMoneyTotal());
			accountLog.setMoneyCollection(act.getMoneyCollection());
			accountLog.setMoneyInsure(act.getMoneyInsure());
			accountLog.setMoneyTenderFreeze(act.getMoneyTenderFreeze());
			accountLog.setMoneyUsable(act.getMoneyUsable());
			accountLog.setMoneyWithdraw(act.getMoneyWithdraw());
			
			accountLog.setCreatedIp(t.getAddip());
			accountLog.setCreatedAt(System.currentTimeMillis()/1000);
			accountLog.setType(Constant.ACCOUNT_LOG_TYPE_TB_JDZJ);
			accountLog.setRemark("理财产品"+t.getBorrowId()+"审核不通过，解冻资金"+t.getAccount()+"元");
			
			userAccountLogDao.insertUserAccountLog(accountLog);
			
			 //投资失败，返还红包。
            if(t.getHongbao_id()!=null && t.getHongbao_id()>0){
                Hongbao hb=	hongbaoDao.queryHongbaoById(t.getHongbao_id());
                if(hb!=null){
                	 //修改红包状态为成功。已使用
                	hb.setStatus(0);
                	hongbaoDao.updateHongbao(hb);
                }
            }
		}
		//更改标的状态 49
		borrowDao.updateBorrowStatus(43, borrow.getId());
	}
    
    public List<BorrowCollection> getBorrowCollectionDataByBorrowId(long borrowId)
    {
    	return borrowCollectionDao.getBorrowCollectionDataByBorrowId(borrowId);
    }
    
    public Page<BorrowRepayment> getRepaymentList(PageRequest<Map<String, String>> pageRequest){
    	return borrowRepaymentDao.queryRepaymentList(pageRequest);
    }
    
    
    public List<BorrowRepayment> queryRepaymentList(Map<String, String> params){
    	return borrowRepaymentDao.queryRepaymentListForExcel(params);
    }
	public List<Borrow> queryBorrow(Map<String, String> params) {
		// TODO Auto-generated method stub
		return borrowDao.queryBorrow(params);
	}
    
}
