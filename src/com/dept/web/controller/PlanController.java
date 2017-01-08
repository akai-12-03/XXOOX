package com.dept.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.context.Constant;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.HongbaoPlan;
import com.dept.web.dao.model.PlanAppendInsure;
import com.dept.web.dao.model.PlanRecord;
import com.dept.web.dao.model.PlanSetting;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.NewsDateUtils;
import com.dept.web.general.util.StringUtils;
import com.dept.web.general.util.mmsg.SendMessageUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.AccountService;
import com.dept.web.service.PlanService;
import com.dept.web.service.UserService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 配资部分
 * 
 * @ClassName:     PlanController
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月16日 下午4:48:59 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@SuppressWarnings("all")
@Controller
public class PlanController extends WebController{
	
    @Autowired
    private PlanService planService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 查询配资记录
     * @Title: planRecord 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/plan/record")
    public String planRecord(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        map.addAttribute("msg", params.get("msg"));
        
        String status = params.get("status");
        
        if(StringUtils.isEmpty(status)){
            
            params.put("status", null);
            
        }else{
            
            if(status.equals("beapproved")){
                
                params.put("status", "9");
                
            }else if(status.equals("running")){
                
                params.put("status", "0");
                
            }else if(status.equals("finished")){
                
                params.put("status", "1");
            }else if(status.equals("refuse")){
                
                params.put("status", "5");
            }
        }
        
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            pageRequest.setFilters(params);

            Page<PlanRecord> planpage = planService.queryRecordByStatus(pageRequest);

            map.addAttribute("planpage", planpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(planpage.getTotalCount(), planpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("status", params.get("status"));
            
            map.addAttribute("username", params.get("username"));
            
            map.addAttribute("mobile", params.get("mobile"));
            
            map.addAttribute("pid", params.get("pid"));
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        
        return "plan/plan_record";
        
        
    }
    
    /**
     * 审核配资记录
     * @Title: updateRecord 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param pid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/plan/updateRecord")
    public String updateRecord(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long pid) throws Exception{
        
        try {
            
            AdminUser aduser = getCurrUser(request, response);
            
            Map<String, String> params = getParamMap(request);
            
            PlanRecord pr = planService.queryPlanRecordById(pid);
            
            if(StringUtils.isEmpty(params.get("opt"))){
                
                map.addAttribute("pse", pr);
                
                return "plan/update_planrecord";
                
            }else{
                
                if(pr.getStatus()==0){
                    
                    pr.setHomsAccount(params.get("homsaccount"));
                    pr.setHomsPwd(params.get("homspwd"));
                    pr.setStatus(1);
                    pr.setUserOperate(aduser.getId());
                    pr.setOperatedAt(NewsDateUtils.getNowTimeStr());
                    
                    //免费体验
                    if(pr.getPlanType()==3){
                        pr.setStartTime(DateUtils.getNowTimeStr());
                        pr.setEndTime(DateUtils.dateadd(DateUtils.getNowTimeStr(), 2)); //两天
                    }else if(pr.getPlanType()==2){
                        pr.setStartTime(DateUtils.getNowTimeStr());
                        pr.setEndTime(DateUtils.rollMonth(DateUtils.getNowTimeStr(), pr.getInterval())); //两天 
                    }else{
                        
                        if(StringUtil.isNotEmpty(params.get("endTime"))){
                            
                            pr.setStartTime(DateUtils.getNowTimeStr());
                            pr.setEndTime(DateUtils.getDateLong(params.get("endTime"), "MM/dd/yyyy")+60*60*23);
                            
                        }
                    }
                    
                    planService.updatePlanRecord(pr);
                    
                    double zhifuewai1 = 0;
                    
                    if(pr.getPlanType()==1){
                        
                        zhifuewai1 = pr.getMoneyFee();
                        
                    }else if(pr.getPlanType()==2){
                        
                        double sylx = pr.getMoneyInsure()*pr.getPower()*pr.getRate()/100;
                        
                        zhifuewai1 = pr.getMoneyInsure()+sylx;
                        
                    //免费体验    
                    }else if(pr.getPlanType()==3){
                        
                        double sylx = 0;
                        
                        zhifuewai1 = pr.getMoneyInsure();
                    }
                    
                    //更新账户记录
                    UserAccount ua = accountService.queryAccountByUser(pr.getUserId());
                    ua.setMoneyTotal(ua.getMoneyTotal()-pr.getMoneyInsure());
                    ua.setMoneyInsure(ua.getMoneyInsure()-pr.getMoneyInsure());
                    ua.setUpdatedAt(NewsDateUtils.getNowTimeStr());
                    
                    accountService.updateAccount(ua);
                    
                    //新建资金记录
                    UserAccount newua = accountService.queryAccountByUser(pr.getUserId());

                    UserAccountLog ual = new UserAccountLog();
                    
                    //新建资金记录
                    ual.setUserId(pr.getUserId());
                    ual.setType(4);
                    ual.setMoneyOperate(pr.getMoneyInsure());
                    ual.setMoneyTotal(newua.getMoneyTotal());
                    ual.setMoneyUsable(newua.getMoneyUsable());
                    ual.setMoneyWithdraw(newua.getMoneyWithdraw());
                    ual.setMoneyInsure(newua.getMoneyInsure());
                    ual.setRemark("分配同花顺账号扣除保证金"+pr.getMoneyInsure()+"元");
                    ual.setCreatedAt(NewsDateUtils.getNowTimeStr());
                    ual.setCreatedIp(IPUtils.getRemortIP(request));
                    ual.setMoneyCollection(newua.getMoneyCollection());
                    ual.setMoneyTenderFreeze(newua.getMoneyTenderFreeze());
                    
                    accountService.createUserAccountLog(ual); 

                    map.addAttribute("msg", "分配成功");
//                  分配了配资账号后，给对应的申请的用户发送一条短信
                 String  content="尊敬的用户，您的配资申请已经通过。请及时登陆e聚财官网查看具体信息。";
                 User senduser = userService.queryByUserId(pr.getUserId());
//              	10表示系统发出的消息
                 SendMessageUtil.sendSMSE(senduser.getMobile(), content);
                 
                    return "redirect:record.html";
                    
                }else if(pr.getStatus()==1){
                    
                    pr.setHomsAccount(params.get("homsaccount"));
                    pr.setHomsPwd(params.get("homspwd"));
                    pr.setUserOperate(aduser.getId());
                    pr.setOperatedAt(NewsDateUtils.getNowTimeStr());
                    
                    planService.updatePlanRecord(pr); 
                    
                    map.addAttribute("msg", "更改密码成功");
                    
                    return "redirect:record.html";
                    
                }else{
                    
                    return "redirect:record.html";
                }
                
            }
            
        } catch (Exception e) {
           e.printStackTrace();
        }
        

        return null;
    }
    
    /**
     * 查看配资设定记录
     * @Title: setshow 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/plan/setshow")
    public String setshow(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        try {
            
            Map<String, String> params = getParamMap(request);
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            pageRequest.setFilters(params);

            Page<PlanSetting> setpage = planService.queryPlanSet(pageRequest);

            map.addAttribute("setpage", setpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(setpage.getTotalCount(), setpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }        
        
        return "plan/plan_setting";
        
    }
    
    /**
     * 创建配资设定
     * @Title: createsetting 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/plan/createsetting")
    public String createsetting(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        try {
            
            Map<String, String> params = getParamMap(request);
            
            AdminUser user =  getCurrUser(request, response);
            
            if(user==null){
                
                return "redirect:index.html";
            }
            
            String ctype = params.get("opt");
            
            if(StringUtils.isEmpty(ctype)){
                
                return "plan/plan_setting_create";
                
            }else if(ctype.equals("add")){
                
                PlanSetting pse = new PlanSetting();
                
                pse.setType(Integer.valueOf(params.get("type")));
                
                if(StringUtils.isEmpty(params.get("power"))){
                    pse.setPower(0.00);
                }else{
                    pse.setPower(Double.valueOf(params.get("power")));
                }
                
                pse.setStatus(0);
                pse.setUserCreate(user.getId());
                pse.setCreatedAt(DateUtils.getNowTimeStr());
                
                planService.createPlanSetting(pse);
                
                map.addAttribute("msg", "添加配资设定成功");
                
                return "redirect:setshow.html";
                
            }else if(ctype.equals("update") && StringUtil.isNotEmpty(params.get("pid"))){
                
                PlanSetting pse = planService.queryPlanSettingById(Long.valueOf(params.get("pid")));
                
                map.addAttribute("pse", pse);
                
                return "plan/plan_setting_upd";
                
            }else if(ctype.equals("upd") && StringUtil.isNotEmpty(params.get("pid"))){
                
                PlanSetting pse = planService.queryPlanSettingById(Long.valueOf(params.get("pid")));
                
                if(pse.getStatus()==1){
                    
                    return "redirect:setshow.html";
                }
                
                pse.setPower(Double.valueOf(params.get("power")));
                pse.setStatus(Integer.valueOf(params.get("status")));
                pse.setUpdatedAt(DateUtils.getNowTimeStr());
                pse.setUserOperate(user.getId());
                
                planService.updatePlanSetting(pse);
                
                map.addAttribute("msg", "更新配资设定成功");
                
                return "redirect:setshow.html";
            }
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }        
        
        return "plan/plan_setting";
        
    }
	
    
    /**
     * 追加保证金记录列表
     * @Title: append_record 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/plan/append_record")
    public String append_record(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        map.addAttribute("msg", params.get("msg"));
                
        if(StringUtil.isNotEmpty(params.get("startTime"))){
            
            String startTime = params.get("startTime").replace("/", "-");
            
            params.put("startTime", startTime);
        }
        
        if(StringUtil.isNotEmpty(params.get("endTime"))){
            
            String endTime = params.get("endTime").replace("/", "-");
            
            params.put("endTime", endTime);
        }        
        
        try {
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            pageRequest.setFilters(params);

            Page<PlanAppendInsure> appendpage = planService.getPlanAppendInsurePage(pageRequest);

            map.addAttribute("appendpage", appendpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(appendpage.getTotalCount(), appendpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
            map.addAttribute("status", params.get("status"));
            
            map.addAttribute("username", params.get("username"));
            
            map.addAttribute("mobile", params.get("mobile"));
            
            map.addAttribute("startTime", params.get("startTime"));
            
            map.addAttribute("endTime", params.get("endTime"));
            
            map.addAttribute("reid", params.get("reid"));
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        
        return "plan/plan_append_insure_log";
        
        
    }
    
    
    
    /**
     * 审核追加保证金
     * @Title: updateAppend 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param pid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/plan/updateAppend")
    public String updateAppend(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long pid) throws Exception{
        
        try {
            
            AdminUser aduser = getCurrUser(request, response);
            
            Map<String, String> params = getParamMap(request);
            
            PlanAppendInsure pair = planService.queryAppendById(pid);
            
            if(StringUtils.isEmpty(params.get("opt"))){
                
                map.addAttribute("pai", pair);
                
                return "plan/update_planAppend";
                
            }else if(params.get("opt").equals("noagree")){
                
                if(pair.getStatus()==0){
                    
                    pair.setStatus(9);
                    pair.setUserOperate(aduser.getId());
                    pair.setOperatedAt(NewsDateUtils.getNowTimeStr());
                    
                    planService.updateAppend(pair);
                                     
                    //更新账户记录
                    UserAccount ua = accountService.queryAccountByUser(pair.getUserId());
                    ua.setMoneyUsable(ua.getMoneyUsable()+pair.getMoneyInsure());
                    ua.setMoneyInsure(ua.getMoneyInsure()-pair.getMoneyInsure());
                    ua.setUpdatedAt(NewsDateUtils.getNowTimeStr());
                    
                    accountService.updateAccount(ua);
                    
                    //新建资金记录
                    UserAccount newua = accountService.queryAccountByUser(pair.getUserId());

                    UserAccountLog ual = new UserAccountLog();
                    
                    //新建资金记录
                    ual.setUserId(pair.getUserId());
                    ual.setType(Constant.ACCOUNT_LOG_TYPE_BZJ_ZJJJ);
                    ual.setMoneyOperate(pair.getMoneyInsure());
                    ual.setMoneyTotal(newua.getMoneyTotal());
                    ual.setMoneyUsable(newua.getMoneyUsable());
                    ual.setMoneyWithdraw(newua.getMoneyWithdraw());
                    ual.setMoneyInsure(newua.getMoneyInsure());
                    ual.setRemark("追加保证金操作拒绝, 退回保证金"+pair.getMoneyInsure()+"元");
                    ual.setCreatedAt(NewsDateUtils.getNowTimeStr());
                    ual.setCreatedIp(IPUtils.getRemortIP(request));
                    ual.setMoneyCollection(newua.getMoneyCollection());
                    ual.setMoneyTenderFreeze(newua.getMoneyTenderFreeze());
                    
                    accountService.createUserAccountLog(ual); 
                    
                    map.addAttribute("msg", "操作成功");
                    
                    return "redirect:append_record.html?status=9";
                       
                }else{
                    
                    return "redirect:append_record.html?status=0";
                }
                
            }else if(params.get("opt").equals("agree")){
                
                if(pair.getStatus()==0){
                    
                    pair.setStatus(1);
                    pair.setUserOperate(aduser.getId());
                    pair.setOperatedAt(NewsDateUtils.getNowTimeStr());
                    
                    planService.updateAppend(pair);

                    //更新账户记录
                    UserAccount ua = accountService.queryAccountByUser(pair.getUserId());
                    ua.setMoneyTotal(ua.getMoneyTotal()-pair.getMoneyInsure());
                    ua.setMoneyInsure(ua.getMoneyInsure()-pair.getMoneyInsure());
                    ua.setUpdatedAt(NewsDateUtils.getNowTimeStr());
                    
                    accountService.updateAccount(ua);
                    
                    //新建资金记录
                    UserAccount newua = accountService.queryAccountByUser(pair.getUserId());

                    UserAccountLog ual = new UserAccountLog();
                    
                    //新建资金记录
                    ual.setUserId(pair.getUserId());
                    ual.setType(12);
                    ual.setMoneyOperate(pair.getMoneyInsure());
                    ual.setMoneyTotal(newua.getMoneyTotal());
                    ual.setMoneyUsable(newua.getMoneyUsable());
                    ual.setMoneyWithdraw(newua.getMoneyWithdraw());
                    ual.setMoneyInsure(newua.getMoneyInsure());
                    ual.setRemark("追加保证金操作扣除"+pair.getMoneyInsure()+"元");
                    ual.setCreatedAt(NewsDateUtils.getNowTimeStr());
                    ual.setCreatedIp(IPUtils.getRemortIP(request));
                    ual.setMoneyCollection(newua.getMoneyCollection());
                    ual.setMoneyTenderFreeze(newua.getMoneyTenderFreeze());
                    
                    accountService.createUserAccountLog(ual); 
                    
                    map.addAttribute("msg", "操作成功");
                    
                    return "redirect:append_record.html?status=1";
                       
                }else{
                    
                    return "redirect:append_record.html?status=0";
                }
                
            }
            
        } catch (Exception e) {
           e.printStackTrace();
        }
        

        return null;
    }    
    
    
    /**
     * 拒绝配资申请
     * @Title: refuseRecord 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param pid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/plan/refuseRecord")
    public String refuseRecord(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long pid) throws Exception{
        
        AdminUser aduser = getCurrUser(request, response);
        
        Map<String,String> params = getParamMap(request);
        
        PlanRecord pai = planService.queryPlanRecordById(pid);
        
        String ctype = params.get("opt");
        
        if(StringUtils.isEmpty(ctype)){
            
            map.addAttribute("pai", pai);
            
            return "plan/refuseRecord";
        }
        
        
        pai.setStatus(5);  //0已经支付待批准 1批准 9待支付 5拒绝
        pai.setUserOperate(aduser.getId());
        pai.setOperatedAt(NewsDateUtils.getNowTimeStr());
        pai.setOpLog(params.get("remark"));
        
        planService.updatePlanRecord(pai);

        //更新账户记录 退回保证金+利息费或者管理费 
//        还有红包
        double totalmoney = 0;
        
        double glf = 0;
        
        if(pai.getPlanType()==1){
            
            totalmoney = pai.getMoneyInsure() + pai.getMoneyFee();
            
            glf = pai.getMoneyFee();
            
        }else if(pai.getPlanType()==2){
            
            totalmoney = pai.getMoneyInsure() + pai.getMoneyInsure()*pai.getPower()*pai.getRate()/100;
            
            glf = pai.getMoneyInsure()*pai.getPower()*pai.getRate()/100;
        }
        
        try{
        //判断该配资申请是否有使用红包 根据userId +  pai.getId()
        Map maps=new HashMap();
        maps.put("userId",pai.getUserId());
        maps.put("planRecordId",pai.getId());
        Long hongbaoId= userService.getHongbaoPlanHongbaoId(maps);
        if(hongbaoId!=0 || hongbaoId!=null){
//        	//如果存在使用红包 修改为可使用状态 1
        	userService.updateStatusByHongbaoId(hongbaoId);
////        	新建使用计划记录
//        	 //添加红包的使用记录
	   		 HongbaoPlan newhongbaoPlan=new HongbaoPlan();
	   		 newhongbaoPlan.setUserId(pai.getUserId());
	   		 newhongbaoPlan.setHongbaoId(hongbaoId);
	   		 newhongbaoPlan.setPlanRecordId(pai.getId());
	   		 newhongbaoPlan.setUpdateAt(NewsDateUtils.getNowTimeStr());
	   		 newhongbaoPlan.setUpdateBy(Integer.parseInt(pai.getUserId().toString()));
	   		 newhongbaoPlan.setHongbaoStatus(1);
	   		 newhongbaoPlan.setPlanRecordStatus(pai.getStatus());
	   		 userService.createNewPlan(newhongbaoPlan);
        }
        
        UserAccount ua = accountService.queryAccountByUser(pai.getUserId());
        ua.setMoneyTotal(ua.getMoneyTotal()+glf);
        ua.setMoneyUsable(ua.getMoneyUsable()+totalmoney);
        ua.setMoneyInsure(ua.getMoneyInsure()-pai.getMoneyInsure());
        ua.setUpdatedAt(NewsDateUtils.getNowTimeStr());
        
        accountService.updateAccount(ua);
        
        //新建资金记录
        UserAccount newua = accountService.queryAccountByUser(pai.getUserId());

        UserAccountLog ual = new UserAccountLog();
        
        //新建资金记录
        ual.setUserId(pai.getUserId());
        ual.setType(22);  //22退回保证金
        ual.setMoneyOperate(totalmoney);
        ual.setMoneyTotal(newua.getMoneyTotal());
        ual.setMoneyUsable(newua.getMoneyUsable());
        ual.setMoneyWithdraw(newua.getMoneyWithdraw());
        ual.setMoneyInsure(newua.getMoneyInsure());
        ual.setRemark("申请配资不通过，退回保证金及相关费用"+totalmoney+"元");
        ual.setCreatedAt(NewsDateUtils.getNowTimeStr());
        ual.setCreatedIp(IPUtils.getRemortIP(request));
        ual.setMoneyCollection(newua.getMoneyCollection());
        ual.setMoneyTenderFreeze(newua.getMoneyTenderFreeze());
        
        accountService.createUserAccountLog(ual); 
        
        
        map.addAttribute("msg", "操作成功");
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        return "redirect:record.html";
        
    }
    
}