package com.cc.db.core.schema.bo;

import com.cc.tool.Util;

/**
 * @author wenlongchen
 * @since Jan 23, 2017
 */
public class Column {
  private String name;
  private String type;
  private TypeEnum bigType;
  private int length;
  private int charLength;
  private int precision;
  private int scale;
  private String nullable;
  private boolean primary;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
    if (this.bigType == null) {
      this.setBigType(TypeEnum.getForType(type));
    }
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getPrecision() {
    return precision;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public String getNullable() {
    return nullable;
  }

  public void setNullable(String nullable) {
    if (Util.startsIgnoreCase(nullable, "Y")) {
      this.nullable = "Y";
    } else {
      this.nullable = "N";
    }
  }

  public TypeEnum getBigType() {
    return bigType;
  }

  public void setBigType(TypeEnum bigType) {
    this.bigType = bigType;
  }

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(boolean primary) {
    this.primary = primary;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public int getCharLength() {
    return charLength;
  }

  public void setCharLength(int charLength) {
    this.charLength = charLength;
  }
}

