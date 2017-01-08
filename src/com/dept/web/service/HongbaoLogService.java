package com.dept.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import com.dept.web.dao.HongbaoLogDao;
import com.dept.web.dao.model.HongbaoLog;


@Service
@Transactional(rollbackFor=Exception.class)
public class HongbaoLogService {

	   @Autowired
	   private HongbaoLogDao hongbaoLogDao;
	   
	   /**
	     * 添加用户红包记录
	     * @Title: getInterestForIndex 
	     * @Description: TODO
	     * @param @return 设定文件 
	     * @return double 返回类型 
	     * @throws
	     */
	   public Long createUserHongbaoLog(HongbaoLog hongbaoLog){
		   
		    return  hongbaoLogDao.insertHonbaoLog(hongbaoLog);
		}
	   
	   /**
	     * 根据用户ID查询对应的红包信息（此处指的是还未使用的红包）
	     * @Title: getInterestForIndex 
	     * @Description: TODO
	     * @param @return 设定文件 
	     * @return double 返回类型 
	     * @throws
	     */
	   public double querySumHongbaoLogById(Long userId){
		   
		    return  hongbaoLogDao.getSumHongbaoLogById(userId);
		}
	   
	   /**
	     * 更新用户
	     * @Title: getInterestForIndex 
	     * @Description: TODO
	     * @param @return 设定文件 
	     * @return double 返回类型 
	     * @throws
	     */
	   public int updateHongbaoLog(HongbaoLog hongbaoLog){
		   
		    return  hongbaoLogDao.update(hongbaoLog);
		}
	   
	   /**
	     * 更新用户
	     * @Title: getInterestForIndex 
	     * @Description: TODO
	     * @param @return 设定文件 
	     * @return double 返回类型 
	     * @throws
	     */

}
