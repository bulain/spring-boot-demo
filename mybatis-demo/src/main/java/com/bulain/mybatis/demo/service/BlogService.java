package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.demo.entity.Blog;

public interface BlogService extends IService<Blog> {

    /**直接删除*/
    boolean directRemove(Wrapper<Blog> queryWrapper);

}