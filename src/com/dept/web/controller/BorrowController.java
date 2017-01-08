package com.dept.web.controller;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.dept.web.context.Constant;
import com.dept.web.context.Global;
import com.dept.web.controller.sq.utils.Common;
import com.dept.web.controller.sq.utils.HttpClientUtil;
import com.dept.web.controller.sq.utils.RsaHelper;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.AjaxData;
import com.dept.web.dao.model.AjaxJsonStr;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowLoan;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.BorrowWish;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.VerifyBorrowLog;
import com.dept.web.general.interest.EndInterestCalculator;
import com.dept.web.general.interest.InterestCalculator;
import com.dept.web.general.interest.MonthEqualCalculator;
import com.dept.web.general.interest.MonthInterest;
import com.dept.web.general.interest.MonthInterestCalculator;
import com.dept.web.general.util.BorrowUtil;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.ExportExcel;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.StatusUtils;
import com.dept.web.general.util.StringUtils;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.BorrowService;
import com.dept.web.service.BorrowTenderService;
import com.dept.web.service.JobService;
import com.dept.web.service.LoanService;
import com.dept.web.service.UserService;
import com.dept.web.service.VerifyService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 理财管理
 * 
 * @ClassName:     BorrowController
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月16日 下午5:06:09 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class BorrowController extends WebController{

    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private BorrowTenderService borrowTenderService;
    
    @Autowired
    private VerifyService verifyService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private LoanService loanService;
    
    /**
     * 发布理财产品
     * @Title: artlist 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/borrow/opborrow_add")
    public String artlist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        String ctype = params.get("opt");
        
        AdminUser user = getCurrUser(request, response);
        
        int adflag = 0;
        
        if(StringUtils.isEmpty(ctype)){
            
            return "borrow/borrow_add";
            
        }else if(ctype.equals("add")){
            
            Borrow borrow = new Borrow();
            
            borrow.setName(params.get("name"));
            
            int borrowType = 0;
            
            if(StringUtils.isNumber(params.get("borrow_type"))){
                
                borrowType = Integer.valueOf(params.get("borrow_type"));
                
                borrow.setBorrowType(borrowType);
                
            }else{
                
                adflag = adflag + 1;
            }
            
           /* int borrowuse = 0;
            
            if(StringUtils.isNumber(params.get("borrow_use"))){
                
                borrowuse = Integer.valueOf(params.get("borrow_use"));
                
                borrow.setBorrowUse(borrowuse);
                
            }else{
                
                adflag = adflag + 1;
            }            */
            
            int repaymentStyle = 0;
            
            if(StringUtils.isNumber(params.get("repayment_style"))){
                
                repaymentStyle = Integer.valueOf(params.get("repayment_style"));
                
                borrow.setRepaymentStyle(repaymentStyle);
                
            }else{
                
                adflag = adflag + 1;
            }
            
            int isDay = 0;
            
            if(StringUtils.isNumber(params.get("isDay"))){
                
                isDay = Integer.valueOf(params.get("isDay"));
                
                borrow.setIsDay(isDay);
                
            }else{
                
                adflag = adflag + 1;
            }            

            double account = 0;
            
            if(StringUtils.isNumber(params.get("account"))){
                
                account = Double.valueOf(params.get("account"));
                
                borrow.setAccount(account);
                
            }else{
                
                adflag = adflag + 1;
            }
            
            double apr = 0;
            
            if(StringUtils.isNumber(params.get("apr"))){
                
                apr = Double.valueOf(params.get("apr"));
                
                borrow.setApr(apr);
                
            }else{
                
                adflag = adflag + 1;
            } 
            
            int timelimit = 0;
            
            if(StringUtils.isNumber(params.get("timelimit"))){
                
                timelimit = Integer.valueOf(params.get("timelimit"));
                
                borrow.setTimeLimit(timelimit);
                
            }else{
                
                adflag = adflag + 1;
            }
            
            double lowestAccount = 0;
            
            if(StringUtils.isNumber(params.get("lowest_account"))){
                
                lowestAccount = Double.valueOf(params.get("lowest_account"));
                
                borrow.setLowestAccount(lowestAccount);
                
            }else{
                
                adflag = adflag + 1;
            }
            
            double mostAccount = 0;

            if(StringUtils.isNumber(params.get("most_account"))){
                
                mostAccount = Double.valueOf(params.get("most_account"));
                
                borrow.setMostAccount(mostAccount);
                
            }else{
                
                adflag = adflag + 1;
            }
            
            int validTime = 0;
            
            if(StringUtils.isNumber(params.get("valid_time"))){
                
                validTime = Integer.valueOf(params.get("valid_time"));
                
                borrow.setValidTime(validTime);
                
            }else{
                
                adflag = adflag + 1;
            }
            
            int award = 0;
            
            if(StringUtils.isNumber(params.get("award"))){
                
                award = Integer.valueOf(params.get("award"));
                
                borrow.setAward(award);
                
                if(award==Constant.BORROW_AWARD_TYPE_PART_ACCOUNT){
                    
                    double funds = 0;
                    
                    if(StringUtils.isNumber(params.get("funds"))){
                        
                        funds = Double.valueOf(params.get("funds"));
                        
                        borrow.setFunds(funds);
                        
                    }else{
                        
                        adflag = adflag + 1;
                    }
                }else if(award==Constant.BORROW_AWARD_TYPE_FUNDS){
                    
                    double partAccount = 0;
                    
                    if(StringUtils.isNumber(params.get("part_account"))){
                        
                        partAccount = Double.valueOf(params.get("part_account"));
                        
                        borrow.setPartAccount(partAccount);
                        
                    }else{
                        
                        adflag = adflag + 1;
                    } 
                }
                
            }else{
                
                adflag = adflag + 1;
            }            
            
            
            if(!StringUtils.isEmpty(params.get("pwd"))){
                borrow.setPwd(params.get("pwd"));
            }
            borrow.setContent(params.get("content"));
            borrow.setFxpj(params.get("fxpj"));  //风险评级
            borrow.setCpxq(params.get("cpxq"));  //产品详情
            borrow.setJyjg(params.get("jyjg"));  //交易结构
            borrow.setFkcs(params.get("fkcs"));  //风控措施
            borrow.setZggf(params.get("zggf"));  //资管各方
            borrow.setContents(params.get("contents"));
            borrow.setCpxqs(params.get("cpxqs"));  //产品详情
            borrow.setJyjgs(params.get("jyjgs"));  //交易结构
            borrow.setFkcss(params.get("fkcss"));  //风控措施
            borrow.setZggfs(params.get("zggfs"));  //资管各方
            if(!StringUtil.isEmpty(params.get("index_status"))){
            	 borrow.setIndex_status(Integer.valueOf(params.get("index_status")));//推荐至首页
            }
            if(borrow.getBorrowType()==1){
            	String num ="HB"+ new SimpleDateFormat("yyyy-MMdd").format(new Date())+"-";
            	borrow.setNum(num);
            }else if(borrow.getBorrowType()==2){
            	String num ="XT"+ new SimpleDateFormat("yyyy-MMdd").format(new Date())+"-";
            	borrow.setNum(num);
            }else if(borrow.getBorrowType()==3){
            	String num ="ZG"+ new SimpleDateFormat("yyyy-MMdd").format(new Date())+"-";
            	borrow.setNum(num);
            }else if(borrow.getBorrowType()==4){
            	String num ="YN"+ new SimpleDateFormat("yyyy-MMdd").format(new Date())+"-";
            	borrow.setNum(num);
            }
            
            
            borrow.setContent(params.get("content"));
            
            borrow.setTrustLevel(params.get("trust_level"));   
            
            String borrower=params.get("borrower");
            
            //垫资标
            if(borrowType==1)
            {
            	String mortgagor=params.get("mortgagor");
            	borrow.setMortgagor(mortgagor);
            	String receivePerson=params.get("receivePerson");
            	List<User> receivePersonlist = userService.queryUserByUsername(receivePerson);
            	 if(receivePersonlist.size()>0){
 	            	borrow.setReceivePerson(receivePersonlist.get(0).getId());//收款人id
 	            }else{
 	            	map.addAttribute("borrow", borrow);
                 	map.addAttribute("msg", "此收款人不存在");
 	                return "borrow/borrow_add";
 	            } 
            	 
            	 String repayPerson=params.get("repayPerson");
            	 List<User> repayPersonlist = userService.queryUserByUsername(repayPerson);
            	 if(repayPersonlist.size()>0){
            		 borrow.setUserId(repayPersonlist.get(0).getId());
 	            	borrow.setRepayPerson(repayPersonlist.get(0).getId());//还款人id
 	            }else{
 	            	map.addAttribute("borrow", borrow);
                 	map.addAttribute("msg", "此还款人不存在");
 	                return "borrow/borrow_add";
 	            } 
            	
            }
            else
            {
            	if(!StringUtils.isEmpty(borrower)){
                	borrow.setUserId(Long.valueOf(params.get("borrower")));//借款人id
                }else{
    	            String username=params.get("username");
    	            List<User> userlist = userService.queryUserByUsername(username);
    	            
    	            if(userlist.size()>0){
    	            	borrow.setUserId(userlist.get(0).getId());//借款人id
    	            }else{
    	            	map.addAttribute("borrow", borrow);
                    	map.addAttribute("msg", "此借款人不存在");
    	                return "borrow/borrow_add";
    	            } 
                }
            }
            
            
            Double borrow_fee=0d;
            if(StringUtils.isNumber(params.get("borrow_fee"))){
            	 borrow_fee=Double.parseDouble(params.get("borrow_fee"));
            }
            
            
            borrow.setBorrow_fee(borrow_fee);
            borrow.setBoOrder(0);
            borrow.setHits(0);
            borrow.setOpenAccount("0");
            borrow.setOpenBorrow("0");
            borrow.setOpenCredit("0");
            borrow.setOpenTender("0");
            borrow.setAddtime(DateUtils.getNowTimeStr());
            borrow.setAddip(IPUtils.getRemortIP(request));
            borrow.setVersion(0);
            borrow.setStatus(Constant.BORROW_STATUS_DDCS);
            borrow.setAccountYes(0.00);
            
            String isTranStr=params.get("isTran");
            if(!StringUtils.isEmpty(isTranStr))
            {
            borrow.setIsTran(Integer.parseInt(isTranStr));
            }
            
            String marketFee=params.get("marketFee");
            if(!StringUtils.isEmpty(marketFee))
            {
            borrow.setMarketFee(Double.parseDouble(marketFee));
            }
            
            //测试用
            borrow.setBorrowerUserId(user.getId());
            if(account<mostAccount){
            	 map.addAttribute("msg", "最大投资金额不能超过标的的金额");
                 return "borrow/borrow_add";
            }else{
            if(adflag==0){
                
                borrowService.createBorrow(borrow);
                
                map.addAttribute("msg", "创建成功");
                
                return "redirect:list.html";
                
            }else{
                
                map.addAttribute("borrow", borrow);
                
                map.addAttribute("msg", "请输入正确的信息");
                
                return "borrow/borrow_add";
            }
            }
        }
        
        return "borrow/borrow_add";
        
        
    }
    
    
    /**
     * 查看标详情
     * @Title: opborrow_view 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param bid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/borrow/opborrow_view")
    public String opborrow_view(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long bid) throws Exception{
        
        Borrow borrow = borrowService.queryBorrowById(bid);
        
        map.addAttribute("borrow", borrow);
        if(borrow.getReceivePerson()!=null)
        {
        	User receiveUser=userService.queryByUserId(borrow.getReceivePerson());
        	map.addAttribute("receiveUser", receiveUser);
        }
        if(borrow.getRepayPerson()!=null)
        {
        	User repayUser=userService.queryByUserId(borrow.getRepayPerson());
        	map.addAttribute("repayUser", repayUser);
        }
        
        return "borrow/borrow_view";
    }
    
    
    /**
     * 初审借款标
     * @Title: opborrow_veifyF 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param bid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/borrow/opborrow_veifyF")
    public String opborrow_veifyF(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long bid) throws Exception{
        
        AdminUser user = getCurrUser(request, response);
        
        Borrow borrow = borrowService.queryBorrowById(bid);
        
        map.addAttribute("borrow", borrow);
        if(borrow.getReceivePerson()!=null)
        {
        	User receiveUser=userService.queryByUserId(borrow.getReceivePerson());
        	map.addAttribute("receiveUser", receiveUser);
        }
        if(borrow.getRepayPerson()!=null)
        {
        	User repayUser=userService.queryByUserId(borrow.getRepayPerson());
        	map.addAttribute("repayUser", repayUser);
        }
        
        if(borrow.getStatus()==Constant.BORROW_STATUS_DDCS){
            
            Map<String,String> params = getParamMap(request);
            
            String ctype = params.get("opt");
            
            if(StringUtil.isEmpty(ctype)){
                
                return "borrow/borrow_verify";
                
            }else if(ctype.equals("upd")){
                
                borrow.setContent(params.get("content"));
                borrow.setVerifyRemark(params.get("remark"));
                borrow.setVerifyTime(DateUtils.getNowTimeStr());
                borrow.setVerifyUser(user.getId());
                borrow.setUpdatetime(DateUtils.getNowTimeStr());
                borrow.setUpdateip(IPUtils.getRemortIP(request));
                if(params.get("status").equals("1")){
                    borrow.setStatus(Constant.BORROW_STATUS_CSTG);
                }else{
                    borrow.setStatus(Constant.BORROW_STATUS_CSSB);
                }
                
                borrowService.updateBorrow(borrow);
                
                VerifyBorrowLog vb = new VerifyBorrowLog();
                vb.setBorrowId(borrow.getId());
                if(params.get("status").equals("1")){
                    vb.setOpType(1);
                }else{
                    vb.setOpType(2);
                }
                vb.setOpStatus(0);
                vb.setRemark(params.get("remark"));
                vb.setCreatedAt(DateUtils.getNowTimeStr());
                vb.setCreatedBy(user.getId());
                
                verifyService.createVerifyBorrowLog(vb);
                
                map.addAttribute("msg", "操作成功");
                
                return "redirect:list.html";
            }
            
        }else{
            
            return "borrow/borrow_view";
        }
        
        return "borrow/borrow_view";
    }
    
    
    /**
     * 借款标复审
     * @Title: opborrow_veifyD 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param bid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/borrow/opborrow_veifyD")
    public String opborrow_veifyD(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long bid) throws Exception{
        
        AdminUser user = getCurrUser(request, response);
        
        Borrow borrow = borrowService.queryBorrowById(bid);
        
        map.addAttribute("borrow", borrow);
        
        if(borrow.getReceivePerson()!=null)
        {
        	User receiveUser=userService.queryByUserId(borrow.getReceivePerson());
        	map.addAttribute("receiveUser", receiveUser);
        }
        if(borrow.getRepayPerson()!=null)
        {
        	User repayUser=userService.queryByUserId(borrow.getRepayPerson());
        	map.addAttribute("repayUser", repayUser);
        }
        
        if(borrow.getStatus()==Constant.BORROW_STATUS_MBDFS){
            
            Map<String,String> params = getParamMap(request);
            
            String ctype = params.get("opt");
            
            if(StringUtil.isEmpty(ctype)){
                
                return "borrow/borrow_verify";
                
            }else if(ctype.equals("upd")){
                
            	/**********************钱多多托管**************/
        		String AuditType = "";
        		if(params.get("status").equals("1")){
                    borrow.setStatus(Constant.BORROW_STATUS_FSTG);
                    AuditType = "1";// 审核类型(1.通过 2.退回 3.二次分配同意 4.二次分配不同意 5.提现通过 6.提现退回)
                }else{
                    borrow.setStatus(Constant.BORROW_STATUS_FSSB);
                    AuditType=  "2";
                }
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
        		StringBuffer sb= new StringBuffer();
        		List<BorrowTender> borrowTenderList= borrowTenderService.queryTenderListByBid(bid);
        		for(BorrowTender tender : borrowTenderList){
        			System.out.println("tender.getLoanNo()========"+tender.getLoanNo());
        			sb.append(tender.getLoanNo()).append(",");
        		}
        		
        		System.out.println("sb==================================="+sb.toString());
        		LoanNoList=sb.substring(0, sb.length()-1);
        		String Remark1="";
        		String Remark2=params.get("remark");
        		String Remark3=String.valueOf(user.getId());
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
        		String[] result =HttpClientUtil.doPostQueryCmd(SubmitURL, req);
				System.out.println("result0=====" + result[0]);
				System.out.println("result1=====" + result[1]);
				JSONObject obj = JSONObject.parseObject(result[1]);
				String Message = obj.getString("Message");
				map.addAttribute("msg", Message);
				return "redirect:/admin/borrow/list.html";
            }
            
        }else{
            
            return "borrow/borrow_view";
        }
        
        return "borrow/borrow_view";
    }
    
    /**
     * 查看标列表
     * @Title: borrowlist 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/borrow/list")
    public String borrowlist(ModelMap map,HttpServletRequest request, HttpServletResponse response,Long bid) throws Exception{
        
    	 if(bid!=null){
  		   
        	 List<BorrowTender> tenderlist = borrowService.queryTenderByBorrow(bid); 
             
//             没有投标记录的，直接改状态
             if(tenderlist.size()==0){
                 
                 borrowService.updateBorrowForStatus(bid, Constant.BORROW_STATUS_YGQ);
                 
             }else{
                 //有投标记录的，退款
                 for (int j = 0; j < tenderlist.size(); j++) {
                 
                	  jobService.unfreezeTender(tenderlist.get(j),bid);
                 }
//                 
                 borrowService.updateBorrowForStatus(bid, Constant.BORROW_STATUS_YGQ);
             }
         }
        	
    	
        Map<String, String> params = getParamMap(request);
        
        map.addAttribute("msg", params.get("msg"));
        
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            pageRequest.setFilters(params);

            Page<Borrow> borrowPage = borrowService.queryBorrowPage(pageRequest);
            
            List<Borrow> borrowList=borrowPage.getResult();
            Borrow borrow;
            int isDay,repaymentStyle,limit;
            double acc,apr;
            
            
            for (int i = 0; i < borrowList.size(); i++) {
				//取出  isDay account  apr period 月数
            	
            	borrow=borrowList.get(i);
            	isDay=borrow.getIsDay();
            	acc=borrow.getAccount();
            	apr=borrow.getApr();
            	limit=borrow.getTimeLimit();
            	if(isDay==1){
//            		到期还本还息
            		borrow.setTotalInterest(acc*apr/100*limit/365);
            		
            	}else{
//            		按月付息到期还本  repaymentStyle
            		repaymentStyle=borrow.getRepaymentStyle();
            		borrow.setTotalInterest(getInterestCalculator(acc,apr/100,limit,repaymentStyle).getTotalAccount()-acc);

            	}
            	
			}
            
            borrowPage.setResult(borrowList);
             
            map.addAttribute("borrowPage", borrowPage);
          
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowPage.getTotalCount(), borrowPage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("id", params.get("id"));
            
            map.addAttribute("name", params.get("name"));
            
            map.addAttribute("startTime", params.get("startTime"));
            
            map.addAttribute("endTime", params.get("endTime"));
            
            map.addAttribute("status", params.get("status"));
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        return "borrow/borrow_list";
        
        
    }    
    
    /**
	 * 
	 * @Description:  计算还款利息
	 * @param:        @param money  借款金额
	 * @param:        @param apr    利率
	 * @param:        @param timelimit  期限
	 * @param:        @param style  还款类型
	 * @param:        @return   
	 * @return:       InterestCalculator   
	 * @throws
	 */
	public InterestCalculator getInterestCalculator(double money,double apr, int timelimit,int style){
		
		InterestCalculator ic = null;
		
		if(style==2){
			ic =new EndInterestCalculator(money,apr,timelimit,InterestCalculator.TYPE_MONTH_END);
			
		}else if(style==3){
			ic =new MonthInterestCalculator(money,apr,timelimit);
		}else{
			ic =new MonthEqualCalculator(money,apr,timelimit);
		}
		
		ic.each();
		
		return ic;
	}

    
    /**
     * 查看预约列表
     * @Title: borrowlist 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/borrow/yuyuelist")
    public String borrowYuYuelist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        map.addAttribute("msg", params.get("msg"));
        String type=params.get("type");
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            params.put("type", type);
            pageRequest.setFilters(params);

            Page<BorrowWish> borrowPage = borrowService.queryBorrowWishPage(pageRequest);

            map.addAttribute("borrowPage", borrowPage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowPage.getTotalCount(), borrowPage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            
            map.addAttribute("startTime", params.get("startTime"));
            
            map.addAttribute("endTime", params.get("endTime"));
            
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        return "borrow/borrow_yuyue_list";
        
        
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
    public static List<BorrowRepayment> getRepayment(Borrow borrow) {
        
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
        
        return repaymentlist;
        
    }
    
    /**
     * 搜索用户
     * @Title: searchBorrower 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response 设定文件 
     * @return void 返回类型 
     * @throws
     */
    @RequestMapping("searchBorrower")
    public void searchBorrower(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam String username){
        
        List<User> userlist = userService.queryUserByUsername(username);
        
        AjaxJsonStr ajstr = new AjaxJsonStr();
        
        ajstr.setStatus(1);
        ajstr.setMsg("成功");
        
        List<AjaxData> ajaxdata = new ArrayList<AjaxData>();
        
        for (int i = 0; i < userlist.size(); i++) {
        	User ajaxUser=userlist.get(i);
        	if(ajaxUser.getAutoType()==null||ajaxUser.getAutoType()!=1)
        	{
        		continue;
        	}
            AjaxData ad = new AjaxData();
            
            ad.setName(ajaxUser.getUsername());
            ad.setId(ajaxUser.getId());
            
            ajaxdata.add(ad);
            
        }
        
        ajstr.setData(ajaxdata);
        out(response, ajstr);
        
    }
    
    /**
     * 投标详情
     * @Title: tender_list 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param bid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/borrow/tenderlist")
    public String tender_list(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long bid) throws Exception{
        
        List<BorrowTender> tenderlist = borrowService.queryTenderByBorrow(bid);
        
        map.addAttribute("tenderlist", tenderlist);
        
        return "borrow/borrow_tender_list";
    }
	
    @RequestMapping("admin/borrow/loanList")
    public String loanList(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException{
    	
    	PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
        populate(pageRequest, request);
        pageRequest.setPageSize(10);
        Map<String, String> params = getParamMap(request);
        if(StringUtil.isNotEmpty(params.get("page"))){
            
            pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
        }
        
        pageRequest.setFilters(params);

        Page<BorrowLoan> loanPage = loanService.getLoanList(pageRequest);
        map.addAttribute("loanPage", loanPage);
      
        
        map.addAttribute("totalPage", PageUtils.computeLastPageNumber(loanPage.getTotalCount(), loanPage.getPageSize()));
        
        map.addAttribute("page",pageRequest.getPageNumber());
        map.addAttribute("msg", request.getParameter("msg"));
    	return "borrow/loan_list";
    }
    
    @RequestMapping("admin/borrow/auditLoan")
    public String auditLoan(ModelMap map,HttpServletRequest request, HttpServletResponse response,@RequestParam long id){
    	BorrowLoan loan = loanService.getLoanById(id);
    	map.addAttribute("msg", request.getParameter("msg"));
    	map.addAttribute("loan", loan);
    	return "borrow/loan";
    }
    
    @RequestMapping("admin/borrow/doAuditLoan")
    public String doAuditLoan(ModelMap map,HttpServletRequest request, HttpServletResponse response,@RequestParam long id){
    	String remark = request.getParameter("remark");
    	String status = request.getParameter("status");
    	AdminUser user = getCurrUser(request, response);
    	if(remark!=null && !"".equals(remark.trim())){
    		BorrowLoan loan = new BorrowLoan();
    		loan.setVerifyUser(user.getId());
    		loan.setId(id);
    		loan.setStatus(Integer.valueOf(status));
    		loan.setRemark(remark);
    		loan.setVerifyTime(TimeUtil.getNowTimeStr());
    		int res = loanService.updateLoan(loan);
    		if(res>0){
    			map.addAttribute("msg", "审核完成");
    		}else{
    			map.addAttribute("msg", "审核失败");
    		}
    		return "redirect:loanList.html";
    	}else{
    		map.addAttribute("msg", "请填写备注信息");
    		return "redirect:auditLoan.html";
    	}
    }
    
    
    /**
	 * 查看用户的借款情况
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("admin/borrow/jieBorrow")
	public String jieBorrow(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
			String type = request.getParameter("type");
			// 查询条件
			String username =request.getParameter("username");
			String borrowname = request.getParameter("borrowname");
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("username",username);
			params.put("borrowname",borrowname);
			params.put("type", type);
			pageRequest.setFilters(params);
			Page<BorrowRepayment> borrowpage = borrowService.getRepaymentList(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			map.addAttribute("type",type);
			map.addAttribute("username",username);
			map.addAttribute("borrowname",borrowname);
			if(type.equals("repayment")){
				map.addAttribute("mt",43);   //正在还款的项目
				return "borrow/repaymentList";
			}else if(type.equals("repaymentyes")){
				map.addAttribute("mt",44);   //已还款的项目
				return "borrow/repaymentyesList";
			}
			return null;
	}
	
	/**
     * 导出excel
     * @Title: recharge_list 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/borrow_excel")
    public String account_excel(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	Map<String, String> params = getParamMap(request);
    	
    	String eType=params.get("eType");
    	
    	//根据用户搜索信息导出excel记录
    	 try {
             OutputStream os = response.getOutputStream();//取得输出流
			    response.reset();//清空输出流
			    //下面是对中文文件名的处理
			    response.setCharacterEncoding("UTF-8");//设置相应内容的编码格式
			    if(eType.equals("jieBorrow")){
			    	String type=params.get("type");
			    	if("repayment".equals(type)){
			    		ExportExcel<BorrowRepayment> ex2 = new ExportExcel<BorrowRepayment>();
			    		String[] alias=new String[]{"Id","borrowname", "username","repaymentAccount","repaymentTimeStr","interest","lateDays","lateInterest"};
		        		String[] names=new String[]{"编号","项目名称", "还款人","应还款金额","应还款时间","应还款利息", "逾期天数","逾期利息"};
		        		List<BorrowRepayment> borrowList = borrowService.queryRepaymentList(params);
		        		BorrowRepayment bor;
						          for (int i = 0; i < borrowList.size(); i++) {
						        	  bor=borrowList.get(i);
						        	  if(bor!=null){
						        		  bor.setId(Long.valueOf(i)+1);
						        		  bor.setRepaymentTimeStr(DateUtils.dateStr(bor.getRepaymentTime(), "yyyy-MM-dd hh:mm:ss"));
						        		  borrowList.set(i, bor);
					        	  }
							}
						          String fname = java.net.URLEncoder.encode("未还款记录表","UTF-8");
								    response.setHeader("Content-Disposition","attachment;filename="+new String(fname.getBytes("UTF-8"),"GBK")+".xls");
								    response.setContentType("application/msexcel");//定义输出类型
			 			     ex2.exportExcel(names, borrowList, os,alias);
			    	}else if("repaymentyes".equals(type)){
			    		ExportExcel<BorrowRepayment> ex2 = new ExportExcel<BorrowRepayment>();
			    		String[] alias=new String[]{"Id","borrowname", "username","repaymentYesaccount","repaymentYestimeStr","interest"};
		        		String[] names=new String[]{"编号","项目名称", "还款人","还款金额","还款时间","已还利息"};
		        		List<BorrowRepayment> borrowList = borrowService.queryRepaymentList(params);
		        		BorrowRepayment bor;
						          for (int i = 0; i < borrowList.size(); i++) {
						        	  bor=borrowList.get(i);
						        	  if(bor!=null){
						        		  bor.setId(Long.valueOf(i)+1);
						        		  bor.setRepaymentYestimeStr(DateUtils.dateStr(bor.getRepaymentYestime(), "yyyy-MM-dd hh:mm:ss"));
						        		  borrowList.set(i, bor);
					        	  }
							}
						          String fname = java.net.URLEncoder.encode("未还款记录表","UTF-8");
								    response.setHeader("Content-Disposition","attachment;filename="+new String(fname.getBytes("UTF-8"),"GBK")+".xls");
								    response.setContentType("application/msexcel");//定义输出类型
			 			     ex2.exportExcel(names, borrowList, os,alias);
			    	}
			    }else if(eType.equals("inviteUser")){
			    	ExportExcel<User> ex2 = new ExportExcel<User>();
		    		String[] alias=new String[]{"inviteUserId","inviteUsername","id", "username","realname","createdAtStr","moneyTotal","money","moneyUsable"};
	        		String[] names=new String[]{"推荐人用户名","推荐人真实姓名","ID", "用户名","真实姓名","创建时间","总金额","操作金额","可用金额"};
	        		List<User> userList = userService.getInviteList(params);
	        		User user;
					          for (int i = 0; i < userList.size(); i++) {
					        	  user=userList.get(i);
					        	  if(user!=null){
					        		  if(user.getInviteUserId()!=null&&!"".equals(user.getInviteUserId())){
											User user1=userService.queryByUserId(Long.valueOf(user.getInviteUserId()));
											if(user1!=null){
												user.setInviteUserId(user1.getUsername());
												user.setInviteUsername(user1.getRealname());
											}
										}
					        		  user.setCreatedAtStr(DateUtils.dateStr(user.getCreatedAt(), "yyyy-MM-dd hh:mm:ss"));
					        		  userList.set(i, user);
				        	  }
						}
					          String fname = java.net.URLEncoder.encode("推荐人列表","UTF-8");
							    response.setHeader("Content-Disposition","attachment;filename="+new String(fname.getBytes("UTF-8"),"GBK")+".xls");
							    response.setContentType("application/msexcel");//定义输出类型
		 			     ex2.exportExcel(names, userList, os,alias);
		 			    map.addAttribute("startTime", params.get("startTime"));
		 			   map.addAttribute("endTime", params.get("endTime"));
			    }
			 response.setCharacterEncoding("UTF-8");
			 os.flush();
			 os.close();
			 map.addAttribute("type", params.get("type"));
             map.addAttribute("username", params.get("username"));
             map.addAttribute("borrowname", params.get("borrowname"));
    	 }catch(Exception e){
    		 e.printStackTrace();
    		
    	 }
    	 return null;
    }
    
    /**
   	 * 查看用户的借款情况
   	 * @return
   	 * @throws InvocationTargetException 
   	 * @throws IllegalAccessException 
   	 */
   	@RequestMapping("admin/borrow/invite_list")
   	public String inviteList(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
   			// 查询条件
   			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
   			populate(pageRequest, request);
   			pageRequest.setPageSize(10);
   			Map<String, String> params = getParamMap(request);
   			if(StringUtil.isNotEmpty(params.get("page"))){
   				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
   			}
   			pageRequest.setFilters(params);
   			Page<User> userPage = userService.getInviteList(pageRequest);
   			List<User> userList=userPage.getResult();
            for (User user : userList) {
				if(user.getInviteUserId()!=null&&!"".equals(user.getInviteUserId())){
					User user1=userService.queryByUserId(Long.valueOf(user.getInviteUserId()));
					if(user1!=null){
						user.setInviteUserId(user1.getUsername());
						user.setInviteUsername(user1.getRealname());
					}
				}
			}
            userPage.setResult(userList);
   			map.addAttribute("page",pageRequest.getPageNumber());
   			map.addAttribute("userPage", userPage);
   			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(userPage.getTotalCount(), userPage.getPageSize()));
   			map.addAttribute("startTime",request.getParameter("startTime"));
   			map.addAttribute("endTime",request.getParameter("endTime"));
   			return "borrow/invite_list";
   	}
   	
   	/**
	 * 导出excel
	 * 
	 * @Title: recharge_list
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/borrow/borrowListExcel")
	public String borrowListExcel(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);

		// 根据用户搜索信息导出excel记录
		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			// 下面是对中文文件名的处理
			response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
			ExportExcel<Borrow> ex2 = new ExportExcel<Borrow>();
			String[] alias = new String[] { "Id", "name", "borrowTypeName",
					"username", "repaymentStyleName", "isDayName",
					"account", "apr","totalInterest","validTime","addtimeStr","statusName"};
			String[] names = new String[] { "编号", "标题", "类型", "借款人",
					"还款方式", "是否天标", "借款金额", "年利率","总利息","有效时间","创建时间","状态" };
			List<Borrow> borrowList = borrowService.queryBorrow(params);
			int isDay,repaymentStyle,limit;
            double acc,apr;
			Borrow borrow=null;
			for (int i = 0; i <borrowList.size(); i++) {
				borrow = borrowList.get(i);
				acc=borrow.getAccount();
				apr=borrow.getApr();
				limit=borrow.getTimeLimit();
				isDay=borrow.getIsDay();
				String borrowTypeName=StatusUtils.getBorrowTypeName(borrow.getBorrowType());
				borrow.setBorrowTypeName(borrowTypeName);
				
				String repaymentStyleName=StatusUtils.getBorrowRepaymentStyle(borrow.getRepaymentStyle());
				borrow.setRepaymentStyleName(repaymentStyleName);
				
				if(isDay==1)
				{
					borrow.setIsDayName("是");
					double taotalInterest=BigDecimal.valueOf(acc*apr/100*limit/365).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					borrow.setTotalInterest(taotalInterest);
				}
				else
				{
					borrow.setIsDayName("否");
					//按月付息到期还本  repaymentStyle
            		repaymentStyle=borrow.getRepaymentStyle();
            		double taotalInterest=BigDecimal.valueOf(getInterestCalculator(acc,apr/100,limit,repaymentStyle).getTotalAccount()-acc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            		borrow.setTotalInterest(taotalInterest);
				}
				
				
				borrow.setAddtimeStr(DateUtils.dateStr(borrow.getAddtime(), "yyyy-MM-dd hh:mm:ss"));
				
				if(borrow.getMortgagor()!=null)
				{
					borrow.setUsername(borrow.getMortgagor());
				}
				
				String statusName=StatusUtils.getBorrowStatusName(borrow.getStatus());
				borrow.setStatusName(statusName);

			}
			String fname = java.net.URLEncoder.encode("理财记录表", "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(fname.getBytes("UTF-8"), "GBK") + ".xls");
			response.setContentType("application/msexcel");// 定义输出类型
			ex2.exportExcel(names, borrowList, os, alias);
			response.setCharacterEncoding("UTF-8");
			os.flush();
			os.close();
			map.addAttribute("id", params.get("id"));
			map.addAttribute("name", params.get("name"));
			map.addAttribute("startTime", params.get("startTime"));
			map.addAttribute("endTime", params.get("endTime"));
			map.addAttribute("status", params.get("status"));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
}
