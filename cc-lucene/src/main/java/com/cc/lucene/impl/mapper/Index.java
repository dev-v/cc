package com.cc.lucene.impl.mapper;


/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class Index {

  String type;
  String field;
  boolean store;
  
  public Index(String field,String type,boolean store){
    this.field=field;
    this.type=type;
    this.store=store;
  }

  public String getType() {
    return type;
  }

  public String getField() {
    return field;
  }

  public boolean isStore() {
    return store;
  }
}

