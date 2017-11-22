package com.bulain.mybatis.demo.service;

import java.util.List;

import com.bulain.mybatis.demo.dao.PagedMapper;
import com.bulain.mybatis.demo.pojo.Paged;
import com.bulain.mybatis.demo.pojo.Search;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 分页服务的实现类。
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
	public long count(final S search) {
		final PagedMapper<T, S> pagedMapper = getPagedMapper();
		
		long total = PageHelper.count(new ISelect() {
		    @Override
		    public void doSelect() {
		    	pagedMapper.find(search);
		    }
		});
		
		return total;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Paged<T> page(S search) {
		PagedMapper<T, S> pagedMapper = getPagedMapper();

		PageHelper.startPage(search.getPage(), search.getPageSize());

		List<T> list = null;
		try {
			list = pagedMapper.find(search);
		} finally {
			PageHelper.clearPage();
		}

		PageInfo pageInfo = new PageInfo(list);

		Paged<T> paged = new Paged<T>();
		paged.setPageSize(pageInfo.getPageSize());
		paged.setTotalPage(pageInfo.getPages());
		paged.setTotalCount(pageInfo.getTotal());
		paged.setPage(pageInfo.getPageNum());
		paged.setData(list);

		return paged;
	}

}
