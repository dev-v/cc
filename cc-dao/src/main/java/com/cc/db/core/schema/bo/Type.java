package com.cc.db.core.schema.bo;

/**
 * @author wenlongchen
 * @since Jan 23, 2017
 */
public class Type {
  /**
   * 大类型 数字、字符、二进制等
   */
  private TypeEnum bigType;
  private String name;
  private int length;
  private int precision;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public TypeEnum getBigType() {
    return bigType;
  }

  public void setBigType(TypeEnum bigType) {
    this.bigType = bigType;
  }

  public int getPrecision() {
    return precision;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

}

