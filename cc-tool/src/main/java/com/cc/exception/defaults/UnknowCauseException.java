package com.cc.exception.defaults;

import com.cc.exception.FrameworkException;

/**
 * @author wenlongchen
 * @since Nov 4, 2016
 */
public class UnknowCauseException extends FrameworkException {
  
  private static final long serialVersionUID = -4300507908714590018L;

  /**
   * @param cause
   * @see UnknowCauseException#UnknowException(String, Throwable)
   */
  public UnknowCauseException(Throwable cause) {
    this("未知错误！",cause);
  }

  /**
   * <pre>
   * 用于包装第三方框架、服务、工具或其他非自定义异常为自定义异常
   * 如 nullpointer、sqlexception、ioexception、springexception、mybatisexception等
   * </pre>
   * @param cause
   */
  public UnknowCauseException(String msg,Throwable cause) {
    super(501,msg,cause);
  }
}

