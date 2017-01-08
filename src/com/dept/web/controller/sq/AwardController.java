/**
 * Project Name:qgfCms
 * File Name:AwardController.java
 * Package Name:com.dept.web.controller.sq
 * Date:2016-3-4下午5:35:26
 * Copyright (c) 2016, gwx@tomcat360.com 
 * 雄猫软件版权所有
*/

package com.dept.web.controller.sq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.controller.WebController;

/**
 * ClassName:AwardController 
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2016-3-4 下午5:35:26 
 * @author   gwx
 * @version  
 * @since    JDK 1.6
 * @see
 */
@Controller
public class AwardController extends WebController{

	private static final Logger LOGGER = Logger.getLogger(AwardController.class);
	
	
	@RequestMapping("qddApi/backToAwardReturn")
	public void backToHongbaorReturn(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("---------------奖励返现页面返回----------------");
	}
	
	@RequestMapping("qddApi/backToAwardNotify")
	public void backToHongbaornotify(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOGGER.debug("---------------奖励返现后台返回----------------");
	}
}

