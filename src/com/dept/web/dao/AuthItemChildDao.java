package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.AuthItemChild;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class AuthItemChildDao extends IbatisBaseDaoImpl<AuthItemChild, Long>{

	public static final String NAME_SPACE_AUTHITEMCHILD = "AuthItemChild";
	
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
	    
	    return getObjList(NAME_SPACE_AUTHITEMCHILD, userid, "USER");
	}
	
    public void delItemChildByUser(long userid){
        
        delete(NAME_SPACE_AUTHITEMCHILD, userid, "BY_USER");
    }
	
}
