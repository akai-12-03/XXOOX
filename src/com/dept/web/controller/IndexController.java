package com.dept.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.Setting;
import com.dept.web.general.util.DateUtils;
import com.dept.web.service.SystemService;

/**
 * 首页
 * @ClassName:     IndexController.java
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-9-23 下午2:11:50
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class IndexController extends WebController{

    
    @Autowired
    private SystemService systemService;
    
    /**
     * 首页
     * @Description:  
     * @param:        @param map
     * @param:        @param request
     * @param:        @param response
     * @param:        @return
     * @param:        @throws NumberFormatException
     * @param:        @throws Exception   
     * @return:       String   
     * @throws
     */
    @RequestMapping("index")
    public String index(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
       
        return "index";
    }
    
    /**
     * 
     * @Title: setting 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("admin/system/setting")
    public String setting(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
       
        List<Setting> settinglist = systemService.querySettinglist();
        
        map.addAttribute("settinglist",settinglist);
            
        return "system/systemInfo";
    }
    
    /**
     * 
     * @Title: settingup 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param sid
     * @param @throws Exception 设定文件 
     * @return void 返回类型 
     * @throws
     */
    @RequestMapping("admin/system/settingup")
    public void settingup(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long sid) throws Exception{
       
        Map<String,String> params = getParamMap(request);
        
        AdminUser user = getCurrUser(request, response);
        
        String name = params.get("name");
        String value = params.get("value");
        
        Setting setting = systemService.querySettingById(Long.valueOf(sid));
        
        setting.setName(name);
        setting.setValue(value);
        setting.setUpdatedBy(user.getId());
        setting.setUpdatedAt(DateUtils.getNowTimeStr());
        
        systemService.updateSetting(setting);
            
        out(response, 1);
    } 

    @RequestMapping("header")
    public String getHeader(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        return "header";
    }
    
    @RequestMapping("footer")
    public String getFooter(ModelMap map, HttpServletRequest request) throws Exception{
        return "footer";
    }
    
	/**
	 * 统一错误提示
	 * @return
	 */
	@RequestMapping("fail")
	public String failed(){
		return "error";
	}
	/**
	 * 找不到url
	 * @return
	 */
	@RequestMapping("404")
	public String erorr_404(){
		return "404";
	}
	/**
	 * 服务器内部错误
	 * @return
	 */
	@RequestMapping("500")
	public String erorr_500(){
		return "500";
	}
	/**
	 * 拒绝访问
	 * @return
	 */
	@RequestMapping("403")
	public String erorr_403(){
		return "403";
	}
	
    /**
	 * 二维码
	 * @return
	 */
	@RequestMapping("qrCode")
	public String qrCode(){
		return "qrCode";
	}
	
}