/**
 * 
 */
package com.dept.web.service;

import java.util.List;

import com.dept.web.dao.UserRecommendDao;
import com.dept.web.dao.model.UserRecommend;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor={Exception.class})
public class UserRecommendService{
	@Autowired
	private UserRecommendDao userRecommendDao;
	
	/**
	 * 
	 * @Title: createUserRecommend 
	 * @Description: TODO
	 * @param @param newUserRecommend
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean createUserRecommend(UserRecommend newUserRecommend){
		newUserRecommend.setCreatetime(Long.valueOf(System.currentTimeMillis() / 1000L));
		newUserRecommend.setUpdatetime(Long.valueOf(System.currentTimeMillis() / 1000L));
		newUserRecommend.setStatus(0);
		
		Long useridLong = this.userRecommendDao.InsertUserRecommend(newUserRecommend);
		return useridLong>0;
	}

	/**
	 * 
	 * @Title: queryUserRecommendByCheckingPage 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<UserRecommend> 返回类型 
	 * @throws
	 */
	public Page<UserRecommend> queryUserRecommendByCheckingPage(
			PageRequest<Map<String, String>> pageRequest) {
		return this.userRecommendDao.queryUserRecommendByChecking(pageRequest);
	}
	
	/**
	 * 
	 * @Title: queryUserRecommendsByUserID 
	 * @Description: TODO
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return List<UserRecommend> 返回类型 
	 * @throws
	 */
	public List<UserRecommend> queryUserRecommendsByUserID(Long uid){
		return this.userRecommendDao.queryUserRecommendsByUserID(uid);
	}
	
	/**
	 * 
	 * @Title: findUserRecommendMobile 
	 * @Description: TODO
	 * @param @param mobile
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int findUserRecommendMobile(String mobile){
		return this.userRecommendDao.findMobile(mobile);
	}
	
	/**
	 * 
	 * @Title: updateStatusByID 
	 * @Description: TODO
	 * @param @param id
	 * @param @param status
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int updateStatusByID(Long id,int status) {
		return this.userRecommendDao.updUserRecommendByID(id, status);
	}
}