package com.bulain.mybatis.demo.pojo;

import com.bulain.mybatis.core.pojo.Search;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BlogSearch extends Search {

	private String title;
	private String descr;
	private String activeFlag;
	private String createdVia;

}
