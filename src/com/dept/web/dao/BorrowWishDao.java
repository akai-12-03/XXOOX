package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Article;
import com.dept.web.dao.model.BorrowWish;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowWishDao extends IbatisBaseDaoImpl<BorrowWish, Long>{

    private static final String NAME_SPACE_BORROWWISH = "BorrowWish";
    
   
	public Long add(BorrowWish borrowWish){
		long b=0;
		try {
			b= (Long) save(borrowWish);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public Page<BorrowWish> queryBorrowWishPage(PageRequest<Map<String, String>> map){
		return pageQuery(NAME_SPACE_BORROWWISH, map, "SEARCHYUYUE");
	}
}
