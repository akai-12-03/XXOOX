package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.AuthRule;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class AuthRuleDao extends IbatisBaseDaoImpl<AuthRule, Long>{

	public static final String NAME_SPACE_AUTHRULE = "AuthRule";
	
	/**
	 * 查询所有的规则
	 * @Title: queryAllRule 
	 * @Description: TODO
	 * @param @return 设定文件 
	 * @return List<AuthRule> 返回类型 
	 * @throws
	 */
    public List<AuthRule> queryAllRule() {
        
        return getObjList(NAME_SPACE_AUTHRULE, null, "ALL");
        
    }
}
