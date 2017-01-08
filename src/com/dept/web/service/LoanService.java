/**
 * Project Name:qgfCms
 * File Name:LoanService.java
 * Package Name:com.dept.web.service
 * Date:2016-3-14上午11:27:56
 * Copyright (c) 2016, gwx@tomcat360.com 
 * 雄猫软件版权所有
*/

package com.dept.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.dao.LoanDao;
import com.dept.web.dao.model.BorrowLoan;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

/**
 * ClassName:LoanService 
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2016-3-14 上午11:27:56 
 * @author   gwx
 * @version  
 * @since    JDK 1.6
 * @see
 */
@Service
public class LoanService {
	@Autowired
	private LoanDao loanDao;
	
	public Page<BorrowLoan> getLoanList(PageRequest<Map<String, String>> pageRequest){
		return loanDao.getLoanList(pageRequest);
	}
	
	public BorrowLoan getLoanById(long id){
		return loanDao.getLoanById(id);
	}
	
	public int updateLoan(BorrowLoan loan){
		return loanDao.updLoanById(loan);
	}
}

