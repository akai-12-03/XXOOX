package com.dept.web.general.interceptor;

import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dept.web.context.Global;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.AuthItemChild;
import com.dept.web.general.context.HttpContext;
import com.dept.web.general.util.Constants;
import com.dept.web.general.util.HttpInclude;
import com.dept.web.general.util.SessionHelper;
import com.dept.web.service.AuthService;
import com.dept.web.service.UserService;

public class AuthInterceptor implements HandlerInterceptor{
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private AuthService authService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
	throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object object, ModelAndView modelAndView) throws Exception {
		
		if (modelAndView != null) {
			String viewName = modelAndView.getViewName();
			if (viewName != null && !viewName.startsWith("redirect:")) {
				// 笔者扩展的httpInclude
				modelAndView.addObject("httpInclude", new HttpInclude(request,
						response));
			}
		}

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {
        // TODO Auto-generated method stub
        String path = request.getServletPath();
        
        if(path.matches(Constants.NO_INTERCEPTOR_PATH)){
            return true;
        }
        
        Long userId = this.getUserId(request, response);
        
        Long menuId = null;
        
        if(userId==null){
            
             return false;
        }
        
        List<AuthItemChild> aclist = authService.queryItemChildByUser(userId);
         
        for(AuthItemChild m : aclist){
            
            String menuUrl = m.getChild();
            
            if(StringUtils.isNotEmpty(menuUrl)){
                
                if(menuUrl.equals("/*")){
                    menuId = m.getId();
                    break;
                }
                
                if(path.contains(menuUrl)){
                    menuId = m.getId();
                    break;
                }
            }
        }
        //System.out.println(path+"===="+menuId);
        
        if(menuId!=null){
           return true;
        }else{
            response.sendRedirect(Global.getValue("admurl")+"/admin/welcome.html?msg=1");
            return false;
        }
	}
	
	
	private Long getUserId(HttpServletRequest aRequest, HttpServletResponse aResponse) {
		Long result = 0l;
		boolean flag = false;
		AdminUser al = null;
		String strLoginedUser = "";
		String strLoginedPassword = "";
		
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
					}
					
					al = userService.geyByLogInfo(strLoginedUser, strLoginedPassword);
					
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
}
