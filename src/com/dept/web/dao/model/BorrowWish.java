package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class BorrowWish extends BaseEntity{

	private long id;
	private String wishMoney;  //借款金额
	private String wishApr;  //借款利率
	private String wishTime; //借款时间
	private String wishUse;  //借款用途
	private String wishRepayStyle;  //还款方式
	private String userPhone;  //联系方式
	private String userPhone2;
	private String userSecurity;
	private String userSecurity2;
	private String userHomeAddress;
	private long userId;   //用户ID
	private String userName;  //用户名
	private int createTime;  //提交时间
	private int status;  //状态0未联系1已联系
	private String realname;  //真实姓名
	
	
	public Long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getWishMoney() {
		return wishMoney;
	}
	public void setWishMoney(String wishMoney) {
		this.wishMoney = wishMoney;
	}
	public String getWishRepayStyle() {
		return wishRepayStyle;
	}
	public void setWishRepayStyle(String wishRepayStyle) {
		this.wishRepayStyle = wishRepayStyle;
	}
	public String getWishApr() {
		return wishApr;
	}
	public void setWishApr(String wishApr) {
		this.wishApr = wishApr;
	}
	public String getWishTime() {
		return wishTime;
	}
	public void setWishTime(String wishTime) {
		this.wishTime = wishTime;
	}
	public String getWishUse() {
		return wishUse;
	}
	public void setWishUse(String wishUse) {
		this.wishUse = wishUse;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
	
	public String getUserPhone2() {
		return userPhone2;
	}
	public void setUserPhone2(String userPhone2) {
		this.userPhone2 = userPhone2;
	}
	public String getUserSecurity() {
		return userSecurity;
	}
	public void setUserSecurity(String userSecurity) {
		this.userSecurity = userSecurity;
	}
	public String getUserSecurity2() {
		return userSecurity2;
	}
	public void setUserSecurity2(String userSecurity2) {
		this.userSecurity2 = userSecurity2;
	}
	public String getUserHomeAddress() {
		return userHomeAddress;
	}
	public void setUserHomeAddress(String userHomeAddress) {
		this.userHomeAddress = userHomeAddress;
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
}
