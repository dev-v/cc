package com.cc.db.core.schema.bo;

import com.cc.tool.Util;

/**
 * @author wenlongchen
 * @since Jan 23, 2017
 */
public enum TypeEnum {
  number, varchar, character, datetime, binary, other;

  public static TypeEnum getForType(String type) {
    if (Util.endsIgnoreCase(type,"int")||Util.equalsIgnoreCase(type, "number","decimal","double","float")) {
      return TypeEnum.number;
    } else if (Util.startsIgnoreCase(type, "VARCHAR")) {
      return TypeEnum.varchar;
    } else if (Util.startsIgnoreCase(type, "CHAR")) {
      return TypeEnum.character;
    } else if (Util.startsIgnoreCase(type, "DATE") || Util.startsIgnoreCase(type, "time")) {
      return TypeEnum.datetime;
    }
    return TypeEnum.other;
  }

  public boolean is(TypeEnum type) {
    return this == type;
  }
}

