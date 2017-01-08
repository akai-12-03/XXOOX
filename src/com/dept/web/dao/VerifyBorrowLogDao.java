package com.dept.web.dao;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.VerifyBorrowLog;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class VerifyBorrowLogDao extends IbatisBaseDaoImpl<VerifyBorrowLog, Long>{

	public static final String NAME_SPACE_VERIFYBORROWLOG = "VerifyBorrowLog";
	
	/**
	 * 获取复审的记录
	 * @Title: queryVerifyByD 
	 * @Description: TODO
	 * @param @param bid
	 * @param @return 设定文件 
	 * @return VerifyBorrowLog 返回类型 
	 * @throws
	 */
	public VerifyBorrowLog queryVerifyByD(long bid){
	    
	    return getObj(NAME_SPACE_VERIFYBORROWLOG, bid, "VERIFY_D");
	}
	
}
