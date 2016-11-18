package com.cc.orm.mybatis.plugin.pagination.dialect;

/**
 * @author wenlongchen
 * @since Nov 4, 2016
 */
public enum DialectEnum {
  mysql(new MySqlDialect()),  
  oracle(new OracleDialect());
  
  private AbstractDialect dialect;

  private DialectEnum(AbstractDialect dialect) {
    this.dialect = dialect;
  }
  
  public AbstractDialect getDialect(){
    return dialect;
  }
  
  public static AbstractDialect getDialect(String dialect){
    DialectEnum dialectEnum=DialectEnum.valueOf(dialect);
    return dialectEnum.getDialect();
  }
}