package com.bulain.mybatis.demo.service;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.demo.model.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class BlogServiceTest {

	@Autowired
	private BlogService blogService;

	private BlogSearch search;

	@Before
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
	public void testFind() {
		search.addOrderBy("id", "asc");
		blogService.find(search);
	}

	@Test
	public void testPage() {
		search.setPage(3);
		search.setPageSize(2);
		search.addOrderBy("id", "asc");
		Paged<Blog> paged = blogService.page(search);
		System.out.println(paged);
	}

}
