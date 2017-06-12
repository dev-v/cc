package com.cc.db.orm.mybatis.mapper;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.ibatis.jdbc.SQL;

import com.alibaba.fastjson.JSONObject;
import com.cc.db.orm.mybatis.plugin.pagination.page.Page;
import com.cc.tool.Util;

/**
 * <pre>
 * 针对单表的基础sql生成器
 * </pre>
 * 
 * @author wenlongchen
 * @since Jun 23, 2016
 */
public abstract class BaseSql {

  private final String table;
  private String queryList;
  private final String keyColumnName;
  private final String keyPropertyName;
  // 数据库字段与属性映射
  private final Map<String, String> columnPropertyMapper;
  // 只能进行增删改操作数据库字段与属性映射
  private final Map<String, String> operateColumnPropertyMapper;
  // 属性与数据库字段的映射
  private final Map<String, String> propertyToColumnMapper;
  protected static final String DEFAULT_KEY_COLUMN = "id";

  public static final String METHOD_getByKey = "getByKey";
  public static final String METHOD_insert = "insert";
  public static final String METHOD_delByKey = "delByKey";
  public static final String METHOD_updateByKey = "updateByKey";
  public static final String METHOD_selectByColumn = "selectByColumn";
  public static final String METHOD_selectByKeys = "selectByKeys";

  /**
   * <pre>
   * </pre>
   * 
   * @param table 针对表的表名
   * @param queryList 常用的一个查询列表(只允许包含数据库原始列)
   * @param keyColumnName 主键列（仅支持单列主键）
   */
  public BaseSql(String table, String queryList, String keyColumnName) {
    this.table = table;
    this.keyColumnName = keyColumnName;
    this.keyPropertyName = Util.toJavaStyle(keyColumnName);
    columnPropertyMapper =
        Util.mapperJavaStyleToCaseInsensitiveMap(queryList, String.valueOf(Util.SYMBOL_COMMA));
    columnPropertyMapper.put(keyColumnName, keyPropertyName);
    operateColumnPropertyMapper = Util.convertToCaseInsensitiveMap(columnPropertyMapper);
    propertyToColumnMapper = new CaseInsensitiveMap<>();
    for (String key : columnPropertyMapper.keySet()) {
      propertyToColumnMapper.put(columnPropertyMapper.get(key), key);
    }
    this.setMapper();
  }

  /**
   * <pre>
   * keyColumnName默认为DEFAULT_KEY_COLUMN
   * </pre>
   * 
   * @param table 表名
   * @param queryList 查询列表 符合规范的数据库字段命名和java属性命名方式的 将进行自动映射 支持下划线和点分割
   * @see <code>BaseSql(String table, String queryList, String keyColumnName)</code>
   */
  public BaseSql(String table, String queryList) {
    this(table, queryList, DEFAULT_KEY_COLUMN);
  }

  /**
   * <pre>
   * 参数不能直接为不加@Param的基础类型，charsequence
   * </pre>
   * 
   * @param params
   * @return
   */
  public String insert(Object params) {
    SQL sql = new SQL() {
      {
        INSERT_INTO(table);
      }
    };

    String sqlStr = new AbstractSqlHandle(sql, params) {

      @Override
      public void handlePremitive(Object obj) {
        throw new UnsupportedOperationException("不支持基础类型！");
      }

      @Override
      public void handleComplex(Map<String, Object> obj) {
        String[] results = null;
        for (String key : obj.keySet()) {
          results = paramsReady(key, obj);
          if (results[0] != null) {
            sql.VALUES(results[0], "#{" + results[1] + '}');
          }
        }
      }
    }.getSql();

    return sqlStr;
  }

  /**
   * <pre>
   * 支持不带@param的基础类型、charsequence
   * 支持map、复合类型或者@param
   * 获取根据主键删除数据的sql
   * </pre>
   * 
   * @return
   */
  public String delByKey(Object params) {

    SQL sql = new SQL() {
      {
        DELETE_FROM(table);// .WHERE(keyColumnName + "=#{" + columnKeyInParams + '}');
      }
    };

    SqlHandle sqlHandle = new AbstractSqlHandle(sql, params) {

      @Override
      public void handlePremitive(Object obj) {
        sql.WHERE(keyColumnName + "=#{" + keyColumnName + '}');
      }

      @Override
      public void handleComplex(Map<String, Object> obj) {
        buildWhereKey(sql, obj);
      }
    };

    return sqlHandle.getSql();
  }

  /**
   * <pre>
   * 生成主键where条件
   * 不区分大小写
   * </pre>
   * 
   * @param sql
   * @param params
   */
  private void buildWhereKey(SQL sql, Map<String, Object> params, boolean isPage) {
    String columnKeyInParams = getKeyIgnoreCaseInParamMap(params, keyColumnName, keyPropertyName);

    if (columnKeyInParams != null) {
      Object values = params.get(columnKeyInParams);
      if (values != null) {
        if (values instanceof Collection) {
          String temp = values.toString();
          temp = temp.substring(1, temp.length() - 1);
          sql.WHERE(keyColumnName + " in (" + temp + ')');
        } else {
          sql.WHERE(keyColumnName + "=#{" + (isPage ? (Page.CONDITION_FIELD_NAME + '.') : "")
              + columnKeyInParams + '}');
        }
      }
    }
  }

