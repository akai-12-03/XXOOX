package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 银行卡信息表
 * 
 * @ClassName:     BankCard
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午9:51:53 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BankCard extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -3968442869604164660L;

    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 直辖市
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 银行ID
     */
    private Long bankId;
    
    /**
     * 银行名称
     */
    private String bankName;
    
    /**
     * 银行详细
     */
    private String bankDetail;
    
    /**
     * 银行卡ID
     */
    private String cardId;
    
    /**
     * 卡号
     */
    private String cardNo;
    
    /**
     * 状态
     */
    private Integer status;
    
    private Long createdAt;
    
    private Long updatedAt;
    
    
    private String username;
    private String realname;
    private String createdAtName;//时间格式名称
    
    

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankDetail() {
        return bankDetail;
    }

    public void setBankDetail(String bankDetail) {
        this.bankDetail = bankDetail;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

	public String getCreatedAtName() {
		return createdAtName;
	}

	public void setCreatedAtName(String createdAtName) {
		this.createdAtName = createdAtName;
	}
    
    
    
}
