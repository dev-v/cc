package com.cc.exception;


/**
 * <pre>
 * 异常代码为500-9999
 * 其中500-599为已占用保留异常代码
 * </pre>
 * 
 * @author wenlongchen
 * @since Nov 4, 2016
 */
public abstract class FrameworkException extends BaseException {

  private static final long serialVersionUID = 3117586896659240947L;

  /**
   * <pre>
   * 异常代码为500-9999
   * 其中500-599为已占用保留异常代码
   * </pre>
   * 
   * @param code
   * @param msg
   * @param cause
   */
  public FrameworkException(int code, String msg, Throwable cause) {
    super(code, msg, cause);
  }

  /**
   * @param code
   * @param msg
   * @see FrameworkException#FrameworkException(int, String, Throwable)
   */
  public FrameworkException(int code, String msg) {
    this(code, msg, null);
  }
}
