package com.dept.web.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Hongbao;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class HongbaoDao  extends IbatisBaseDaoImpl<Hongbao, Long> {
	
	private static final String NAME_SPACE_HONGBAO = "Hongbao";
	
	public Hongbao queryHongbaoById(Long hongbaoId){
		return getObj(NAME_SPACE_HONGBAO, hongbaoId, "BYHONGBAOID");
	}
	
	public Page<Hongbao> queryHongbaoByUserId(PageRequest<Map<String, String>> map){
		return pageQuery(NAME_SPACE_HONGBAO, map, "FIND_HONGBAOLOG_BY_USERID");
	}
	
	
	public Page<Hongbao> queryHongbaoList(PageRequest<Map<String, String>> map){
		return pageQuery(NAME_SPACE_HONGBAO, map, "FIND_HONGBAOLIST");
	}
	
	public List<Hongbao> queryHongbaoListForEx(Map<String, String> params){
		return getObjList(NAME_SPACE_HONGBAO, params, "FIND_HONGBAOLIST");
	}
	
	/**
	 * 更新红包状态
	 * @param 
	 * @return
	 */
	public int updateHongbao(Hongbao hongbao){
		return update(NAME_SPACE_HONGBAO, hongbao, "UPSTATUS");
	}
	/**
	 * 添加红包记录
	 * @param 
	 * @return
	 */
	public Long insertHonbao(Hongbao hongbao){
		
		return (Long) save(NAME_SPACE_HONGBAO, hongbao, "HONGBAO");
	}
	/**
	 * 根据ID查找未使用的红包总金额
	 * @param 
	 * @return
	 */
	public double getSumHongbaoById(Long userId){
		Object obj=getObjs(NAME_SPACE_HONGBAO, userId, "USERID");
		if(obj==null){
			return 0;
		}else{
		  return Double.parseDouble(obj.toString());
		}
	}
	public List<Hongbao>  queryListHongbaoByIDAndStatus(Map map){
  		
  		return getObjList(NAME_SPACE_HONGBAO, map, "STATUS");
  	}

	public Integer queryCountHongbaoLogByIDAndStatus(Map map){
  		
  		return getSelectCount(NAME_SPACE_HONGBAO, map, "IDSTATUS");
  	}

}
