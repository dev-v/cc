package com.cc.orm.mybatis.plugin.pagination;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author wenlongchen
 * @since Nov 1, 2016
 */
public class CCDefaultSqlSource implements SqlSource {
  BoundSql boundSql;

  protected CCDefaultSqlSource(BoundSql boundSql) {
    this.boundSql = boundSql;
  }

  @Override
  public BoundSql getBoundSql(Object parameterObject) {
    return boundSql;
  }

}

