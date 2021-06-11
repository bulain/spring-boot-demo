package com.bulain.elastic.model;

import java.util.Date;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "blog")
public class Blog {
	
	private Long id;
	private String title;
	private String descr;
	private String activeFlag;
	private String createdVia;
	private String remarks;
	private Date createdAt;
	private String createdBy;
	private Date updatedAt;
	private String updatedBy;

}
