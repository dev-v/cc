package com.cc.orm.mybatis.plugin.pagination.page;

import java.util.Collections;
import java.util.List;

/**
 * @author wenlongchen
 * @param <E> 查询返回的数据类型
 * @param <Q> 查询条件类型
 */
public class Page <E,Q>{

  public static final long MAX_PAGE_SIZE = 1000;
  
  public static final String CONDITION_FIELD_NAME="condition";

  /**
   * 当前页，从1开始
   */
  private long currentPage;

  /**
   * 每页数量
   */
  private long pageSize;

  /**
   * 总数据行数
   */
  private long count;

  /**
   * 总页数
   */
  private long allPage;

  /**
   * 当前页查询的数据
   */
  private List<E> datas;

  /**
   * 查询条件
   */
  private Q condition;

  /**
   * 排序字段
   */
  private String sort;

  /**
   * @param currentPage 当前页 从1开始
   * @param pageSize 当前页行数
   */
  public Page(long currentPage, long pageSize) {
    this.currentPage = currentPage > 0 ? currentPage : 1;
    this.pageSize = pageSize > 0 ? pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize : 1;
  }

  /**
   * pageSize 默认20
   * 
   * @param currentPage
   * @see Page#Page(long, long)
   */
  public Page(long currentPage) {
    this(currentPage, 20);
  }

  /**
   * <pre>
   * currentPage 默认1
   * </pre>
   * 
   * @see Page#Page(long)
   */
  public Page() {
    this(1);
  }

  public long getCurrentPage() {
    return currentPage;
  }

  public long getPageSize() {
    return pageSize;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count > -1 ? count : 0;
    long temp = count % pageSize;
    allPage = (count / pageSize) + (temp == 0 ? 0 : 1);
    if (currentPage > allPage)
      currentPage = allPage;
  }

  public long getAllPage() {
    return allPage;
  }

  public List<E> getDatas() {
    return datas;
  }

  public void setDatas(List<E> datas) {
    this.datas = datas;
  }

  public Q getCondition() {
    return condition;
  }

  public void setCondition(Q condition) {
    this.condition = condition;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  @SuppressWarnings("unchecked")
  public void emptyDatas() {
    this.datas = Collections.EMPTY_LIST;
  }

}

