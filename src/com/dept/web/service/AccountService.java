package com.dept.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.AdminUserDao;
import com.dept.web.dao.AuthItemChildDao;
import com.dept.web.dao.AuthRuleDao;
import com.dept.web.dao.BankCardDao;
import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.UserAccountLogDao;
import com.dept.web.dao.UserRechargeDao;
import com.dept.web.dao.UserWithdrawDao;
import com.dept.web.dao.model.BankCard;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserRecharge;
import com.dept.web.dao.model.UserWithdraw;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class AccountService {
	
	@Autowired
	private AuthItemChildDao authItemChildDao;
	
    @Autowired
    private AdminUserDao adminUserDao;
    
    @Autowired
    private AuthRuleDao authRuleDao;
    
    @Autowired
    private UserAccountDao userAccountDao;
    
    @Autowired
    private UserAccountLogDao userAccountLogDao;
    
    @Autowired
    private UserRechargeDao userRechargeDao;
    
    @Autowired
    private UserWithdrawDao userWithdrawDao;
    
    @Autowired
    private BankCardDao bankCardDao;
    
 
    /**
     * 更新用户资产
     * @Title: updateAccount 
     * @Description: TODO
     * @param @param userAccount
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAccount(UserAccount userAccount){
        
        return userAccountDao.update(userAccount);
    }
    
    /**
     * 创建新的资金记录
     * @Title: createUserAccountLog 
     * @Description: TODO
     * @param @param ual
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createUserAccountLog(UserAccountLog ual){
        
        return (Long) userAccountLogDao.save(ual);
    }
    
    /**
     * 
     * @Title: queryAccountByUser 
     * @Description: TODO
     * @param @param userid
     * @param @return 设定文件 
     * @return UserAccount 返回类型 
     * @throws
     */
    public UserAccount queryAccountByUser(long userid){
        
        return userAccountDao.queryByUserForOffline(userid);
    }
    
    /**
     * 查询充值记录
     * @Title: queryUserRecharge 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public List<UserRecharge> queryListUserRecharge(Map<String, String> map){
      
      return userRechargeDao.queryListUserRecharge(map);
    }
    
    /**
     * 查询提现记录
     * @Title: queryUserRecharge 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public List<UserWithdraw> queryListUserWithdraw(Map<String, String> map){
      
      return userWithdrawDao.queryListUserWithdraw(map);
    }
    
    /**
     * 查询充值记录
     * @Title: queryUserRecharge 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public Page<UserRecharge> queryUserRecharge(PageRequest<Map<String, String>> pageRequest){
      
      return userRechargeDao.queryUserRecharge(pageRequest);
    }
    
    /**
     * 查询取现记录
     * @Title: queryUserRecharge 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public Page<UserWithdraw> queryUserWithdraw(PageRequest<Map<String, String>> pageRequest){
      
      return userWithdrawDao.queryWithdraw(pageRequest);
    }
    
    /**
     * 银行卡管理
     * @Title: queryUserRecharge 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public Page<BankCard> queryBankCard(PageRequest<Map<String, String>> pageRequest){
      
      return bankCardDao.queryBankCard(pageRequest);
    }

    /**
     * 银行卡管理
     * @Title: queryUserRecharge 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public BankCard queryUserBankCard(Long id){
      
      return bankCardDao.queryUserBankCard(id);
    }
    
    /**
     * 
     * @Title: queryWithdrawById 
     * @Description: TODO
     * @param @param id
     * @param @return 设定文件 
     * @return UserWithdraw 返回类型 
     * @throws
     */
    public UserWithdraw queryWithdrawById(long id){
        
        return userWithdrawDao.queryWithdrawById(id);
    }
    
    /**
     * 
     * @Title: queryWithdrawById 
     * @Description: TODO
     * @param @param id
     * @param @return 设定文件 
     * @return UserWithdraw 返回类型 
     * @throws
     */
    public UserWithdraw queryWithdrawByOrderId(String id){
        
        return userWithdrawDao.queryWithdrawByOrderId(id);
    }
    
    /**
     * 审核提现
     * @Title: verifyWithDraw 
     * @Description: TODO
     * @param @param uw
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int verifyWithDraw(UserWithdraw uw){
        
        return userWithdrawDao.update(uw);
    }
    
    /**
     * 审核提现
     * @Title: verifyWithDraw 
     * @Description: TODO
     * @param @param uw
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int qddVerifyWithDraw(UserWithdraw uw){
        return userWithdrawDao.qddUupdate(uw);
    }
    
    /**
     * 获取需要线下充值用户的列表
     * @Title: queryUserAccountPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserAccount> 返回类型 
     * @throws
     */
    public Page<UserAccount> queryUserAccountPage(PageRequest<Map<String, String>> pageRequest){
        
        return userAccountDao.queryUserAccount(pageRequest);
    }
    
    /**
     * 创建充值记录
     * @Title: createRecharge 
     * @Description: TODO
     * @param @param ur
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createRecharge(UserRecharge ur){
        
        return (Long) userRechargeDao.save(ur);
        
    }
    
    
    /**
     * 查询线下充值记录列表
     * @Title: queryUserAccountPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserAccount> 返回类型 
     * @throws
     */
    public Page<UserRecharge> queryOfflineRechargePage(PageRequest<Map<String, String>> pageRequest){
        
        return userRechargeDao.queryOfflineRechargePage(pageRequest);
    }
    /**
     * 查询用户资金记录列表
     * @Title: queryUserAccountPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserAccount> 返回类型 
     * @throws
     */
    public Page<UserAccountLog> queryUserAccountLogPage(PageRequest<Map<String, String>> pageRequest){
        
        return userAccountLogDao.queryUserAccountLogPage(pageRequest);
    }
    /**
     * ID查找线下充值记录
     * @Title: queryOfflineRechargeById 
     * @Description: TODO
     * @param @param rid
     * @param @return 设定文件 
     * @return UserRecharge 返回类型 
     * @throws
     */
    public UserRecharge queryOfflineRechargeById(long rid){
        
        return userRechargeDao.queryOfflineRechargeById(rid);
    }
    
    /**
     * 更新线下充值记录
     * @Title: updateByStatus 
     * @Description: TODO
     * @param @param ur
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateByStatus(UserRecharge ur){
        
        return userRechargeDao.updateByStatus(ur);
    }
    
    /**
     * 查询线下扣款记录列表
     * @Title: queryOfflineDeductPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<UserRecharge> 返回类型 
     * @throws
     */
    public Page<UserRecharge> queryOfflineDeductPage(PageRequest<Map<String, String>> pageRequest){
        
        return userRechargeDao.queryOfflineDeductPage(pageRequest);
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
        
        return userRechargeDao.queryOfflineDeductById(rid);
    }
    
    public int updateBankCard(BankCard b){
        
        return bankCardDao.update(b);
    }

	public List<BankCard> queryBankCard(Map<String, String> params) {
		// TODO Auto-generated method stub
		return bankCardDao.queryBankCard(params);
	}
    
}
