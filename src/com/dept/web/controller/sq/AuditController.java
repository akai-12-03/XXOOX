/**
 * Project Name:qgfCms
 * File Name:AuditController.java
 * Package Name:com.dept.web.controller.sq
 * Date:2016-2-26下午3:47:13
 * Copyright (c) 2016, gwx@tomcat360.com 
 * 雄猫软件版权所有
*/

package com.dept.web.controller.sq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.context.Constant;
import com.dept.web.controller.BorrowController;
import com.dept.web.controller.WebController;
import com.dept.web.controller.sq.utils.Common;
import com.dept.web.controller.sq.utils.RsaHelper;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserWithdraw;
import com.dept.web.dao.model.VerifyBorrowLog;
import com.dept.web.general.interest.InterestCalculator;
import com.dept.web.general.interest.MonthInterest;
import com.dept.web.general.util.BorrowUtil;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.AccountService;
import com.dept.web.service.BorrowService;
import com.dept.web.service.BorrowTenderService;
import com.dept.web.service.UserService;
import com.dept.web.service.VerifyService;

/**
 * ClassName:AuditController 
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2016-2-26 下午3:47:13 
 * @author   gwx
 * @version  
 * @since    JDK 1.6
 * @see
 */
@Controller
public class AuditController extends WebController{
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private VerifyService verifyService;
	@Autowired
	private UserService userService;
	@Autowired
	private BorrowTenderService borrowTenderService;
	private String verifySignature;
	private static final Logger LOGGER = Logger.getLogger(AuditController.class);

