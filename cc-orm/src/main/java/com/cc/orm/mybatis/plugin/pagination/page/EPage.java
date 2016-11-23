package com.cc.orm.mybatis.plugin.pagination.page;


/**
 * <pre>
 * 查询条件和查询数据类型一样
 * </pre>
 * 
 * @author wenlongchen
 * @since Nov 18, 2016
 */
public class EPage<E> extends Page<E, E> {

  public EPage() {
    super();
  }

  public EPage(long currentPage, long pageSize) {
    super(currentPage, pageSize);
  }

  public EPage(long currentPage) {
    super(currentPage);
  }

}

