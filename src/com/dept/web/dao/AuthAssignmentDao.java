package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.AuthAssignment;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class AuthAssignmentDao extends IbatisBaseDaoImpl<AuthAssignment, Long>{

	public static final String NAME_SPACE_AUTHASSIGNMENT = "AuthAssignment";
	
	   
	/**
	 * 获取权限列表
	 * @Title: queryAuthAssignmentPage 
	 * @Description: TODO
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Page<AuthAssignment> 返回类型 
	 * @throws
	 */
    public Page<AuthAssignment> queryAuthAssignmentPage(PageRequest<Map<String, String>> map){
        
        return pageQuery(NAME_SPACE_AUTHASSIGNMENT, map, "ALL_PAGE");
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
        
        return getObj(NAME_SPACE_AUTHASSIGNMENT, uid, "USER");
    }
    
    /**
     * 删除后台用户权限
     * @Title: deleteByUser 
     * @Description: TODO
     * @param @param userid
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int deleteByUser(long userid){
        
        return delete(NAME_SPACE_AUTHASSIGNMENT, userid, "USER");
    }
    
}
