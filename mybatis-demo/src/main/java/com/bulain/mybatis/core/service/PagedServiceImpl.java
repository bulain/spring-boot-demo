package com.bulain.mybatis.core.service;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.core.pojo.Search;
import com.bulain.mybatis.demo.dao.BlogMapper;
import com.bulain.mybatis.demo.entity.Blog;

/**
 * 分页服务的实现类。
 *
 * @author Bulain
 */
public abstract class PagedServiceImpl<M extends PagedMapper<T>, T> extends ServiceImpl<M, T> implements PagedService<T> {

    @Override
    public List<T> find(Search search) {
        return baseMapper.find(search);
    }

    @Override
    public Paged<T> page(Search search) {
        IPage<T> pagination = new Page<T>(search.getPage(), search.getPageSize());
        IPage<T> ret = baseMapper.find(pagination, search);

        Paged<T> paged = new Paged<T>();
        paged.setPageSize((int) ret.getSize());
        paged.setTotalPage((int) ret.getPages());
        paged.setTotalCount(ret.getTotal());
        paged.setPage((int) ret.getCurrent());
        paged.setData(ret.getRecords());

        return paged;
    }

}
