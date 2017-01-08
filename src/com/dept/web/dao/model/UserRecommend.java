package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 
 * 用户推荐实体模型
 * @author yeyong
 *
 */
public class UserRecommend extends BaseEntity {

	/**
	 * 自动生成串号
	 */
	private static final long serialVersionUID = 6767187093466808331L;
	/**
	 * 推荐用户ID
	 */
	private Integer userid;	
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 状态（默认0，审核中；1，审核通过）
	 */
	private Integer status;
	/**
	 * 提交时间
	 */
	private Long createtime;
	/**
	 * 最后更新时间
	 */
	private Long updatetime;
	/**
	 * 推荐人
	 */
	private String uname;
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public Long getUpdatetime() {
		return updatetime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}
	
	
}
