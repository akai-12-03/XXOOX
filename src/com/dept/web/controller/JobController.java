package com.dept.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

//import com.alibaba.fastjson.JSONObject;
import com.dept.web.context.Constant;
//import com.dept.web.context.Global;
//import com.dept.web.controller.sq.utils.Common;
//import com.dept.web.controller.sq.utils.HttpClientUtil;
//import com.dept.web.controller.sq.utils.RsaHelper;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.SmsJopDao;
//import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.SmsJop;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.VerifyBorrowLog;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.service.BorrowService;
import com.dept.web.service.BorrowTenderService;
import com.dept.web.service.JobService;
import com.dept.web.service.UserService;
import com.dept.web.service.VerifyService;

/**
 * 
 * 
 * @ClassName:     JobController
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月22日 上午11:27:38 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Component
@Transactional(rollbackFor=Exception.class)
public class JobController extends WebController{
	private static final Logger LOGGER = Logger.getLogger(JobController.class);
    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private VerifyService verifyService;
    
    @Autowired
    private JobService jobService;
    
    @Autowired
   	private SmsJopDao smsJopDao;
    @Autowired
   	private HongbaoDao hongbaoDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BorrowTenderService borrowTenderService;
    
    /**自动复审
     * @Title: index 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
//    public synchronized void autoReVerifyBorrow() throws Exception{
//    	System.out.print("自动复审线程开始~~~~~~");
//    	  try {
//              //查询需要复审通过的标
//              List<Borrow> borrowlist=borrowService.queryReVerifyBorrowList(Constant.BORROW_STATUS_MBDFS, Constant.BORROW_VERIFY_OPT_TYPE_CSTG);
//              if(borrowlist.size()>0){
//  	            for (int i = 0; i < borrowlist.size(); i++) {
//  	            	/**********************钱多多托管**************/
//  	        		String AuditType = "";
//  	                AuditType = "1";// 审核类型(1.通过 2.退回 3.二次分配同意 4.二次分配不同意 5.提现通过 6.提现退回)
//  	        		String PlatformMoneymoremore = Global.getValue("qdd_PlatformMoneymoremore");
//  	        		//获取前台项目的根目录
//  	        		String LoanNoList = "";// 乾多多流水号列表(投标返回的流水号LoanNo：LN12345678901234；用，隔开)
//  	        		String SubmitURLPrefix=Global.getValue("qdd_submitUrl");
//  	            	String SubmitURL="";
//  	            	String myProject=Common.myProject;
//  	            	if(myProject.equals("test"))
//  					{
//  						SubmitURL = SubmitURLPrefix+"/loan/toloantransferaudit.action";
//  					}
//  					else
//  					{
//  						SubmitURL = "https://audit."+SubmitURLPrefix+"/loan/toloantransferaudit.action";
//  					}
//  	            	String ReturnURL = Global.getValue("qdd_notifyCmsUrl")
//  							+ "/qddApi/LoanTransferAuditReturn.html";
//  					String NotifyURL = Global.getValue("qdd_notifyCmsUrl")
//  							+ "/qddApi/LoanTransferAuditNotify.html";
//  					
//  	        		String SignInfo = "";
//  	        		String privatekey = Common.privateKeyPKCS8;
//  	        		StringBuffer sb= new StringBuffer();
//  	        		List<BorrowTender> borrowTenderList= borrowTenderService.queryTenderListByBid(borrowlist.get(i).getId());
//  	        		for(BorrowTender tender : borrowTenderList){
//  	        			System.out.println("tender.getLoanNo()========"+tender.getLoanNo());
//  	        			sb.append(tender.getLoanNo()).append(",");
//  	        		}
//  	        		
//  	        		System.out.println("sb==================================="+sb.toString());
//  	        		LoanNoList=sb.substring(0, sb.length()-1);
//  	        		String Remark1="";
//  	        		String Remark2="同意";
//  	        		String Remark3="";
//  	        		String dataStr = LoanNoList + PlatformMoneymoremore + AuditType  + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
//  	        		// 签名
//  	        		RsaHelper rsa = RsaHelper.getInstance();
//  	        		
//  	        		SignInfo = rsa.signData(dataStr, privatekey);
//  	        		
//  	        		Map<String, String> req = new HashMap<String, String>();
//  	        		req.put("LoanNoList", LoanNoList);
//  					req.put("PlatformMoneymoremore", PlatformMoneymoremore);
//  					req.put("AuditType", AuditType);
//  					req.put("Remark1", Remark1);
//  					req.put("Remark2", Remark2);
//  					req.put("Remark3", Remark3);
//  					req.put("ReturnURL", ReturnURL);
//  					req.put("NotifyURL", NotifyURL);
//  					req.put("SignInfo", SignInfo);
//  	        		String[] result =HttpClientUtil.doPostQueryCmd(SubmitURL, req);
//  					System.out.println("result0=====" + result[0]);
//  					System.out.println("result1=====" + result[1]);
//  	            }
//              }
//              System.out.print("复审线程结束~~~~~~");
//          } catch (Exception e) {
//              e.printStackTrace();
//          }
//    	System.out.print("自动复审线程结束~~~~~~");
//    }
    
    
    
    /**复审通过并放款
     * @Title: index 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    public synchronized void ReVerifyBorrow() throws Exception{
        
        System.out.print("复审线程开始~~~~~~");
       
        try {
            
            //查询需要复审通过的标
            List<Borrow> borrowlist=borrowService.queryReVerifyBorrowList(Constant.BORROW_STATUS_FSTG, Constant.BORROW_VERIFY_OPT_TYPE_CSTG);
            if(borrowlist.size()>0){
	            for (int i = 0; i < borrowlist.size(); i++) {
	                if(borrowlist.get(i)!=null){
	                	if(jobService.exVerifyBorrow(borrowlist.get(i))){
	                    
	                    VerifyBorrowLog vbl = verifyService.queryVerifyByD(borrowlist.get(i).getId());
	                    
	                    vbl.setOpStatus(1); //已经处理
	                    vbl.setUpdatedAt(DateUtils.getNowTimeStr());
	                    vbl.setUpdatedBy(1L);
	                    
	                    verifyService.updateVerifyBorrowLog(vbl);
	                    
	                    borrowService.updateBorrowForStatus(borrowlist.get(i).getId(), Constant.BORROW_STATUS_HKZ);
	                	}   
	                }
	            }
            }
            System.out.print("复审线程结束~~~~~~");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
    
    
    /**
     * 还款
     * @Title: borrowRepay 
     * @Description: TODO
     * @param @throws Exception 设定文件 
     * @return void 返回类型 
     * @throws
     */
    public synchronized void borrowRepay() throws Exception{
        
        //System.out.print("还款线程开始");
        
        try {
            List<BorrowRepayment> repaylist = borrowService.queryRepaymentForStatus(0);
            
            for (int i = 0; i < repaylist.size(); i++) {
                
                jobService.exRepay(repaylist.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //System.out.print("还款线程结束");
    }
    
    
    /**
     * 过期处理，包括流标
     * @Title: overdueBorrow 
     * @Description: TODO
     * @param @throws Exception 设定文件 
     * @return void 返回类型 
     * @throws
     */
    public synchronized void overdueBorrow() throws Exception{
        
        //System.out.print("过期线程开始");
        
        try {
            //查询到期的标
            List<Borrow> borrowlist = borrowService.queryOverBorrow();
            
            for (int i = 0; i < borrowlist.size(); i++) {
              
              List<BorrowTender> tenderlist = borrowService.queryTenderByBorrow(borrowlist.get(i).getId()); 
              
              //没有投标记录的，直接改状态
              if(tenderlist.size()==0){
                  
                  borrowService.updateBorrowForStatus(borrowlist.get(i).getId(), Constant.BORROW_STATUS_YGQ);
                  
              }else{
                  //有投标记录的，退款
                  for (int j = 0; j < tenderlist.size(); j++) {
                  
                      jobService.unfreezeTender(tenderlist.get(j), borrowlist.get(i).getId());
                      
                  }
                  
                  borrowService.updateBorrowForStatus(borrowlist.get(i).getId(), Constant.BORROW_STATUS_YGQ);
              }
              
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus()
            .setRollbackOnly();
        }
        
        //System.out.print("过期线程结束");
        
    }
    
    
    
    /**
     * 复审失败线程处理
     * @throws Exception
     */
    public synchronized void VerifyFullFail() throws Exception{
    	 System.out.println("复审失败线程开始~~~~");
         try {
         	//查询复审失败的记录
         	List<Borrow> borrowlist = borrowService.queryBorrowListByStatus(Constant.BORROW_STATUS_FSSB);
             for (Borrow borrow:borrowlist) {
     		   		//更改标的状态 43
            	 	  borrowService.autoVerifyFullFail(borrow);	
	            	  System.out.println("复审失败线程开始 调用成功~~~~");
             }
     		            
         } catch (Exception e) {
             e.printStackTrace();
         }
       
        
        System.out.println("复审失败线程结束");
    }
    
    
   /* *//**
     * 转让金额计算
     * @Title: computerTransfer 
     * @Description: TODO
     * @param  设定文件 
     * @return void 返回类型 
     * @throws
     *//*
    public void computerTransfer(){
        
        try {
            List<Market> marketlist = transferService.searchMarketByDay();
            
            for (int i = 0; i < marketlist.size(); i++) {
                
                jobService.computerTransferDebt(marketlist.get(i));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }*/
    /**
     * 防短信轰炸
     */
    
    public  void smsJop(){
    	try {
			 //查询所有记录
    		long time = getNowCreateTime1();
    		SmsJop smsjop = new SmsJop(); 
    		List<SmsJop> smslist= smsJopDao.selectSmsJopByAddtime(smsjop);
    		System.out.println("短信轰炸第一步~~~~~~~~~~~~~");
    		for(SmsJop hl : smslist){
    			if(Long.valueOf(hl.getEndTime())-time<=0){
    				smsJopDao.deleteSmsJop(hl.getId());
    				System.out.println("短信轰炸第二步~~~~~~~~~~~~~");
    			}
    		}
    		System.out.println("短信轰炸第三步~~~~~~~~~~~~~");
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    /**
     * 红包过期
     * @return
     */
    public synchronized void hongbaoLose(){
    	//查询所有未使用的红包。
    	 Map<String, String> param = new HashMap<String, String>();
    	 param.put("status", "0");
    	 List<Hongbao> hblist=	hongbaoDao.queryListHongbaoByIDAndStatus(param);
    	for(Hongbao hb:hblist){
    		//判断每个红包是否到期。 
    		if(!StringUtils.isEmpty(hb.getEndtime())){
    			Long times = Long.valueOf(hb.getEndtime()) - Long.valueOf(TimeUtil.getNowTimeStr());
    			//如果截止时间－当前时间小于0，则过期。
    			if(times<=0){
    				//红包到期之后－修改红包状态3.
    				hb.setStatus(3);
    				hongbaoDao.updateHongbao(hb);
    			}
    		}
    		
    	}
    }
    
    /**
     * 
     * @desc 用途描述:  返回说明:新用户送红包
     * @exception 内部异常说明:
     * @throws 抛出异常说明:
     * @author gwx
     * @version 1.0      
     * @created 2016-3-8 上午11:52:20 
     * @mod 修改描述:
     * @modAuthor 修改人:
     */
    public synchronized void songhongbao(){
    	LOGGER.debug("==============送红包任务开始===============");
    	List<User> userList = userService.queryNewUserList("1");//新用户投资满1000
    	if(userList!=null){
    		for (User user : userList) {
    			Hongbao hb= new Hongbao();
    			hb.setUser_id(user.getId());
    			hb.setMoney(10d);
    			hb.setStatus(0);
    			hb.setType(1);
    			hb.setAddtime(TimeUtil.getNowTimeStr());
    			String endtime = TimeUtil.getEndTimeHMS(System.currentTimeMillis() / 1000, 3, 0, 0, 0, 0);
    			DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");            
    	        Date date=null;
				try {
					date = fmt.parse(endtime);
					hb.setEndtime(String.valueOf(date.getTime()/1000));
					int res=userService.updateHongbaoStatus("1",user.getId());
					if(res>0){
						hongbaoDao.insertHonbao(hb);
					}
				} catch (ParseException e) {
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus()
		            .setRollbackOnly();
					LOGGER.error("送红包时间转换异常："+e);
					return;
				}
			}
    	}
    	List<User> userList1 = userService.queryNewUserList("2");//新用户投资满3000
    	if(userList1!=null){
    		for (User user : userList1) {
    			if(user.getInviteUserId()!=null&&!"".equals(user.getInviteUserId().trim())){
    				User user1 =userService.queryByUserId(Long.valueOf(user.getInviteUserId()));
    				if(user1!=null){
    					Hongbao hb= new Hongbao();
    					hb.setUser_id(user1.getId());
    					hb.setMoney(30d);
    					hb.setStatus(0);
    					hb.setType(2);
    					hb.setAddtime(TimeUtil.getNowTimeStr());
    					String endtime = TimeUtil.getEndTimeHMS(System.currentTimeMillis() / 1000, 3, 0, 0, 0, 0);
    					DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");            
    					Date date=null;
    					try {
    						date = fmt.parse(endtime);
    						hb.setEndtime(String.valueOf(date.getTime()/1000));
    						int res=userService.updateHongbaoStatus("2",user.getId());
    						if(res>0){
    							hongbaoDao.insertHonbao(hb);
    						}
						} catch (Exception e) {
							e.printStackTrace();
							TransactionAspectSupport.currentTransactionStatus()
        		            .setRollbackOnly();
    						LOGGER.error("送红包时间转换异常："+e);
    						return;
    	    			}
    				}
    			}
			}
    	}
    	LOGGER.debug("==============送红包任务结束===============");
    }
    
    
    public Long getNowCreateTime1() {

		long datetime = (System.currentTimeMillis() / 1000);

		return datetime;
	}
  
}