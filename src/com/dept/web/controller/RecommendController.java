package com.dept.web.controller;
import com.dept.web.service.UserRecommendService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RecommendController extends WebController {

	@Autowired
	private UserRecommendService urService;
	
	@RequestMapping({"admin/recommend/checking"})
	public String checked(ModelMap map,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map params = getParamMap(request);

	    String type = (String)params.get("opt");

	    String id = (String)params.get("id");
	    
	    String status = (String)params.get("rstatus");

	    map.addAttribute("msg", params.get("msg"));

	    if (StringUtils.isEmpty(type))
	    {
	      try
	      {
	        PageRequest pageRequest = new PageRequest();
	        populate(pageRequest, request);
	        pageRequest.setPageSize(10);

	        if (StringUtil.isNotEmpty((String)params.get("page")))
	        {
	          pageRequest.setPageNumber(Integer.valueOf((String)params.get("page")).intValue());
	        }
	        
	        if(StringUtil.isNotEmpty(status)){
	        	params.put("status", "1");
	        	map.addAttribute("rstatus", "1");
	        }else{
	        	params.put("status", "0");
	        	map.addAttribute("rstatus", "");
	        }

	        pageRequest.setFilters(params);

	        Page accountpage = this.urService.queryUserRecommendByCheckingPage(pageRequest);

	        map.addAttribute("accountpage", accountpage);

	        map.addAttribute("totalPage", Integer.valueOf(PageUtils.computeLastPageNumber(accountpage.getTotalCount(), accountpage.getPageSize())));

	        map.addAttribute("page", Integer.valueOf(pageRequest.getPageNumber()));

	        map.addAttribute("username", params.get("username"));

	        map.addAttribute("mobile", params.get("mobile"));

	        return "recommend/check_list";
	      }
	      catch (Exception e)
	      {
	        e.printStackTrace();
	      }
	    }
		return null;
	}
	
	@RequestMapping({"admin/recommend/checked"})
	public String Addchecked(ModelMap map,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try {
			int uid =Integer.parseInt(request.getParameter("uid"));
			int result=urService.updateStatusByID((long) uid,1);
			return "redirect:checking.html?rstatus=1";
		} catch (Exception e) {
			// TODO: handle exception
		return null;
		}
	}
}
