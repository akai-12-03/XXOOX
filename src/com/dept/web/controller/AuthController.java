package com.dept.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.AuthAssignment;
import com.dept.web.dao.model.AuthItem;
import com.dept.web.dao.model.AuthItemChild;
import com.dept.web.general.util.ArrayUtil;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.MD5;
import com.dept.web.general.util.SessionHelper;
import com.dept.web.general.util.SessionHelper.SessionType;
import com.dept.web.service.AuthService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 权限管理
 * 
 * @ClassName:     AuthController
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月5日 上午9:55:12 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class AuthController extends WebController{

    @Autowired
    private AuthService authService;

    
    /**
     * 登录
     * @Title: auth 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("auth")
    public String auth(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        String username = params.get("username");
        
        String password = params.get("password");
        
        AdminUser user = authService.getAdminUserByLoginfo(username, password);
        
        if(user==null){
            
            map.addAttribute("errmsg", "用户名与密码不符！");
            
            return "index";
            
        }else{
            
            this.putCurrUser(request, response, user);
            
            return "redirect:admin/welcome.html";
        }
    }
    
    /**
     * 欢迎页面
     * @Title: welcome 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/welcome")
    public String welcome(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    
        AdminUser user = getCurrUser(request, response);
        
        List<AuthItemChild> aclist = authService.queryItemChildByUser(user.getId());

        if(StringUtils.isNotEmpty(request.getParameter("msg")) && request.getParameter("msg").equals("1")){
            
            map.addAttribute("msg", "您没有权限查看");
        }
        
        map.addAttribute("user", user);
        map.addAttribute("menuList", aclist);

        SessionHelper.setSession(request, SessionType.AUTHLIST, aclist);
        
        Properties props=System.getProperties(); //获得系统属性集    
        String osName = props.getProperty("os.name"); //操作系统名称    
        String osArch = props.getProperty("os.arch"); //操作系统构架    
        String osVersion = props.getProperty("os.version"); //操作系统版本
        String javaVersion = props.getProperty("java.version");
        
        map.addAttribute("osName", osName);
        map.addAttribute("osArch", osArch);
        map.addAttribute("osVersion", osVersion);
        map.addAttribute("javaVersion", javaVersion);
        
        return "dashboard/welcome";
        
    }
    
    
    /**
     * 更新用户密码
     * @Title: changePassword 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/auth/chgPass")
    public String changePassword(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        String ctype = params.get("opt");
        
        AdminUser user = getCurrUser(request, response);
           
        if(StringUtils.isEmpty(ctype)){
            
            map.addAttribute("aduser", user);
            
            return "auth/chgpass";
            
        }else if(ctype.equals("upd")){
            
            String oldpass = params.get("password");
            String newpass = params.get("newpassword");
            String newpassr = params.get("newpasswordr");
            
            if(StringUtils.isEmpty(oldpass) || StringUtils.isEmpty(newpass) || StringUtils.isEmpty(newpassr)){
                
                map.addAttribute("msg", "密码不可为空");
                
                map.addAttribute("aduser", user);
                
                return "auth/chgpass";
                
            }else{
                
                MD5 md5 = new MD5();
                
                if(md5.getMD5ofStr(oldpass).equals(user.getPasswordHash())){
                    
                      user.setPasswordHash(md5.getMD5ofStr(newpass));
                      user.setUpdatedAt(DateUtils.getNowTimeStr());
                      
                      authService.updateAdminUser(user);
                      
                      map.addAttribute("msg", "更新成功");
                      
                      map.addAttribute("aduser", user);
                      
                      return "auth/chgpass";
                      
                }else{
                    
                    map.addAttribute("msg", "原密码不正确");
                    
                    map.addAttribute("aduser", user);
                    
                    return "auth/chgpass";
                }
                
            }
            
        }
    
        return null;
    }
    
    
    /**
     * 权限列表
     * @Title: authlist 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/power/powerlist")
    public String authlist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            
            Map<String,String> params = getParamMap(request);
            
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            
            if(StringUtil.isNotEmpty(params.get("page"))){
                
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            
            pageRequest.setFilters(params);

            Page<AuthAssignment> authpage = authService.queryAssignmentPage(pageRequest);

            map.addAttribute("authpage", authpage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(authpage.getTotalCount(), authpage.getPageSize()));
            
            map.addAttribute("page",pageRequest.getPageNumber());
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return "auth/auth_list";
    }
    
    
    /**
     * 权限选择
     * @Title: auth_select 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/power/power_select")
    public String auth_select(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long uid) throws Exception{
        
        Map<String,String> params = getParamMap(request);
        
        String ctype = params.get("opt");
        
        AuthAssignment au = authService.getAssignmentByUser(uid);
        
        map.addAttribute("au", au);
        
        //已经获得的权限列表
        List<AuthItemChild> selchildlist = authService.queryItemChildByUser(uid);
        
        String[] list1 = new String[selchildlist.size()];
        
        for (int i = 0; i < selchildlist.size(); i++) {
            
            list1[i]=selchildlist.get(i).getChild();
        }
        
        if(StringUtils.isEmpty(ctype)){
            
            AdminUser user = authService.getAdminUserById(uid);
            
            map.addAttribute("user", user);
                        
            map.addAttribute("selchildlist", selchildlist);
            
            //未获得的权限列表
            List<AuthItem> authItemlist = authService.queryAuthItem();
            
            String[] list2 = new String[authItemlist.size()];

            for (int i = 0; i < authItemlist.size(); i++) {
                
                list2[i]=authItemlist.get(i).getName();
               
            }
            
            String[] arraystr = ArrayUtil.minus(list1, list2);
            
            List<AuthItem> noselchildlist =  new ArrayList<AuthItem>();
            
            for (int i = 0; i < arraystr.length; i++) {
                
                AuthItem item = authService.getItemByName(arraystr[i]);
                
                noselchildlist.add(item);
            }
            
            map.addAttribute("noselchildlist", noselchildlist);
            
            return "auth/auth_option";
            
        }else{
            
            if(au==null){
                
                au = new AuthAssignment();
                
                au.setUserId(uid);
                au.setItemName(params.get("authcode"));
                au.setDescription(params.get("authDescription"));
                au.setCreatedAt(DateUtils.getNowTimeStr());
                
                authService.createAuthAssignment(au);
            }else{
                au.setUserId(uid);
                au.setItemName(params.get("authcode"));
                au.setDescription(params.get("authDescription"));
                authService.updateAuthAssignment(au);
            }
            
            String[] optionstr = params.get("sltstr").split(",");
            
            authService.delAuthItemChildByUser(uid);
            
            for (int i = 0; i < optionstr.length; i++) {
                
                AuthItem item = authService.getItemByName(optionstr[i]);
                
                AuthItemChild child = new AuthItemChild();
                
                child.setChild(optionstr[i]);
                
                child.setParent(params.get("authcode"));
                
                child.setDescription(item.getDescription());
                
                authService.createNewAuthChild(child);
                
            }
            
            return "redirect:powerlist.html";
        }
        

    }
}