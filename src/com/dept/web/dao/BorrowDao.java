package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Borrow;
import com.dept.web.general.util.DateUtils;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowDao extends IbatisBaseDaoImpl<Borrow, Long>{

	public static final String NAME_SPACE_BORROW = "Borrow";

	/**
	 * 查询标
	 * @Title: queryBorrowPage 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<Borrow> 返回类型 
	 * @throws
	 */
    public Page<Borrow> queryBorrowPage(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_BORROW, pageRequest, "SEARCH");
        
    }
    
    /**
     * 查询发标总个数和总金额
     * 
     */
    public Borrow getBorrowCountAndMoney(){
    	return getObj(NAME_SPACE_BORROW, null, "BORROW_COUNT_MONEY");
    }
    
    /**
     * 根据状态查询标
     * @param status
     * @return
     */
    public List<Borrow> queryBorrowListByStatus(int status){
    	return getObjList(NAME_SPACE_BORROW, status, "BORROWSTATUS");
    }
    
    /**
     * 借款人情况
     * @param pageRequest
     * @return
     */
    public Page<Borrow> queryBorrow(PageRequest<Map<String, String>> pageRequest) {
	    
	    return pageQuery(NAME_SPACE_BORROW, pageRequest, "USERBORROWTENDER");
	    
	}
    
    
    /**
     * ID查找标
     * @Title: queryBorrowById 
     * @Description: TODO
     * @param @param id
     * @param @return 设定文件 
     * @return Borrow 返回类型 
     * @throws
     */
    public Borrow queryBorrowById(long id){
        
        return getObj(NAME_SPACE_BORROW, id, "ID");
        
    }
    
    
    /**
     * 获取复审通过的理财产品
     * @Title: queryReVerifyBorrowList 
     * @Description: TODO
     * @param @param params
     * @param @return 设定文件 
     * @return List<Borrow> 返回类型 
     * @throws
     */
    public List<Borrow> queryReVerifyBorrowList(Map<String, Integer> params){
        
        return getObjList(NAME_SPACE_BORROW, params, "REVERIFY");
    }
    
    /**
     * 更新产品状态
     * @Title: updateBorrowForStatus 
     * @Description: TODO
     * @param @param params
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateBorrowForStatus(Map<String, String> params){
        
        return update(NAME_SPACE_BORROW, params, "FOR_STATUS");
    }
    
    
    /**
     * 查询过期的产品
     * @Title: queryOverBorrow 
     * @Description: TODO
     * @param @return 设定文件 
     * @return List<Borrow> 返回类型 
     * @throws
     */
    public List<Borrow> queryOverBorrow(){
        
        long nowd = DateUtils.getNowTimeStr();
        
        return getObjList(NAME_SPACE_BORROW, nowd, "OVER");
    }
    public int updateBorrowStatus(int status, long id) {
		//String sql="update borrow set status="+status+" where id= "+id;
		Borrow b = new Borrow();
		b.setStatus(status);
		b.setId(id);
		 int count = update(NAME_SPACE_BORROW, b, "UPDATE_BORROW_STATUS");
		return count;
	}

	public List<Borrow> queryBorrow(Map<String, String> params) {
		// TODO Auto-generated method stub
		return getObjList(NAME_SPACE_BORROW, params, "SEARCHBORROWEXCEL");
	}

}
