package com.cc.task.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.cc.task.ITask;

/**
 * @author wenlongchen
 * @since May 30, 2016
 */
public class Executer implements IExecuter{

  private static final Logger logger = Logger.getLogger(Executer.class);

  /**
   * 任务执行情况结果
   */
  private Result result;

  /**
   * 要执行的任务
   */
  private ITask<Object> task;

  /**
   * 线程数量
   */
  private int threadSize;

  /**
   * 线程池
   */
  private List<Thread> threads;

  /**
   * 当前活跃的线程数量
   */
  private volatile int activeThreads;

  /**
   * 是否处于运行状态
   */
  private boolean isExecuted;

  /**
   * 任务统计周期
   */
  private final long cycleTime;

  private Lock activeThreadsLock = new ReentrantLock();

  /**
   * @param threadSize 线程池大小
   * @param cycleTime 统计时间间隔（毫秒）
   */
  public Executer(int threadSize, long cycleTime) {
    threadSize = threadSize < 1 ? 1 : threadSize;
    this.threadSize = threadSize;
    this.cycleTime = cycleTime;
  }

  /**
   * 默认cycleTime为5000
   * 
   * @param threadSize
   */
  public Executer(int threadSize) {
    this(threadSize, 5000);
  }

  /**
   * 默认threadSize为20
   */
  public Executer() {
    this(20);
  }

  /**
   * 刷新任务
   * 
   * @param task 要执行的任务
   */
  @SuppressWarnings("unchecked")
  public void freshTask(ITask<? extends Object> task) {
    this.task = (ITask<Object>) task;
    this.resetState();
  }

  /**
   * 重置状态
   */
  private void resetState() {
    isExecuted = false;
    activeThreads = threadSize;
    result = new Result(task.getTitle(), task.size(), threadSize, cycleTime);
  }

  /**
   * 执行任务
   */
  public void execute() {
    logger.info("开始执行任务...");
    if (task == null) {
      return;
    }
    threads = new ArrayList<Thread>(threadSize);
    for (int i = 0; i < threadSize; i++) {
      threads.add(new Thread(new Runnable() {
        public void run() {
          // 处理正常任务
          Object t = task.getTask();
          while (t != null) {
            try {
              task.handle(t);
              task.handled(t);
            } catch (Exception e) {
              logger.error(e);
              task.addFailureTask(t);
            }
            t = task.getTask();
          }

          // 处理失败任务
          logger.info("开始处理失败任务：" + Thread.currentThread());
          t = task.getFailureTask();
          while (t != null) {
            try {
              task.failureHandle(t);
            } catch (Exception e) {
              logger.error(e);
            }
            t = task.getFailureTask();
          }

          activeThreadsLock.lock();
          --activeThreads;
          activeThreadsLock.unlock();
        }
      }));
    }
    result.statistic(task.size());
    for (Thread thread : threads) {
      thread.start();
    }
    this.isExecuted = true;
  }

  /**
   * 获取任务执行时间，任务未执行完成时，将进行阻塞
   * 
   * @return
   */
  @Override
  public Result getResult() {
    if (isExecuted) {
      while (true) {
        activeThreadsLock.lock();
        if (activeThreads == 0) {
          activeThreadsLock.unlock();
          result.statistic(task.size());
          break;
        }
        activeThreadsLock.unlock();
        result.statistic(task.size());
      }
      return result;
    }
    throw new IllegalStateException("任务还未开始执行！");
  }
}

