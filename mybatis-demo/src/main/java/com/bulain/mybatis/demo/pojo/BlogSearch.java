package com.bulain.mybatis.demo.pojo;

import com.bulain.mybatis.core.pojo.Search;
import com.bulain.mybatis.demo.entity.Blog;

public class BlogSearch extends Search<Blog> {
	private static final long serialVersionUID = 1L;

	private String title;
	private String descr;
	private String activeFlag;
	private String createdVia;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getCreatedVia() {
		return createdVia;
	}

	public void setCreatedVia(String createdVia) {
		this.createdVia = createdVia;
	}

}
