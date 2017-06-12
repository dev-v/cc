package com.cc.db.core.schema.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cc.db.core.schema.bo.Column;
import com.cc.db.core.schema.bo.DialectEnum;
import com.cc.db.core.schema.bo.Table;
import com.cc.db.core.schema.bo.TypeEnum;
import com.cc.db.core.schema.type.Conventer;
import com.cc.db.core.schema.type.OracleToMysqlConventer;
import com.cc.db.core.source.DBConfigUtil;

/**
 * @author wenlongchen
 * @since Jan 22, 2017
 */
public abstract class AbstractSchemaService implements SchemaService {

  protected JdbcTemplate jdbcTemplate;

  private Conventer oracleToMysqlConventer = new OracleToMysqlConventer();

  public AbstractSchemaService(DataSource dataSource) {
    this.jdbcTemplate = DBConfigUtil.getJdbcTemplate(dataSource);
  }

  /**
   * 
   */
  @Override
  public String getSchemaSqlForMysql(Table table) {
    Table temp = table;
    if (DialectEnum.oracle.is(table.getDialect())) {
      temp = oracleToMysqlConventer.convent(table);
    }

    //开始
    StringBuilder sb = new StringBuilder("CREATE TABLE ");
    sb.append(temp.getName());
    sb.append("(");
    
    //列
    for (Column column : temp.getAllColumns()) {
      if (!TypeEnum.other.is(column.getBigType())) {
        sb.append("\r\n  ");
        sb.append(getSchemaSqlForMysql(column));
        sb.append(",");
      }
    }
    
    //时间戳列
    sb.append("\r\n  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,");
    
    //主键
    List<Column> keyColumns = temp.getKeys();
    if (keyColumns.size() > 0) {
      sb.append("\r\n  PRIMARY KEY(");
      for (Column column : keyColumns) {
        sb.append(column.getName());
        sb.append(",");
      }
      sb.setLength(sb.length() - 1);
      sb.append(")");
    } else {
      sb.setLength(sb.length() - 1);
    }
    
    //索引
    sb.append(",\r\n");
    sb.append("  KEY `update_at` (`updated_at`)");
    
    //结束
    sb.append("\r\n);\r\n");
    return sb.toString();
  }

  private String getSchemaSqlForMysql(Column column) {
    TypeEnum type = column.getBigType();
    String sql = "`" + column.getName() + "`  " + column.getType();
    if (TypeEnum.varchar.is(type) || TypeEnum.character.is(type)) {
      sql += "(" + column.getLength() + ")";
    } else if (TypeEnum.number.is(type) && column.getType().equals("decimal")) {
      sql += "(" + column.getLength() + "," + column.getPrecision() + ")";
    }

    if ("N".equalsIgnoreCase(column.getNullable())) {
      sql += "  NOT NULL";
    }
    return sql;
  }

  @Override
  public String getInsertOrUpdateSqlForSpringBatchWithMysql(Table table) {
    StringBuilder sb = new StringBuilder("INSERT INTO ");
    sb.append(table.getName());
    sb.append(" \r\n  (");
    for (Column column : table.getAllColumns()) {
      if (!TypeEnum.other.is(column.getBigType())) {
        sb.append("`"+column.getName() + "`,");
      }
    }
    sb.setLength(sb.length() - 1);
    sb.append(") \r\n");
    sb.append("VALUES \r\n  (");
    for (Column column : table.getAllColumns()) {
      if (!TypeEnum.other.is(column.getBigType())) {
        sb.append(":" + column.getName() + ",");
      }
    }
    sb.setLength(sb.length() - 1);
    sb.append(") \r\nON DUPLICATE KEY UPDATE ");
    for (Column column : table.getColumns()) {
      if (!TypeEnum.other.is(column.getBigType())) {
        sb.append("\r\n  `" + column.getName() + "`=" + "VALUES(`" + column.getName() + "`),");
      }
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }
  
  @Override
  public String getInsertOrUpdateSqlForSpringBatchWithOracle(Table table) {
    String tableAlias="DA_T";
    String tableName=table.getName();
    String columnName;
    StringBuilder sb = new StringBuilder("MERGE INTO ");
    sb.append(table.getName());
    sb.append(" USING(SELECT \r\n  ");
    //查询列
    for(Column column:table.getAllColumns()){
      if(TypeEnum.other.is(column.getBigType())){
        continue;
      }
      columnName=column.getName();
      sb.append(":"+columnName);
      sb.append(" "+columnName);
      sb.append(",");
    }
    sb.setLength(sb.length()-1);
    sb.append("\r\n");
    sb.append("FROM DUAL) ");
    sb.append(tableAlias);

    sb.append(" ON \r\n  (");
    //主键条件
    for(Column column:table.getKeys()){
      if(TypeEnum.other.is(column.getBigType())){
        continue;
      }
      columnName=column.getName();
      sb.append(tableName+"."+columnName);
      sb.append(" = ");
      sb.append(tableAlias+"."+columnName);
      sb.append(",");
    }
    sb.setLength(sb.length()-1);
    sb.append(")\r\n");
    
    sb.append("WHEN MATCHED THEN UPDATE SET \r\n  ");
    
    //set 列
    for(Column column:table.getColumns()){
      if(TypeEnum.other.is(column.getBigType())){
        continue;
      }
      columnName=column.getName();
      sb.append(columnName);
      sb.append(" = ");
      sb.append(tableAlias+"."+columnName);
      sb.append(",");
    }
    sb.setLength(sb.length()-1);
    
    sb.append("\r\n");
    sb.append("WHEN NOT MATCHED THEN INSERT \r\n  (");
    //列举列
    for(Column column:table.getAllColumns()){
      if(TypeEnum.other.is(column.getBigType())){
        continue;
      }
      columnName=column.getName();
      sb.append(columnName);
      sb.append(",");
    }
    sb.setLength(sb.length()-1);
    sb.append(") \r\n");
    
    sb.append("VALUES \r\n  (");
    //列举插入列
    for(Column column:table.getAllColumns()){
      if(TypeEnum.other.is(column.getBigType())){
        continue;
      }
      columnName=column.getName();
      sb.append(tableAlias+'.'+columnName);
      sb.append(",");
    }
    sb.setLength(sb.length()-1);
    sb.append(")");
    
    return sb.toString();
  }

  @Override
  public String getMaxRowscnSqlForOracle(String tablename) {
    return "SELECT MAX(ORA_ROWSCN) maxRowscn FROM " + tablename;
  }

  @Override
  public String getDataSqlOfMinMaxRowscnForOracle(Table table) {
    StringBuilder sb = new StringBuilder("SELECT ");

    for (Column column : table.getAllColumns()) {
      if (!TypeEnum.other.is(column.getBigType())) {
        sb.append(column.getName());
        sb.append(',');
      }
    }
    sb.setLength(sb.length() - 1);

    sb.append(" FROM ");
    sb.append(table.getName());
    sb.append(" WHERE ORA_ROWSCN > #{MINROWSCN} AND ORA_ROWSCN <= #{MAXROWSCN} ");

    // sb.append("ORDER BY ");
    // for(Column column:table.getKeys()){
    // sb.append(column.getName());
    // sb.append(',');
    // }
    // sb.setLength(sb.length()-1);

    return sb.toString();
  }
}

