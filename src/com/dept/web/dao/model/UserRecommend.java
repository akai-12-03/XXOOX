package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 
 * �û��Ƽ�ʵ��ģ��
 * @author yeyong
 *
 */
public class UserRecommend extends BaseEntity {

	/**
	 * �Զ����ɴ���
	 */
	private static final long serialVersionUID = 6767187093466808331L;
	/**
	 * �Ƽ��û�ID
	 */
	private Integer userid;	
	/**
	 * �û���
	 */
	private String username;
	/**
	 * �ֻ���
	 */
	private String mobile;
	/**
	 * ״̬��Ĭ��0������У�1�����ͨ����
	 */
	private Integer status;
	/**
	 * �ύʱ��
	 */
	private Long createtime;
	/**
	 * ������ʱ��
	 */
	private Long updatetime;
	/**
	 * �Ƽ���
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
