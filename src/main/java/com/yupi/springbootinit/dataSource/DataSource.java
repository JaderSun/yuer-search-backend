package com.yupi.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 数据源接口 (新接入的数据源必须实现)
 * @param <T>
 *
 * @author <a href="https://github.com/JaderSun">JaderSun</a>
 */
public interface DataSource<T> {

    /**
     * 搜索
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
