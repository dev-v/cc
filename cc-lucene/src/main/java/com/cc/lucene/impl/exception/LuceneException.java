package com.cc.lucene.impl.exception;

import com.cc.exception.FrameworkException;

/**
 * <pre>
 * lucene异常代码段为600-609
 * </pre>
 * @author wenlongchen
 * @since Nov 8, 2016
 */
public abstract class LuceneException extends FrameworkException {

  private static final long serialVersionUID = 3397961664854371635L;

  public LuceneException(int code, String msg) {
    super(code, msg);
  }

}

