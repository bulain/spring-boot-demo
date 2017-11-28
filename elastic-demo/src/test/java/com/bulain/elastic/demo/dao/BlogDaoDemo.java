package com.bulain.elastic.demo.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bulain.elastic.ElasticApplication;
import com.bulain.elastic.demo.model.Blog;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticApplication.class)
public class BlogDaoDemo {

	@Autowired
	private BlogDao blogDao;

	@Test
	public void testSave() {
		Blog data = new Blog();
		data.setId(1L);
		data.setTitle("abd");
		data.setDescr("descr");
		data.setCreatedVia("Thread");
		data.setActiveFlag("Y");
		data.setCreatedAt(new Date());
		data.setCreatedBy("PT");
		blogDao.save(data);
	}

	@Test
	public void testFindOne() {
		Blog blog = blogDao.findOne(1L);
		assertNotNull(blog);
	}

	@Test
	public void testExists() {
		boolean exists = blogDao.exists(1L);
		assertTrue(exists);
	}

	@Test
	public void testFindAll() {
		Iterable<Blog> iter = blogDao.findAll();
		assertNotNull(iter);
	}

	@Test
	public void testCount() {
		long count = blogDao.count();
		assertTrue(count > 0);
	}

	@Test
	public void testDeleteID() {
		blogDao.delete(1L);
	}

}
