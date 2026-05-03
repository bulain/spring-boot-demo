package com.bulain.mybatis.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.core.pojo.Search;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分页服务的实现类。
 *
 * @author Bulain
 */
public abstract class PagedServiceImpl<M extends PagedMapper<T>, T> extends ServiceImpl<M, T> implements PagedService<T> {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(T entity) {
        return super.save(entity);
    }

    @Override
    public List<T> find(Search search) {
        return baseMapper.find(search);
    }

    @Override
    public Paged<T> page(Search search) {
        IPage<T> pagination = new Page<>(search.getPage(), search.getPageSize());
        IPage<T> pageResult = baseMapper.find(pagination, search);
        return Paged.from(pageResult);
    }

}
