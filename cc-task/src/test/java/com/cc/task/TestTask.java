package com.cc.task;

import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * @author wenlongchen
 * @since Oct 20, 2016
 */
public class TestTask extends AbstractTask<String> {
  private static final Logger log=Logger.getLogger(TestTask.class);

  public TestTask(Collection<String> tasks) {
    super("测试任务", tasks);
  }

  @Override
  public void handle(String task) {
    log.info(task);
    task+="***************************";
    log.info(task);
  }

}

