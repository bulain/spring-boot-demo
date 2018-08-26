package com.bulain.mybatis.core.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.core.pojo.Search;

/**
 * 分页服务的实现类。
 * 
 * @author Bulain
 */
public abstract class PagedServiceImpl<T, S extends Search> extends BasicServiceImpl<T> implements PagedService<T, S> {

	protected abstract PagedMapper<T, S> getPagedMapper();

	@Override
	public Paged<T> find(S search) {
		PagedMapper<T, S> pagedMapper = getPagedMapper();

		List<T> list = pagedMapper.find(search);

		Paged<T> paged = new Paged<T>();
		paged.setPageSize(list.size());
		paged.setTotalPage(1);
		paged.setTotalCount(list.size());
		paged.setPage(1);
		paged.setData(list);

		return paged;
	}

	@Override
	public Paged<T> page(S search) {
		PagedMapper<T, S> pagedMapper = getPagedMapper();

		List<T> list = null;
		Page<T> pagination = new Page<T>(search.getPage(), search.getPageSize());
		list = pagedMapper.find(search, pagination);

		Paged<T> paged = new Paged<T>();
		paged.setPageSize(pagination.getSize());
		paged.setTotalPage((int) pagination.getPages());
		paged.setTotalCount(pagination.getTotal());
		paged.setPage(pagination.getCurrent());
		paged.setData(list);

		return paged;
	}

}
