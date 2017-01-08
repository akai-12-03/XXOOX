package com.dept.web.general.util;

import org.springframework.context.ApplicationContext;

public class Constants {
	
	public static final String SESSION_USER_ID = "userId";
		
	public static String RETURN_TYPE_FILE = "FILE";
	
    public static final String SESSION_SECURITY_CODE = "sessionSecCode";
    
    public static final String SESSION_USER = "sessionUser";
    
    public static final String SESSION_USER_RIGHTS = "sessionUserRights";
    
    public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
    
    public static final String NO_INTERCEPTOR_PATH = ".*/((index)|(auth)|(welcome)|(code)|(qddApi)).*";//不对匹配该值的访问路径拦截（正则）
    
    public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	
    public static final String PLAN_STATUS_RATIFY = "ratify";

}
