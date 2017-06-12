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
 * @since Jan 22, 2017
 */
public class OracleSchemaService extends AbstractSchemaService {

  public OracleSchemaService(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Table getTable(String tablename) {
    Table table = new Table();
    table.setDialect(DialectEnum.oracle);
    table.setName(tablename);

    String getKeysSql =
        "select col.column_name colName from user_constraints con,user_cons_columns col "
            + "where con.constraint_name = col.constraint_name " + "and con.constraint_type='P' "
            + "and col.table_name = ?";
    List<String> keys = jdbcTemplate.queryForList(getKeysSql, String.class, tablename);

    String getColumnsSql = "SELECT "
        + "COLUMN_NAME name,DATA_TYPE type,DATA_LENGTH length,CHAR_LENGTH charLength,DATA_PRECISION precision,DATA_SCALE scale,NULLABLE nullable "
        + "FROM user_tab_columns " + "WHERE TABLE_NAME=? ORDER BY COLUMN_ID";

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

