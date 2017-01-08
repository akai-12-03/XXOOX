package com.dept.web.dao;


import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowLoan;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class LoanDao  extends IbatisBaseDaoImpl<BorrowLoan, Long> {
	
	private static final String NAME_SPACE_LOAN = "Loan";
	
	/**
	 * 添加借款申请
	 * @param 
	 * @return
	 */
	public Long insertLoan(BorrowLoan loan){
		
		return (Long) save(NAME_SPACE_LOAN, loan, "LOAN");
	}
	
	public Page<BorrowLoan> getLoanList(PageRequest<Map<String, String>> pageRequest){
		return pageQuery(NAME_SPACE_LOAN,pageRequest,"LOANLIST");
	}
	
	public BorrowLoan getLoanById(long id){
		return getObj(NAME_SPACE_LOAN,id,"LOANBYID");
	}
	public int updLoanById(BorrowLoan loan){
		return update(NAME_SPACE_LOAN,loan,"UPDATELOAN");
	}
}