  /**
   * <pre>
   * 生成主键where条件
   * 不区分大小写
   * </pre>
   * 
   * @param sql
   * @param params
   */
  private void buildWhereKey(SQL sql, Map<String, Object> params) {
    buildWhereKey(sql, params, false);
  }

  /**
   * <pre>
   * 支持不带@param的基础类型、charsequence
   * 支持map、复合类型或者@param
   * 生成根据主键获取数据的sql
   * </pre>
   *
   * @return
   */
  public String getByKey(Object params) {
    SQL sql = new SQL() {
      {
        SELECT(queryList).FROM(table);
      }
    };

    SqlHandle sqlHandle = new AbstractSqlHandle(sql, params) {

      @Override
      public void handlePremitive(Object obj) {
        sql.WHERE(keyColumnName + "=#{" + keyColumnName + '}');
      }

      @Override
      public void handleComplex(Map<String, Object> obj) {
        buildWhereKey(sql, obj);
      }
    };

    return sqlHandle.getSql();
  }

  /**
   * <pre>
   * 支持map、复合类型
   * 根据主键修改数据
   * </pre>
   * 
   * @param params
   * @return
   */
  public String updateByKey(Object params) {

    SQL sql = new SQL() {
      {
        UPDATE(table);
      }
    };

    SqlHandle sqlHandle = new AbstractSqlHandle(sql, params) {

      @Override
      public void handlePremitive(Object obj) {
        throw new UnsupportedOperationException("不支持基础类型！");
      }

      @Override
      public void handleComplex(Map<String, Object> params) {
        String[] results = null;
        for (String key : params.keySet()) {
          if (!isKey(key)) {
            results = paramsReady(key, params);
            if (results[0] != null) {
              sql.SET(results[0] + "=#{" + results[1] + '}');
            }
          }
        }
        buildWhereKey(sql, params);
      }
    };

    return sqlHandle.getSql();
  }

  /**
   * <pre>
   * 根据查询参数字段生成查询sql
   * 所有查询参数的条件用and和=组装
   * 支持主键集合及优先使用主键索引查询
   * </pre>
   * 
   * @param params
   * @return
   */
  public String selectByColumn(Object params) {

    SQL sql = new SQL() {
      {
        SELECT(queryList).FROM(table);
      }
    };

    SqlHandle sqlHandle = new AbstractSqlHandle(sql, params) {

      @Override
      public void handlePremitive(Object obj) {
        sql.WHERE(keyColumnName + "=#{" + keyColumnName + '}');
      }

      @Override
      public void handleComplex(Map<String, Object> params) {
        buildSql(params, false);
      }

      @SuppressWarnings("unchecked")
      @Override
      public void handlePage(Page<?, ?> page) {
        if (page.getCondition() != null) {
          Map<String, Object> params = (Map<String, Object>) JSONObject.toJSON(page.getCondition());
          buildSql(params, true);
        }
      }

      private void buildSql(Map<String, Object> params, boolean isPage) {
        buildWhereKey(sql, params, isPage);
        String condition;
        for (String key : params.keySet()) {
          if (!isKey(key)) {
            condition = isPage ? conditionPageWrap(key, params, "=") : conditionEQ(key, params);
            if (condition != null) {
              sql.AND().WHERE(condition);
            }
          }
        }
      }
    };

    return sqlHandle.getSql();
  }

  /**
   * <pre>
   * 根据主键集合查询数据
   * </pre>
   * 
   * @param keys
   * @return
   */
  public String selectByKeys(Object params) {
    SQL sql = new SQL() {
      {
        SELECT(queryList).FROM(table);
      }
    };

    SqlHandle sqlHandle = new AbstractSqlHandle(sql, params) {

      @Override
      public void handlePremitive(Object obj) {
        sql.WHERE(keyColumnName + "=#{" + keyColumnName + '}');
      }

      @Override
      public void handleCollection(Collection<Object> collection) {
        String temp = collection.toString();
        temp = temp.substring(1, temp.length() - 1);
        sql.WHERE(keyColumnName + " in (" + temp + ')');
      }

      @Override
      public void handleComplex(Map<String, Object> obj) {
        buildWhereKey(sql, obj);
      }
    };

    return sqlHandle.getSql();
  }

  /**
   * <pre>
   * 手动添加或覆盖数据库列和java属性的映射
   * 对查询列表及添加、修改操作有效
   * </pre>
   * 
   * @param column 列名
   * @param javaProperty java属性名
   */
  protected void mapper(String column, String javaProperty) {
    columnPropertyMapper.put(column, javaProperty);
    operateColumnPropertyMapper.put(column, javaProperty);
    propertyToColumnMapper.put(javaProperty, column);
    this.setMapper();
  }

