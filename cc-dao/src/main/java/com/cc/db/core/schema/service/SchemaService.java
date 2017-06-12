package com.cc.db.core.schema.service;

import com.cc.db.core.schema.bo.Table;

/**
 * @author wenlongchen
 * @since Jan 22, 2017
 */
public interface SchemaService {
  Table getTable(String tablename);
  
  /**
   * <pre>
   * 获取mysql语法的脚本
   * </pre>
   * @param table
   * @return
   */
  String getSchemaSqlForMysql(Table table);
  
  /**
   * <pre>
   * </pre>
   * @param table
   * @return
   */
  String getInsertOrUpdateSqlForSpringBatchWithMysql(Table table);

  String getInsertOrUpdateSqlForSpringBatchWithOracle(Table table);
  
  String getMaxRowscnSqlForOracle(String tablename);
  
  String getDataSqlOfMinMaxRowscnForOracle(Table table);
}

