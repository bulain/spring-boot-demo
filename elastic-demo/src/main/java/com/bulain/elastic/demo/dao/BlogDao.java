package com.bulain.elastic.demo.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bulain.elastic.demo.model.Blog;

public interface BlogDao extends ElasticsearchRepository<Blog, Long> {

}
