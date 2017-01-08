package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.User;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
@SuppressWarnings("all")
public class UserDao extends IbatisBaseDaoImpl<User, Long>{

	public static final String NAME_SPACE_USER = "User";
	
	/**
	 * 查找前台用户
	 * @Title: queryUser 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<User> 返回类型 
	 * @throws
	 */
    public Page<User> queryUser(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_USER, pageRequest, "SEARCH");
        
    }
    
	/**
	 * 统计前台用户投资列表
	 * @Title: queryUser 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<User> 返回类型 
	 * @throws
	 */
    public Page<User> queryUserTendInfo(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_USER, pageRequest, "SEARCHUSERTENDINFO");
        
    }
    
    public int updateInviteIdByUserId(Long userId,Long inviteId){
    	Map map=new HashMap();
    	map.put("userId", userId);
    	if(inviteId==0){
    		map.put("inviteId", "");
    		System.out.println();
    	}
    	else if(inviteId==null){
    		map.put("inviteId", "");
    	}else{
    		map.put("inviteId", inviteId);
    	}
    	
    	Object o= getObj(NAME_SPACE_USER, map, "UPDATEINVITEBYID");
    	return o==null?0:Integer.parseInt(o.toString());
    }
    
    public int updateHongbaoStatus(String type,Long id){
    	Map map = new HashMap();
    	map.put("status", type);
    	map.put("id", id);
    	return update(NAME_SPACE_USER, map, "UPDATEHONGBAOSTATUS");
    }
    
    /**
     * 
     * @Description:  TODO
     * @param:        @param userId
     * @param:        @return   
     * @return:       User   
     * @throws
     */
    public User queryByUserId(long userId){
        
        return getObj(NAME_SPACE_USER, userId, "ID");
        
    }
    
    
    /**
     * 
     * @Description:  TODO
     * @param:        @param userId
     * @param:        @return   
     * @return:       User   
     * @throws
     */
    public User queryByMidId(String midId){
        
        return getObj(NAME_SPACE_USER, midId, "MIDID");
        
    }
    
    /**
     * 
     * @Description:  TODO
     * @param:        @param userId
     * @param:        @return   
     * @return:       User   
     * @throws
     */
    public String queryMidByUserId(long userId){
        Object obj= getObj(NAME_SPACE_USER, userId, "MIDBYID");
        String str="";
        if(obj!=null){
        	 str= obj.toString();
        }
        return str;
        
    }
    
    /**
     * 根据用户名查找用户
     * @Title: queryUserByUsername 
     * @Description: TODO
     * @param @param username
     * @param @return 设定文件 
     * @return User 返回类型 
     * @throws
     */
    public User queryUserByUsername(String username){
        
        return getObj(NAME_SPACE_USER, username, "USERNAME");
    }
    
    /**
     * 根据用户名模糊查找用户
     * @Title: queryUserByUsernameList 
     * @Description: TODO
     * @param @param username
     * @param @return 设定文件 
     * @return List<User> 返回类型 
     * @throws
     */
    public List<User> queryUserByUsernameList(String username){
        
        return getObjList(NAME_SPACE_USER, username, "USERNAME_LIST");
    }
    
       
    /**      
     * @desc 用途描述: 获取满足送红包新用户列表
     * @param type
     * @return 返回说明:
     * @exception 内部异常说明:
     * @throws 抛出异常说明:
     * @author gwx
     * @version 1.0      
     * @created 2016-3-8 上午10:27:36 
     * @mod 修改描述:
     * @modAuthor 修改人:
        
     */
    public List<User> queryNewUserList(String value){
    	Map map=new HashMap();
    	map.put("status", value);
    	return getObjList(NAME_SPACE_USER,map,"NEWUSER_LIST");
    }
    
    /**
     * ID查找用户
     * @Title: queryUserById 
     * @Description: TODO
     * @param @param userId
     * @param @return 设定文件 
     * @return User 返回类型 
     * @throws
     */
    public User queryUserById(Long userId){
        
        return getObj(NAME_SPACE_USER, userId, "ID");
    }
    
    public Page<User> queryInviteList(PageRequest<Map<String, String>> pageRequest) {
        return pageQuery(NAME_SPACE_USER, pageRequest, "SEARCHINVITE");
    }
    public List<User> queryInviteListForExl(Map<String, String> params) {
        return getObjList(NAME_SPACE_USER, params, "SEARCHINVITE");
    }

	public List<User> queryUser(Map<String, String> params) {
		// TODO Auto-generated method stub
		return getObjList(NAME_SPACE_USER, params, "SEARCHEXCELUSER");
	}
	
	public List<User> queryUserTendInfo(Map<String, String> params) {
		// TODO Auto-generated method stub
		return getObjList(NAME_SPACE_USER, params, "SEARCHEXCELUSERTENDINFO");
	}
    
}
