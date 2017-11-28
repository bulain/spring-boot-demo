package com.bulain.elastic.demo.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.test.context.junit4.SpringRunner;

import com.bulain.elastic.ElasticApplication;
import com.bulain.elastic.demo.model.Blog;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticApplication.class)
public class TemplateDemo {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Test
	public void testQueryForList() {
		Criteria criteria = Criteria.where("id").is(1L);
		CriteriaQuery query = new CriteriaQuery(criteria);
		List<Blog> list = elasticsearchTemplate.queryForList(query, Blog.class);
		logger.debug("{}", list);
	}

}
