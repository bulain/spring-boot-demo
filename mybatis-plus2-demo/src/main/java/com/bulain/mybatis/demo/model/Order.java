package com.bulain.mybatis.demo.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;

@TableName("ORDERS")
public class Order {

	@TableId
	private Long id;
	private String orderNo;
	private String extnRefNo1;
	private String extnRefNo2;
	private String extnRefNo3;
	private String createdVia;
	private String remarks;
	private Date createdAt;
	private String createdBy;
	private Date updatedAt;
	private String updatedBy;
	@Version
	private Long version;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getExtnRefNo1() {
		return extnRefNo1;
	}
	public void setExtnRefNo1(String extnRefNo1) {
		this.extnRefNo1 = extnRefNo1;
	}
	public String getExtnRefNo2() {
		return extnRefNo2;
	}
	public void setExtnRefNo2(String extnRefNo2) {
		this.extnRefNo2 = extnRefNo2;
	}
	public String getExtnRefNo3() {
		return extnRefNo3;
	}
	public void setExtnRefNo3(String extnRefNo3) {
		this.extnRefNo3 = extnRefNo3;
	}
	public String getCreatedVia() {
		return createdVia;
	}
	public void setCreatedVia(String createdVia) {
		this.createdVia = createdVia;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}

}
