package com.dept.web.dao;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.DebtTransfer;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class DebtTransferDao extends IbatisBaseDaoImpl<DebtTransfer, Long>{

	public static final String NAME_SPACE_DEBTTRANSFER = "DebtTransfer";

}