	/**
	 * 接收审核页面返回信息
	 * 
	 * @return
	 */
	@RequestMapping("qddApi/LoanTransferAuditReturn")
	public String testLoanTransferAuditReturn(ModelMap map, HttpServletRequest request, HttpServletResponse response)
	{
		String LoanNoList=request.getParameter("LoanNoList");
		String LoanNoListFail=request.getParameter("LoanNoListFail");
		String PlatformMoneymoremore=request.getParameter("PlatformMoneymoremore");
		String AuditType=request.getParameter("AuditType");//1.通过2.退回3.二次分配同意4.二次分配不同意5.提现通过6.提现退回
		String RandomTimeStamp=request.getParameter("RandomTimeStamp");
		String Remark1=request.getParameter("Remark1");
		String Remark2=request.getParameter("Remark2");
		String Remark3=request.getParameter("Remark3");
		String ResultCode=request.getParameter("ResultCode");
		String Message=request.getParameter("Message");
		String SignInfo=request.getParameter("SignInfo");
		try
		{
			request.setCharacterEncoding("UTF-8");
			
			String publickey = Common.publicKey;
			
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanNoList + LoanNoListFail + PlatformMoneymoremore + AuditType + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
			
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
			this.verifySignature = Boolean.toString(verifySignature);
			LOGGER.info("审核页面返回:verifySignature="+this.verifySignature);
			LOGGER.info("审核页面返回码:ResultCode="+ResultCode);
			if(Remark3!=null&&!"".equals(Remark3.trim())&&!"null".equals(Remark3.trim())){
				AdminUser user = userService.queryAdminUserById(Long.valueOf(Remark3));
				if(user!=null){
					this.putCurrUser(request, response, user);
					if(verifySignature){
						map.addAttribute("msg", Message);
					}else{
						map.addAttribute("msg", "审核返回信息数据签名验证失败");
					}
					if("1".equals(AuditType)||"2".equals(AuditType)){
						return "redirect:/admin/borrow/list.html";
					}else if("5".equals(AuditType)||"6".equals(AuditType)){
						return "redirect:/admin/withdraw_list.html";
					}
				}else{
					map.addAttribute("msg", "请核对审核人是否为管理员");
					return "redirect:/index.html";
				}
			}else{
				map.addAttribute("msg", "请用正确的管理员帐号审核");
				return "redirect:/index.html";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		map.addAttribute("msg", Message);
        return "redirect:/admin/withdraw_list.html";
	}
	
	/**
	 * 接收审核后台通知信息
	 * 
	 * @return
	 */
	@RequestMapping("qddApi/LoanTransferAuditNotify")
	public void testLoanTransferAuditNotify(ModelMap map, HttpServletRequest request, HttpServletResponse response)
	{
		String LoanNoList=request.getParameter("LoanNoList");
		String LoanNoListFail=request.getParameter("LoanNoListFail");
		String PlatformMoneymoremore=request.getParameter("PlatformMoneymoremore");
		String AuditType=request.getParameter("AuditType");//1.通过2.退回3.二次分配同意4.二次分配不同意5.提现通过6.提现退回
		String RandomTimeStamp=request.getParameter("RandomTimeStamp");
		String Remark1=request.getParameter("Remark1");
		String Remark2=request.getParameter("Remark2");
		String Remark3=request.getParameter("Remark3");
		String ResultCode=request.getParameter("ResultCode");
		String Message=request.getParameter("Message");
		String ReturnTimes=request.getParameter("ReturnTimes");
		String SignInfo=request.getParameter("SignInfo");
		try
		{
			request.setCharacterEncoding("UTF-8");
			
			String publickey = Common.publicKey;
			
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanNoList + LoanNoListFail + PlatformMoneymoremore + AuditType + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
			LOGGER.info("审核后台通知:verifySignature="+verifySignature);
			LOGGER.info("审核返回码:ResultCode="+ResultCode);
			LOGGER.info("审核返回次数:ReturnTimes="+ReturnTimes);
			if(verifySignature){
				if("88".equals(ResultCode)){
					if("5".equals(AuditType)){//提现通过
					  UserWithdraw uw = accountService.queryWithdrawByOrderId(LoanNoList);
					  uw.setStatus(1);
					  uw.setMessage(Message);
                      uw.setUpdatedAt(DateUtils.getNowTimeStr().toString());
                      accountService.qddVerifyWithDraw(uw);
                      /**
                       * 更改资金账户
                       */
                      UserAccount newua = accountService.queryAccountByUser(uw.getCreatedBy());
                      
                      newua.setMoneyWithdraw(newua.getMoneyWithdraw()-uw.getMoneyWithdraw());
                      newua.setMoneyTotal(newua.getMoneyTotal()-uw.getMoneyWithdraw());
                      newua.setUpdatedAt(DateUtils.getNowTimeStr());
                      
                      accountService.updateAccount(newua);
                      
                      /**
                       * 添加资金记录
                       */
                      UserAccountLog ual = new UserAccountLog();
                      
                      UserAccount newua_hand = accountService.queryAccountByUser(uw.getCreatedBy());
                      
                      //新建资金记录
                      ual.setUserId(uw.getCreatedBy());
                      ual.setType(5);
                      ual.setMoneyOperate(uw.getMoneyWithdraw());
                      ual.setMoneyTotal(newua_hand.getMoneyTotal());
                      ual.setMoneyUsable(newua_hand.getMoneyUsable());
                      ual.setMoneyWithdraw(newua_hand.getMoneyWithdraw());
                      ual.setMoneyInsure(newua_hand.getMoneyInsure());
                      ual.setRemark("审核提现成功，扣除"+uw.getMoneyWithdraw()+"元");
                      ual.setCreatedAt(DateUtils.getNowTimeStr());
                      ual.setCreatedIp(IPUtils.getRemortIP(request));
                      ual.setMoneyCollection(newua_hand.getMoneyCollection());
                      ual.setMoneyTenderFreeze(newua_hand.getMoneyTenderFreeze());
                      accountService.createUserAccountLog(ual);
					}else if("6".equals(AuditType)){//提现不通过
						AuditFailure(LoanNoList, Message, request);
					}else if("1".equals(AuditType)||"2".equals(AuditType)){
						String loan[]=LoanNoList.split(",");
						BorrowTender tender = borrowTenderService.queryTenderByLoanNo(loan[0]);
						Borrow borrow= borrowService.queryBorrowById(tender.getBorrowId());
//						borrow.setContent(Remark1);
		                borrow.setVerifyRemark(Remark2);
		                borrow.setVerifyTime(DateUtils.getNowTimeStr());
		                borrow.setVerifyUser(Remark3==null||Remark3.trim()==""||Remark3.trim()=="null"?null:Long.valueOf(Remark3));
		                borrow.setUpdatetime(DateUtils.getNowTimeStr());
		                borrow.setUpdateip(IPUtils.getRemortIP(request));
		                VerifyBorrowLog vb = new VerifyBorrowLog();
		                if("1".equals(AuditType)){
		                	vb.setOpType(3);
		                	borrow.setStatus(Constant.BORROW_STATUS_FSTG);
		                }else{
		                	borrow.setStatus(Constant.BORROW_STATUS_FSSB);
		                    vb.setOpType(4);
		                }
		                
		                borrowService.updateBorrow(borrow);
		                
		                vb.setBorrowId(borrow.getId());
		                vb.setOpStatus(0);
		                vb.setRemark(Remark2);
		                vb.setCreatedAt(DateUtils.getNowTimeStr());
		                vb.setCreatedBy(Remark3==null||Remark3.trim()==""||Remark3.trim()=="null"?null:Long.valueOf(Remark3));
		                
		                verifyService.createVerifyBorrowLog(vb);
		                
		                //复审不通过，退回投资者投标金额
		                
		                //通过后生成还款记录
		                if (borrow.getStatus()==Constant.BORROW_STATUS_FSTG) {
		                    // 生产批量还款记录
		                    try {
		                        borrowService.addBatchRepayment(getRepayment(borrow));
		                    } catch (Exception e) {
		                        e.printStackTrace();
		                    }
		                }
					}
				}else{
					AuditFailure(LoanNoList, Message, request);
				}
			}else{
				AuditFailure(LoanNoList, Message, request);
			}
			response.setContentType("text/plain;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write("SUCCESS");
			response.getWriter().flush();
			response.getWriter().close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
     * 生成还款计划
     * @Title: getRepayment 
     * @Description: TODO
     * @param @param borrow
     * @param @return 设定文件 
     * @return List<BorrowRepayment> 返回类型 
     * @throws
     */
    public  List<BorrowRepayment> getRepayment(Borrow borrow) {
        
        List<BorrowRepayment> repaymentlist = new ArrayList<BorrowRepayment>();
        
        if(borrow.getIsDay()==1){
            
            BorrowRepayment repay=new BorrowRepayment();            
            repay.setBorrowId(borrow.getId());
            repay.setStatus(Constant.BORROW_REPAYMENT_STATUS_XJ);
            repay.setWebstatus(Constant.BORROW_REPAYMENT_WEBSTATUS_FDH);
            repay.setRepOrder(1);
            repay.setRepaymentTime(BorrowUtil.getRepayTime(borrow, repay.getRepOrder()).getTime()/1000);
            double lixi = NumberUtil.ceil(borrow.getAccount()/365*borrow.getTimeLimit()*borrow.getApr()/100,2);
            lixi = BigDecimal.valueOf(lixi).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            double repayment_account=borrow.getAccount() + lixi;   
            
            repay.setRepaymentAccount(repayment_account);
            repay.setInterest(lixi);
            repay.setCapital(borrow.getAccount());
            repay.setAddtime(DateUtils.getNowTimeStr());
            repay.setAddip("1.1.1.1");
            repay.setRepaymentYesaccount(0.00);
            repay.setLateDays(0);
            repay.setLateInterest(0.00);
            repay.setForfeit(0.00);
            repay.setReminderFee(0.00);
            
            repaymentlist.add(repay);
            
        }else{
            if(borrow.getRepaymentStyle()==Constant.BORROW_REPAYMENT_STYLE_DEBX)
            {
            	List<BorrowCollection> list=borrowService.getBorrowCollectionDataByBorrowId(borrow.getId());
            	int prj = 1;
            	for(BorrowCollection c:list)
            	{
            		BorrowRepayment repay=new BorrowRepayment();
                    
                    repay.setStatus(Constant.BORROW_REPAYMENT_STATUS_XJ);
                    repay.setWebstatus(Constant.BORROW_REPAYMENT_WEBSTATUS_FDH);
                    repay.setBorrowId(borrow.getId());
                    repay.setRepOrder(prj++);
                    repay.setRepaymentTime(BorrowUtil.getRepayTime(borrow, repay.getRepOrder()).getTime()/1000);
                    double repayment_account=NumberUtil.ceil((c.getRepayAccount()),4);
                    repay.setRepaymentAccount(repayment_account);
                    double repaymeng_interest=NumberUtil.ceil(c.getInterest(),4);
                    repay.setInterest(repaymeng_interest);
                    double repaymeng_accountPerMon=NumberUtil.ceil(c.getCapital(),4);
                    repay.setCapital(repaymeng_accountPerMon);
                    repay.setAddtime(DateUtils.getNowTimeStr());
                    repay.setAddip("1.1.1.1");
                    repay.setRepaymentYesaccount(0.00);
                    repay.setLateDays(0);
                    repay.setLateInterest(0.00);
                    repay.setForfeit(0.00);
                    repay.setReminderFee(0.00);                
                    repaymentlist.add(repay);
            	}
            }
            else
            {
            	  InterestCalculator ic = BorrowUtil.getInterestCalculator(borrow.getAccount(),borrow.getApr()/100,Integer.valueOf(borrow.getTimeLimit()), borrow.getRepaymentStyle());
                  List<MonthInterest> monthList = ic.getMonthList();
                  int prj = 1;
                  for (MonthInterest mi : monthList) {
                      BorrowRepayment repay=new BorrowRepayment();
                      
                      repay.setStatus(Constant.BORROW_REPAYMENT_STATUS_XJ);
                      repay.setWebstatus(Constant.BORROW_REPAYMENT_WEBSTATUS_FDH);
                      repay.setBorrowId(borrow.getId());
                      repay.setRepOrder(prj++);
                      repay.setRepaymentTime(BorrowUtil.getRepayTime(borrow, repay.getRepOrder()).getTime()/1000);
                      double repayment_account=NumberUtil.ceil((mi.getAccountPerMon()+mi.getInterest()),4);
                      repay.setRepaymentAccount(repayment_account);
                      double repaymeng_interest=NumberUtil.ceil(mi.getInterest(),4);
                      repay.setInterest(repaymeng_interest);
                      double repaymeng_accountPerMon=NumberUtil.ceil(mi.getAccountPerMon(),4);
                      repay.setCapital(repaymeng_accountPerMon);
                      repay.setAddtime(DateUtils.getNowTimeStr());
                      repay.setAddip("1.1.1.1");
                      repay.setRepaymentYesaccount(0.00);
                      repay.setLateDays(0);
                      repay.setLateInterest(0.00);
                      repay.setForfeit(0.00);
                      repay.setReminderFee(0.00);                
                      repaymentlist.add(repay);
                  }
            }
          
            
        }
        
        return repaymentlist;
        
    }
	
	public void AuditFailure(String LoanNoList,String Message,HttpServletRequest request){
		UserWithdraw uw = accountService.queryWithdrawByOrderId(LoanNoList);
		if (uw != null) {
			uw.setStatus(2);
			uw.setMessage(Message);
			uw.setUpdatedAt(DateUtils.getNowTimeStr().toString());
			accountService.qddVerifyWithDraw(uw);
			/**
			 * 更改资金账户
			 */
			UserAccount newua = accountService.queryAccountByUser(uw
					.getCreatedBy());

			newua.setMoneyWithdraw(newua.getMoneyWithdraw()
					- uw.getMoneyWithdraw());
			newua.setMoneyUsable(newua.getMoneyUsable() + uw.getMoneyWithdraw());
			newua.setUpdatedAt(DateUtils.getNowTimeStr());

			accountService.updateAccount(newua);

			/**
			 * 添加资金记录
			 */
			UserAccountLog ual = new UserAccountLog();

			UserAccount newua_hand = accountService.queryAccountByUser(uw
					.getCreatedBy());

			// 新建资金记录
			ual.setUserId(uw.getCreatedBy());
			ual.setType(6); // 5代表提现成功6代表提现失败解冻
			ual.setMoneyOperate(uw.getMoneyWithdraw());
			ual.setMoneyTotal(newua_hand.getMoneyTotal());
			ual.setMoneyUsable(newua_hand.getMoneyUsable());
			ual.setMoneyWithdraw(newua_hand.getMoneyWithdraw());
			ual.setMoneyInsure(newua_hand.getMoneyInsure());
			ual.setRemark("审核提现不通过，解除冻结金额" + uw.getMoneyWithdraw() + "元");
			ual.setCreatedAt(DateUtils.getNowTimeStr());
			ual.setCreatedIp(IPUtils.getRemortIP(request));
			ual.setMoneyCollection(newua_hand.getMoneyCollection());
			ual.setMoneyTenderFreeze(newua_hand.getMoneyTenderFreeze());
			accountService.createUserAccountLog(ual);
		}else{
			LOGGER.error("提现审核返回接口异常：未找到当前流水号数据");
		}
	}
	
	
	/**
	 * 接收审核页面返回信息
	 * 
	 * @return
	 */
	@RequestMapping("qdd/loantransferauditreturn")
	public String loantransferauditreturn(ModelMap map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			request.setCharacterEncoding("UTF-8");
			String PlatformMoneymoremore = request
					.getParameter("PlatformMoneymoremore");
			String LoanNoList = request.getParameter("LoanNoList");
			String LoanNoListFail = request.getParameter("LoanNoListFail");
			String AuditType = request.getParameter("AuditType");
			String ResultCode = request.getParameter("ResultCode");
			String Message = request.getParameter("Message");
			String SignInfo = request.getParameter("SignInfo");

			String publickey = Common.publicKey;

			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanNoList + LoanNoListFail
					+ PlatformMoneymoremore + AuditType + ResultCode;

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					publickey);
			this.verifySignature = Boolean.toString(verifySignature);
			if(verifySignature&&"88".equals(ResultCode)){
				LOGGER.info("还款审核成功！");
			}else{
				LOGGER.info("还款审核失败！"+Message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:login.html?";
	}

	/**
	 * 接收审核后台通知信息
	 * 
	 * @return
	 */
	@RequestMapping("qdd/loantransferauditnotify")
	public void loantransferauditnotify(ModelMap map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			request.setCharacterEncoding("UTF-8");
			String PlatformMoneymoremore = request
					.getParameter("PlatformMoneymoremore");
			String LoanNoList = request.getParameter("LoanNoList");
			String LoanNoListFail = request.getParameter("LoanNoListFail");
			String AuditType = request.getParameter("AuditType");
			String ResultCode = request.getParameter("ResultCode");
			String Message = request.getParameter("Message");
			String SignInfo = request.getParameter("SignInfo");
			String publickey = Common.publicKey;

			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanNoList + LoanNoListFail
					+ PlatformMoneymoremore + AuditType + ResultCode;
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					publickey);
			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			if(verifySignature&&"88".equals(ResultCode)){
				LOGGER.info("还款审核成功！");
			}else{
				LOGGER.info("还款审核失败！"+Message);
			}
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write("SUCCESS");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

