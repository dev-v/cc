package com.cc.task;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.cc.task.impl.IExecuter;
import com.cc.task.impl.Result;

/**
 * @author wenlongchen
 * @since Oct 20, 2016
 */
public class AbstractTaskTest {

  @Test
  public void test() {
    Collection<String> tasks=new ArrayList<>();
    for(int i=0,len=1000;i<len;i++){
      tasks.add(String.valueOf(i));
    }
    TestTask task =new TestTask(tasks);
    
    IExecuter executer=Executers.execute(task);
    Result result=executer.getResult();
    System.out.println(result.toString());
  }

}

