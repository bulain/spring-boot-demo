package com.bulain.mybatis.demo.service;

import com.bulain.mybatis.MybatisApplication;
import com.bulain.mybatis.demo.model.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisApplication.class)
public class BlogServiceDemo {

	@Autowired
	private BlogService blogService;

	private BlogSearch search;

	@BeforeEach
	public void setUp() {
		search = new BlogSearch();
		search.setTitle("abd");
		search.setDescr("descr");
		search.setActiveFlag("Y");
		search.setCreatedVia("Thread");
	}

	@Test
	public void testInsert() {
		Blog data = new Blog();
		data.setTitle("abd");
		data.setDescr("descr");
		data.setCreatedVia("Thread");
		data.setActiveFlag("Y");
		data.setCreatedAt(new Date());
		data.setCreatedBy("PT");
		blogService.insert(data, true);
	}

	@Test
	public void testUpdates() {
		Blog data = new Blog();
		data.setId(1L);
		data.setTitle("abd");
		data.setDescr("descr");
		data.setCreatedVia("Thread");
		data.setActiveFlag("Y");
		data.setCreatedAt(new Date());
		data.setCreatedBy("PT");
		blogService.update(data, true);
	}

	@Test
	public void testCount() {
		blogService.count(search);
	}

	@Test
	public void testFind() {
		search.addOrderBy("id", "asc");
		blogService.find(search);
	}

	@Test
	public void testPage() {
		search.setPage(3);
		search.setPageSize(2);
		search.addOrderBy("id", "asc");
		blogService.page(search);
	}

}
