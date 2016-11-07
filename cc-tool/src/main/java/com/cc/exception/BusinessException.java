package com.cc.exception;


/**
 * <pre>
 * 异常代码为10000 - Integer.MAX_VALUE
 * 其中10000-19999为已占用保留代码
 * </pre>
 * 
 * @author wenlongchen
 * @since Nov 4, 2016
 */
public abstract class BusinessException extends BaseException {

  private static final long serialVersionUID = -7000578683743571040L;

  /**
   * <pre>
   * 异常代码为10000 - Integer.MAX_VALUE
   * 其中10000-19999为已占用保留代码
   * </pre>
   * 
   * @param code
   * @param msg
   * @param cause
   */
  public BusinessException(int code, String msg, Throwable cause) {
    super(code, msg, cause);
  }

  /**
   * @param code
   * @param msg
   * @see BusinessException#BusinessException(int, String, Throwable)
   */
  public BusinessException(int code, String msg) {
    this(code, msg, null);
  }
}

