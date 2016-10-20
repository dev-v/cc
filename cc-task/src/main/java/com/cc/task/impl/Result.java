package com.cc.task.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import org.apache.log4j.Logger;

import com.cc.tool.Util;

/**
 * @author wenlongchen
 * @since May 30, 2016
 */
public class Result implements Serializable {

  private static final long serialVersionUID = 2263092147441452660L;

  private static final Logger logger = Logger.getLogger(Result.class);

  /**
   * 统计结果集
   */
  List<StatisticResult> statisticResults;

  /**
   * 每隔多久统计一次数据 单位纳秒
   */
  private final long cycleTime;

  /**
   * 总任务量
   */
  private long totalTask;

  /**
   * 总用时
   */
  private long totalTime;

  /**
   * 使用的线程数量
   */
  private final int threadSize;

  /**
   * 任务标题
   */
  private final String taskTitle;

  /**
   * 创建一个统计结果集
   * 
   * @param taskTitle 任务标题
   * @param threadSize 线程数量
   * @param cycleTime 毫秒
   */
  public Result(String taskTitle, int totalTask, int threadSize, long cycleTime) {
    this.taskTitle = taskTitle;
    this.totalTask = totalTask;
    this.threadSize = threadSize;
    this.cycleTime = Util.timeMili2Nano(cycleTime);
  }

  /**
   * <pre>
   * 进行一次统计
   * 统计完成后 将阻塞执行该方法的线程cycleTime毫秒
   * </pre>
   * 
   * @param remainTaskCount 剩余任务量
   */
  public void statistic(int remainTaskCount) {
    logger.info("剩余任务量：" + remainTaskCount);
    StatisticResult cuResult;
    if (statisticResults == null) {
      statisticResults = new ArrayList<StatisticResult>();
      statisticResults.add(cuResult = new StatisticResult(remainTaskCount, null));
    } else {
      statisticResults.add(cuResult =
          new StatisticResult(remainTaskCount, statisticResults.get(statisticResults.size() - 1)));
    }
    totalTime = cuResult.getCurTime() - statisticResults.get(0).getCurTime();
    LockSupport.parkNanos(cycleTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("任务名称：" + taskTitle + "\r\n使用线程数量：" + threadSize
        + "\r\n统计时间间隔（毫秒）：" + Util.timeNano2Mili(cycleTime) + "\r\n统计结果：\r\n用时\t\t任务量\r\n");
    StatisticResult result;
    for (int i = 0, len = statisticResults.size(); i < len; i++) {
      result = statisticResults.get(i);
      sb.append(result.getUseTime() + "\t\t" + result.getHandleTaskCount() + "\r\n");
    }
    sb.append("totalTime(毫秒)：" + totalTime + "\t\ttaskCount：" + totalTask);
    sb.append("\r\n");
    return sb.toString();
  }

  public List<StatisticResult> getStatisticResults() {
    return statisticResults;
  }

  public long getCycleTime() {
    return cycleTime;
  }

  public long getTotalTask() {
    return totalTask;
  }

  public long getTotalTime() {
    return totalTime;
  }

  public int getThreadSize() {
    return threadSize;
  }

  public String getTaskTitle() {
    return taskTitle;
  }
}
