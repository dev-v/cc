package com.cc.db.core.schema.type;

import java.util.ArrayList;
import java.util.List;

import com.cc.db.core.schema.bo.Column;
import com.cc.db.core.schema.bo.DialectEnum;
import com.cc.db.core.schema.bo.Table;
import com.cc.db.core.schema.bo.TypeEnum;
import com.cc.tool.BeanUtil;

/**
 * @author wenlongchen
 * @since Jan 23, 2017
 */
public class OracleToMysqlConventer extends AbstractConventer {

  @Override
  public Column convent(Column oldColumn) {
    Column column = BeanUtil.o2o(oldColumn, Column.class);
    TypeEnum bigType = column.getBigType();
    if (TypeEnum.number.is(bigType)) {
      int length = column.getLength();
      int presicion = column.getPrecision();
      int scale = column.getScale();
      if (presicion == 0 || scale == 0) {
        if (presicion != 0) {
          length = presicion;
        }
        if (length < 3) {
          column.setType("tinyint");
        } else if (length < 5) {
          column.setType("smallint");
        } else if (length < 7) {
          column.setType("mediumint");
        } else if (length < 10) {
          column.setType("int");
        } else {
          column.setType("bigint");
        }
      } else {
        column.setType("decimal");
      }
      column.setLength(length);
      column.setPrecision(scale);
    } else if (TypeEnum.varchar.is(bigType)) {
      column.setType("varchar");
      column.setLength(column.getCharLength());
    } else if (TypeEnum.character.is(bigType)) {
      column.setType("char");
    } else if (TypeEnum.datetime.is(bigType)) {
      column.setType("datetime");
    }

    return column;
  }

  @Override
  public Table convent(Table oldTable) {
    Table table = new Table();
    table.setDialect(DialectEnum.mysql);
    table.setName(oldTable.getName());

    List<Column> allColumns = new ArrayList<>();
    List<Column> columns = new ArrayList<>();
    List<Column> keyColumns = new ArrayList<>();
    table.setAllColumns(allColumns);
    table.setColumns(columns);
    table.setKeys(keyColumns);

    Column column;
    for (Column oldColumn : oldTable.getAllColumns()) {
      column = convent(oldColumn);
      if (column.isPrimary()) {
        keyColumns.add(column);
      } else {
        columns.add(column);
      }
      allColumns.add(column);
    }

    return table;
  }
}

