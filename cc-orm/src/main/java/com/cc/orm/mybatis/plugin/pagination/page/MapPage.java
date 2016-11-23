package com.cc.orm.mybatis.plugin.pagination.page;

import java.util.Map;

/**
 * <pre>
 * 查询条件和返回的Entity都使用Map
 * </pre>
 * 
 * @author wenlongchen
 * @since Nov 18, 2016
 */
public class MapPage extends MapConditionPage<Map<String, Object>> {

  public MapPage() {
    super();
  }

  public MapPage(long currentPage, long pageSize) {
    super(currentPage, pageSize);
  }

  public MapPage(long currentPage) {
    super(currentPage);
  }

}