  /**
   * <pre>
   * 手动添加或覆盖数据库列和java属性的映射
   * 对添加、修改操作有效
   * </pre>
   */
  protected void operateMapper(String column, String javaProperty) {
    operateColumnPropertyMapper.put(column, javaProperty);
    propertyToColumnMapper.put(javaProperty, column);
  }

  /**
   * 自定义 字符串到 表column的映射, case in sensitive.
   * 
   * @param paramName
   * @param column
   */
  protected void propertyToColumnMapper(String paramName, String column) {
    propertyToColumnMapper.put(paramName, column);
  }

  /**
   * <pre>
   * 根据最新的列、属性映射情况，重新生产查询列表
   * </pre>
   */
  private final void setMapper() {
    StringBuilder builder = new StringBuilder();
    for (String column : columnPropertyMapper.keySet()) {
      builder.append(column).append(' ').append(columnPropertyMapper.get(column)).append(',');
    }
    if (builder.length() > 0) {
      builder.setLength(builder.length() - 1);
    }
    this.queryList = builder.toString();
  }

  /**
   * <pre>
   * 获取表名
   * </pre>
   * 
   * @return
   */
  public String getTable() {
    return table;
  }

  /**
   * <pre>
   * 获取查询列表
   * </pre>
   * 
   * @return
   */
  public String getQueryList() {
    return queryList;
  }

  /**
   * <pre>
   * 获取主键字段名称
   * </pre>
   * 
   * @return
   */
  public String getKeyColumnName() {
    return keyColumnName;
  }

  /**
   * <pre>
   * 生成 = 条件
   * </pre>
   * 
   * @param column
   * @param params
   * @return
   */
  protected final String conditionEQ(String column, Map<String, ?> params) {
    return conditionWrap(column, params, "=");
  }

  /**
   * <pre>
   * 生成 >= 条件
   * </pre>
   * 
   * @param column
   * @param params
   * @return
   */
  protected final String conditionGE(String column, Map<String, ?> params) {
    return conditionWrap(column, params, ">=");
  }

  /**
   * <pre>
   * 生成 <= 条件
   * </pre>
   * 
   * @param column
   * @param params
   * @return
   */
  protected final String conditionLE(String column, Map<String, ?> params) {
    return conditionWrap(column, params, "<=");
  }

  /**
   * <pre>
   * 包裹条件
   * </pre>
   * 
   * @param column
   * @param params
   * @param operate
   * @return
   */
  final protected String conditionWrap(String column, Map<String, ?> params, String operate) {
    String[] results = paramsReady(column, params);
    if (results[0] != null) {
      return results[0] + operate + "#{" + results[1] + '}';
    }
    return null;
  }

  /**
   * <pre>
   * 组装分页查询条件
   * </pre>
   * 
   * @param column
   * @param params
   * @param operate
   * @return
   */
  final protected String conditionPageWrap(String column, Map<String, ?> params, String operate) {
    String[] results = paramsReady(column, params);
    if (results[0] != null) {
      return results[0] + operate + "#{" + Page.CONDITION_FIELD_NAME + '.' + results[1] + '}';
    }
    return null;
  }

  /**
   * <pre>
   * 返回的结果集中：
   * 第一个参数为数据库字段名称 
   * 第二个参数为对应的用户参数的属性名称 
   * 
   * 若无法组装 则第一个参数为null
   * 
   * 会处理主键字段
   * 
   * 支持数据库字段和java属性格式
   * </pre>
   * 
   * @param column 参数集合中的字段名称
   * @param params
   * @return
   */
  private String[] paramsReady(String column, Map<String, ?> params) {
    // 返回的结果集 第一个参数为数据字段名称 第二个参数为对应的查询条件中的属性名称 若无法组装条件 则第一个参数为null
    String[] results = {null, column};

    if (params.get(column) != null) {
      results[0] = getColumnName(column);
    }

    return results;
  }



  private String getKeyIgnoreCaseInParamMap(Map<String, ?> params, String... wantedKeys) {
    Set<String> paramKeySet = params.keySet();
    for (String wantedKey : wantedKeys) {
      for (String key : paramKeySet) {
        if (key.equalsIgnoreCase(wantedKey)) {
          return key;
        }
      }
    }
    return null;
  }

  /**
   * <pre>
   * 根据字符窜获取对应的数据库字段名称
   * 字符串可以为数据库字段名称、java属性
   * 
   * 不区分大小写
   * 
   * 没有对应数据库字段名称返回null
   * </pre>
   * 
   * @param name
   * @return
   */
  public String getColumnName(String name) {
    // 数据库字段
    if (columnPropertyMapper.containsKey(name)) {
      return name;
    }
    // java属性
    if (propertyToColumnMapper.containsKey(name)) {
      return propertyToColumnMapper.get(name);
    }
    return null;
  }

  /**
   * <pre>
   * 判定给定字符是否为主键字段或属性
   * </pre>
   * 
   * @param name
   * @return
   */
  public boolean isKey(String name) {
    return name != null
        && (name.equalsIgnoreCase(keyColumnName) || name.equalsIgnoreCase(keyPropertyName));
  }

}
