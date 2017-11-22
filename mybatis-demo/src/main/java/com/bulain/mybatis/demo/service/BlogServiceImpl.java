package com.bulain.mybatis.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bulain.mybatis.demo.dao.BlogMapper;
import com.bulain.mybatis.demo.model.Blog;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
    private BlogMapper blogMapper;

    @Override
    public Blog get(Long id) {
        return blogMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Long insert(Blog data, boolean forced) {
        if (forced) {
            blogMapper.insert(data);
        } else {
            blogMapper.insertSelective(data);
        }
        return data.getId();
    }

    @Override
    public Long update(Blog data, boolean forced) {
        if (forced) {
            blogMapper.updateByPrimaryKey(data);
        } else {
            blogMapper.updateByPrimaryKeySelective(data);
        }
        return data.getId();
    }

    @Override
    public void delete(Long id) {
        blogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Long save(Blog data, boolean forced) {
        if (data.getId() == null) {
            return insert(data, forced);
        } else {
            return update(data, forced);
        }
    }
    

}
