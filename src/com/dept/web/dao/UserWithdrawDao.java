package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserWithdraw;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class UserWithdrawDao extends IbatisBaseDaoImpl<UserWithdraw, Long>{

	public static final String NAME_SPACE_USERWITHDRAW = "UserWithdraw";
	
	 public List<UserWithdraw> queryListUserWithdraw(Map<String, String> map) {
	        
	        return getObjList(NAME_SPACE_USERWITHDRAW, map, "STATUS_FOR_SEARCH");
	        
	    }

	/**
	 * 
	 * @Title: queryWithdraw 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<PlanRecord> 返回类型 
	 * @throws
	 */
	public Page<UserWithdraw> queryWithdraw(PageRequest<Map<String, String>> pageRequest) {
	    
	    return pageQuery(NAME_SPACE_USERWITHDRAW, pageRequest, "STATUS_FOR_SEARCH");
	    
	}

	/**
	 * ID查找提现记录
	 * @Title: queryWithdrawById 
	 * @Description: TODO
	 * @param @param id
	 * @param @return 设定文件 
	 * @return UserWithdraw 返回类型 
	 * @throws
	 */
	public UserWithdraw queryWithdrawById(long id){
	    
	    return getObj(NAME_SPACE_USERWITHDRAW, id, "ID");
	}
	
	/**
	 * ORDERID查找提现记录
	 * @Title: queryWithdrawById 
	 * @Description: TODO
	 * @param @param id
	 * @param @return 设定文件 
	 * @return UserWithdraw 返回类型 
	 * @throws
	 */
	public UserWithdraw queryWithdrawByOrderId(String id){
	    
	    return getObj(NAME_SPACE_USERWITHDRAW, id, "ORDER_ID");
	}
	
	public UserWithdraw getUserWithDrawByTotal(){
		return getObj(NAME_SPACE_USERWITHDRAW, null, "USER_WITHdRAW_TOTAL");
	}
	
	   
	/**      
	 * @desc 用途描述: 双钱接口返回更新
	 * @param uw
	 * @return 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author gwx
	 * @version 1.0      
	 * @created 2016-2-29 上午10:29:20 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	    
	 */
	public int qddUupdate(UserWithdraw uw){
		return update(NAME_SPACE_USERWITHDRAW, uw, "QDDUPDATE");
	}
}
