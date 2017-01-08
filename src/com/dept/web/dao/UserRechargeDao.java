package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserRecharge;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class UserRechargeDao extends IbatisBaseDaoImpl<UserRecharge, Long>{

	public static final String NAME_SPACE_USERRECHARGE = "UserRecharge";
	
    public List<UserRecharge> queryListUserRecharge(Map<String, String> map) {
        
        return getObjList(NAME_SPACE_USERRECHARGE, map, "FOR_SEARCH");
        
    }
    
    public  int queryIsFirstRechargeByUserId(Long userId){
		
		return getSelectCount(NAME_SPACE_USERRECHARGE, userId, "ISFIRST");
	}

	/**
	 * 查找充值记录
	 * @Title: queryUserRecharge 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<UserRecharge> 返回类型 
	 * @throws
	 */
    public Page<UserRecharge> queryUserRecharge(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_USERRECHARGE, pageRequest, "FOR_SEARCH");
        
    }
    public UserRecharge getUserRechargeByTotal(){
    	return getObj(NAME_SPACE_USERRECHARGE, null, "USER_RECHARGE_TOTAL");
    }
    public UserRecharge getUserRechargeByTotal2(){
    	return getObj(NAME_SPACE_USERRECHARGE, null, "USER_RECHARGE_TOTALL");
    }
    
    /**
     * 获取线下充值记录
     * @Title: queryOfflineRechargePage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public Page<UserRecharge> queryOfflineRechargePage(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_USERRECHARGE, pageRequest, "FOR_SEARCH_OFFLINE");
        
    }    
    
    /**
     * ID查找线下充值记录
     * @Title: queryById 
     * @Description: TODO
     * @param @param rid
     * @param @return 设定文件 
     * @return UserRecharge 返回类型 
     * @throws
     */
    public UserRecharge queryOfflineRechargeById(long rid){
        
        return getObj(NAME_SPACE_USERRECHARGE, rid, "ID_FOR_OFFLINE");
    }
    
    
    /**
     * 更新充值记录状态
     * @Title: updateByStatus 
     * @Description: TODO
     * @param @param ur
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateByStatus(UserRecharge ur){
        
        return update(NAME_SPACE_USERRECHARGE, ur, "STATUS");
    }

    
    /**
     * 获取线下扣款记录
     * @Title: queryOfflineDeductPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public Page<UserRecharge> queryOfflineDeductPage(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_USERRECHARGE, pageRequest, "FOR_SEARCH_OFFLINE_DEDUCT");
        
    }
    
    
    /**
     * ID查找线下扣款记录
     * @Title: queryOfflineDeductById 
     * @Description: TODO
     * @param @param rid
     * @param @return 设定文件 
     * @return UserRecharge 返回类型 
     * @throws
     */
    public UserRecharge queryOfflineDeductById(long rid){
        
        return getObj(NAME_SPACE_USERRECHARGE, rid, "ID_FOR_OFFLINE_DEDUCT");
    }
}
