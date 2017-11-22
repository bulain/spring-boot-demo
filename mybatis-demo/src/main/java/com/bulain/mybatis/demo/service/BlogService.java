package com.bulain.mybatis.demo.service;

import com.bulain.mybatis.demo.model.Blog;


public interface BlogService {
    Blog get(Long id);
    Long insert(Blog data, boolean forced);
    Long update(Blog data, boolean forced);
    void delete(Long id);
    Long save(Blog data, boolean forced);
}
