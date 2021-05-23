package com.bulain.elastic.demo.dao;

import com.bulain.elastic.ElasticApplication;
import com.bulain.elastic.demo.model.Blog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
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
		Optional<Blog> opt = blogDao.findById(1L);
		if (opt.isPresent()) {
			Blog blog = opt.get();
			assertNotNull(blog);
		}
	}

	@Test
	public void testExists() {
		boolean exists = blogDao.existsById(1L);
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
		blogDao.deleteById(1L);
	}

}
