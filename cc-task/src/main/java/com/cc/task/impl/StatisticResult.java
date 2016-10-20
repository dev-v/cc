package com.cc.task.impl;

import java.io.Serializable;

/**
 * @author wenlongchen
 * @since May 30, 2016
 */
public class StatisticResult implements Serializable {

  private static final long serialVersionUID = -4508400475745949925L;

  /**
   * 此统计量为第几次统计 从0开始
   */
  private int order;

  /**
   * 第n次统计时 当前时间 单位毫秒
   */
  private long curTime;

  /**
   * 第n次统计任务时 任务所花费的时间 单位毫秒
   */
  private long useTime;

  /**
   * 第n次统计时 剩余任务量
   */
  private int remainTaskCount;

  /**
   * 第n次统计时 所处理的任务量
   */
  private int handleTaskCount;

  /**
   * 前一次统计结果 第一次时 为null
   */
  // private StatisticResult prevStaticResult;

  /**
   * <pre>
   * 创建一个统计结果
   * </pre>
   * 
   * @param remainTaskCount 剩余任务量
   */
  protected StatisticResult(int remainTaskCount, StatisticResult prevStaticResult) {
    this.curTime = System.currentTimeMillis();
    this.remainTaskCount = remainTaskCount;
    if (prevStaticResult == null) {
      this.order = 0;
      this.useTime = 0;
      this.handleTaskCount = 0;
    } else {
      this.order = prevStaticResult.getOrder() + 1;
      this.useTime = this.curTime - prevStaticResult.getCurTime();
      this.handleTaskCount = prevStaticResult.getRemainTaskCount() - this.remainTaskCount;
    }
  }

  public int getOrder() {
    return order;
  }

  public long getCurTime() {
    return curTime;
  }

  public int getRemainTaskCount() {
    return remainTaskCount;
  }

  public long getUseTime() {
    return useTime;
  }

  public int getHandleTaskCount() {
    return handleTaskCount;
  }
}

