package com.cc.orm.mybatis.mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

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
  // private static final Logger log = Logger.getLogger(BaseSql.class);

  private final String table;
  private String queryList;
  private final String keyColumnName;
  private final Map<String, String> columnPropertyMapper;
  private final Map<String, String> operateColumnPropertyMapper;
  private final Map<String, String> paramToColumnMapper;
  protected static final String DEFAULT_KEY_COLUMN = "id";

  private String getByKeyColumnSql;
  private String delByKeyColumnSql;

  /**
   * 根据主键获取数据
   */
  public static final String METHOD_getByKey = "getByKey";
  
  public static final String METHOD_insert = "insert";
  
  public static final String METHOD_delByKey = "delByKey";
  
  /**
   * 自定义查询条件列获取数据
   */
  public static final String METHOD_selectByColumn = "selectByColumn";
  
  /**
   * 根据主键集合获取数据
   */
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
    columnPropertyMapper = Util.mapperJavaStyle(queryList, String.valueOf(Util.SYMBOL_COMMA));
    operateColumnPropertyMapper = new HashMap<String, String>(columnPropertyMapper);
    paramToColumnMapper = new HashMap<String, String>();
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
   * 生成根据主键获取数据的sql
   * </pre>
   * 
   * @return
   */
  public String getByKey() {
    if (getByKeyColumnSql == null) {
      getByKeyColumnSql = new SQL() {
        {
          SELECT(queryList).FROM(table)
              .WHERE(keyColumnName + "=#{" + columnPropertyMapper.get(keyColumnName) + '}');
        }
      }.toString();
    }
    return getByKeyColumnSql;
  }

  public String insert(final Map<String, Object> params) {
    return new SQL() {
      {
        INSERT_INTO(table);
        for (String key : params.keySet()) {
          if (params.get(key) != null && operateColumnPropertyMapper.get(key) != null) {
            VALUES(key, "#{" + key + '}');
          }
        }
      }
    }.toString();
  }

  /**
   * <pre>
   * 获取根据主键删除数据的sql
   * </pre>
   * 
   * @return
   */
  public String delByKey() {
    if (delByKeyColumnSql == null) {
      delByKeyColumnSql = new SQL() {
        {
          DELETE_FROM(table)
              .WHERE(keyColumnName + "=#{" + columnPropertyMapper.get(keyColumnName) + '}');
        }
      }.toString();
    }
    return delByKeyColumnSql;
  }

  /**
   * <pre>
   * 根据主键修改数据
   * </pre>
   * 
   * @param params
   * @return
   */
  public String updateByKey(final Map<String, String> params) {
    return new SQL() {
      {
        UPDATE(table);
        for (String key : params.keySet()) {
          if (params.get(key) != null && operateColumnPropertyMapper.get(key) != null
              && key != keyColumnName) {
            SET(key + "=#{" + key + '}');
          }
        }
        WHERE(keyColumnName + "=#{" + keyColumnName + '}');
      }
    }.toString();
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
  public String selectByColumn(final Map<String, Object> params) {
    return new SQL() {
      {
        SQL sql = SELECT(queryList).FROM(table);
        if (params != null) {
          if (params.containsKey(keyColumnName)) {
            Object keys = params.get(keyColumnName);
            if (keys instanceof Collection) {
              String temp = keys.toString();
              temp = temp.substring(1, temp.length() - 1);
              sql.WHERE(keyColumnName + " in (" + temp + ')');
            } else {
              sql.WHERE(keyColumnName + "=#{" + keyColumnName + '}');
            }
          }
          String condition;
          for (String key : params.keySet()) {
            condition = conditionEQ(key, params);
            if (condition != null) {
              sql.AND().WHERE(condition);
            }
          }
        }
      }
    }.toString();
  }

  /**
   * <pre>
   * 根据主键集合查询数据
   * </pre>
   * 
   * @param keys
   * @return
   */
  public String selectByKeys(final Map<String, Collection<Serializable>> keys) {
    return new SQL() {
      {
        SQL sql = SELECT(queryList).FROM(table);
        String temp = keys.get(keyColumnName).toString();
        temp = temp.substring(1, temp.length() - 1);
        sql.WHERE(keyColumnName + " in (" + temp + ')');
      }
    }.toString();
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
  }

  protected void paramToColumnMapper(String paramName, String column) {
    paramToColumnMapper.put(paramName, column);
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
    if (builder.length() > 0)
      builder.setLength(builder.length() - 1);
    this.queryList = builder.toString();
    this.getByKeyColumnSql = null;
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
   * @param param
   * @return
   */
  protected final String conditionEQ(String column, Map<String, ? extends Object> param) {
    return conditionWrap(column, param, "=");
  }

  /**
   * <pre>
   * 生成 >= 条件
   * </pre>
   * 
   * @param column
   * @param param
   * @return
   */
  protected final String conditionGE(String column, Map<String, ? extends Object> param) {
    return conditionWrap(column, param, ">=");
  }

  /**
   * <pre>
   * 生成 <= 条件
   * </pre>
   * 
   * @param column
   * @param param
   * @return
   */
  protected final String conditionLE(String column, Map<String, ? extends Object> param) {
    return conditionWrap(column, param, "<=");
  }

  /**
   * <pre>
   * 包裹条件
   * </pre>
   * 
   * @param column
   * @param param
   * @param operate
   * @return
   */
  protected String conditionWrap(String column, Map<String, ? extends Object> param,
      String operate) {
    if (param.get(column) != null && columnPropertyMapper.get(column) != null
        && !column.equals(keyColumnName)) {
      return column + operate + "#{" + column + '}';
    }
    String tableColumn = paramToColumnMapper.get(column);
    if (tableColumn != null) {
      return tableColumn + operate + "#{" + column + '}';
    }
    return null;
  }
}
