package com.dept.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.UserRechargeDao;
import com.dept.web.dao.UserWithdrawDao;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.Article;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.HongbaoLog;
import com.dept.web.dao.model.PlanRecord;
import com.dept.web.dao.model.Site;
import com.dept.web.service.BorrowService;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserRecharge;
import com.dept.web.dao.model.UserWithdraw;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.ProperUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.excel.ExcelHelper;
import com.dept.web.service.AccountService;
import com.dept.web.service.BorrowTenderService;
import com.dept.web.service.HelpService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 新闻资讯
 * 
 * @ClassName:     NewsController
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月30日 上午9:58:25 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class NewsController extends WebController{

    @Autowired
    private HelpService helpService;
    
    @Autowired
    private BorrowTenderService borrowTenderService;
    
    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserWithdrawDao userWithdrawDao;
    
    @Autowired
    private UserAccountDao userAccountDao;
    
    @Autowired
    private UserRechargeDao userRechargeDao;
    
    /**
     * 新闻添加编辑
     * @Title: newsedit 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/newsview")
    public String newsedit(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
       
        Map<String, String> params = getParamMap(request);
        
        String type = params.get("opt");
        
        List<Site> sitelist = helpService.querySiteList();
        
        map.addAttribute("sitelist", sitelist);
        
        if(StringUtils.isEmpty(type)){
            
            map.addAttribute("opt", "add");
            
            return "news/newsedit";
            
        }else if(type.equals("add")){
            
            AdminUser user = new AdminUser();
            
            Article art = new Article();
            art.setSiteId(Long.valueOf(params.get("siteId")));
            art.setName(params.get("name"));
            art.setLittitle(params.get("littitle"));
            art.setStatus(Integer.valueOf(params.get("status")));
            art.setSource(params.get("source"));
            art.setAuthor(params.get("author"));
            art.setSummary(params.get("summary"));
            art.setContent(params.get("content"));
            if(StringUtils.isEmpty(params.get("aOrder"))){
            	map.addAttribute("msg", "排行不能为空");
                return "news/newsedit";
            }else{
                
                art.setAorder(Integer.valueOf(params.get("aOrder")));
            }
            art.setAorder(Integer.valueOf(params.get("aOrder")));
            art.setIsComment(Integer.valueOf(params.get("isComment")));
            
            art.setPublish(DateUtils.getNowTimeStr());
            art.setUserId(user.getId());
            Site site=helpService.getSiteById(art.getSiteId());
            if(site!=null){
            	 art.setFlag(site.getNid());
            }
            art.setCreatedAt(DateUtils.getNowTimeStr());
            String  litpic=params.get("litpic");
            //上传图片
            if(!StringUtils.isEmpty(litpic)){
				Document  doc = Jsoup.parse(litpic);
				if(doc.getElementsByTag("img").size()>0){
					Element eme= doc.getElementsByTag("img").get(0);
					String pics  = eme.attr("src");
					if(!StringUtils.isEmpty(pics)){
						 art.setLitpic(pics);
					}
				}
				
			}
            helpService.createArticle(art);
            map.addAttribute("msg", "添加成功");
            map.addAttribute("opt", "add");
            
            return "news/newsedit";
            
        }else if(type.equals("edit")){
            
            String aid = params.get("aid");
            
            if(StringUtils.isEmpty(aid)){
                
                map.addAttribute("opt", "add");
                
                return "news/newsedit";
                
            }else{
                map.addAttribute("opt", "upd");
                
                Article art = helpService.queryArtById(Long.valueOf(aid));
                
                map.addAttribute("article", art);
                
                return "news/newsedit";
            }
            
        }else if(type.equals("upd")){
            
            String aid = params.get("aid");
            
            if(StringUtils.isEmpty(aid)){
                
                return "news/newsedit";
                
            }else{
                
                Article art = helpService.queryArtById(Long.valueOf(aid));
                
                art.setSiteId(Long.valueOf(params.get("siteId")));
                art.setName(params.get("name"));
                art.setLittitle(params.get("littitle"));
                art.setStatus(Integer.valueOf(params.get("status")));
                art.setSource(params.get("source"));
                art.setAuthor(params.get("author"));
                art.setSummary(params.get("summary"));
                art.setContent(params.get("content"));
                
              //上传图片
                String  litpic=params.get("litpic");
                if(!StringUtils.isEmpty(litpic)){
    				Document  doc = Jsoup.parse(litpic);
    				Element eme= doc.getElementsByTag("img").get(0);
    				String pics  = eme.attr("src");
    				if(!StringUtils.isEmpty(pics)){
    					 art.setLitpic(pics);
    				}
    			}
                if(StringUtils.isEmpty(params.get("aOrder"))){
                    
                    art.setAorder(0);
                }else{
                    
                    art.setAorder(Integer.valueOf(params.get("aOrder")));
                }
                art.setIsComment(Integer.valueOf(params.get("isComment")));

                art.setUpdatedAt(DateUtils.getNowTimeStr());
                
                helpService.updateArticle(art);
                
                map.addAttribute("msg", "更新成功");
                
                return "news/newsedit";
            }
            
        }else{
            
            return null;
        }
       
    }
    
    /**
	 * 资金一览
	 */
	@RequestMapping("admin/report/amoneyTotal")
	public String moneyTotal(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		/**
		 * 查询有效的发标个数 和有效的发标总金额
		 */
		Borrow borrow =  borrowService.getBorrowCountAndMoney();
		if(borrow!=null){
			 map.addAttribute("borrowCount", borrow.getBorrowCount());
			 map.addAttribute("borrowMoneyCount", borrow.getBorrowMoneyCount());
			 map.addAttribute("borrowMoneyCountYes", borrow.getBorrowMoneyCountYes());
		}
		/**
		 * 查询线上充值总金额
		 */
		UserRecharge recharge=	userRechargeDao.getUserRechargeByTotal();
		if(recharge!=null){
			 map.addAttribute("recharegeTotalMoney", recharge.getTotalMoney());
		}
		/**
		 * 查询线下充值总金额
		 */
		UserRecharge recharge2=	userRechargeDao.getUserRechargeByTotal2();
		if(recharge2!=null){
			 map.addAttribute("recharegeTotalMoney2", recharge2.getTotalMoney());
		}
		/**
		 * 查询提现总金额和提现总手续费
		 */
		UserWithdraw withdraw= userWithdrawDao.getUserWithDrawByTotal();
		if(withdraw!=null){
			 map.addAttribute("withdrawTotalMoney", withdraw.getTotalMoney());
		}
		/**
		 *  查询可用余额总金额
		 */
		UserAccount account = userAccountDao.getUserAccountByUserMoneyTotal();
		if(account!=null){
			map.addAttribute("accountTotalMoney", account.getTotalMoney());
		}
		return "report/money_list";
	}
	
	 /**
     * 用户投标明细表
     */
    @RequestMapping("borrow/borrowTender")
    public String tender(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtils.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            if(StringUtils.isNotEmpty(params.get("startTime"))){
                
                String startTime = params.get("startTime");
                
                params.put("startTime", startTime);
            }
            
            if(StringUtils.isNotEmpty(params.get("endTime"))){
                
                String endTime = params.get("endTime");
                
                params.put("endTime", endTime);
            }
            
            pageRequest.setFilters(params);
            Page<BorrowTender> tenderpage = borrowService.queryBorrowTender(pageRequest);
            for( BorrowTender ten:tenderpage.getResult()){
            	String time =ten.getTzsj();
            	ten.setTzsjs(TimeUtil.getStr1(time));
            }
            /**
             * 导出数据
             */
            if("excel".equals(params.get("excel"))){
            	//导出路径
            	String rootUrl=ProperUtil.getProperty("config", "url");
            	//导出文件名
            	String downloadFile="borrowTenderExport"+System.currentTimeMillis()+".xls";
            	//
            	String infile=rootUrl + downloadFile;
            	
            	String[] names=new String[]{"borrowId","borrowname", "username","tzsjs","tzqx","tzmoney","apr","interest","hongbao_money"};
        		String[] titles=new String[]{"标ID","标名称", "用户名","投资时间","投资期限","投资金额", "利率","利息","使用红包金额"};
            	
        		ExcelHelper.writeExcel(infile, tenderpage.getResult(), BorrowTender.class, Arrays.asList(names), Arrays.asList(titles));
        		export(response,infile, downloadFile);
        		return null;
            }

            map.addAttribute("tenderpage", tenderpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(tenderpage.getTotalCount(), tenderpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("startTime", params.get("startTime"));
            map.addAttribute("endTime", params.get("endTime"));
            map.addAttribute("username", params.get("username"));
            String aaa=params.get("username");
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return "borrow/borrowTender";
        
    }
    
    @RequestMapping("admin/moneyLog")
    public String depositRecord_list(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            	
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            pageRequest.setFilters(params);
            Page<UserRecharge> accountpage = accountService.queryUserRecharge(pageRequest);
            if("excel".equals(params.get("excel"))){
            	//导出路径
            	String rootUrl=ProperUtil.getProperty("config", "url");
            	//导出文件名
            	String downloadFile="borrowTenderExport"+System.currentTimeMillis()+".xls";
            	//
            	String infile=rootUrl + downloadFile;
            	String[] names=new String[]{"orderId", "status","username","remark","createdAt","createdIp","updatedAt"};
        		String[] titles=new String[]{"充值单号", "是否充值成功","充值用户名", "备注","充值时间","充值IP地址", "审核时间"};
            	
        		ExcelHelper.writeExcel(infile, accountpage.getResult(), UserRecharge.class, Arrays.asList(names), Arrays.asList(titles));
        		export(response,infile, downloadFile);
        		return null;
            }
            
            map.addAttribute("accountpage", accountpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(accountpage.getTotalCount(), accountpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("status", params.get("status"));
            String aa=params.get("status");
            map.addAttribute("orderId", params.get("orderId"));
            String aa2=params.get("orderId");
            map.addAttribute("username", params.get("username"));
            String aa3=params.get("username");
            map.addAttribute("startTime", params.get("startTime"));
            map.addAttribute("endTime", params.get("endTime"));
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return "report/depositRecord_list";
    }
    
    /**
     * 借款人情况
     */
    @RequestMapping("borrow/borrowlist")
    public String borrowlist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtils.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            if(StringUtils.isNotEmpty(params.get("startTime"))){
                
                String startTime = params.get("startTime");
                
                params.put("startTime", startTime);
            }
            
            if(StringUtils.isNotEmpty(params.get("endTime"))){
                
                String endTime = params.get("endTime");
                
                params.put("endTime", endTime);
            }
            
            pageRequest.setFilters(params);

            Page<Borrow> borrowpage = borrowService.queryBorrow(pageRequest);
            
            /**
             * 导出数据
             */
            if("excel".equals(params.get("excel"))){
            	//导出路径
            	String rootUrl=ProperUtil.getProperty("config", "url");
            	//导出文件名
            	String downloadFile="borrowTenderExport"+System.currentTimeMillis()+".xls";
            	//
            	String infile=rootUrl + downloadFile;
            	
            	String[] names=new String[]{"id","name", "addtime","verifyTime","account","award","apr","timeLimit","username","hktime","hkinterest","hkaccount"};
        		String[] titles=new String[]{"标ID","标名称","发标时间", "审核通过时间","发标金额", "奖励金额","利率","投资期限","借款人","还款时间","利息","已还款金额"};
            	
        		ExcelHelper.writeExcel(infile, borrowpage.getResult(), Borrow.class, Arrays.asList(names), Arrays.asList(titles));
        		export(response,infile, downloadFile);
        		return null;
            }

            map.addAttribute("borrowpage", borrowpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("startTime", params.get("startTime"));
            map.addAttribute("endTime", params.get("endTime"));
            map.addAttribute("username", params.get("username"));
            String aaa=params.get("username");
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return "borrow/borrowlist";
        
    }
    @RequestMapping("admin/tbqklist")
    public String tbqklist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            pageRequest.setFilters(params);
            Page<BorrowTender> tbqktpage = borrowTenderService.queryTbqk(pageRequest);

            /**
             * 导出数据
             */
            if("excel".equals(params.get("excel"))){
            	//导出路径
            	String rootUrl=ProperUtil.getProperty("config", "url");
            	//导出文件名
            	String downloadFile="honbaoExport"+System.currentTimeMillis()+".xls";
            	//
            	String infile=rootUrl + downloadFile;
            	
            	String[] names=new String[]{"id", "name","apr","username","account","interest"};
        		String[] titles=new String[]{"标ID", "标名","利润(%)", "投资人","投资金额","利息"};
            	
        		ExcelHelper.writeExcel(infile, tbqktpage.getResult(), HongbaoLog.class, Arrays.asList(names), Arrays.asList(titles));
        		export(response,infile, downloadFile);
        		return null;
            }
            
            map.addAttribute("tbqktpage", tbqktpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(tbqktpage.getTotalCount(), tbqktpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("name", params.get("name"));
            String aa=params.get("name");
            map.addAttribute("id", params.get("id"));
            String aa2=params.get("id");
            map.addAttribute("username", params.get("username"));
            String aa3=params.get("username");
            map.addAttribute("startTime", params.get("startTime"));
            map.addAttribute("endTime", params.get("endTime"));
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return "report/tbqk_list";
    }
 
    
    @RequestMapping("admin/artlist")
    public String artlist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            if(StringUtils.isNotEmpty(params.get("startTime"))){
                
                String startTime = params.get("startTime");
                
                params.put("startTime", startTime);
            }
            
            if(StringUtils.isNotEmpty(params.get("endTime"))){
                
                String endTime = params.get("endTime");
                
                params.put("endTime", endTime);
            }
            pageRequest.setFilters(params);

            Page<Article> artpage = helpService.queryArtPage(pageRequest);

            map.addAttribute("artpage", artpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(artpage.getTotalCount(), artpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        
        return "news/news_list";
        
        
    }
	
}
