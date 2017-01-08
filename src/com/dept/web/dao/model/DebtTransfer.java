package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 债权转让
 * 
 * @ClassName:     DebtTransfer
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年6月25日 下午1:45:13 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class DebtTransfer extends BaseEntity{  
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 6304438970369565771L;

    /**
     * 投标记录
     */
    private Long tenderId;
    
    /**
     * 原投标记录用户
     */
    private Long tenderUserId;
    
    /**
     * 接收人用户
     */
    private Long receiveUserId;
    
    /**
     * 标记录ID
     */
    private Long borrowId;
    
    /**
     * 转让价格
     */
    private Double transferPrice;
    
    /**
     * 垫付收益利息
     */
    private Double advanceInterest;
    
    private Long createdAt;
    
    private Long createdBy;
    
    private Long updateAt;
    
    private Long updateBy;

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getTenderUserId() {
        return tenderUserId;
    }

    public void setTenderUserId(Long tenderUserId) {
        this.tenderUserId = tenderUserId;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Double getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(Double transferPrice) {
        this.transferPrice = transferPrice;
    }

    public Double getAdvanceInterest() {
        return advanceInterest;
    }

    public void setAdvanceInterest(Double advanceInterest) {
        this.advanceInterest = advanceInterest;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }
    
}
