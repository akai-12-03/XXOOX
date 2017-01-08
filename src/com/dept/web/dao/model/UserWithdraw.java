package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 用户提现申请
 * 
 * @ClassName:     UserWithdraw
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午11:01:09 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class UserWithdraw extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -5513008250575170443L;

    /**
     * 提现金额
     */
    private Double moneyWithdraw;
    
    private String remark;
    
    private Integer status;  //0待审核1审核成功2审核失败
    
    private Long createdBy;
    
    private String createdAt;
    
    private Long updatedBy;
    
    private String updatedAt;
    
    private String createdIp;
    
    private String username;
    
    private String realname;
    
    private String updatedByUsername;
    
    private String statusStr;
    
    private String useCard;
    
    private String cardName;
    
    private String cardNo;
    
    private String totalMoney;
    
    private String orderId;
    
    private String message;//第三方返回信息
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getUseCard() {
		return useCard;
	}

	public void setUseCard(String useCard) {
		this.useCard = useCard;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(int status) {
		if(status==1){
			statusStr="成功";
		}else if(status==0){
			statusStr="待审核";
		}else{
			statusStr="审核不通过";
		}
	}

	public Double getMoneyWithdraw() {
        return moneyWithdraw;
    }

    public void setMoneyWithdraw(Double moneyWithdraw) {
        this.moneyWithdraw = moneyWithdraw;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

   

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }


    public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
    }

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

    public String getUpdatedByUsername() {
        return updatedByUsername;
    }

    public void setUpdatedByUsername(String updatedByUsername) {
        this.updatedByUsername = updatedByUsername;
    }
    
    
    
}
