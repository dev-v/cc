package com.cc.exception.defaults;

import com.cc.exception.FrameworkException;

/**
 * @author wenlongchen
 * @since Nov 4, 2016
 */
public class IllegalDefineException extends FrameworkException {

  private static final long serialVersionUID = 8749688966400729038L;

  public IllegalDefineException() {
    this("信息定义错误！");
  }

  public IllegalDefineException(String msg) {
    super(502,msg);
  }
}

