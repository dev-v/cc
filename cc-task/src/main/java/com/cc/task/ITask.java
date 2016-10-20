package com.cc.task;

/**
 * @author wenlongchen
 * @since May 30, 2016
 */
public interface ITask<T> {

  /**
   * <pre>
   * 具体处理任务的业务
   * </pre>
   * 
   * @param task
   */
  void handle(T task);

  /**
   * <pre>
   * 处理完成的任务
   * </pre>
   * @param task
   */
  void handled(T task);

  /**
   * <pre>
   * 处理失败的任务
   * </pre>
   * @param task
   */
  void failureHandle(T task);

  /**
   * <pre>
   * 对任务执行情况进行检查
   * </pre>
   */
  void checked();

  /**
   * <pre>
   * 获取剩余任务数量 必须线程安全的
   * </pre>
   * 
   * @return
   */
  int size();

  /**
   * <pre>
   * 获取一个任务 必须线程安全的
   * </pre>
   * 
   * @return
   */
  T getTask();

  /**
   * <pre>
   * 新增任务
   * 在任务处理失败时 允许重新加入任务
   * 线程安全的
   * </pre>
   * 
   * @param taks
   */
  void addFailureTask(T taks);

  /**
   * <pre>
   * 获取失败的任务
   * </pre>
   * 
   * @return
   */
  T getFailureTask();

  /**
   * <pre>
   * 获取任务标题
   * </pre>
   * 
   * @return
   */
  String getTitle();
}
