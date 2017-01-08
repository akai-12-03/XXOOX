package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserAccountLog;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class UserAccountLogDao extends IbatisBaseDaoImpl<UserAccountLog, Long>{
	public static final String NAME_SPACE_ACCOUNTLOG = "UserAccountLog";
	
	/**
     * 获取线下充值记录
     * @Title: queryOfflineRechargePage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public Page<UserAccountLog> queryUserAccountLogPage(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_ACCOUNTLOG, pageRequest, "FOR_SEARCH_ACCOUNTLOG");
        
    } 

    
    /**
	 * 添加
	 * @param accountLog
	 * @return
	 */
	public Object insertUserAccountLog(UserAccountLog accountLog) {
		return save(NAME_SPACE_ACCOUNTLOG, accountLog, "USERACCOUNTLOG");
	}
}
