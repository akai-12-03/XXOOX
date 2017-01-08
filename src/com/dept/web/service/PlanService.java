package com.dept.web.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.PlanAppendInsureDao;
import com.dept.web.dao.PlanRecordDao;
import com.dept.web.dao.PlanSettingDao;
import com.dept.web.dao.model.PlanAppendInsure;
import com.dept.web.dao.model.PlanRecord;
import com.dept.web.dao.model.PlanSetting;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class PlanService {
    
    @Autowired
    private PlanRecordDao planRecordDao;
    
    @Autowired
    private PlanSettingDao planSettingDao;
    
    @Autowired
    private PlanAppendInsureDao planAppendInsureDao;
    
    /**
     * 查找配资计划
     * @Title: queryRecordByStatus 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<PlanRecord> 返回类型 
     * @throws
     */
    public Page<PlanRecord> queryRecordByStatus(PageRequest<Map<String, String>> pageRequest){
        
        return planRecordDao.queryPlanByStatus(pageRequest);
    }
    
    
    /**
     * 查找所有配资设定
     * @Title: queryPlanSet 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<PlanSetting> 返回类型 
     * @throws
     */
    public Page<PlanSetting> queryPlanSet(PageRequest<Map<String, String>> pageRequest){

        return planSettingDao.queryAllPlanSetting(pageRequest);
    }
    
    
    /**
     * 创建配资设定
     * @Title: createPlanSetting 
     * @Description: TODO
     * @param @param pse
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createPlanSetting(PlanSetting pse){
        
        return (Long) planSettingDao.save(pse);
    }
    
    
    /**
     * 更新配资设定
     * @Title: updatePlanSetting 
     * @Description: TODO
     * @param @param pse
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updatePlanSetting(PlanSetting pse){
        
        return planSettingDao.update(pse);
    }
    
    
    /**
     * ID查找配资设定
     * @Title: queryPlanSettingById 
     * @Description: TODO
     * @param @param pid
     * @param @return 设定文件 
     * @return PlanSetting 返回类型 
     * @throws
     */
    public PlanSetting queryPlanSettingById(long pid){
        
        PlanSetting pse = new PlanSetting();
        
        pse.setId(pid);
        
        return planSettingDao.getObj(pse);
    }
    
    /**
     * 
     * @Title: queryPlanRecordById 
     * @Description: TODO
     * @param @param id
     * @param @return 设定文件 
     * @return PlanRecord 返回类型 
     * @throws
     */
    public PlanRecord queryPlanRecordById(long id){
        
        PlanRecord pr = new PlanRecord();
        pr.setId(id);
        
        return planRecordDao.getObj(pr);
    }
    
    
    /**
     * 
     * @Title: updatePlanRecord 
     * @Description: TODO
     * @param @param pr
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updatePlanRecord(PlanRecord pr){
        
        return planRecordDao.updatePlanRecord(pr);
    }
    
    /**
     * 查询追加保证金记录列表
     * @Title: getPlanAppendInsurePage 
     * @Description: TODO
     * @param @param map
     * @param @return 设定文件 
     * @return Page<PlanAppendInsure> 返回类型 
     * @throws
     */
    public Page<PlanAppendInsure> getPlanAppendInsurePage(PageRequest<Map<String, String>> map){
        
        return planAppendInsureDao.getPlanAppendInsurePage(map);
    }
    
    /**
     * ID查找追加保证金记录
     * @Title: queryAppendById 
     * @Description: TODO
     * @param @param id
     * @param @return 设定文件 
     * @return PlanAppendInsure 返回类型 
     * @throws
     */
    public PlanAppendInsure queryAppendById(long id){
        
        return planAppendInsureDao.getAppendById(id);
    }
    
    
    /**
     * 更新保证金记录
     * @Title: updateAppend 
     * @Description: TODO
     * @param @param pai
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateAppend(PlanAppendInsure pai){
        
        return planAppendInsureDao.update(pai);
    }
}
