package com.dept.web.dao.model;


import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class Hongbao  extends BaseEntity{
	
  private static final long serialVersionUID = -7674246555556L;
  private Long	id;
  private Long user_id;//用户id
  private Double money;//红包金额

  private String addtime;//添加时间	
  private String endtime;//结束时间	
  private String updatetime;//更新时间
  private Integer type;//类型。 1:用户注册送。 2:推荐好友送。
  private Integer status;//状态： 0 未使用。 1:已使用。 2:正在使用中。3:已过期
  private String typeStr;
  private String statusStr;
  private String name;
  private String username;
  private String usetime;
  
  
  
	public String getUsetime() {
	return usetime;
}
public void setUsetime(String usetime) {
	this.usetime = usetime;
}
	public String getTypeStr() {
	return typeStr;
}
public void setTypeStr(int type) {
	if(type ==1){
		typeStr="用户注册送";
	}else if(type ==2){
		typeStr="推荐好友送";
	}else{
		typeStr="其他";
	}
}
	public String getStatusStr() {
	return statusStr;
}
public void setStatusStr(int status) {
	if(status==0){
		statusStr="未使用";
	}else if(status==1){
		statusStr="已使用";
	}else if(status==2){
		statusStr="使用中";
	}else if(status==3){
		statusStr="已过期";
	}else{
		statusStr="其他";
	}
}
	public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
	public String getUpdatetime() {
  		return updatetime;
  	}
  	public void setUpdatetime(String updatetime) {
  		this.updatetime = updatetime;
  	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
  
  
  
  
	
}