package com.dept.web.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dept.web.context.Constant;
import com.dept.web.context.Global;
import com.dept.web.controller.sq.model.LoanInfoBean;
import com.dept.web.controller.sq.model.LoanReturnInfoBean;
import com.dept.web.controller.sq.utils.Common;
import com.dept.web.controller.sq.utils.HttpClientUtil;
import com.dept.web.controller.sq.utils.RsaHelper;
import com.dept.web.dao.BorrowCollectionDao;
import com.dept.web.dao.BorrowDao;
import com.dept.web.dao.BorrowRepaymentDao;
import com.dept.web.dao.BorrowTenderDao;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.MarketDao;
import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.UserAccountLogDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.Market;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.general.util.BorrowUtil;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.NewsDateUtils;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.mmsg.SendMessageUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class JobService {
	
	
	private static final Logger LOGGER = Logger.getLogger(JobService.class);
	
    @Autowired
    private BorrowDao borrowDao;
    
    @Autowired
    private BorrowTenderDao borrowTenderDao;
    
    @Autowired
    private UserAccountDao userAccountDao;
    
    @Autowired
    private UserAccountLogDao userAccountLogDao;
    
    @Autowired
    private BorrowCollectionDao borrowCollectionDao;
    
    @Autowired
    private BorrowRepaymentDao borrowRepaymentDao;
    
    @Autowired
    private HongbaoDao hongbaoDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BorrowTenderService borrowTenderService;
    
    @Autowired
    private MarketDao marketDao;
    
    /**
     * 处理通过复审的理财产品，并放款
     * @Title: exVerifyBorrow 
     * @Description: TODO
     * @param @param borrow
     * @param @return 设定文件 
     * @return boolean 返回类型 
     * @throws
     */
    public boolean exVerifyBorrow(Borrow borrow) throws Exception{
        
        try {
            
            //获取投标记录
            List<BorrowTender> tenderlist = borrowTenderDao.queryTenderListByBorrow(borrow.getId());
            
            for (int i = 0; i < tenderlist.size(); i++) {
                
                BorrowTender tender = tenderlist.get(i);
                
                if(tender.getStatus()==Constant.BORROW_TENDER_STATUS_XJ){
                    
                    //扣除投资人的冻结资金，生成待收本金
                    int count = userAccountDao.updAccountByVerify(tender.getAccount(), tender.getInterest(), tender.getUserId());
                    
                    if(count>0){
                        
                        UserAccount newua = userAccountDao.queryByUser(tender.getUserId());

                        UserAccountLog ual = new UserAccountLog();
                        
                        //新建资金记录
                        ual.setUserId(tender.getUserId());
                        ual.setType(Constant.ACCOUNT_LOG_TYPE_JKKCDJZJ);
                        ual.setMoneyOperate(tender.getAccount()+tender.getInterest());
                        ual.setMoneyTotal(newua.getMoneyTotal());
                        ual.setMoneyUsable(newua.getMoneyUsable());
                        ual.setMoneyWithdraw(newua.getMoneyWithdraw());
                        ual.setMoneyInsure(newua.getMoneyInsure());
                        ual.setRemark("扣除理财产品编号为"+borrow.getId()+"的冻结金额"+tender.getAccount()+"元， 并生成待收本金"+tender.getAccount()+"和待收利息"+tender.getInterest());
                        ual.setCreatedAt(NewsDateUtils.getNowTimeStr());
                        ual.setCreatedIp("1.1.1.1");
                        ual.setMoneyTenderFreeze(newua.getMoneyTenderFreeze());
                        ual.setMoneyCollection(newua.getMoneyCollection());
                        userAccountLogDao.save(ual);
                        
                        
                        String startTime= DateUtils.getDateStrByDay(1);
                        String content="【钱功夫】尊敬的用户您好，您投资的产品"+borrow.getName()+",于"+startTime+"开始计息，投资期限为"+borrow.getTimeLimit();
                        if(borrow.getIsDay()==1)
                        {
                        	content+="天";
                        }
                        else
                        {
                        	content+="个月";
                        }
                        
	                    User user=userService.queryByUserId(tender.getUserId());
	                    SendMessageUtil.sendSMSByHuaxin(user.getMobile(), content);
                           
                    }else{
                        
                        break;
                    }
                    
                    //奖励部分
                    double awardValue=0;
                    if(borrow.getAward()==1){// 按投标金额
                    	awardValue=borrow.getFunds()/borrow.getAccount()*tender.getAccount();
                    }else if(borrow.getAward()==2){// 按比例
                        awardValue= (tender.getAccount()*borrow.getPartAccount()/100);
                    }else{
                        awardValue=0;
                    }
                    if(awardValue>0){
                       String mid = userService.queryMidByUserId(tender.getUserId());
                       if(mid !=null){
                    		 awardReturn(String.valueOf(NumberUtil.format2(awardValue)),borrow.getId(), mid,tender.getUserId());
//                    	   }
                       }else{
                    	   LOGGER.error("奖励金额返现失败，未找到用户"+tender.getUserId()+"的第三方平台标识");
                       }
                    }
                    
                    //修改Tender表中的待收利息
                    tender.setWaitAccount(tender.getAccount());
                    tender.setWaitInterest(tender.getInterest());
                    tender.setStatus(Constant.TENDER_STATUS_CG);
                    borrowTenderDao.update(tender);
                    
                    //确定待收记录的还款时间
                    List<BorrowCollection> bclist = borrowCollectionDao.queryCollectionListByTender(tender.getId());
                    for (int j = 0; j < bclist.size(); j++) {
                        
                        BorrowCollection bc = bclist.get(j);
                        
                        bc.setRepayTime(BorrowUtil.getRepayTime(borrow,bc.getColOrder()).getTime()/1000);
                        bc.setUpdatetime(DateUtils.getNowTimeStr());
                        bc.setUpdateip("1.1.1.1");
                        borrowCollectionDao.update(bc);
                        
                    }
                    
                    //投资成功返还红包。
                    if(tender.getHongbao_id()!=null && tender.getHongbao_id()>0){
                    	//将金额加入账户
	                    Hongbao hb=	hongbaoDao.queryHongbaoById(tender.getHongbao_id());
	                    if(hb!=null){
	                    	giveHongbao(tender.getHongbao_id(),String.valueOf(tender.getBorrowId()));
	                    }
                    }
                }
            }
            
            //借款入账并生成资金记录
            
            int count3 = 0;
            
            if(borrow.getBorrowType()==1)//垫资标
            {
            	count3= userAccountDao.updAccountByBorrower(borrow.getAccount(), borrow.getReceivePerson());
            }
            else
            {
            	count3=userAccountDao.updAccountByBorrower(borrow.getAccount(), borrow.getUserId());
            }
            

            if(count3>0){
                
                UserAccount borroweracc =null;
                if(borrow.getBorrowType()==1)//垫资标
                {
                	borroweracc=userAccountDao.queryByUser(borrow.getReceivePerson());
                }
                else
                {
                	borroweracc=userAccountDao.queryByUser(borrow.getUserId());
                }

                UserAccountLog borrowerlog = new UserAccountLog();
                
                //新建资金记录
                if(borrow.getBorrowType()==1)//垫资标
                {
                	borrowerlog.setUserId(borrow.getReceivePerson());
                }
                else
                {
                	 borrowerlog.setUserId(borrow.getUserId());
                }
                borrowerlog.setType(Constant.ACCOUNT_LOG_TYPE_JKRZ);
                borrowerlog.setMoneyOperate(borrow.getAccount());
                borrowerlog.setMoneyTotal(borroweracc.getMoneyTotal());
                borrowerlog.setMoneyUsable(borroweracc.getMoneyUsable());
                borrowerlog.setMoneyWithdraw(borroweracc.getMoneyWithdraw());
                borrowerlog.setMoneyInsure(borroweracc.getMoneyInsure());
                borrowerlog.setRemark("理财编号为"+borrow.getId()+"借款入账"+borrow.getAccount()+"元成功");
                borrowerlog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                borrowerlog.setCreatedIp("1.1.1.1");
                borrowerlog.setMoneyTenderFreeze(borroweracc.getMoneyTenderFreeze());
                borrowerlog.setMoneyCollection(borroweracc.getMoneyCollection());
                
                userAccountLogDao.save(borrowerlog);
                
            }
            
            double totalAwardValue=0;
            String remark="扣除理财编号为"+borrow.getId()+"的投资奖励";
            
            if(borrow.getAward()==1){// 按投标金额
                totalAwardValue = borrow.getFunds();
                
            }else if(borrow.getAward()==2){// 按比例
                totalAwardValue= (borrow.getAccount()*borrow.getPartAccount())/100;
            }else{
                totalAwardValue=0;
            }
            
            if(totalAwardValue>0){
                
                int count4 =0;
                if(borrow.getBorrowType()==1)//垫资标
                {
                	count4=userAccountDao.updAccountByAward(-totalAwardValue, borrow.getReceivePerson());
                }
                else
                {
                	count4=userAccountDao.updAccountByAward(-totalAwardValue, borrow.getUserId());
                }
                
                if(count4>0){
                    
                    UserAccount awardtolacc =null;
                    if(borrow.getBorrowType()==1)//垫资标
                    {
                    	awardtolacc=userAccountDao.queryByUser(borrow.getReceivePerson());
                    }
                    else
                    {
                    	awardtolacc=userAccountDao.queryByUser(borrow.getUserId());
                    }

                    UserAccountLog awardtollog = new UserAccountLog();
                    
                    //新建资金记录
                    if(borrow.getBorrowType()==1)//垫资标
                    {
                    	awardtollog.setUserId(borrow.getReceivePerson());
                    }
                    else
                    {
                    awardtollog.setUserId(borrow.getUserId());
                    }
                    awardtollog.setType(Constant.ACCOUNT_LOG_TYPE_JKRZ_KCJL);
                    awardtollog.setMoneyOperate(totalAwardValue);
                    awardtollog.setMoneyTotal(awardtolacc.getMoneyTotal());
                    awardtollog.setMoneyUsable(awardtolacc.getMoneyUsable());
                    awardtollog.setMoneyWithdraw(awardtolacc.getMoneyWithdraw());
                    awardtollog.setMoneyInsure(awardtolacc.getMoneyInsure());
                    awardtollog.setRemark(remark+totalAwardValue+"元");
                    awardtollog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                    awardtollog.setCreatedIp("1.1.1.1");
                    awardtollog.setMoneyTenderFreeze(awardtolacc.getMoneyTenderFreeze());
                    awardtollog.setMoneyCollection(awardtolacc.getMoneyCollection());
                    
                    userAccountLogDao.save(awardtollog);
                       
                }
            }
            
            deductFee(borrow);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus()
            .setRollbackOnly();
            return false;
        }
    }
    
    public void deductFee(Borrow borrow){
		//扣除交易手续费
		if(borrow.getBorrow_fee()==null)
		{
			borrow.setBorrow_fee(0d);
		}
		if(borrow.getAccount()==null)
		{
			borrow.setAccount(0d);
		}
		double fee=Double.valueOf(borrow.getBorrow_fee());
//		double fee = Global.getDouble("borrow_fee");
		fee = Double.valueOf(borrow.getAccount())*fee/100;
		
		if(fee>0){
			UserAccount act =null;
			if(borrow.getBorrowType()==1)//垫资标
			{
				userAccountDao.updAccountByBorrower(-fee,borrow.getReceivePerson());//扣除手续费
				act = userAccountDao.queryByUser(borrow.getReceivePerson());
			}
			else
			{
				userAccountDao.updAccountByBorrower(-fee,borrow.getUserId());//扣除手续费
				act = userAccountDao.queryByUser(borrow.getUserId());
			}
			
			 //新建扣除资金记录
			UserAccountLog awardtollog=new UserAccountLog();
			 if(borrow.getBorrowType()==1)//垫资标
             {
             	awardtollog.setUserId(borrow.getReceivePerson());
             }
             else
             {
             awardtollog.setUserId(borrow.getUserId());
             }
            awardtollog.setType(Constant.ACCOUNT_LOG_TYPE_JKRZ_SXF);
            awardtollog.setMoneyOperate(fee);
            awardtollog.setMoneyTotal(act.getMoneyTotal());
            awardtollog.setMoneyUsable(act.getMoneyUsable());
            awardtollog.setMoneyWithdraw(act.getMoneyWithdraw());
            awardtollog.setMoneyInsure(act.getMoneyInsure());
            awardtollog.setRemark("扣除手续费"+fee+"元");
            awardtollog.setCreatedAt(NewsDateUtils.getNowTimeStr());
            awardtollog.setCreatedIp("1.1.1.1");
            awardtollog.setMoneyTenderFreeze(act.getMoneyTenderFreeze());
            awardtollog.setMoneyCollection(act.getMoneyCollection());
            
            userAccountLogDao.save(awardtollog);
		}
	}
    
    
    /**
     * 还款操作
     * @Title: exRepay 
     * @Description: TODO
     * @param @param repay
     * @param @return
     * @param @throws Exception 设定文件 
     * @return boolean 返回类型 
     * @throws
     */
    public boolean exRepay(BorrowRepayment repay) throws Exception{
        
        if(repay.getStatus()==0 && repay.getWebstatus()==1){
            Borrow borrow = borrowDao.queryBorrowById(repay.getBorrowId());
            
            if(borrow!=null){
                
                double capital= BigDecimal.valueOf(repay.getCapital()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                double interest=BigDecimal.valueOf(repay.getInterest()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                
                //还款（网站垫付情况），要求加入 还款日之前的逾期利息
                double lateInterest= 0;
                boolean isWebPay = false;
                
                //逾期的标要扣除逾期的利息（逾期的罚金）
                double repayLateInterest = 0;
                List<BorrowCollection> collectList=borrowCollectionDao.queryCollectionForRepayment(borrow.getId(),repay.getRepOrder());
                
                /********调用第三方接口还款分账---资金释放*****/
    			try {
    				String flag=repaymentAudit(collectList,repay);
    				if(flag =="" && !"88".equals(flag))
    				{
    					return false;
    				}
    			} catch (Exception e) {
    				e.printStackTrace();
    				return false;
    			}
    			/********调用第三方接口还款分账---资金释放*****/
                
                if(repay.getWebstatus() == 1 && repay.getStatus() == Constant.BORROW_REPAYMENT_STATUS_YQ 
                        && repay.getLateInterest()!= null){
                    
                    isWebPay = true;
//                    lateInterest = repay.getLateInterest() ;
                    
//                    lateInterest =BigDecimal.valueOf(repay.getLateInterest()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                      lateInterest =0;
                    //扣除逾期罚款利息
                    if(lateInterest > 0){
//                      userAccountDao.updateAccountByRepaymentUnfreeze(lateInterest, borrow.getUserId());
                        UserAccount act = userAccountDao.queryByUser(borrow.getUserId());
                        
                        UserAccountLog ualog = new UserAccountLog();
                        
                        //新建资金记录
                        ualog.setUserId(borrow.getUserId());
                        ualog.setType(Constant.ACCOUNT_LOG_TYPE_HK_KCFX);
                        ualog.setMoneyOperate(lateInterest);
                        ualog.setMoneyTotal(act.getMoneyTotal());
                        ualog.setMoneyUsable(act.getMoneyUsable());
                        ualog.setMoneyWithdraw(act.getMoneyWithdraw());
                        ualog.setMoneyInsure(act.getMoneyInsure());
                        ualog.setRemark("理财产品编号为"+borrow.getId()+"还款，网站垫付扣除罚息"+lateInterest+"元");
                        ualog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                        ualog.setCreatedIp("1.1.1.1");
                        ualog.setMoneyTenderFreeze(act.getMoneyTenderFreeze());
                        ualog.setMoneyCollection(act.getMoneyCollection());
                        
                        userAccountLogDao.save(ualog);
                        
                    }
                }else{
                    
//                    double lateInterestStr   = repay.getLateInterest();
//                    if(lateInterestStr>0){ //不能为空
//                        repayLateInterest = BigDecimal.valueOf(lateInterestStr).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
//                    }
                    repayLateInterest =0;
                }
                
                double repaymentAccount=BigDecimal.valueOf(repay.getRepaymentAccount()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                userAccountDao.updateAccountByRepaymentUnfreeze(repaymentAccount, borrow.getUserId());
                //从用户冻结账户中扣除还款本金
                if(capital>0){
                    //userAccountDao.updateAccountByRepaymentUnfreeze(capital, borrow.getUserId());
                    
                    UserAccount actca = userAccountDao.queryByUser(borrow.getUserId());
                    
                    UserAccountLog caualog = new UserAccountLog();
                    
                    //新建资金记录
                    caualog.setUserId(borrow.getUserId());
                    caualog.setType(Constant.ACCOUNT_LOG_TYPE_HK_KCBJ);
                    caualog.setMoneyOperate(capital);
                    caualog.setMoneyTotal(actca.getMoneyTotal()+interest);
                    caualog.setMoneyUsable(actca.getMoneyUsable());
                    caualog.setMoneyWithdraw(actca.getMoneyWithdraw());
                    caualog.setMoneyInsure(actca.getMoneyInsure());
                    caualog.setRemark("理财产品编号为"+borrow.getId()+"还款，扣除本金"+capital+"元");
                    caualog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                    caualog.setCreatedIp("1.1.1.1");
                    caualog.setMoneyTenderFreeze(actca.getMoneyTenderFreeze()+interest);
                    caualog.setMoneyCollection(actca.getMoneyCollection());
                    
                    userAccountLogDao.save(caualog);

                }
                
                //扣除还款利息
                if(interest>0){
//                    userAccountDao.updateAccountByRepaymentUnfreeze(interest, borrow.getUserId());
                    
                    UserAccount actin = userAccountDao.queryByUser(borrow.getUserId());
                    
                    UserAccountLog inualog = new UserAccountLog();
                    
                    //新建资金记录
                    inualog.setUserId(borrow.getUserId());
                    inualog.setType(Constant.ACCOUNT_LOG_TYPE_HK_KCLX);
                    inualog.setMoneyOperate(interest);
                    inualog.setMoneyTotal(actin.getMoneyTotal());
                    inualog.setMoneyUsable(actin.getMoneyUsable());
                    inualog.setMoneyWithdraw(actin.getMoneyWithdraw());
                    inualog.setMoneyInsure(actin.getMoneyInsure());
                    inualog.setRemark("理财产品编号为"+borrow.getId()+"还款，扣除利息"+interest+"元");
                    inualog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                    inualog.setCreatedIp("1.1.1.1");
                    inualog.setMoneyTenderFreeze(actin.getMoneyTenderFreeze());
                    inualog.setMoneyCollection(actin.getMoneyCollection());
                    
                    userAccountLogDao.save(inualog);
                    
                }
                 
//                double lastInter=repayAccount-capital-interest;
//                if(lastInter>0)
//                {
//                	lastInter=BigDecimal.valueOf(lastInter).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
//                	userAccountDao.updateAccountByRepaymentUnfreeze(lastInter, borrow.getUserId());
//                }
                
              
                
                
                //扣除逾期的利息
                if(repayLateInterest>0){
                    
                    userAccountDao.updateAccountByRepaymentUnfreeze(repayLateInterest, borrow.getUserId());
                    
                    UserAccount actLatein = userAccountDao.queryByUser(borrow.getUserId());
                    
                    UserAccountLog lateinualog = new UserAccountLog();
                    
                    //新建资金记录
                    lateinualog.setUserId(borrow.getUserId());
                    lateinualog.setType(Constant.ACCOUNT_LOG_TYPE_HK_KCLX);
                    lateinualog.setMoneyOperate(repayLateInterest);
                    lateinualog.setMoneyTotal(actLatein.getMoneyTotal());
                    lateinualog.setMoneyUsable(actLatein.getMoneyUsable());
                    lateinualog.setMoneyWithdraw(actLatein.getMoneyWithdraw());
                    lateinualog.setMoneyInsure(actLatein.getMoneyInsure());
                    lateinualog.setRemark("理财产品编号为"+borrow.getId()+"还款，扣除逾期利息"+repayLateInterest+"元");
                    lateinualog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                    lateinualog.setCreatedIp("1.1.1.1");
                    lateinualog.setMoneyTenderFreeze(actLatein.getMoneyTenderFreeze());
                    lateinualog.setMoneyCollection(actLatein.getMoneyCollection());
                    
                    userAccountLogDao.save(lateinualog);
                    
                }              
                
                
                //还款操作
                if(!isWebPay){
                    
                    for (int i = 0; i < collectList.size(); i++) {
                        
                        BorrowCollection bc = collectList.get(i);
                        
                        double cCapital=BigDecimal.valueOf(bc.getCapital()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                        double cInterest=BigDecimal.valueOf(bc.getInterest()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();

                        BorrowTender repaytender = borrowTenderDao.queryTenderById(bc.getTenderId());

                        //归还投资人本金
                        if(cCapital>0){
                            
                            userAccountDao.updateAccountByRepayment(cCapital, repaytender.getUserId());
                            
                            UserAccount actrepay = userAccountDao.queryByUser(repaytender.getUserId());
                            
                            UserAccountLog repaylog = new UserAccountLog();
                            
                            //新建资金记录
                            repaylog.setUserId(repaytender.getUserId());
                            repaylog.setType(Constant.ACCOUNT_LOG_TYPE_HK_GHBJ);
                            repaylog.setMoneyOperate(cCapital);
                            repaylog.setMoneyTotal(actrepay.getMoneyTotal());
                            repaylog.setMoneyUsable(actrepay.getMoneyUsable());
                            repaylog.setMoneyWithdraw(actrepay.getMoneyWithdraw());
                            repaylog.setMoneyInsure(actrepay.getMoneyInsure());
                            repaylog.setRemark("理财产品编号为"+borrow.getId()+"还款，归还本金"+cCapital+"元");
                            repaylog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                            repaylog.setCreatedIp("1.1.1.1");
                            repaylog.setMoneyTenderFreeze(actrepay.getMoneyTenderFreeze());
                            repaylog.setMoneyCollection(actrepay.getMoneyCollection());
                            
                            userAccountLogDao.save(repaylog);
                        }
                        
                        //归还投资人利息
                        if(cInterest>0){
                            
                            userAccountDao.updateAccountByRepayment(cInterest, repaytender.getUserId());
                            
                            UserAccount actrepayIn = userAccountDao.queryByUser(repaytender.getUserId());
                            
                            UserAccountLog repayInlog = new UserAccountLog();
                            
                            //新建资金记录
                            repayInlog.setUserId(repaytender.getUserId());
                            repayInlog.setType(Constant.ACCOUNT_LOG_TYPE_HK_GHLX);
                            repayInlog.setMoneyOperate(cInterest);
                            repayInlog.setMoneyTotal(actrepayIn.getMoneyTotal());
                            repayInlog.setMoneyUsable(actrepayIn.getMoneyUsable());
                            repayInlog.setMoneyWithdraw(actrepayIn.getMoneyWithdraw());
                            repayInlog.setMoneyInsure(actrepayIn.getMoneyInsure());
                            repayInlog.setRemark("理财产品编号为"+borrow.getId()+"还款，归还利息"+cInterest+"元");
                            repayInlog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                            repayInlog.setCreatedIp("1.1.1.1");
                            repayInlog.setMoneyTenderFreeze(actrepayIn.getMoneyTenderFreeze());
                            repayInlog.setMoneyCollection(actrepayIn.getMoneyCollection());
                            
                            userAccountLogDao.save(repayInlog);
                            
                            //扣除投资人利息管理费
                            //double tender_mange_fee= Double.valueOf(Global.SYSTEMINFO.getValue("tender_mange_fee"));
                            double tender_mange_fee = 0;
                            
                            if(cInterest*tender_mange_fee>0){
                                
                                userAccountDao.updateAccountByUsable(cInterest*tender_mange_fee, repaytender.getUserId());
                                
                                UserAccount feeact = userAccountDao.queryByUser(repaytender.getUserId());
                                
                                UserAccountLog feeactlog = new UserAccountLog();
                                
                                //新建资金记录
                                feeactlog.setUserId(repaytender.getUserId());
                                feeactlog.setType(Constant.ACCOUNT_LOG_TYPE_HK_GHLX_GLF);
                                feeactlog.setMoneyOperate(cInterest*tender_mange_fee);
                                feeactlog.setMoneyTotal(feeact.getMoneyTotal());
                                feeactlog.setMoneyUsable(feeact.getMoneyUsable());
                                feeactlog.setMoneyWithdraw(feeact.getMoneyWithdraw());
                                feeactlog.setMoneyInsure(feeact.getMoneyInsure());
                                feeactlog.setRemark("理财产品编号为"+borrow.getId()+"还款，扣除利息管理费"+cInterest*tender_mange_fee+"元");
                                feeactlog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                                feeactlog.setCreatedIp("1.1.1.1");
                                feeactlog.setMoneyTenderFreeze(feeact.getMoneyTenderFreeze());
                                feeactlog.setMoneyCollection(feeact.getMoneyCollection());
                                
                                userAccountLogDao.save(repayInlog);
                            }
                            
                        }
                        
                        //还投资人 逾期的利息  按投资的比例来计算
                        double totalRepay = repay.getCapital(); //借款标总的金额
                        
                        if(repayLateInterest > 0){
                            
                            double tenderLateInterest = NumberUtil.format2(repayLateInterest*(cCapital/totalRepay));
                            
                            if(tenderLateInterest>0){
                                
                                userAccountDao.updateAccountByUsable(tenderLateInterest, repaytender.getUserId());
                                
                                UserAccount actlateIn = userAccountDao.queryByUser(repaytender.getUserId());
                                
                                UserAccountLog actlateInlog = new UserAccountLog();
                                
                                //新建资金记录
                                actlateInlog.setUserId(repaytender.getUserId());
                                actlateInlog.setType(Constant.ACCOUNT_LOG_TYPE_HK_GHYQLX);
                                actlateInlog.setMoneyOperate(cInterest*tenderLateInterest);
                                actlateInlog.setMoneyTotal(actlateIn.getMoneyTotal());
                                actlateInlog.setMoneyUsable(actlateIn.getMoneyUsable());
                                actlateInlog.setMoneyWithdraw(actlateIn.getMoneyWithdraw());
                                actlateInlog.setMoneyInsure(actlateIn.getMoneyInsure());
                                actlateInlog.setRemark("理财产品编号为"+borrow.getId()+"还款，归还逾期利息"+cInterest*tenderLateInterest+"元");
                                actlateInlog.setCreatedAt(NewsDateUtils.getNowTimeStr());
                                actlateInlog.setCreatedIp("1.1.1.1");
                                actlateInlog.setMoneyTenderFreeze(actlateIn.getMoneyTenderFreeze());
                                actlateInlog.setMoneyCollection(actlateIn.getMoneyCollection());
                                
                                userAccountLogDao.save(actlateInlog);                         
                                
                            }
                            
                            bc.setLateDays(repay.getLateDays());
                            bc.setLateInterest(tenderLateInterest);
                        }
                        
                        //更新tender记录
                        borrowTenderDao.modifyTender(cCapital, cInterest, repaytender.getId());
                        
                        
                        //更新collection记录
                        bc.setStatus(Constant.BORROW_COLLECTION_STATUS_YHK);
                        bc.setRepayYestime(DateUtils.getNowTimeStr());
                        bc.setRepayYesaccount(bc.getRepayAccount());
                        borrowCollectionDao.update(bc);
                        
            			//发送还款短信
                        BigDecimal allMoney = BigDecimal.valueOf(bc.getRepayAccount()).setScale(2, BigDecimal.ROUND_DOWN);//总金额
                        double resultMoney = allMoney.doubleValue();
                        String Amount1 = String.valueOf(resultMoney);
                        
                        String content="【钱功夫】尊敬的用户您好，您投资的产品"+borrow.getName()+"收到回款";
                        if (cCapital>0) {
                        	content +="（本息）:";
                        } else {
                        	content +="（利息）:";
                        }
                        content = content + Amount1 +"元，并已成功转入您的钱功夫账户，请您查收。";
                        
	                    User user=userService.queryByUserId(repaytender.getUserId());
	                    SendMessageUtil.sendSMSByHuaxin(user.getMobile(), content);
                    }
                    
                }
                //还款完成，先将还款表中的状态改为已还款。
                repay.setRepaymentYestime(DateUtils.getNowTimeStr());
                repay.setStatus(Constant.BORROW_REPAYMENT_STATUS_YH);
                repay.setRepaymentYesaccount(capital+interest+lateInterest);
                
                borrowRepaymentDao.modifyRepaymentYes(repay);
                
                //判断是否是最后一期， 如果是最后一期，修改为已还款6，不是最后一期，修改为5.
                int repayStatus=Constant.BORROW_STATUS_HKZ;
                /**
                 * 判断是否为最后一期还款
                 */     
                if(isLastP(repay.getBorrowId(), repay.getCapital()+repay.getInterest(),repay.getRepaymentAccount())){
                    repayStatus=Constant.BORROW_STATUS_YHK;
                }
                Map<String, String> fip = new HashMap<String,String>();
                fip.put("status", String.valueOf(repayStatus));
                fip.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
                fip.put("ip", "1.1.1.1");
                fip.put("id", String.valueOf(borrow.getId()));
                
                borrowDao.updateBorrowForStatus(fip);
                
               
                
            }
        }
        return false;
    }
    
    
    /**
     * 判断是否为最后一期且还完款
     * @param borrow_id
     * @param repay_num   还款金额
     * @param repayment_num  应还款金额
     * @return
     */
    public boolean isLastP(long borrow_id, double repay_num, double repayment_num){
        
        int periods = 0;
        
        Borrow borrow = borrowDao.queryBorrowById(borrow_id);
        
        
        /**
         * 如果是一次性还款
         */
        if(borrow.getRepaymentStyle()!=null && borrow.getRepaymentStyle()==Constant.BORROW_REPAYMENT_STYLE_DQHBHX){
            if((repay_num-repayment_num)>=0.00){
                return true;
            }
        }
        
        periods = borrowRepaymentDao.subBorrowAndRepayment(borrow.getId());
        periods=borrow.getTimeLimit()-periods;
        //是否为最后一期
        if(periods==0){
            if((repay_num-repayment_num)<=0.0001){
                return true;
            }
        }
        
        return false;
    }
    
    
    /**
     * 投标取消时解冻资金
     * @Title: unfreezeTender 
     * @Description: TODO
     * @param @param tender
     * @param @return 设定文件 
     * @return boolean 返回类型 
     * @throws
     */
    public boolean unfreezeTender(BorrowTender tender, long bid){
        
        if(tender.getStatus()==Constant.TENDER_STATUS_XJ){
        	
        	/**********************钱多多托管**************/
    		String AuditType = "2";
    		String PlatformMoneymoremore = Global.getValue("qdd_PlatformMoneymoremore");
    		//获取前台项目的根目录
    		String LoanNoList = "";// 乾多多流水号列表(投标返回的流水号LoanNo：LN12345678901234；用，隔开)
    		String SubmitURLPrefix=Global.getValue("qdd_submitUrl");
        	String SubmitURL="";
        	String myProject=Common.myProject;
        	if(myProject.equals("test"))
			{
				SubmitURL = SubmitURLPrefix+"/loan/toloantransferaudit.action";
			}
			else
			{
				SubmitURL = "https://audit."+SubmitURLPrefix+"/loan/toloantransferaudit.action";
			}
        	String ReturnURL = Global.getValue("qdd_notifyCmsUrl")
					+ "/qddApi/LoanTransferAuditReturn.html";
			String NotifyURL = Global.getValue("qdd_notifyCmsUrl")
					+ "/qddApi/LoanTransferAuditNotify.html";
			
    		String SignInfo = "";
    		String privatekey = Common.privateKeyPKCS8;
    		LoanNoList=tender.getLoanNo();
    		String Remark1="";
    		String Remark2="";
    		String Remark3="";
    		String dataStr = LoanNoList + PlatformMoneymoremore + AuditType  + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
    		// 签名
    		RsaHelper rsa = RsaHelper.getInstance();
    		
    		SignInfo = rsa.signData(dataStr, privatekey);
    		
    		Map<String, String> req = new HashMap<String, String>();
    		req.put("LoanNoList", LoanNoList);
			req.put("PlatformMoneymoremore", PlatformMoneymoremore);
			req.put("AuditType", AuditType);
			req.put("Remark1", Remark1);
			req.put("Remark2", Remark2);
			req.put("Remark3", Remark3);
			req.put("ReturnURL", ReturnURL);
			req.put("NotifyURL", NotifyURL);
			req.put("SignInfo", SignInfo);
			LOGGER.info("撤标流标过期标接口发送数据"+SignInfo.toString());
    		String[] result =HttpClientUtil.doPostQueryCmd(SubmitURL, req);
    		LOGGER.info("撤标流标过期标接口返回数据"+result.toString());
			JSONObject obj = JSONObject.parseObject(result[1]);
//			String Message = obj.getString("Message");
			String ResultCode=obj.getString("ResultCode");
			if("88".equals(ResultCode)){
				//解冻资金
	            int count = userAccountDao.updateAccountByUnFreeze(tender.getAccount(), tender.getUserId());
	            
	            if(count>0){
	                
	                UserAccount ua = userAccountDao.queryByUser(tender.getUserId());
	                
	                UserAccountLog ual = new UserAccountLog();
	                
	                //新建资金记录
	                ual.setUserId(tender.getUserId());
	                ual.setType(Constant.ACCOUNT_LOG_TYPE_TB_JDZJ);
	                ual.setMoneyOperate(tender.getAccount());
	                ual.setMoneyTotal(ua.getMoneyTotal());
	                ual.setMoneyUsable(ua.getMoneyUsable());
	                ual.setMoneyWithdraw(ua.getMoneyWithdraw());
	                ual.setMoneyInsure(ua.getMoneyInsure());
	                ual.setRemark("理财产品编号为"+bid+"流标，解冻"+tender.getAccount()+"元");
	                ual.setCreatedAt(NewsDateUtils.getNowTimeStr());
	                ual.setCreatedIp("1.1.1.1");
	                ual.setMoneyTenderFreeze(ua.getMoneyTenderFreeze());
	                ual.setMoneyCollection(ua.getMoneyCollection());
	                
	                userAccountLogDao.save(ual);
	                
	                //更新标状态
	                borrowTenderDao.modifyTenderStatus(Constant.TENDER_STATUS_SB, tender.getId());
	                
	                
	              //投资失败，返还红包。
	                if(tender.getHongbao_id()!=null && tender.getHongbao_id()>0){
	                    Hongbao hb=	hongbaoDao.queryHongbaoById(tender.getHongbao_id());
	                    if(hb!=null){
	                    	 //修改红包状态为成功。已使用
	                    	hb.setStatus(0);
	                    	hongbaoDao.updateHongbao(hb);
	                    }
	                }
	                
	                
	                return true;
	                
	            }else{
	                
	                return false;
	            }
			}else{
				return false;
			}
        }else{
            
            return false;
        }
    /*}*/
    
    
  /*  *//**
     * 每日计算未被转让的
     * @Title: computerTransferDebt 
     * @Description: TODO
     * @param @param market
     * @param @return 设定文件 
     * @return boolean 返回类型 
     * @throws
     *//*
    public boolean computerTransferDebt(Market market){
        
        Borrow borrow = borrowDao.queryBorrowById(market.getBorrowId());
        
//        List<BorrowRepayment> repayment = borrowRepaymentDao.q
        
        BorrowTender tender = borrowTenderDao.queryTenderById(market.getTenderId());
        
        double collectionMoney = 0;

        
        // 天标处理
        if(borrow.getIsDay()==1){
            
            int remaindays = market.getRemainingDays()-1;

            //获取还款时间
            market.setRemainingDays(remaindays);
            collectionMoney = remaindays/borrow.getTimeLimit()*tender.getRepaymentAccount();
            market.setCollectionMoney(collectionMoney);
            market.setUpdateAt(DateUtils.getNowTimeStr());
            market.setUpdateBy(1L);  //表示系统
            
            marketDao.update(market);
            
        //一次性还款的标处理    
        }else if(borrow.getRepaymentStyle()== Constant.BORROW_REPAYMENT_STYLE_DQHBHX){ 
            
            int remaindays = borrow.getTimeLimit()*30-1;
            collectionMoney = remaindays/borrow.getTimeLimit()*30*tender.getRepaymentAccount();
            market.setRemainingDays(remaindays);
            market.setCollectionMoney(collectionMoney);
            market.setUpdateAt(DateUtils.getNowTimeStr());
            market.setUpdateBy(1L);  //表示系统
            
            marketDao.update(market);
        
        //等额本息的标处理    
        }else if(borrow.getRepaymentStyle()== Constant.BORROW_REPAYMENT_STYLE_DEBX){
            
            int remaindays = borrow.getTimeLimit()*30-1;
            market.setRemainingDays(remaindays);
            
            collectionMoney = remaindays/borrow.getTimeLimit()*30*tender.getRepaymentAccount();
            market.setCollectionMoney(collectionMoney);
            
            BorrowCollection collection = borrowCollectionDao.queryPeriodByStatus(market.getBorrowId(), Constant.BORROW_COLLECTION_STATUS_HKZ);
            
            market.setRepayOrder(collection.getColOrder());
            market.setRepayTotalOrder(borrow.getTimeLimit());
            
            market.setUpdateAt(DateUtils.getNowTimeStr());
            market.setUpdateBy(1L);  //表示系统
            
            marketDao.update(market);
            
        //每月还息到期还本    
        }else if(borrow.getRepaymentStyle()== Constant.BORROW_REPAYMENT_STYLE_MYHX){
            */
//            int remaindays = borrow.getTimeLimit()*30-1;
//            
//            market.setRemainingDays(remaindays);
//            
//            //判断是否为最后一期
//            BorrowCollection collection = borrowCollectionDao.queryPeriodByStatus(market.getBorrowId(), Constant.BORROW_COLLECTION_STATUS_HKZ);
//            
//            if(collection.getColOrder()==borrow.getTimeLimit()){
//                
//                collectionMoney = remaindays/30*collection.getRepayAccount();
//                
//                market.setCollectionMoney(collectionMoney);
//                
//            }else{
//                
//                //暂时空
//                
//            }
//            
//            market.setRepayOrder(collection.getColOrder());
//            market.setRepayTotalOrder(borrow.getTimeLimit());
//            
//            market.setUpdateAt(DateUtils.getNowTimeStr());
//            market.setUpdateBy(1L);  
/*            
        }*/
        
        /*return true;*/
        
    }
	
	public void awardReturn(String awardValue, long bid, String mid,long userid){
		String remark = "收到理财产品编号为" + bid + "的投资奖励";
		// 返还奖励金额
		List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
		String pid = Global.getValue("qdd_PlatformMoneymoremore");
		// 参数集合
		String uuid = UUID.randomUUID().toString();
		String uuid2 = UUID.randomUUID().toString();
		/** 投资奖励 */
		LoanInfoBean mlib = new LoanInfoBean();
		mlib.setLoanOutMoneymoremore(pid);
		mlib.setLoanInMoneymoremore(mid);
		mlib.setOrderNo(uuid);
		mlib.setBatchNo(uuid2);
		mlib.setAmount(awardValue);
		mlib.setTransferName("奖励金额");
		mlib.setRemark("奖励金额");
		listmlib.add(mlib);

		// 组装签名信息
		String privatekey = Common.privateKeyPKCS8;
		String LoanJsonList = Common.JSONEncode(listmlib);
		String PlatformMoneymoremore = pid;
		int TransferAction = 2;
		int Action = 2;
		int TransferType = 2;
		int NeedAudit = 1;
		String NotifyURL = "";
		NotifyURL = Global.getValue("qdd_notifyCmsUrl")
				+ "/qddApi/backToAwardReturn.html";
		String Remark1 = "";
		String Remark2 = "";
		String Remark3 = "";
		String ReturnURL = Global.getValue("qdd_notifyCmsUrl")
				+ "/qddApi/backToAwardNotify.html";
		String dataStr = LoanJsonList + PlatformMoneymoremore + TransferAction
				+ Action + TransferType + NeedAudit + Remark1 + Remark2
				+ Remark3 + ReturnURL + NotifyURL;

		RsaHelper rsa = RsaHelper.getInstance();
		String SignInfo = rsa.signData(dataStr, privatekey);

		// 参数编码
		LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
		String myProject = Common.myProject;
		String SubmitURL = "";
		if (myProject.equals("test")) {
			SubmitURL = Global.getValue("qdd_submitUrl") + "/loan/loan.action";
		} else {
			SubmitURL = "https://transfer." + Global.getValue("qdd_submitUrl")
					+ "/loan/loan.action";
		}
		Map<String, String> paramsMap = new TreeMap<String, String>();
		paramsMap.put("SubmitURL", SubmitURL);
		paramsMap.put("LoanJsonList", LoanJsonList);
		paramsMap.put("PlatformMoneymoremore", pid);
		paramsMap.put("TransferAction", String.valueOf(TransferAction));
		paramsMap.put("Action", String.valueOf(Action));
		paramsMap.put("TransferType", String.valueOf(TransferType));
		paramsMap.put("NeedAudit", String.valueOf(NeedAudit));
		paramsMap.put("Remark1", Remark1);
		paramsMap.put("Remark2", Remark2);
		paramsMap.put("Remark3", Remark3);
		paramsMap.put("ReturnURL", ReturnURL);
		paramsMap.put("NotifyURL", NotifyURL);
		paramsMap.put("SignInfo", SignInfo);
		LOGGER.info("奖励接口发送数据："+paramsMap.toString());
		String[] res = HttpClientUtil.doPostQueryCmd(SubmitURL, paramsMap);
		LOGGER.info("奖励接口返回数据："+res.toString());			
//		JSONObject obj = JSONObject.parseObject(res[1]);
		JSONObject obj=null;
		try {
		JSONArray arr = JSONArray.parseArray(res[1]);
		obj=arr.getJSONObject(0);
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("奖励金额返回处理异常，异常信息：" + e);
			try{
				obj = JSONObject.parseObject(res[1]);
			}catch (Exception e1) {
				LOGGER.error("奖励金额返回处理异常，异常信息：" + e);
				e1.printStackTrace();
			}
		}
		if(obj!=null){
		String resultCode = obj.getString("ResultCode");
		String Message = obj.getString("Message");
		String resultLoanJsonList = obj.getString("LoanJsonList");
		resultLoanJsonList = Common.UrlDecoder(LoanJsonList, "utf-8");
		List<Object> list = Common.JSONDecodeList(resultLoanJsonList,
				LoanReturnInfoBean.class);
		LoanReturnInfoBean bean = (LoanReturnInfoBean) list.get(0);
		// 签名
		if ("88".equals(resultCode)) {
			
				User user = userService.queryByMidId(bean
						.getLoanInMoneymoremore());
				int count2 = userAccountDao.updAccountByAward(
						Double.valueOf(bean.getAmount()), user.getId());

				if (count2 > 0) {

					UserAccount awardacc = userAccountDao.queryByUser(user
							.getId());

					UserAccountLog awardlog = new UserAccountLog();

					// 新建资金记录
					awardlog.setUserId(user.getId());
					awardlog.setType(Constant.ACCOUNT_LOG_TYPE_TZCGJL);
					awardlog.setMoneyOperate(Double.valueOf(bean.getAmount()));
					awardlog.setMoneyTotal(awardacc.getMoneyTotal());
					awardlog.setMoneyUsable(awardacc.getMoneyUsable());
					awardlog.setMoneyWithdraw(awardacc.getMoneyWithdraw());
					awardlog.setMoneyInsure(awardacc.getMoneyInsure());
					awardlog.setRemark(remark
							+ Double.valueOf(bean.getAmount()) + "元");
					awardlog.setCreatedAt(NewsDateUtils.getNowTimeStr());
					awardlog.setCreatedIp("1.1.1.1");
					awardlog.setMoneyTenderFreeze(awardacc
							.getMoneyTenderFreeze());
					awardlog.setMoneyCollection(awardacc.getMoneyCollection());

					userAccountLogDao.save(awardlog);

				} else {
					LOGGER.error("奖励金额返现更新金额表失败");
				}
		}else{
			UserAccountLog awardlog = new UserAccountLog();
			UserAccount awardacc = userAccountDao.queryByUser(userid);
			// 新建资金记录
			awardlog.setType(Constant.ACCOUNT_LOG_TYPE_TZCGJL);
			awardlog.setMoneyOperate(Double.valueOf(awardValue));
			awardlog.setMoneyTotal(awardacc.getMoneyTotal());
			awardlog.setMoneyUsable(awardacc.getMoneyUsable());
			awardlog.setMoneyWithdraw(awardacc.getMoneyWithdraw());
			awardlog.setMoneyInsure(awardacc.getMoneyInsure());
			awardlog.setRemark("理财产品编号为" + bid + "的投资奖励返现失败"+Message);
			awardlog.setCreatedAt(NewsDateUtils.getNowTimeStr());
			awardlog.setCreatedIp("1.1.1.1");
			awardlog.setMoneyTenderFreeze(awardacc
					.getMoneyTenderFreeze());
			awardlog.setMoneyCollection(awardacc.getMoneyCollection());

			userAccountLogDao.save(awardlog);
			}
		}else {
			LOGGER.error("奖励金额返现解析报文异常");
		}
	}
	
	public void giveHongbao(long hbId,String borrowId){
		//送红包
		Hongbao hb= hongbaoDao.queryHongbaoById(hbId);
		if (hb != null) {
			String mid = userService.queryMidByUserId(hb.getUser_id());
			List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
			String pid = Global.getValue("qdd_PlatformMoneymoremore");
			String uuid = UUID.randomUUID().toString();
			// 参数集合
			/** 转账给投资返现红包 */
			LoanInfoBean mlib = new LoanInfoBean();
			mlib.setLoanOutMoneymoremore(pid);
			mlib.setLoanInMoneymoremore(mid);
			mlib.setOrderNo(uuid);
			mlib.setBatchNo(borrowId);
			mlib.setAmount(String.valueOf(NumberUtil.format2(hb.getMoney())));
			mlib.setTransferName("红包");
			mlib.setRemark("返现红包");
			listmlib.add(mlib);

			// 组装签名信息
			String privatekey = Common.privateKeyPKCS8;
			String LoanJsonList = Common.JSONEncode(listmlib);
			String PlatformMoneymoremore = pid;
			int TransferAction = 2;
			int Action = 2;
			int TransferType = 2;
			int NeedAudit = 1;
			String NotifyURL = "";
			NotifyURL = Global.getValue("qdd_notifyCmsUrl")
					+ "/qddApi/backToHongbaoReturn.html";
			String Remark1 = "";
			String Remark2 = "";
			String Remark3 = "";
			String ReturnURL = Global.getValue("qdd_notifyCmsUrl")
					+ "/qddApi/backToHongbaoNotify.html";
			String dataStr = LoanJsonList + PlatformMoneymoremore
					+ TransferAction + Action + TransferType + NeedAudit
					+ Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;

			RsaHelper rsa = RsaHelper.getInstance();
			String SignInfo = rsa.signData(dataStr, privatekey);

			// 参数编码
			LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
			String myProject = Common.myProject;
			String SubmitURL = "";
			if (myProject.equals("test")) {
				SubmitURL = Global.getValue("qdd_submitUrl")
						+ "/loan/loan.action";
			} else {
				SubmitURL = "https://transfer."
						+ Global.getValue("qdd_submitUrl")
						+ "/loan/loan.action";
			}
			Map<String, String> paramsMap = new TreeMap<String, String>();
			paramsMap.put("SubmitURL", SubmitURL);
			paramsMap.put("LoanJsonList", LoanJsonList);
			paramsMap.put("PlatformMoneymoremore", pid);
			paramsMap.put("TransferAction", String.valueOf(TransferAction));
			paramsMap.put("Action", String.valueOf(Action));
			paramsMap.put("TransferType", String.valueOf(TransferType));
			paramsMap.put("NeedAudit", String.valueOf(NeedAudit));
			paramsMap.put("Remark1", Remark1);
			paramsMap.put("Remark2", Remark2);
			paramsMap.put("Remark3", Remark3);
			paramsMap.put("ReturnURL", ReturnURL);
			paramsMap.put("NotifyURL", NotifyURL);
			paramsMap.put("SignInfo", SignInfo);
			LOGGER.info("红包返现接口发送数据："+paramsMap.toString());
			String[] res = HttpClientUtil.doPostQueryCmd(SubmitURL, paramsMap);
			LOGGER.info("红包返现接口返回数据："+res.toString());
			JSONObject obj=null;
			try {
			JSONArray resultArr = JSONArray.parseArray(res[1]);
			obj = resultArr.getJSONObject(0);
			}catch (Exception e) {
				e.printStackTrace();
				try{
					obj = JSONObject.parseObject(res[1]);
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if(obj!=null){
			String resultCode = obj.getString("ResultCode");
			String Message = obj.getString("Message");
			String resultLoanJsonList = obj.getString("LoanJsonList");
			resultLoanJsonList = Common.UrlDecoder(LoanJsonList, "utf-8");
			List<Object> list = Common.JSONDecodeList(resultLoanJsonList,
					LoanReturnInfoBean.class);
			LoanReturnInfoBean bean = (LoanReturnInfoBean) list.get(0);
			// 签名
				if ("88".equals(resultCode)) {
					User user = userService.queryByMidId(bean
							.getLoanInMoneymoremore());
					hb.setStatus(1);
					hb.setUpdatetime(TimeUtil.getNowTimeStr());
					hongbaoDao.updateHongbao(hb);
					// 修改账户余额。
					int count5 = userAccountDao.updAccountByBorrower(
							hb.getMoney(), user.getId());
					if (count5 > 0) {
						// 新建资金记录
						UserAccount uchb = userAccountDao.queryByUser(user
								.getId());
						UserAccountLog hbuclog = new UserAccountLog();
						hbuclog.setUserId(user.getId());
						hbuclog.setType(Constant.BORROW_HONGBAO_MONEY);
						hbuclog.setMoneyOperate(hb.getMoney());
						hbuclog.setMoneyTotal(uchb.getMoneyTotal());
						hbuclog.setMoneyUsable(uchb.getMoneyUsable());
						hbuclog.setMoneyWithdraw(uchb.getMoneyWithdraw());
						hbuclog.setMoneyInsure(uchb.getMoneyInsure());
						hbuclog.setRemark("投资理财产品编号为" + bean.getBatchNo()
								+ "成功,返还红包金额" + hb.getMoney() + "元。");
						hbuclog.setCreatedAt(NewsDateUtils.getNowTimeStr());
						hbuclog.setCreatedIp("1.1.1.1");
						hbuclog.setMoneyTenderFreeze(uchb
								.getMoneyTenderFreeze());
						hbuclog.setMoneyCollection(uchb.getMoneyCollection());
						userAccountLogDao.save(hbuclog);
					}
				}else{
					hb.setStatus(0);
					hongbaoDao.updateHongbao(hb);
					
					UserAccount uchb = userAccountDao.queryByUser(hb.getUser_id());
					UserAccountLog hbuclog = new UserAccountLog();
					hbuclog.setUserId(hb.getUser_id());
					hbuclog.setType(Constant.BORROW_HONGBAO_MONEY);
					hbuclog.setMoneyOperate(hb.getMoney());
					hbuclog.setMoneyTotal(uchb.getMoneyTotal());
					hbuclog.setMoneyUsable(uchb.getMoneyUsable());
					hbuclog.setMoneyWithdraw(uchb.getMoneyWithdraw());
					hbuclog.setMoneyInsure(uchb.getMoneyInsure());
					hbuclog.setRemark("投资理财产品编号为" + borrowId
							+ "失败,"+Message+",应返还红包金额" + hb.getMoney() + "元。");
					hbuclog.setCreatedAt(NewsDateUtils.getNowTimeStr());
					hbuclog.setCreatedIp("1.1.1.1");
					hbuclog.setMoneyTenderFreeze(uchb
							.getMoneyTenderFreeze());
					hbuclog.setMoneyCollection(uchb
							.getMoneyCollection());
					userAccountLogDao.save(hbuclog);
				}
			} else{
				LOGGER.error("红包返现接口失败，接口返回报文解析异常");
			}
		}else {
			LOGGER.error("红包返现接口失败，未找到id=" + hbId
					+ "的红包");
		} 
	}
	
	/**
	 * 还款审核
	 * 
	 * @TODO:
	 * @param map
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 */
	public String repaymentAudit(List<BorrowCollection> collectLists,
			BorrowRepayment repayment) throws Exception {
		String resultCode = "";
		try {
			String PlatformMoneymoremore = Global
					.getValue("qdd_PlatformMoneymoremore");
			String SubmitURLPrefix = Global.getValue("qdd_submitUrl");

			String myProject = Common.myProject;
			String SubmitURL="";
			if (myProject.equals("test")) {
				SubmitURL = SubmitURLPrefix
						+ "/loan/toloantransferaudit.action";
			} else {
				SubmitURL = "https://audit." + SubmitURLPrefix
						+ "/loan/toloantransferaudit.action";
			}
			String NotifyURL = Global.getValue("qdd_notifyCmsUrl")
					+ "/qdd/loantransferauditnotify.html";
			
			
			String ReturnURL = Global.getValue("qdd_notifyCmsUrl")
					+ "/qdd/loantransferauditreturn.html";
			String AuditType = "1";
			StringBuffer sb = new StringBuffer();
			for (BorrowCollection borrowCollect : collectLists) {
				if (!StringUtils.isEmpty(borrowCollect.getLoanNo())) {
					sb.append(borrowCollect.getLoanNo()).append(",");
				}
			}
			String LoanNoList="";
			if (sb.length() > 0) {
				LoanNoList = sb.substring(0, sb.length() - 1);
			}

			String privatekey = Common.privateKeyPKCS8;

			String dataStr = LoanNoList + PlatformMoneymoremore + AuditType
					+ ReturnURL + NotifyURL;
			// 签名
			RsaHelper rsa = RsaHelper.getInstance();
			String SignInfo = rsa.signData(dataStr, privatekey);

			Map<String, String> reqmap = new HashMap<String, String>();
			reqmap.put("AuditType", AuditType);
			reqmap.put("PlatformMoneymoremore", PlatformMoneymoremore);
			reqmap.put("LoanNoList", LoanNoList);
			reqmap.put("LoanNoListFail", LoanNoList);
			reqmap.put("NotifyURL", NotifyURL);
			reqmap.put("SubmitURL", SubmitURL);
			reqmap.put("ReturnURL", ReturnURL);
			reqmap.put("SignInfo", SignInfo);

			String[] result =HttpClientUtil.doPostQueryCmd(SubmitURL, reqmap);
			System.out.println("result0=====" + result[0]);
			System.out.println("result1=====" + result[1]);
			JSONObject obj = JSONObject.parseObject(result[1]);
			resultCode = obj.getString("ResultCode");
			String reLoanNoListFail=obj.getString("LoanNoListFail");
			if ("88".equals(resultCode)) {
				LOGGER.info("还款审核成功！");
				if(reLoanNoListFail!=null&&!"".equals(reLoanNoListFail.trim())&&!"null".equals(reLoanNoListFail)){
					LOGGER.info("还款审核失败的订单="+reLoanNoListFail);
				}
			} else {
				LOGGER.error("还款提交钱多多资金托管有误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return resultCode;
	}
}
