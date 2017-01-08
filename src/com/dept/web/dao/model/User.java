package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class User extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -5386946940071781247L;

    private String username;
    
    /**
     * 登录密码
     */
    private String passwordHash;
    
    /**
     * 交易密码
     */
    private String passwordPayHash;
    
    /**
     * 重置密码token
     */
    private String passwordResetToken;
    
    /**
     * 记住我
     */
    private String authKey;
    
    /**
     * 用户角色
     */
    private Integer role;
    
    /**
     * 用户状态
     */
    private Integer status;
    
    private String mobile;
    
    private String email;
    
    /**
     * 真实姓名
     */
    private String realname;
    
    /**
     * 身份证
     */
    private String idCard;
    
    /**
     * 性别
     */
    private Integer sex;
    
    /**
     * 生日
     */
    private String birthday;
    
    /**
     * 邮箱认证状态
     */
    private Integer emailBindingStatus;
    
    /**
     * 手机认证状态
     */
    private Integer mobileBindingStatus;
    
    /**
     * 实名认证状态
     */
    private Integer realVerifyStatus;
    
    /**
     * 银行卡绑定状态
     */
    private Integer cardBindingStatus;
    
    /**
     * 银行卡ID基数
     */
    private Long cardIdBase;
    /**
     * 注册时间
     */
    private Long createdAt;
    /**
     * 修改时间
     */
    private Long updatedAt;
    
    /**
     * 注册ip
     */
    private String createdIp;

    /**
     * user.litpic
     * 
     */
    private String litpic;
    private String bigPic;
    private String onPic;
    private String smallPic;
    /**
     * 推荐人ID
     */
    private String inviteUserId;
    
    private String inviteUsername;
    
    /**
     * 总资产
     */
    private Double moneyTotal;
    
    /**
     * 可用余额
     */
    private Double moneyUsable;
    
    private Double money;
    private String createdAtStr;
    
    //新增 
    /**
     * 是否已投资||1:表示已投资，0：表示未投资
     */
    private int isTouzi;
    /**
     * 是否已充值||1：表示已充值，0：表示为充值
     */
    private int isRecharge;
    
    private Double account;
    private Double capital;
    private Double interest;
    private String realVerify;
    private Integer autoType;
    
    private String tendertime;

	public String getTendertime() {
		return tendertime;
	}

	public void setTendertime(String tendertime) {
		this.tendertime = tendertime;
	}

	public String getCreatedAtStr() {
		return createdAtStr;
	}

	public void setCreatedAtStr(String createdAtStr) {
		this.createdAtStr = createdAtStr;
	}

	public Double getMoneyTotal() {
		return moneyTotal;
	}

	public void setMoneyTotal(Double moneyTotal) {
		this.moneyTotal = moneyTotal;
	}

	public Double getMoneyUsable() {
		return moneyUsable;
	}

	public void setMoneyUsable(Double moneyUsable) {
		this.moneyUsable = moneyUsable;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getInviteUsername() {
		return inviteUsername;
	}

	public void setInviteUsername(String inviteUsername) {
		this.inviteUsername = inviteUsername;
	}

	public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public String getOnPic() {
        return onPic;
    }

    public void setOnPic(String onPic) {
        this.onPic = onPic;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordPayHash() {
        return passwordPayHash;
    }

    public void setPasswordPayHash(String passwordPayHash) {
        this.passwordPayHash = passwordPayHash;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getEmailBindingStatus() {
        return emailBindingStatus;
    }

    public void setEmailBindingStatus(Integer emailBindingStatus) {
        this.emailBindingStatus = emailBindingStatus;
    }

    public Integer getMobileBindingStatus() {
        return mobileBindingStatus;
    }

    public void setMobileBindingStatus(Integer mobileBindingStatus) {
        this.mobileBindingStatus = mobileBindingStatus;
    }

    public Integer getRealVerifyStatus() {
        return realVerifyStatus;
    }

    public void setRealVerifyStatus(Integer realVerifyStatus) {
        this.realVerifyStatus = realVerifyStatus;
    }

    public Integer getCardBindingStatus() {
        return cardBindingStatus;
    }

    public void setCardBindingStatus(Integer cardBindingStatus) {
        this.cardBindingStatus = cardBindingStatus;
    }

    public Long getCardIdBase() {
        return cardIdBase;
    }

    public void setCardIdBase(Long cardIdBase) {
        this.cardIdBase = cardIdBase;
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

    public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
    }

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public int getIsTouzi() {
        return isTouzi;
    }

    public void setIsTouzi(int isTouzi) {
        this.isTouzi = isTouzi;
    }

    public int getIsRecharge() {
        return isRecharge;
    }

    public void setIsRecharge(int isRecharge) {
        this.isRecharge = isRecharge;
    }

	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public Double getCapital() {
		return capital;
	}

	public void setCapital(Double capital) {
		this.capital = capital;
	}

	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public String getRealVerify() {
		return realVerify;
	}

	public void setRealVerify(String realVerify) {
		this.realVerify = realVerify;
	}

	public Integer getAutoType() {
		return autoType;
	}

	public void setAutoType(Integer autoType) {
		this.autoType = autoType;
	}
    
}
