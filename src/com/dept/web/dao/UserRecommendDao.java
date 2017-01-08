/**
 * 
 * �û��Ƽ�DAO��
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
	 * �����û��Ƽ� 
	 * @param userR �û��Ƽ�ʵ��
	 * @return �����û��Ƽ�ID
	 */
	public Long InsertUserRecommend(UserRecommend userR) {
		return (Long) save(userR);
	}
	
	/**
	 * �����û�ID��ȡ�Ƽ��û��б�
	 * @param uid
	 * @return
	 */
	public List<UserRecommend> queryUserRecommendsByUserID(Long uid){
		return getObjList(NAME_SPACE_USERRECOMMEND, uid, "UID");
	}
	
	/**
	 * �����ֻ����Ƿ��ظ�
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
	 * �����Ƽ��û�״̬
	 * @param id �Ƽ�id
	 * @param status ״̬
	 * @return
	 */
	public int updUserRecommendByID(Long id, int status) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(id));
		params.put("status", String.valueOf(status));
		return update(NAME_SPACE_USERRECOMMEND, params, "USERRECOMMEND_BY_ID");
	}
}