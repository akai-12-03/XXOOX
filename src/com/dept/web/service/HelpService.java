package com.dept.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.ArticleDao;
import com.dept.web.dao.SiteDao;
import com.dept.web.dao.model.Article;
import com.dept.web.dao.model.Site;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class HelpService {
    
    @Autowired
    private ArticleDao articleDao;
    
    @Autowired
    private SiteDao siteDao;
    
    public List<Article> queryArtForIndexDisplay(String nid, int count){
        
        return articleDao.queryArtForIndex(nid, count);
        
    }
    
    /**
     * 获取SITE列表
     * @Title: querySiteList 
     * @Description: TODO
     * @param @return 设定文件 
     * @return List<Site> 返回类型 
     * @throws
     */
    public List<Site> querySiteList(){
        
        return siteDao.queryAllSite();
        
    }
    public Site getSiteById(Long id){
        
        return siteDao.getSiteById(id);
        
    }
    /**
     * 新建文章
     * @Title: createArticle 
     * @Description: TODO
     * @param @param art
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createArticle(Article art){
        
        return (Long) articleDao.save(art);
    }
    
    /**
     * ID查询文章记录
     * @Title: queryArtById 
     * @Description: TODO
     * @param @param aid
     * @param @return 设定文件 
     * @return Article 返回类型 
     * @throws
     */
    public Article queryArtById(long aid){
        
        return articleDao.queryArtById(aid);
    }
    
    /**
     * 更新文章
     * @Title: updateArticle 
     * @Description: TODO
     * @param @param art
     * @param @return 设定文件 
     * @return int 返回类型 
     * @throws
     */
    public int updateArticle(Article art){
        
        return articleDao.update(art);
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
    public Page<Article> queryArtPage(PageRequest<Map<String, String>> pageRequest){
        
        return articleDao.queryArtPage(pageRequest);
    } 
    
    
}
