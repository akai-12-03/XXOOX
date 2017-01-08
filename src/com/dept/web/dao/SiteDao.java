package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Site;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class SiteDao extends IbatisBaseDaoImpl<Site, Long>{

    private static final String NAME_SPACE_SITE = "Site";
    
    /**
     * 查询SITE
     * @Title: queryAllSite 
     * @Description: TODO
     * @param @return 设定文件 
     * @return List<Site> 返回类型 
     * @throws
     */
    public List<Site> queryAllSite(){
        
        return getObjList(NAME_SPACE_SITE, null, "ALL");
    }
    public Site getSiteById(Long id){
        
        return getObj(NAME_SPACE_SITE, id, "SITE_BY_ID");
    }
}
