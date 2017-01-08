package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 还款计划
 * 
 * @ClassName:     BorrowRepayment
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月16日 下午3:36:23 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BorrowRepayment extends BaseEntity{
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 2906882328617000546L;

    private Integer status;
    
    private Integer webstatus;
    
    private Integer repOrder;
    
    private Long borrowId;
    
    private Long repaymentTime;
    
    private Long repaymentYestime;
    
    private Double repaymentAccount;
    
    private Double repaymentYesaccount;
    
    private Integer lateDays;
    
    private Double lateInterest;
    
    private Double interest;
    
    private Double capital;
    
    private Double forfeit;
    
    private Double reminderFee;
    
    private Long addtime;
    
    private String addip;
    
    private String updateip;
    
    private Long updatetime;
    private String repaymentTimeStr;
    private String repaymentYestimeStr;
    private String username;
    private String borrowname;
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBorrowname() {
		return borrowname;
	}

	public void setBorrowname(String borrowname) {
		this.borrowname = borrowname;
	}

	public String getRepaymentTimeStr() {
		return repaymentTimeStr;
	}

	public void setRepaymentTimeStr(String repaymentTimeStr) {
		this.repaymentTimeStr = repaymentTimeStr;
	}

	public String getRepaymentYestimeStr() {
		return repaymentYestimeStr;
	}

	public void setRepaymentYestimeStr(String repaymentYestimeStr) {
		this.repaymentYestimeStr = repaymentYestimeStr;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWebstatus() {
        return webstatus;
    }

    public void setWebstatus(Integer webstatus) {
        this.webstatus = webstatus;
    }

    public Integer getRepOrder() {
        return repOrder;
    }

    public void setRepOrder(Integer repOrder) {
        this.repOrder = repOrder;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Long getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Long repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Long getRepaymentYestime() {
        return repaymentYestime;
    }

    public void setRepaymentYestime(Long repaymentYestime) {
        this.repaymentYestime = repaymentYestime;
    }

    public Double getRepaymentAccount() {
        return repaymentAccount;
    }

    public void setRepaymentAccount(Double repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }

    public Double getRepaymentYesaccount() {
        return repaymentYesaccount;
    }

    public void setRepaymentYesaccount(Double repaymentYesaccount) {
        this.repaymentYesaccount = repaymentYesaccount;
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

    public Double getForfeit() {
        return forfeit;
    }

    public void setForfeit(Double forfeit) {
        this.forfeit = forfeit;
    }

    public Double getReminderFee() {
        return reminderFee;
    }

    public void setReminderFee(Double reminderFee) {
        this.reminderFee = reminderFee;
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
