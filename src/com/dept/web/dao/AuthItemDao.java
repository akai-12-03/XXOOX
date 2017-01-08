package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.AuthItem;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class AuthItemDao extends IbatisBaseDaoImpl<AuthItem, Long>{

	public static final String NAME_SPACE_AUTHITEM = "AuthItem";
	
	/**
	 * 获取全部权限
	 * @Title: queryAuthItem 
	 * @Description: TODO
	 * @param @return 设定文件 
	 * @return List<AuthItem> 返回类型 
	 * @throws
	 */
	public List<AuthItem> queryAuthItem(){
	    
	    return getObjList(NAME_SPACE_AUTHITEM, null, "ALL");
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
	    
	    return getObj(NAME_SPACE_AUTHITEM, name, "NAME");
	}

	
}
