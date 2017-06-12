package com.cc.db.orm.mybatis.mapper;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.alibaba.fastjson.JSON;
import com.cc.db.orm.mybatis.plugin.pagination.page.Page;
import com.cc.tool.Util;

/**
 * @author wenlongchen
 * @since Nov 15, 2016
 */
public abstract class AbstractSqlHandle implements SqlHandle {

  protected SQL sql;
  private Object params;

  public AbstractSqlHandle(SQL sql, Object params) {
    this.sql = sql;
    this.params = params;
  }

  @Override
  public void handleCollection(Collection<Object> collection) {
    throw new UnsupportedOperationException("不支持集合或数组类型！");
  }

  @Override
  public void handlePage(Page<?, ?> page) {
    throw new UnsupportedOperationException("不支持集合或数组类型！");
  }

  @SuppressWarnings("unchecked")
  @Override
  public String getSql() {
    if (params != null) {
      if (Util.isPremitive(params)) {
        handlePremitive(params);
      } else if (params instanceof Map) {
        handleComplex((Map<String, Object>) params);
      } else if (params instanceof Page) {
        handlePage((Page<?, ?>) params);
      } else if (params instanceof Collection) {
        handleCollection((Collection<Object>) params);
      } else {
        handleComplex((Map<String, Object>) JSON.toJSON(params));
      }
    }
    return sql.toString();
  }

}

