package com.cc.lucene;

import java.io.InputStream;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class IndexObj {

  public IndexObj(Object obj) {
    this.obj = obj;
  }

  public IndexObj() {}

  private Object obj;
  private InputStream[] inps;

  public Object getObj() {
    return obj;
  }

  public void setObj(Object obj) {
    this.obj = obj;
  }

  public InputStream[] getInps() {
    return inps;
  }

  public void setInps(InputStream[] inps) {
    this.inps = inps;
  }
}

