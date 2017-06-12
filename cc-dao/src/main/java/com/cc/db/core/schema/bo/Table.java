package com.cc.db.core.schema.bo;

import java.util.List;

/**
 * @author wenlongchen
 * @since Jan 22, 2017
 */
public class Table {
  private String name;
  private DialectEnum dialect;
  private List<Column> keys;
  private List<Column> columns;
  private List<Column> allColumns;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Column> getKeys() {
    return keys;
  }

  public void setKeys(List<Column> keys) {
    this.keys = keys;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }

  public List<Column> getAllColumns() {
    return allColumns;
  }

  public void setAllColumns(List<Column> allColumns) {
    this.allColumns = allColumns;
  }

  public DialectEnum getDialect() {
    return dialect;
  }

  public void setDialect(DialectEnum dialect) {
    this.dialect = dialect;
  }

}

