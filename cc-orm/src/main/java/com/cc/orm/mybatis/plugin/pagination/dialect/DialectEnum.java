package com.cc.orm.mybatis.plugin.pagination.dialect;

import com.cc.exception.defaults.IllegalDefineException;

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
    if(dialectEnum==null){
      throw new IllegalDefineException("数据库方言【 "+dialect+" 】设置错误，请使用字符串mysql或oracle！");
    }
    return dialectEnum.getDialect();
  }
  
  public static void main(String[] args) {
    System.out.println(getDialect("aaa"));
  }
}