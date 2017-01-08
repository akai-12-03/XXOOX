/**
 * Project Name:qgfCms
 * File Name:LoanController.java
 * Package Name:com.dept.web.controller.sq
 * Date:2016-3-4下午3:09:48
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
 * ClassName:LoanController 
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2016-3-4 下午3:09:48 
 * @author   gwx
 * @version  
 * @since    JDK 1.6
 * @see
 */
@Controller
public class HongBaoController extends WebController{
	protected String verifySignature = "";
	
	private static final Logger LOGGER = Logger.getLogger(HongBaoController.class);
	
	@RequestMapping("qddApi/backToHongbaoReturn")
	public void backToHongbaorReturn(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("---------------送红包页面返回----------------");
	}
	
	@RequestMapping("qddApi/backToHongbaoNotify")
	public void backToHongbaornotify(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOGGER.debug("---------------送红包后台返回----------------");
	}
}

