package com.cc.db.core.schema.bo;


/**
 * @author wenlongchen
 * @since Jan 23, 2017
 */
public enum DialectEnum {
  oracle,
  mysql;

  public boolean is(DialectEnum dialect) {
    return this == dialect;
  }
}

