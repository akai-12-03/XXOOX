package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 投标
 * 
 * @ClassName:     BorrowTender
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月16日 下午3:43:52 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BorrowTender extends BaseEntity{
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 5956259325721463375L;

    private Long userId;
    
    private Integer status;
    
    private Long borrowId;
    
    private Double money;
    
    private Double account;
    
    private Double repaymentAccount;
    
    private Double interest;
    
    private Double repaymentYesaccount;
    
    private Double waitAccount;
    
    private Double waitInterest;
    
    private Double repaymentYesinterest;
    
    private Long addtime;
    
    private String addip;
    
    private String updateip;
    
    private String updatetime;
    
    private String username;

    private int trustStatus;
    private String trustTrxId;
    private String trustFreezeOrdId;
    private String trustIsFreeze;
    private String trustFreezeTrxId;
    
    private  Long hongbao_id ;
    private Double hongbao_money;
    
    
    private String name;
    
    
    private String borrowname;
    private String tzsj;
    private String tzsjs;
    private String tzqx;
    private Double tzmoney;
    private Double apr;
    private int award;
    private Double hbmoney;
    private int isday;
    private Double jlmoney;
    private Long user_id;
    
    private Double yesinterest;
    
    private String loanNo;
    private long apiLogId;
    private Integer transfer;
    
    public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public long getApiLogId() {
		return apiLogId;
	}

	public void setApiLogId(long apiLogId) {
		this.apiLogId = apiLogId;
	}

	public String getTzsjs() {
		return tzsjs;
	}

	public void setTzsjs(String tzsjs) {
		this.tzsjs = tzsjs;
	}

	public int getTrustStatus() {
		return trustStatus;
	}

	public void setTrustStatus(int trustStatus) {
		this.trustStatus = trustStatus;
	}

	public String getTrustTrxId() {
		return trustTrxId;
	}

	public void setTrustTrxId(String trustTrxId) {
		this.trustTrxId = trustTrxId;
	}

	public String getTrustFreezeOrdId() {
		return trustFreezeOrdId;
	}

	public void setTrustFreezeOrdId(String trustFreezeOrdId) {
		this.trustFreezeOrdId = trustFreezeOrdId;
	}

	public String getTrustIsFreeze() {
		return trustIsFreeze;
	}

	public void setTrustIsFreeze(String trustIsFreeze) {
		this.trustIsFreeze = trustIsFreeze;
	}

	public String getTrustFreezeTrxId() {
		return trustFreezeTrxId;
	}

	public void setTrustFreezeTrxId(String trustFreezeTrxId) {
		this.trustFreezeTrxId = trustFreezeTrxId;
	}

	

	
	public Long getHongbao_id() {
		return hongbao_id;
	}

	public void setHongbao_id(Long hongbao_id) {
		this.hongbao_id = hongbao_id;
	}

	public Double getHongbao_money() {
		return hongbao_money;
	}

	public void setHongbao_money(Double hongbao_money) {
		this.hongbao_money = hongbao_money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBorrowname() {
		return borrowname;
	}

	public void setBorrowname(String borrowname) {
		this.borrowname = borrowname;
	}

	public String getTzsj() {
		return tzsj;
	}

	public void setTzsj(String tzsj) {
		this.tzsj = tzsj;
	}

	public String getTzqx() {
		return tzqx;
	}

	public void setTzqx(String tzqx) {
		this.tzqx = tzqx;
	}

	public Double getTzmoney() {
		return tzmoney;
	}

	public void setTzmoney(Double tzmoney) {
		this.tzmoney = tzmoney;
	}

	public Double getApr() {
		return apr;
	}

	public void setApr(Double apr) {
		this.apr = apr;
	}

	public int getAward() {
		return award;
	}

	public void setAward(int award) {
		this.award = award;
	}

	public Double getHbmoney() {
		return hbmoney;
	}

	public void setHbmoney(Double hbmoney) {
		this.hbmoney = hbmoney;
	}

	public int getIsday() {
		return isday;
	}

	public void setIsday(int isday) {
		this.isday = isday;
	}

	public Double getJlmoney() {
		return jlmoney;
	}

	public void setJlmoney(Double jlmoney) {
		this.jlmoney = jlmoney;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Double getYesinterest() {
		return yesinterest;
	}

	public void setYesinterest(Double yesinterest) {
		this.yesinterest = yesinterest;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public Double getRepaymentAccount() {
        return repaymentAccount;
    }

    public void setRepaymentAccount(Double repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getRepaymentYesaccount() {
        return repaymentYesaccount;
    }

    public void setRepaymentYesaccount(Double repaymentYesaccount) {
        this.repaymentYesaccount = repaymentYesaccount;
    }

    public Double getWaitAccount() {
        return waitAccount;
    }

    public void setWaitAccount(Double waitAccount) {
        this.waitAccount = waitAccount;
    }

    public Double getWaitInterest() {
        return waitInterest;
    }

    public void setWaitInterest(Double waitInterest) {
        this.waitInterest = waitInterest;
    }

    public Double getRepaymentYesinterest() {
        return repaymentYesinterest;
    }

    public void setRepaymentYesinterest(Double repaymentYesinterest) {
        this.repaymentYesinterest = repaymentYesinterest;
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

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

	public Integer getTransfer() {
		return transfer;
	}

	public void setTransfer(Integer transfer) {
		this.transfer = transfer;
	}
    
    
}
