package com.dept.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.dao.VerifyBorrowLogDao;
import com.dept.web.dao.model.VerifyBorrowLog;

@Service
public class VerifyService {

    @Autowired
    private VerifyBorrowLogDao verifyBorrowLogDao;

    
    /**
     * 创建新审核记录
     * @Title: createVerifyBorrowLog 
     * @Description: TODO
     * @param @param vbl
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createVerifyBorrowLog(VerifyBorrowLog vbl){
        
        return (Long) verifyBorrowLogDao.save(vbl);
    }
    
    /**
     * 获取复审的标
     * @Title: queryVerifyByD 
     * @Description: TODO
     * @param @param bid
     * @param @return 设定文件 
     * @return VerifyBorrowLog 返回类型 
     * @throws
     */
    public VerifyBorrowLog queryVerifyByD(long bid){
        
        return verifyBorrowLogDao.queryVerifyByD(bid);
    }
    
    /**
     * 
     * @Title: updateVerifyBorrowLog 
     * @Description: TODO
     * @param @param vbl
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateVerifyBorrowLog(VerifyBorrowLog vbl){
        
        return verifyBorrowLogDao.update(vbl);
    }
}
