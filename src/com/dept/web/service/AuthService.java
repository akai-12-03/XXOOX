package com.dept.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.dao.AdminUserDao;
import com.dept.web.dao.AuthAssignmentDao;
import com.dept.web.dao.AuthItemChildDao;
import com.dept.web.dao.AuthItemDao;
import com.dept.web.dao.AuthRuleDao;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.AuthAssignment;
import com.dept.web.dao.model.AuthItem;
import com.dept.web.dao.model.AuthItemChild;
import com.dept.web.dao.model.AuthRule;
import com.dept.web.general.util.MD5;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
public class AuthService {

    @Autowired
    private AuthItemDao authItemDao;
    
	@Autowired
	private AuthItemChildDao authItemChildDao;
	
    @Autowired
    private AdminUserDao adminUserDao;
    
    @Autowired
    private AuthRuleDao authRuleDao;
    
    @Autowired
    private AuthAssignmentDao authAssignmentDao;
	
	/**
	 * 查询用户的访问权限
	 * @Title: queryItemChildByUser 
	 * @Description: TODO
	 * @param @param userid
	 * @param @return 设定文件 
	 * @return List<AuthItemChild> 返回类型 
	 * @throws
	 */
	public List<AuthItemChild> queryItemChildByUser(long userid){
	    
		return authItemChildDao.queryItemChildByUser(userid);
	}
	
	/**
	 * 后台用户登录信息
	 * @Title: getAdminUserByLoginfo 
	 * @Description: TODO
	 * @param @param username
	 * @param @param password
	 * @param @return 设定文件 
	 * @return AdminUser 返回类型 
	 * @throws
	 */
	public AdminUser getAdminUserByLoginfo(String username, String password){
	    
	    MD5 md5 = new MD5();
	    
	    return adminUserDao.getByUserNamePassword(username, md5.getMD5ofStr(password));
	}
	
	
	/**
	 * userId获取AdminUser
	 * @Title: getAdminUserById 
	 * @Description: TODO
	 * @param @param userid
	 * @param @return 设定文件 
	 * @return AdminUser 返回类型 
	 * @throws
	 */
    public AdminUser getAdminUserById(long userid){
        
        return adminUserDao.queryByAdminUserId(userid);
    }
    
    /**
     * 获取所有RULE
     * @Title: queryAllRule 
     * @Description: TODO
     * @param @return 设定文件 
     * @return List<AuthRule> 返回类型 
     * @throws
     */
    public List<AuthRule> queryAllRule(){
        
        return authRuleDao.queryAllRule();
    }
	
    /**
     * 更新用户
     * @Title: updateAdminUser 
     * @Description: TODO
     * @param @param adu
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAdminUser(AdminUser adu){
        
        return adminUserDao.update(adu);
    }
    
    
    /**
     * 获取权限列表
     * @Title: queryAssignmentPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<AuthAssignment> 返回类型 
     * @throws
     */
    public Page<AuthAssignment> queryAssignmentPage(PageRequest<Map<String, String>> pageRequest){
        
        return authAssignmentDao.queryAuthAssignmentPage(pageRequest);
    }
    
    
    /**
     * 获取所有权限
     * @Title: queryAuthItem 
     * @Description: TODO
     * @param @return 设定文件 
     * @return List<AuthItem> 返回类型 
     * @throws
     */
    public List<AuthItem> queryAuthItem(){
        
        return authItemDao.queryAuthItem();
    }
    
    /**
     * 
     * @Title: createNewAuthChild 
     * @Description: TODO
     * @param @param authItemChild
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createNewAuthChild(AuthItemChild authItemChild){
        
        return (Long) authItemChildDao.save(authItemChild);
    }
	
    
    /**
     * 
     * @Title: getItemByName 
     * @Description: TODO
     * @param @param name
     * @param @return 设定文件 
     * @return AuthItem 返回类型 
     * @throws
     */
    public AuthItem getItemByName(String name){
        
        return authItemDao.getItemByName(name);
    }
    
    
    /**
     * 
     * @Title: getAssignmentByUser 
     * @Description: TODO
     * @param @param uid
     * @param @return 设定文件 
     * @return AuthAssignment 返回类型 
     * @throws
     */
    public AuthAssignment getAssignmentByUser(long uid){
        
        return authAssignmentDao.getAssignmentByUser(uid);
    }
    
    
    /**
     * 
     * @Title: delAuthItemChildByUser 
     * @Description: TODO
     * @param @param uid
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public void delAuthItemChildByUser(long uid){
        
        authItemChildDao.delItemChildByUser(uid);
    }
    
    
    
    /**
     * 更新分配
     * @Title: updateAuthAssignment 
     * @Description: TODO
     * @param @param authass
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAuthAssignment(AuthAssignment authass){
        
        return authAssignmentDao.update(authass);
    }
    
    /**
     * 
     * @Title: createAuthAssignment 
     * @Description: TODO
     * @param @param authass
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createAuthAssignment(AuthAssignment authass){
        
        return (Long) authAssignmentDao.save(authass);
    }
}
