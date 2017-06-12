package com.cc.db.core.schema.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.cc.db.core.schema.bo.Column;
import com.cc.db.core.schema.bo.DialectEnum;
import com.cc.db.core.schema.bo.Table;
import com.cc.tool.BeanUtil;

/**
 * @author wenlongchen
 * @since Feb 4, 2017
 */
public class MysqlSchemaService extends AbstractSchemaService {

  public MysqlSchemaService(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Table getTable(String tablename) {
    Table table = new Table();
    table.setDialect(DialectEnum.mysql);
    table.setName(tablename);

    String getKeysSql = "select column_name colName from KEY_COLUMN_USAGE where table_name = ?";
    List<String> keys = jdbcTemplate.queryForList(getKeysSql, String.class, tablename);

    String getColumnsSql = "SELECT "
        + "COLUMN_NAME name,IS_NULLABLE nullable,DATA_TYPE type,CHARACTER_MAXIMUM_LENGTH length "
        + "FROM COLUMNS " + "WHERE TABLE_NAME=? ORDER BY ORDINAL_POSITION";

    List<Map<String, Object>> maps = jdbcTemplate.queryForList(getColumnsSql, tablename);

    List<Column> allColumns = BeanUtil.o2o(maps, Column.class);
    table.setAllColumns(allColumns);
    List<Column> columns = new ArrayList<>();
    List<Column> keyColumns = new ArrayList<>(keys.size());
    for (Column column : allColumns) {
      if (keys.contains(column.getName())) {
        column.setPrimary(true);
        keyColumns.add(column);
      } else {
        columns.add(column);
      }
    }
    table.setKeys(keyColumns);
    table.setColumns(columns);

    return table;
  }

}

