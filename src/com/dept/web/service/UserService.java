package com.dept.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.AdminUserDao;
import com.dept.web.dao.AuthAssignmentDao;
import com.dept.web.dao.BankCardDao;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.HongbaoLogDao;
import com.dept.web.dao.HongbaoPlanDao;
import com.dept.web.dao.UserDao;
import com.dept.web.dao.UserRechargeDao;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.BankCard;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.HongbaoPlan;
import com.dept.web.dao.model.User;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private AdminUserDao adminUserDao;
    
    @Autowired
    private BankCardDao bankCardDao;
    
    @Autowired
    private AuthAssignmentDao authAssignmentDao;

    @Autowired
    private HongbaoPlanDao hongbaoPlanDao;
    
    @Autowired
    private HongbaoLogDao hongbaoLogDao;
    
    @Autowired
    private HongbaoDao hongbaoDao;
    
    @Autowired
    private UserRechargeDao userRechargeDao;
    
    /**
  	* 获取用户是否首次充值
  	 * @param userId
  	 * @return
  	 */
  	public int getIsFirstRechargeByUserId(Long userId){
  		
  		return userRechargeDao.queryIsFirstRechargeByUserId(userId);
  	}
    

  	public int updateInviteIdByUserId(Long userId,Long inviteId){
  		return userDao.updateInviteIdByUserId(userId,inviteId);
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
        return userDao.queryByUserId(userId);
        
    }
  	
  	/**
     * 
     * @Description:  TODO
     * @param:        @param username
     * @param:        @return   
     * @return:       User   
     * @throws
     */
  	public User queryByUserName(String username){
        return userDao.queryUserByUsername(username);
        
    }
  	
  	/**
     * 
     * @Description:  TODO
     * @param:        @param userId
     * @param:        @return   
     * @return:       User   
     * @throws
     */
  	public User queryByMidId(String MId){
        return userDao.queryByMidId(MId);
        
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
        return userDao.queryMidByUserId(userId);
    }
    
    /**
     * 根据planrecordId +userId 查找对应的红包使用计划记录中的红包ID
     */
    public Long getHongbaoPlanHongbaoId(Map map){
        return hongbaoPlanDao.getHongbaoPlanHongbaoId(map);
    }
    
    /**
     * 根据用户ID修改红包的使用记录
     */
    
    public  int updateStatusByHongbaoId(Long hongbaoId){
        return hongbaoLogDao.updateStatusById(hongbaoId);
    }
    
    public int updateHongbaoStatus(String type,Long id){
    	return userDao.updateHongbaoStatus(type,id);
    }
    /**
     * 新增红包对应的使用计划 记录
     */
    public Long createNewPlan(HongbaoPlan hongPlan){
        return (Long)hongbaoPlanDao.save(hongPlan);
    }
  
     
    /**
     * 根据ID查找后台用户
     * @Title: queryAdminUserById 
     * @Description: TODO
     * @param @param userId
     * @param @return 设定文件 
     * @return AdminUser 返回类型 
     * @throws
     */
    public AdminUser queryAdminUserById(long userId){
        
        return adminUserDao.queryByAdminUserId(userId);
    }
    
    
    /**
     * 查找前台用户列表
     * @Title: queryUser 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<User> 返回类型 
     * @throws
     */
    public Page<User> queryUser(PageRequest<Map<String, String>> pageRequest){
        
        return userDao.queryUser(pageRequest);
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
    public Page<User> queryUserTendInfo(PageRequest<Map<String, String>> pageRequest){
        
        return userDao.queryUserTendInfo(pageRequest);
    }
    
    /**
     * 查找后台用户列表
     * @Title: queryBackUser 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<AdminUser> 返回类型 
     * @throws
     */
    public Page<AdminUser> queryBackUser(PageRequest<Map<String, String>> pageRequest){
        
        return adminUserDao.queryBackUser(pageRequest);
    }
    
    /**
     * 查询后台用户
     * @Title: geyByLogInfo 
     * @Description: TODO
     * @param @param username
     * @param @param password
     * @param @return 设定文件 
     * @return AdminUser 返回类型 
     * @throws
     */
    public AdminUser geyByLogInfo(String username, String password){
        
        return adminUserDao.getByUserNamePassword(username, password);
    }
    
    
    /**
     * 创建后台用户
     * @Title: createAdminUser 
     * @Description: TODO
     * @param @param user
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createAdminUser(AdminUser user){
        
        return (Long) adminUserDao.save(user);
    }
    
    
    /**
     * 更新后台用户
     * @Title: updateAdminUser 
     * @Description: TODO
     * @param @param user
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAdminUser(AdminUser user){
        
        return adminUserDao.update(user);
    }
    
    
    /**
     * 删除后台用户
     * @Title: delAdminUser 
     * @Description: TODO
     * @param @param user
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int delAdminUser(AdminUser user){
        
        int cont = adminUserDao.delete(user);
        
        cont = authAssignmentDao.deleteByUser(user.getId());
        
        return cont;
    }
    
    /**
     * 检查用户名是否重复
     * @Title: isRepeatUsername 
     * @Description: TODO
     * @param @param username
     * @param @return 设定文件 
     * @return boolean 返回类型 
     * @throws
     */
    public boolean isRepeatUsername(String username){
        
        AdminUser aduser = adminUserDao.queryByUsername(username);
        
        User user = userDao.queryUserByUsername(username);
        
        if(aduser==null && user==null){
            
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * 查询用户绑定的银行卡
     * @Title: queryUserBank 
     * @Description: TODO
     * @param @param userid
     * @param @return 设定文件 
     * @return BankCard 返回类型 
     * @throws
     */
    public BankCard queryUserBank(long userid){
        
        return bankCardDao.queryUserBank(userid);
        
    }
    
    /**
     * 
     * @Title: queryUserByUsername 
     * @Description: TODO
     * @param @param username
     * @param @return 设定文件 
     * @return User 返回类型 
     * @throws
     */
    public List<User> queryUserByUsername(String username){
        
        return userDao.queryUserByUsernameList(username);
    }
    
       
    /**      
     * @desc 用途描述: 获取满足送红包的新用户列表
     * @param type
     * @return 返回说明:
     * @exception 内部异常说明:
     * @throws 抛出异常说明:
     * @author gwx
     * @version 1.0      
     * @created 2016-3-8 上午10:26:14 
     * @mod 修改描述:
     * @modAuthor 修改人:
        
     */
    public List<User> queryNewUserList(String status){
    	return userDao.queryNewUserList(status);
    }
    
    
    /**
     * ID查询前台用户
     * @Title: queryWebUserById 
     * @Description: TODO
     * @param @param userId
     * @param @return 设定文件 
     * @return User 返回类型 
     * @throws
     */
    public User queryWebUserById(long userId){
        
        return userDao.queryUserById(userId);
    }
    
    
    /**
     * 更新前台用户
     * @Title: updateWebUser 
     * @Description: TODO
     * @param @param user
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateWebUser(User user){
        
        return userDao.update(user);
    }
    
    public Page<Hongbao>  getHongbaoBypage(PageRequest<Map<String, String>> map){
    	return hongbaoDao.queryHongbaoList(map);
    } 
    public List<Hongbao>  queryHongbaoList(Map<String, String> params){
    	return hongbaoDao.queryHongbaoListForEx(params);
    } 
    
    public Page<User>  getInviteList(PageRequest<Map<String, String>> map){
    	return userDao.queryInviteList(map);
    } 
    
    public List<User>  getInviteList(Map<String, String> params){
    	return userDao.queryInviteListForExl(params);
    }


	public List<User> queryUser(Map<String, String> params) {
		// TODO Auto-generated method stub
		return userDao.queryUser(params);
	} 
	
	public List<User> queryUserTendInfo(Map<String, String> params) {
		// TODO Auto-generated method stub
		return userDao.queryUserTendInfo(params);
	} 
}
