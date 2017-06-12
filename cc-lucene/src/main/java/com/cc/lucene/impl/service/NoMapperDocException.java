package com.cc.lucene.impl.service;


/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class NoMapperDocException extends RuntimeException {

  private static final String msg = "";

  protected NoMapperDocException(String doc_obj) {
    super(msg + doc_obj);
  }

  private static final long serialVersionUID = 6610397506235274084L;
}

