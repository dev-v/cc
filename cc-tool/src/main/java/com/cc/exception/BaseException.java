package com.cc.exception;

import com.cc.exception.defaults.IllegalDefineException;

/**
 * <pre>
 * 通用业务异常定义
 * 
 * code代码范围定义：code代码必须为大于或等于500的整形数
 * 500 - 9999 为框架、工具代码、中间件等通用服务预留代码段 开发框架、中间件等通用组件在构建自定义异常架构时，需要预先申请自己的框架异常代码段
 * 10000 - Integer.MAX_VALUE 为业务服务包装异常可用的代码段，每个业务系统在建立业务系统自定义异常构架时，需要预先申请自己的业务异常代码段
 * </pre>
 * 
 * @author wenlongchen
 * @since Nov 4, 2016
 */
public abstract class BaseException extends RuntimeException {

  private static final long serialVersionUID = 6585823440055877896L;

  private int code;

  /**
   * @param code 异常代码
   * @param msg 异常消息
   * @param cause 引起该异常的其他异常
   * @see BaseException
   */
  public BaseException(int code, String msg, Throwable cause) {
    super(msg, cause);
    this.code = code;
    checkDefine();
  }

  /**
   * <pre>
   * cause为null
   * </pre>
   * 
   * @param code
   * @param msg
   * @see BaseException#BaseException(int, String, Throwable)
   */
  public BaseException(int code, String msg) {
    this(code, msg, null);
  }

  /**
   * <pre>
   * 返回异常代码
   * </pre>
   * 
   * @return
   */
  public int getCode() {
    return this.code;
  }

  /**
   * <pre>
   * 检查异常的定义是否合法
   * </pre>
   */
  private void checkDefine() {
    
    if (this instanceof FrameworkException) {
      if (code > 499 && code < 1000) {
        return;
      }
    }
    
    if (this instanceof BusinessException) {
      if (code > 999) {
        return;
      }
    }
    
    throw new IllegalDefineException("异常代码段定义错误或异常类型定义错误，框架异常需要继承FrameworkException，业务异常需要继承！");
  }
}
