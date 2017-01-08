package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 
 * 
 * @ClassName:     AuthAssignment
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午9:10:29 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class AuthAssignment extends BaseEntity{
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 6807039140111844524L;

    private String itemName;
    
    private Long userId;
    
    private Long createdAt;
    
    private String username;
    
    private String description;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
