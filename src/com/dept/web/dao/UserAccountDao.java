package com.dept.web.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserAccount;
import com.dept.web.general.util.DateUtils;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class UserAccountDao extends IbatisBaseDaoImpl<UserAccount, Long>{

	public static final String NAME_SPACE_ACCOUNT = "UserAccount";
	/**
	 * 新增用户资金账户表
	 * @param account
	 * @return
	 */
	public Long insertAccount(UserAccount account) {
		return (Long) save(account);
	}
	
	
	/**
	 * 
	 * @Title: queryByUser 
	 * @Description: TODO
	 * @param @param userid
	 * @param @return 设定文件 
	 * @return UserAccount 返回类型 
	 * @throws
	 */
	public UserAccount queryByUser(long userid){
	    
	    return getObj(NAME_SPACE_ACCOUNT, userid, "USERID");
	}
	 /**
     * 查询用户可用余额总金额
     */
    public UserAccount getUserAccountByUserMoneyTotal(){
    	return getObj(NAME_SPACE_ACCOUNT, null, "USERACCOUNT_USERMONEY_TOTAL");
    }
	/**
	 * 线下充值查找用户
	 * @Title: queryByUserForOffline 
	 * @Description: TODO
	 * @param @param userid
	 * @param @return 设定文件 
	 * @return UserAccount 返回类型 
	 * @throws
	 */
    public UserAccount queryByUserForOffline(long userid){
        
        return getObj(NAME_SPACE_ACCOUNT, userid, "USERID_FOR_OFFLINE");
    }	
	
	
	
	/**
	 * 线下充值查找用户
	 * @Title: queryUserAccount 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<UserAccount> 返回类型 
	 * @throws
	 */
    public Page<UserAccount> queryUserAccount(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_ACCOUNT, pageRequest, "FOR_RECHARGE_SEARCH");
        
    }
    
    /**
     * 更新审核后用户的资金
     * @Title: updAccountByVerify 
     * @Description: TODO
     * @param @param params
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updAccountByVerify(double money, double interest, long userId){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("money", String.valueOf(money));
        params.put("interest", String.valueOf(interest));
        params.put("userId", String.valueOf(userId));
        params.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
                
        return update(NAME_SPACE_ACCOUNT, params, "ACCOUNT_BY_VERIFY");
    }
    
    
    /**
     * 更新用户投资奖励
     * @Title: updAccountByAward 
     * @Description: TODO
     * @param @param money
     * @param @param userId
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updAccountByAward(double money, long userId){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("money", String.valueOf(money));
        params.put("userId", String.valueOf(userId));
        params.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
                
        return update(NAME_SPACE_ACCOUNT, params, "ACCOUNT_BY_AWARD");
    }
    
    /**
     * 借款入账更新用户资金记录
     * @Title: updAccountByBorrower 
     * @Description: TODO
     * @param @param money
     * @param @param userId
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updAccountByBorrower(double money, long userId){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("money", String.valueOf(money));
        params.put("userId", String.valueOf(userId));
        params.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
                
        return update(NAME_SPACE_ACCOUNT, params, "ACCOUNT_BY_BORROWER");
    }
    
    
    /**
     * 更新用户还款冻结金额
     * @Title: updateAccountByRepayment 
     * @Description: TODO
     * @param @param money
     * @param @param userId
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAccountByRepaymentUnfreeze(double money, long userId){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("money", String.valueOf(money));
        params.put("userId", String.valueOf(userId));
        params.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
                
        return update(NAME_SPACE_ACCOUNT, params, "ACCOUNT_BY_REPAYMENT_UNFREEZE");
        
    }
    
    /**
     * 更新用户还款冻结金额
     * @Title: updateAccountByRepayment 
     * @Description: TODO
     * @param @param money
     * @param @param userId
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAccountByRepayment(double money, long userId){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("money", String.valueOf(money));
        params.put("userId", String.valueOf(userId));
        params.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
                
        return update(NAME_SPACE_ACCOUNT, params, "ACCOUNT_BY_REPAYMENT");
        
    }
    
    
    /**
     * 更新用户资金可用金额
     * @Title: updateAccountByUsable 
     * @Description: TODO
     * @param @param money
     * @param @param userId
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAccountByUsable(double money, long userId){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("money", String.valueOf(money));
        params.put("userId", String.valueOf(userId));
        params.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
                
        return update(NAME_SPACE_ACCOUNT, params, "ACCOUNT_BY_USABLE");
        
    }
    
    
    /**
     * 解冻用户投标冻结金额
     * @Title: updateAccountByUnFreeze 
     * @Description: TODO
     * @param @param money
     * @param @param userId
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAccountByUnFreeze(double money, long userId){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("money", String.valueOf(money));
        params.put("userId", String.valueOf(userId));
        params.put("nowd", String.valueOf(DateUtils.getNowTimeStr()));
                
        return update(NAME_SPACE_ACCOUNT, params, "ACCOUNT_TENDER_UNFREEZE");
        
    }
    
    public int updateAccountNotZero(double totalVar, double useVar,double nouseVar, long user_id) {
		//String sql="update account set total=total+"+totalVar+",use_money=use_money+"+useVar+",no_use_money=no_use_money+"+nouseVar+" where user_id="+user_id+" and use_money+"+useVar+">=0";
		UserAccount a = new UserAccount();
		a.setMoneyTotal(totalVar);
		a.setMoneyUsable(useVar);
		a.setMoneyTenderFreeze(nouseVar);
		a.setUserId(user_id);
		update(NAME_SPACE_ACCOUNT, a, "UPDATE_ACCOUNT_NOT_ZERO");
		return 1;
	}
}
