package com.cc.exception.defaults;

import com.cc.exception.FrameworkException;

/**
 * @author wenlongchen
 * @since Nov 4, 2016
 */
public class UnknowException extends FrameworkException {

  private static final long serialVersionUID = -3805141739471674998L;

  /**
   * @see UnknowException#UnknowBusinessException(String)
   */
  public UnknowException() {
    this("未知错误！");
  }

  /**
   * <pre>
   * 一个普通业务异常
   * </pre>
   */
  public UnknowException(String msg) {
    super(500,"未知错误！");
  }

}

