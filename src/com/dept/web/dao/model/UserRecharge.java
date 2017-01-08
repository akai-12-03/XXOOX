package com.dept.web.dao.model;
import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 用户充值申请
 * 
 * @ClassName:     UserRecharge
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:59:00 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class UserRecharge extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 767424148339715046L;

    private Long id;
    /**
     * 支付订单ID
     */
    private String orderId;
    
    /**
     * 充值账号
     */
    private String account;
    
    /**
     * 银行卡号
     */
    private String cardNo;
    
    /**
     * 充值金额
     */
    private Double moneyRecharge;
    
    private String remark;
    
    /**
     * 第三方支付平台（联动，连连
     */
    private Long thirdPlatform;
    
    /**
     * 第三方平台对应的订单支付ID
     */
    private Long thirdPlatformOrderId;
    
    /**
     * 支付来源（IOS，Android，Web，Wap）
     */
    private String paySource;
    
    /**
     * 第三方平台回复的支付结果
     */
    private String payResult;
    
    /**
     * 充值状态
     */
    private Integer status;
    
    private Long createdBy;
    
    private Long updatedBy;
    
    private String createdAt;
    
    private String updatedAt;
    
    private String createdIp;

    private String username;
    
    private String realname;
    
    private String statusStr;//用于转换
    
    private String paySourceStr;
    
    private String totalMoney;
    
    
    public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getPaySourceStr() {
		return paySourceStr;
	}

	public void setPaySourceStr(String paySource) {
		
		if(paySource.equals("Offline")){
			 paySourceStr="线下充值";
		}else if(paySource.equals("SQ")){
			 paySourceStr="双乾";
		}else if(paySource.equals("Deduct_Offline")){
			 paySourceStr="线下扣款";
		}else if(paySource.equals("TL")){
			 paySourceStr="通联";
		}else{
			 paySourceStr="";
		}
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(int status  ) {
		if(status==1){
			statusStr="成功";
		}else{
			statusStr="失败";
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Double getMoneyRecharge() {
        return moneyRecharge;
    }

    public void setMoneyRecharge(Double moneyRecharge) {
        this.moneyRecharge = moneyRecharge;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getThirdPlatform() {
        return thirdPlatform;
    }

    public void setThirdPlatform(Long thirdPlatform) {
        this.thirdPlatform = thirdPlatform;
    }

    public Long getThirdPlatformOrderId() {
        return thirdPlatformOrderId;
    }

    public void setThirdPlatformOrderId(Long thirdPlatformOrderId) {
        this.thirdPlatformOrderId = thirdPlatformOrderId;
    }

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
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
    
    
}
