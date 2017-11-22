package com.bulain.mybatis.demo.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bulain.mybatis.MybatisApplication;
import com.bulain.mybatis.demo.model.Blog;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MybatisApplication.class)
public class BlogServiceDemo {

	@Autowired
	private BlogService blogService;

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

}
