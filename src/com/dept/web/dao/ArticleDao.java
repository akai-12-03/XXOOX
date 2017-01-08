package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Article;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class ArticleDao extends IbatisBaseDaoImpl<Article, Long>{

    private static final String NAME_SPACE_ARTICLE = "Article";
    
    /**
     * 显示首页公告
     * @Title: queryArtForIndex 
     * @Description: TODO
     * @param @param nid
     * @param @param count
     * @param @return 设定文件 
     * @return List<Article> 返回类型 
     * @throws
     */
    public List<Article> queryArtForIndex(String nid, int count){
        
        Map<String,Object> params = new HashMap<String,Object>();
        
        params.put("nid", nid);
        params.put("start", 1);
        params.put("end", count);
        
        return getObjList(NAME_SPACE_ARTICLE, params, "INDEX");
    }
    
    
    /**
     * 
     * @Title: queryArtById 
     * @Description: TODO
     * @param @param id
     * @param @return 设定文件 
     * @return Article 返回类型 
     * @throws
     */
    public Article queryArtById(long id){
        
        return getObj(NAME_SPACE_ARTICLE, id, "ID");
    }
    
    
    /**
     * 查询文章
     * @Title: queryArtPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<Article> 返回类型 
     * @throws
     */
    public Page<Article> queryArtPage(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_ARTICLE, pageRequest, "SEARCH");
        
    }

}
