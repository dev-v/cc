package com.cc.task.impl;

/**
 * @author wenlongchen
 * @since May 30, 2016
 */
public interface IExecuter {

  /**
   * <pre>
   * 获取执行结果 
   * 该方法将阻塞调用该方法的线程 知道任务执行完成返回任务执行结果
   * </pre>
   * @return
   */
  Result getResult();

  /**
   * <pre>
   * 提交任务
   * </pre>
   * 
   * @param task
   * @return
   */
//  Result submit(ITask<? extends Object> task);

  /**
   * <pre>
   * 启动容器
   * </pre>
   */
//  void startup();

  /**
   * <pre>
   * 停止容器
   * </pre>
   */
//  void shutdown();
  
  
}
