package com.cc.orm.mybatis.plugin.pagination.dialect;

import com.cc.orm.mybatis.plugin.pagination.page.Page;

/**
 * @author wenlongchen
 * @since Nov 1, 2016
 */
public class MySqlDialect extends AbstractDialect {
  
  @SuppressWarnings("rawtypes")
  @Override
  public String getPageSql(String originalSql, Page page) {
    long offset = (page.getCurrentPage() - 1) * page.getPageSize();
    return originalSql + genenrateOrderBy(page) + " limit " + offset + "," + page.getPageSize();
  }

  @SuppressWarnings("rawtypes")
  private String genenrateOrderBy(Page page) {
    if (page.getSort() != null) {
      return " order by " + page.getSort();
    }
    return "";
  }

}

