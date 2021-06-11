package com.bulain.active;

import java.io.Serializable;

public class ActivemqData implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String descr;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	@Override
	public String toString() {
		return "RabbitData [id=" + id + ", name=" + name + ", descr=" + descr + "]";
	}
	
}
