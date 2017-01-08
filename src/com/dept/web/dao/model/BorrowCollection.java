package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 待收计划
 * 
 * @ClassName:     BorrowCollection
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月16日 下午3:36:38 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BorrowCollection extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 1160787194850903853L;

    private Integer status;
    
    private Integer colOrder;
    
    private Long tenderId;
    
    private Long repayTime;
    
    private Long repayYestime;
    
    private Double repayAccount;
    
    private Double repayYesaccount;
    
    private Double interest;
    
    private Double capital;
    
    private Integer lateDays;
    
    private Double lateInterest;
    
    private Long addtime;
    
    private String addip;
    
    private String updateip;
    
    private Long updatetime;
    
    private String loanNo;
    
	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getColOrder() {
        return colOrder;
    }

    public void setColOrder(Integer colOrder) {
        this.colOrder = colOrder;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(Long repayTime) {
        this.repayTime = repayTime;
    }

    public Long getRepayYestime() {
        return repayYestime;
    }

    public void setRepayYestime(Long repayYestime) {
        this.repayYestime = repayYestime;
    }

    public Double getRepayAccount() {
        return repayAccount;
    }

    public void setRepayAccount(Double repayAccount) {
        this.repayAccount = repayAccount;
    }

    public Double getRepayYesaccount() {
        return repayYesaccount;
    }

    public void setRepayYesaccount(Double repayYesaccount) {
        this.repayYesaccount = repayYesaccount;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getCapital() {
        return capital;
    }

    public void setCapital(Double capital) {
        this.capital = capital;
    }

    public Integer getLateDays() {
        return lateDays;
    }

    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    public Double getLateInterest() {
        return lateInterest;
    }

    public void setLateInterest(Double lateInterest) {
        this.lateInterest = lateInterest;
    }

    public Long getAddtime() {
        return addtime;
    }

    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }

    public String getAddip() {
        return addip;
    }

    public void setAddip(String addip) {
        this.addip = addip;
    }

    public String getUpdateip() {
        return updateip;
    }

    public void setUpdateip(String updateip) {
        this.updateip = updateip;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }
    
    
}
