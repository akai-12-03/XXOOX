package com.dept.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.context.Constant;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.Financing;
import com.dept.web.dao.model.FinancingApply;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.VerifyBorrowLog;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.StringUtils;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.FinancingApplyService;
import com.dept.web.service.FinancingService;
import com.dept.web.service.UserService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;


/**
 *融资部分
 * 
 * @ClassName: BorrowController.java
 * 
 * @version V1.0
 * @Date 2014-8-15 下午2:08:12 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class FinancingController extends WebController {

	@Autowired
	private FinancingService financingService;
	
	@Autowired
	private FinancingApplyService financingApplyService;
	  
    @Autowired
    private UserService userService;
	/**
	 * 发布融资标
	 */
    @RequestMapping("admin/financ/financ_add")
    public String artlist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        String ctype = params.get("opt");
        AdminUser user = getCurrUser(request, response);
        if(StringUtils.isEmpty(ctype)){
            
            return "financ/financ_add";
            
        }else if(ctype.equals("add")){
        	Financing f = new  Financing();
        	f.setName(params.get("name"));
        	if(!StringUtils.isEmpty(params.get("types1"))){
        		f.setTypes1(Integer.valueOf(params.get("types1")));
        	}else{
        		f.setTypes1(0);
        	}
        	if(!StringUtils.isEmpty(params.get("borrower"))){
            	f.setUser_id(Long.valueOf(params.get("borrower")));//借款人id
            }else{
	            String username=params.get("username");
	            List<User> userlist = userService.queryUserByUsername(username);
	            
	            if(userlist.size()>0){
	            	f.setUser_id(userlist.get(0).getId());//借款人id
	            }else{
	            	map.addAttribute("financ", f);
                	map.addAttribute("msg", "此借款人不存在");
	                return "financ/financ_add";
	            }
            }
        	if(!StringUtils.isEmpty(params.get("city"))){
        		f.setCity(Integer.valueOf(params.get("city")));
        	}else{
        		f.setCity(0);
        	}
        	if(!StringUtils.isEmpty(params.get("trade"))){
        		f.setTrade(Integer.valueOf(params.get("trade")));
        	}else{
        		f.setTrade(0);
        	}
        	if(!StringUtils.isEmpty(params.get("types"))){
        		f.setTypes(Integer.valueOf(params.get("types")));
        	}else{
        		map.addAttribute("financ", f);
            	map.addAttribute("msg", "请选择分类");
                return "financ/financ_add";
        	}
        	if(!StringUtils.isEmpty(params.get("guarantee"))){
        		f.setGuarantee(Integer.valueOf(params.get("guarantee")));
        	}else{
        		map.addAttribute("financ", f);
            	map.addAttribute("msg", "请选择担保方式");
                return "financ/financ_add";
        	}
        	if(!StringUtils.isEmpty(params.get("money"))){
        		f.setMoney(Double.valueOf(params.get("money")));
        	}else{
        		map.addAttribute("financ", f);
            	map.addAttribute("msg", "请输入融资金额");
                return "financ/financ_add";
        	}
        	if(!StringUtils.isEmpty(params.get("apr"))){
        		f.setApr(Double.valueOf(params.get("apr")));
        	}else{
        		f.setApr(0D);
        	}
        	if(!StringUtils.isEmpty(params.get("time_limit"))){
        		f.setTime_limit(Integer.valueOf(params.get("time_limit")));
        	}else{
        		f.setTime_limit(0);
        	}
        	if(!StringUtils.isEmpty(params.get("risk"))){
        		f.setRisk(Integer.valueOf(params.get("risk")));
        	}else{
        		f.setRisk(0);
        	}
        	f.setContent(params.get("content"));
        	f.setJdbg(params.get("jdbg"));
        	f.setFkxx(params.get("fkxx"));
        	f.setHkjh(params.get("hkjh"));
        	f.setZjjy(params.get("zjjy"));
        	f.setContents(params.get("contents"));
        	f.setJdbgs(params.get("jdbgs"));
        	f.setFkxxs(params.get("fkxxs"));
        	f.setHkjhs(params.get("hkjhs"));
        	f.setZjjys(params.get("zjjys"));
        	f.setAddtime(String.valueOf(DateUtils.getNowTimeStr()));
        	f.setStatus(0);
        	Financing financing = new Financing();
        	List list =financingService.queryFinancingList(financing);
        	String num ="RZ"+ new SimpleDateFormat("yyyy-MMdd").format(new Date())+"-";
        	f.setNum(num);
        	f.setCounts(0);
        	financingService.insertFinancing(f);
        	map.addAttribute("msg", "创建成功");
        	 return "financ/financ_add";
        }
        return "financ/financ_add";
    }
	
	
    /**
     * 查看融资标列表
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
    @RequestMapping("admin/financ/financ_list")
    public String financList(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	
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

            Page<Financing> financingPage = financingService.queryFinancingPage(pageRequest);
            
            List<Financing> financList=financingPage.getResult();
            for(Financing f: financList){
            	if(f.getUser_id()>0){
            		User user = userService.queryByUserId(f.getUser_id());
            		if(user!=null){
            			f.setUsername(user.getUsername());	
            		}
            	}
            }
            financingPage.setResult(financList);
             
            map.addAttribute("financingPage", financingPage);
          
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(financingPage.getTotalCount(), financingPage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("name", params.get("name"));
            
            map.addAttribute("startTime", params.get("startTime"));
            
            map.addAttribute("endTime", params.get("endTime"));
            
            map.addAttribute("status", params.get("status"));
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return "financ/financ_list";
    }    
    
    /**
     * 查看融资详细信息
     */
	
    @RequestMapping("admin/financ/financ_detail")
    public String financDetail(ModelMap map,HttpServletRequest request, HttpServletResponse response,@RequestParam long bid) throws Exception{
    	 Financing financing = financingService.queryById(bid);
    	 if(financing.getUser_id()>0){
     		User user = userService.queryByUserId(financing.getUser_id());
     		if(user!=null){
     			financing.setUsername(user.getUsername());	
     		}
     	}
         map.addAttribute("financing", financing);
         return "financ/financ_detail";
    }
	
    /**
     * 审借款标
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
    @RequestMapping("admin/financ/financ_veifyF")
    public String opborrow_veifyF(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam String bid,@RequestParam String pid) throws Exception{
        
      //  AdminUser user = getCurrUser(request, response);
        
    	 Financing financing = financingService.queryById(Long.valueOf(bid));
         
    	 if(financing.getUser_id()>0){
      		User user = userService.queryByUserId(financing.getUser_id());
      		if(user!=null){
      			financing.setUsername(user.getUsername());	
      		}
      	}
          map.addAttribute("financing", financing);
          map.addAttribute("pid", pid);
             Map<String,String> params = getParamMap(request);
             
             String ctype = params.get("opt");
             
             if(StringUtil.isEmpty(ctype)){
                 
                 return "financ/financ_verify";
                 
             }else if(ctype.equals("upd")){
                 
            	 financing.setStatus(Integer.valueOf(request.getParameter("status")));
            	 financing.setStarttime(String.valueOf(DateUtils.getNowTimeStr()));
            	 financingService.updateFinancingStatus(financing);
                 map.addAttribute("msg", "操作成功");
                 return "redirect:financ_list.html";
             }
        return "financ/financ_detail";
    }
    
	
    /**
     * 查看申请融资标列表
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
    @RequestMapping("admin/financ/financApply_list")
    public String financApplyList(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	
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

            Page<FinancingApply> financingApplyPage = financingApplyService.queryFinancingApplyPage(pageRequest);
            
            List<FinancingApply> financApplyList=financingApplyPage.getResult();
            for(FinancingApply f: financApplyList){
            	if(f.getUser_id()>0){
            		User user = userService.queryByUserId(f.getUser_id());
            		if(user!=null){
            			f.setUsername(user.getUsername());	
            		}
            	}
            }
            financingApplyPage.setResult(financApplyList);
             
            map.addAttribute("financingApplyPage", financingApplyPage);
          
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(financingApplyPage.getTotalCount(), financingApplyPage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("name", params.get("name"));
            
            map.addAttribute("startTime", params.get("startTime"));
            
            map.addAttribute("endTime", params.get("endTime"));
            
            map.addAttribute("status", params.get("status"));
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return "financ/financApply_list";
    }  
    
    
    
    /**
     * 查看申请融资详细信息
     */
	
    @RequestMapping("admin/financ/financApply_detail")
    public String financApplyDetail(ModelMap map,HttpServletRequest request, HttpServletResponse response,@RequestParam long bid) throws Exception{
    	FinancingApply financingApply = financingApplyService.queryById(bid);
    	 if(financingApply.getUser_id()>0){
     		User user = userService.queryByUserId(financingApply.getUser_id());
     		if(user!=null){
     			financingApply.setUsername(user.getUsername());	
     		}
     	}
         map.addAttribute("financingApply", financingApply);
         return "financ/financApply_detail";
    }
    
    
    /**
     * 审借申请融资款标
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
    @RequestMapping("admin/financ/financApply_veifyF")
    public String opborrowApply_veifyF(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam String bid,@RequestParam String pid) throws Exception{
    	 FinancingApply financingApply = financingApplyService.queryById(Long.valueOf(bid));
         
    	 if(financingApply.getUser_id()>0){
      		User user = userService.queryByUserId(financingApply.getUser_id());
      		if(user!=null){
      			financingApply.setUsername(user.getUsername());	
      		}
      	}
          map.addAttribute("financingApply", financingApply);
          map.addAttribute("pid", pid);
             Map<String,String> params = getParamMap(request);
             
             String ctype = params.get("opt");
             
             if(StringUtil.isEmpty(ctype)){
                 
                 return "financ/financApply_verify";
                 
             }else if(ctype.equals("upd")){
                 
            	 financingApply.setStatus(Integer.valueOf(request.getParameter("status")));
            	 financingApplyService.updateFinancingApplyById(financingApply);
                 map.addAttribute("msg", "操作成功");
                 return "redirect:financApply_list.html";
             }
        return "financ/financApply_detail";
    }
    
    
}