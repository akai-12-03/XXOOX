/**
 * Project Name:qgfCms
 * File Name:p.java
 * Package Name:com.dept.web.service
 * Date:2016-3-16下午7:54:18
 * Copyright (c) 2016, gwx@tomcat360.com 
 * 雄猫软件版权所有
*/

package com.dept.web.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.context.Global;
import com.dept.web.dao.model.SystemInfo;
import com.sendinfo.common.lang.StringUtil;

/**
 * ClassName:p 
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2016-3-16 下午7:54:18 
 * @author   gwx
 * @version  
 * @since    JDK 1.6
 * @see
 */
@Service
public class PersonService  implements InitializingBean,DisposableBean{
  
	private static Global global;
	
    @Autowired
    private SystemService systemService;

	@Override
	public void destroy() throws Exception {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		    SystemInfo info = systemService.getSystemInfo();
			global.SYSTEMINFO=info;
	}
}


