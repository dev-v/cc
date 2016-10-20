package com.cc.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * @author wenlongchen
 * @since May 30, 2016
 */
public abstract class AbstractTask<T> implements ITask<T> {

  private static final Logger logger = Logger.getLogger(AbstractTask.class);

  /**
   * 多线程执行时 要获取的任务容器
   */
  private LinkedBlockingQueue<T> tasks;

  private LinkedBlockingQueue<T> failureTasks;

  private List<T> handledTasks;

  /**
   * 任务标题
   */
  private final String title;

  /**
   * 原始任务
   */
  private Collection<T> originTasks;

  /**
   * @param title 任务标题
   * @param tasks 任务列表
   */
  public AbstractTask(String title, Collection<T> tasks) {
    this.originTasks = tasks;
    this.title = title;
    this.tasks = new LinkedBlockingQueue<T>();
    this.handledTasks = new ArrayList<T>();
    this.failureTasks = new LinkedBlockingQueue<T>();
    this.resetTask();
  }

  /**
   * <pre>
   * 重置任务，以便容器重新执行
   * </pre>
   */
  private void resetTask() {
    logger.info("初始化任务队列 begin");

    long time = System.currentTimeMillis();
    if (originTasks != null) {
      this.tasks.clear();
      this.handledTasks.clear();
      this.tasks.addAll(originTasks);
    }

    logger.info("初始化任务队列 end 用时（ms）：" + (System.currentTimeMillis() - time));
  }

  @Override
  public T getTask() {
    return tasks.poll();
  }

  @Override
  public void addFailureTask(T taks) {
    try {
      failureTasks.put(taks);
    } catch (InterruptedException e) {
      logger.info(e);
    }
  }

  @Override
  public int size() {
    return tasks.size();
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public void handled(T task) {
    this.handledTasks.add(task);
  }

  @Override
  public void failureHandle(T task) {
    // default do nothing
  }

  @Override
  public T getFailureTask() {
    return failureTasks.poll();
  }

  @Override
  public void checked() {
    logger.info("检查任务执行情况 begin");
    T t;
    Set<T> tempSet = new HashSet<T>();
    // 没有执行或执行失败的任务
    Set<T> notExecuteSet = new HashSet<T>();
    // 重复执行的任务
    Set<T> repeatExecuteSet = new HashSet<T>();
    for (int i = 0, len = handledTasks.size(); i < len; i++) {
      if (!originTasks.contains(t = handledTasks.get(i))) {
        notExecuteSet.add(t);
      }
      if (tempSet.contains(t)) {
        repeatExecuteSet.add(t);
      }
      tempSet.add(t);
    }

    if (notExecuteSet.size() > 0) {
      logger.info("没有执行或执行失败的任务：\r\n" + notExecuteSet);
    }

    if (repeatExecuteSet.size() > 0) {
      logger.info("重复执行的任务：\r\n" + repeatExecuteSet);
    }
    logger.info("检查任务执行情况 end");
  }
}

