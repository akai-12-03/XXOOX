package com.dept.web.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.AdminUser;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class AdminUserDao extends IbatisBaseDaoImpl<AdminUser, Long>{

	public static final String NAME_SPACE_ADMINUSER = "AdminUser";
	
    /**
     * 
     * @Description:  TODO
     * @param:        @param userId
     * @param:        @return   
     * @return:       User   
     * @throws
     */
    public AdminUser queryByAdminUserId(long userId){
        
        return getObj(NAME_SPACE_ADMINUSER, userId, "USERID");
        
    }
    
    
    /**
     * 
     * @Description:  TODO
     * @param:        @param username
     * @param:        @param password
     * @param:        @return   
     * @return:       User   
     * @throws
     */
    public AdminUser getByUserNamePassword(String username, String password){
        
        Map<String, String> filters = new HashMap<String,String>();
        filters.put("username", username);
        filters.put("password_hash", password);
        
        return getObj(NAME_SPACE_ADMINUSER, filters, "LOGINFO");
        
    }
	
    
    /**
     * 查找后台用户列表
     * @Title: queryBackUser 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<AdminUser> 返回类型 
     * @throws
     */
    public Page<AdminUser> queryBackUser(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_ADMINUSER, pageRequest, "SEARCH");
        
    }
    
    
    /**
     * 根据用户名查找
     * @Title: queryByUsername 
     * @Description: TODO
     * @param @param username
     * @param @return 设定文件 
     * @return AdminUser 返回类型 
     * @throws
     */
    public AdminUser queryByUsername(String username){
        
        return getObj(NAME_SPACE_ADMINUSER, username, "USERNAME");
    }
}
