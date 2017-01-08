package com.dept.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.support.SessionStatus;

import com.dept.web.dao.model.AdminUser;
import com.dept.web.general.context.HttpContext;
import com.dept.web.general.util.SessionHelper;
import com.dept.web.general.util.SessionHelper.SessionType;
import com.dept.web.general.util.tools.iphelper.IPSeeker;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.AuthService;
import com.google.gson.GsonBuilder;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.controller.BaseController;

/**
 * 基础类
 * @ClassName:     WebController.java
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-8-10 上午11:09:38
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class WebController extends BaseController {

    protected String base;
    
    @Autowired
    private AuthService authService;
    
    /**
     * 默认分页条数为每页10条
     */
    protected static final Integer DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 把request参数封装到VO
     * 
     * @author jianjiang
     * @param bean
     * @param request
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void populate(Object bean, HttpServletRequest request) throws IllegalAccessException,
            InvocationTargetException {
        BeanUtils.populate(bean, request.getParameterMap());
    }
    
    public String getBase(HttpServletRequest request) {
        int port = request.getServerPort();
        if (port == 80) {
            return request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/";
        } else {
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath() + "/";
        }
    }
    
    /**
     * 获取参数map (把HttpServletRequest里的ParameterMap统一转换成HashMap)
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getParamMap(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> reqParamMap = new HashMap<String, Object>();
        reqParamMap.putAll(request.getParameterMap());
        for (String key : reqParamMap.keySet()) {
            paramMap.put(key, request.getParameter(key));
        }
        return paramMap;
    }
    

    
    final protected void out(HttpServletResponse response, Object target) {
        GsonBuilder gbuild = new GsonBuilder();
        try {
            response.setContentType("text/javascript;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(gbuild.create().toJson(target));
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Response writing failure.", e);
        }
    }
    
    /**
     * 获得登录用户信息
     * 
     * <pre>
     * 返回用户信息
     * </pre>
     * 
     * @param request
     * @return
     */
    final protected AdminUser getCurrUser(HttpServletRequest request, HttpServletResponse response) {
        Long userId = this.getUserId(request, response);
        if (userId == null || userId.intValue() <= 0) {
            return null;
        }
        
        return this.authService.getAdminUserById(userId);
    }
    
    final protected void putCurrUser(HttpServletRequest request, HttpServletResponse response, AdminUser user) {
        if (user == null)
            SessionHelper.setUserId(request, response, null);
        else
            SessionHelper.setUserId(request, response, user.getId());
    }
    
    @SuppressWarnings("unused")
    private Long getUserId(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        Long result = 0l;
        boolean flag = false;
        AdminUser al = null;
        String strLoginedUser = "";
        String strLoginedPassword = "";
        
        String jessionId = "";
        
        try {
            result = SessionHelper.getUserId(aRequest, aResponse);
            
            if (result == null || result > 0l) {
                Cookie ary[] = aRequest.getCookies();
                if (ary != null && ary.length > 0) {
                    for (Cookie item : ary) {
                        if (HttpContext.SessionKey.LOGINED_USER.toString().equalsIgnoreCase(item.getName()))
                            strLoginedUser = URLDecoder.decode(item.getValue(),"utf-8").replace("#", "@");
                            
                        if (HttpContext.SessionKey.LOGINED_PASSWORD.toString().equalsIgnoreCase(item.getName()))
                            strLoginedPassword = item.getValue();
                        if (HttpContext.SessionKey.JSESSIONID.toString().equalsIgnoreCase(item.getName()))
                            jessionId = item.getValue();                        
                    }
                    
                    al = authService.getAdminUserByLoginfo(strLoginedUser, strLoginedPassword);
                    
                    if (al != null)
                        flag = true;
                }
            }
            
            if (flag) {
                return al.getId();
            } else {
                return result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    
    /**
     * 获取http请求的实际IP
     * @return
     */
    protected String getRequestIp(HttpServletRequest request){
        String realip=IPUtils.getRemortIP(request);
        return realip;
    }
    
    /**
     * 获取IP所在地
     * @return
     */
    protected String getAreaByIp(HttpServletRequest request){
        String realip=getRequestIp(request);
        return getAreaByIp(realip);
    }
    protected String getAreaByIp(String ip){
        IPSeeker ipSeeker = IPSeeker.getInstance();
        String nowarea=ipSeeker.getArea(ip);
        return nowarea;
    }
    
    /**
	 * 
	 * @method: export() 
	 * @TODO:  
	 * @param infile
	 * @param downloadFile
	 * @throws Exception void
	 */
   protected void export(HttpServletResponse response,String infile,String downloadFile) throws Exception{
		File inFile = new File(infile);
		InputStream ins = new BufferedInputStream(new FileInputStream(infile));
		byte[] buffer = new byte[ins.available()];
		ins.read(buffer);
		ins.close();
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename="+ new String(downloadFile.getBytes()));
		response.addHeader("Content-Length", "" + inFile.length());
		OutputStream ous = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		ous.write(buffer);
		ous.flush();
		ous.close();
	}
    
    
    
    /**
     * 
     * @Description:  记住密码
     * @param:        @param request
     * @param:        @param response
     * @param:        @param status
     * @param:        @param username
     * @param:        @param password
     * @param:        @param userid
     * @param:        @param type on记住
     * @param:        @throws UnsupportedEncodingException   
     * @return:       void   
     * @throws
     */
    protected void rememberMe(HttpServletRequest request, HttpServletResponse response, SessionStatus status, 
            
            String username, String password, long userid, String type) throws UnsupportedEncodingException{
        
        if(StringUtil.isEmpty(type)|| type.equals("off")){
            this.putCurrUser(request, response, null);
            status.setComplete();
            request.getSession().removeAttribute("currUser");
            Cookie ckUsername, ckSessionid;
            ckUsername = new Cookie("logined_user", ""); // user是代表用户的bean
            
            ckUsername.setPath("/");
            response.addCookie(ckUsername);
            
            ckSessionid = new Cookie("logined_password", "");
            
            ckSessionid.setPath("/");
            response.addCookie(ckSessionid);
            
            SessionHelper.setSession(request, SessionType.USER_ID, userid);

        }else if (type.equals("on")) {
            
            /** cookies相关 start */
            Cookie ckUsername, ckSessionid;
            String value=URLEncoder.encode(username,"UTF-8");
            ckUsername = new Cookie("logined_user", value.replace("@", "#")); // user是代表用户的bean

            ckUsername.setPath("/");
            ckUsername.setMaxAge(60 * 60 * 24 * 14); // 设置Cookie有效期为14天
            response.addCookie(ckUsername);
            
            ckSessionid = new Cookie("logined_password", password.toUpperCase());
            ckSessionid.setPath("/");
            ckSessionid.setMaxAge(60 * 60 * 24 * 14);
            response.addCookie(ckSessionid);
            
            /** cookies相关 end */
        } else{
            this.putCurrUser(request, response, null);
            status.setComplete();
            request.getSession().removeAttribute("currUser");
            Cookie ckUsername, ckSessionid;
            ckUsername = new Cookie("logined_user", ""); // user是代表用户的bean
            
            ckUsername.setPath("/");
            response.addCookie(ckUsername);
            
            ckSessionid = new Cookie("logined_password", "");
            
            ckSessionid.setPath("/");
            response.addCookie(ckSessionid);
        }
        
    }
  
}