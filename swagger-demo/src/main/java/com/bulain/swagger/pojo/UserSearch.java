package com.bulain.swagger.pojo;

import com.bulain.core.pojo.Req;

public class UserSearch extends Req {
	private static final long serialVersionUID = 1L;

	private String loginName;// 登录名
	private String fullName;// 姓名

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
