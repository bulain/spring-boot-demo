package com.bulain.elastic.dao;

import com.bulain.elastic.model.Blog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface BlogDao extends ElasticsearchRepository<Blog, Long> {

}
