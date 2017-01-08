package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BankCard;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BankCardDao extends IbatisBaseDaoImpl<BankCard, Long>{

    public static final String NAME_SPACE_BANK_CARD = "BankCard";
    
    
    /**
     * 
     * @Title: queryUserBank 
     * @Description: TODO
     * @param @param userid
     * @param @return 设定文件 
     * @return BankCard 返回类型 
     * @throws
     */
    public BankCard queryUserBank(long userid) {
        
        return getObj(NAME_SPACE_BANK_CARD, userid, "USER");
        
    }
    
  public Page<BankCard> queryBankCard(PageRequest<Map<String, String>> pageRequest) {
    
    return pageQuery(NAME_SPACE_BANK_CARD, pageRequest, "QUERYBANKCARD");
    
  }
  
  public BankCard queryUserBankCard(Long id) {
	    
	    return getObj(NAME_SPACE_BANK_CARD, id, "USERBANKCARD");
	    
	}
  
  public void deleteByBankCardId(int id) {
		delete(NAME_SPACE_BANK_CARD, id, "BY_ID");
	}
  
  /**
	 * 查询该银行卡是否处于绑定状态
	 * @param cardNo
	 * @return
	 */
	public BankCard getBankCardByCardNo(String cardNo) {
		return getObj(NAME_SPACE_BANK_CARD, cardNo, "CARDNO");
	}

public List<BankCard> queryBankCard(Map<String, String> params) {
	// TODO Auto-generated method stub
	return getObjList(NAME_SPACE_BANK_CARD, params, "EXCELBANKCARD");
}
}
