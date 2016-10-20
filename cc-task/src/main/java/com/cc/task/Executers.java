package com.cc.task;

import com.cc.task.impl.Executer;
import com.cc.task.impl.IExecuter;

/**
 * @author wenlongchen
 * @since Oct 20, 2016
 */
public class Executers {
  /**
   * <pre>
   * 执行一个任务
   * 返回执行容器，通过执行容器可以获得执行结果
   * </pre>
   * @param task
   * @param threadSize 使用的线程数量
   * @param statisticIntervalTime 每隔多久统计一次任务执行情况（单位毫秒）
   * @return
   */
  public static <T> IExecuter execute(ITask<T> task,int threadSize,int statisticIntervalTime){
    Executer executer = new Executer(threadSize, statisticIntervalTime);
    executer.freshTask(task);
    executer.execute();
    return executer;
  }
  
  /**
   * <pre>
   * 默认统计时间间隔10000
   * </pre>
   * @param task
   * @param threadSize
   * @return
   * @see Executers#execute(ITask, int, int)
   */
  public static <T> IExecuter execute(ITask<T> task,int threadSize){
    return execute(task, threadSize, 10000);
  }
  
  /**
   * <pre>
   * 默认线程数：Runtime.getRuntime().availableProcessors()
   * </pre>
   * @param task
   * @return
   * @see Executers#execute(ITask, int)
   */
  public static <T> IExecuter execute(ITask<T> task){
    return execute(task, Runtime.getRuntime().availableProcessors());
  }
}

