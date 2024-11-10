package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.bulain.mybatis.core.service.PagedService;
import com.bulain.mybatis.demo.entity.Blog;

public interface BlogService extends PagedService<Blog> {

    /**直接删除*/
    boolean directRemove(Wrapper<Blog> queryWrapper);

}
