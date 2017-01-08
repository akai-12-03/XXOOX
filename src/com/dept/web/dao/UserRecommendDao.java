/**
 * 
 * 用户推荐DAO类
 * @author admin
 *
 */
package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;

import com.dept.web.dao.model.UserRecommend;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class UserRecommendDao extends IbatisBaseDaoImpl<UserRecommend, Long> {
	public static final String NAME_SPACE_USERRECOMMEND = "UserRecommend";
	/**
	 * 插入用户推荐 
	 * @param userR 用户推荐实体
	 * @return 新增用户推荐ID
	 */
	public Long InsertUserRecommend(UserRecommend userR) {
		return (Long) save(userR);
	}
	
	/**
	 * 根据用户ID获取推荐用户列表
	 * @param uid
	 * @return
	 */
	public List<UserRecommend> queryUserRecommendsByUserID(Long uid){
		return getObjList(NAME_SPACE_USERRECOMMEND, uid, "UID");
	}
	
	/**
	 * 查找手机号是否重复
	 * @param mobile
	 * @return
	 */
	public int findMobile(String mobile){
		return getSelectCount(NAME_SPACE_USERRECOMMEND,mobile,"MOBILE");
	}

	public Page<UserRecommend> queryUserRecommendByChecking(
			PageRequest<Map<String, String>> pageRequest) {
		return pageQuery(NAME_SPACE_USERRECOMMEND, pageRequest, "FOR_CHECKING_SEARCH");
	}
	
	/**
	 * 更新推荐用户状态
	 * @param id 推荐id
	 * @param status 状态
	 * @return
	 */
	public int updUserRecommendByID(Long id, int status) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(id));
		params.put("status", String.valueOf(status));
		return update(NAME_SPACE_USERRECOMMEND, params, "USERRECOMMEND_BY_ID");
	}
}