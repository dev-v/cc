package com.cc.lucene;


/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class IndexPage {

  /**
   * 总量
   */
  private long total;

  /**
   * 分页查询到的量
   */
  private long[] keys;

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public long[] getKeys() {
    return keys;
  }

  public void setKeys(long[] keys) {
    this.keys = keys;
  }
}

