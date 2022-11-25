package com.bulain.elastic.dao;

import com.bulain.elastic.ElasticApplication;
import com.bulain.elastic.model.Blog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ElasticApplication.class)
class TemplateDemo {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    void testSave() {

        Blog data = new Blog();
        data.setId(2L);
        data.setTitle("abd");
        data.setDescr("descr");
        data.setCreatedVia("Thread");
        data.setActiveFlag("Y");
        data.setCreatedAt(new Date());
        data.setCreatedBy("PT");

        IndexQuery query = new IndexQuery();
        query.setId("2");
        query.setObject(data);

        IndexCoordinates coordinates = elasticsearchTemplate.getIndexCoordinatesFor(Blog.class);
        elasticsearchTemplate.index(query, coordinates);

    }

    @Test
    void testQueryForList() {
        Criteria criteria = Criteria.where("id").is(1L);
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<Blog> search = elasticsearchTemplate.search(query, Blog.class);
        logger.debug("{}", search.toList());
    }

    @Test
    void testDelete() {
        elasticsearchTemplate.delete("2", Blog.class);
    }

}
