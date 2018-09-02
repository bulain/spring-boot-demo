package com.bulain.shiro.pojo;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String loginName;//登录名
	private String passwordHash;//哈希密码
	private String slat;//盐
	private String fullName;//姓名
	private String activeFlag;//激活标志
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getSlat() {
		return slat;
	}
	public void setSlat(String slat) {
		this.slat = slat;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	
}
