package com.cc.db.orm.mybatis.plugin.pagination.page;

import java.util.Map;

/**
 * <pre>
 * 查询条件使用Map
 * </pre>
 * @author wenlongchen
 * @since Nov 18, 2016
 */
public class MapConditionPage<E> extends Page<E, Map<String,Object>> {

  public MapConditionPage() {
    super();
  }

  public MapConditionPage(long currentPage, long pageSize) {
    super(currentPage, pageSize);
  }

  public MapConditionPage(long currentPage) {
    super(currentPage);
  }

}