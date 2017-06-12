package com.cc.db.core.schema.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.cc.db.core.schema.bo.Table;
import com.cc.tool.Util;


/**
 * @author wenlongchen
 * @since Jan 22, 2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("dev")
public class SchemaServiceTest {
  private Logger log = Logger.getLogger(SchemaServiceTest.class);

  @Resource(name = "mainappDataSource")
  private DataSource mainappDataSource;

  @Resource(name = "dajobschemaDataSource")
  private DataSource dajobschemaDataSource;

  @Resource(name = "crmDataSource")
  private DataSource crmDataSource;

  @Resource(name = "wmDataSource")
  private DataSource wmDataSource;

  private SchemaService service;

  private Random random = new Random();

  @Test
  public void crm() {
    service = new MysqlSchemaService(crmDataSource);
    printTable("da_main_crm_trans");
  }

  @Test
  public void dajobbiz() {
    service = new MysqlSchemaService(dajobschemaDataSource);

    String sql;
    Table table = service.getTable("da_loan");
//    Table table = service.getTable("da_uniauth_user");
    sql = service.getInsertOrUpdateSqlForSpringBatchWithMysql(table);
    log.info(sql);
    
    printTable("da_loan");
  }

  @Test
  public void mainapp() {
    service = new OracleSchemaService(mainappDataSource);
    String str;
    // printTable("SL$ACTOR");
    // printTable("SL$LPAY");
    // printTable("SL$LOAN");
    // printTable("SL$LOAN_APP");
    // printTable("SL$LP");
    // printTable("SL$LPD");
    // printTable("SL$TRADE");
    // printTable("SL$TRANSACTION");
    // printTable("SL$VIRTUAL_LOAN");
    // printTable("SL$BD_CHANNEL");
    // printTable("GL_SL_TRANSACTION");
    // printTable("SL$ADDR");
    // printTable("SL$EMPLOYMENT");
    // printTable("SL$PI");
    // printTable("SL$GUARANTOR_PERSONAL");
    // printTable("SL$CONTACT");
    // printTable("SL$ACCOUNT");
    // printTable("SL$GUARANTOR_CORPORATE");


    // Table table = service.getTable("GL_SL_TRANSACTION");
    // str = service.getInsertOrUpdateSqlForSpringBatchWithOracle(table);
    // log.info(str);
    // str=service.getMaxRowscnSqlForOracle("GL_SL_TRANSACTION");
    // log.info(str);
    // str=service.getDataSqlOfMinMaxRowscnForOracle(table);
    // log.info(str);

    // printTableSchema("SL$ACTOR");
    // printTableSchema("SL$LPAY");
    // printTableSchema("SL$LOAN");
     printTableSchema("SL$LOAN_APP");
    // printTableSchema("SL$LP");
    // printTableSchema("SL$LPD");
    // printTableSchema("SL$TRADE");
    // printTableSchema("SL$TRANSACTION");
    // printTableSchema("SL$VIRTUAL_LOAN");
    // printTableSchema("SL$BD_CHANNEL");
    // printTableSchema("GL_SL_TRANSACTION");
    // printTableSchema("SL$ADDR");
    // printTableSchema("SL$EMPLOYMENT");
    // printTableSchema("SL$PI");
    // printTableSchema("SL$GUARANTOR_PERSONAL");
    // printTableSchema("SL$CONTACT");
    // printTableSchema("SL$ACCOUNT");
    // printTableSchema("SL$GUARANTOR_CORPORATE");

  }

  @Test
  public void wm() {
    service = new OracleSchemaService(wmDataSource);
    // printTable("SL$GUARANTOR_CORPORATE");
    printTableSchema("WM$WALLET");
  }

  @Test
  public void dajob() {
    service = new MysqlSchemaService(dajobschemaDataSource);
    // printTable("icrc_loan_info");
    // printTable("MI_PAY_PLAN");
    printTable("da_uniauth_seller_grp");


    // printTableSchema("icrc_loan_info");
  }

  @Test
  public void test() {
    getPreParams();
  }

  private void printTableSchema(String tableName) {
    Table table = service.getTable(tableName);
    String result = service.getSchemaSqlForMysql(table);
    if (tableName.contains("$")) {
      System.out.println(result.replace(tableName, "SLPROD_" + tableName.split("[$]")[1]));
    } else {
      System.out.println(result);
    }
  }

  private Collection<Map<String, String>> getPreParams() {
    String last = "lastExecutionTime";
    String current = "currentExecutionTime";
    Calendar curCalendar = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(Util.dateToTime("2010-01-01 00:00:00"));

    long everyTime = 1000 * 60 * 60 * 24 * 30;
    long maxTime = curCalendar.getTimeInMillis();
    long lastTime = calendar.getTimeInMillis();
    long curTime = 0;

    List<Map<String, String>> params = new ArrayList<>();
    do {

    } while (curTime > maxTime);
    System.out.println(Util.formatDate(calendar));
    return params;
  }

  private Collection<Map<String, String>> getPreIdParams() {
    String minId = "minId";
    String maxId = "maxId";

    // long everyCount=10000;
    // long maxTime=curCalendar.getTimeInMillis();
    // long lastTime=calendar.getTimeInMillis();
    // long curTime=0;

    List<Map<String, String>> params = new ArrayList<>();
    // do{
    //
    // }while(curTime>maxTime);
    // System.out.println(Util.formatDate(calendar));
    return params;
  }

  private void printTable(String tableName) {
    log.info("------------------ " + tableName + " --------------------------");
    Table table = service.getTable(tableName);
    //
    // log.info("-- MaxRowscn ForOracle");
    // log.info(service.getMaxRowscnSqlForOracle(table.getName()));
    //
    // log.info("-- MinMaxRowscn ForOracle");
    // log.info(service.getDataSqlOfMinMaxRowscnForOracle(table));
    //
    //
    // log.info("-- InsertOrUpdate ForMysql");
    // log.info(service.getInsertOrUpdateSqlForSpringBatchWithMysql(table));

    Map<String, String> oracle = new HashMap<>();
    oracle.put("name", "mainapp");

    Map<String, String> mysql = new HashMap<>();
    mysql.put("name", "dajob_bizdata");

    String newTableName;

    if (tableName.contains("$")) {
      newTableName = "SLPROD_" + tableName.split("[$]")[1];
    } else {
      newTableName = tableName;
    }
    Map<String, Object> batchJob = new HashMap<>();
    String jobName = "MAPPER_" + newTableName;
    batchJob.put("name", jobName);
    batchJob.put("preExecuteParams", "[{MINROWSCN:0}]");
    batchJob.put("isPre", "2");

    Map<String, Object> scheduleJob = new HashMap<>();
    scheduleJob.put("name", jobName + "_SCHEDULE");
    batchJob.put("scheduleJob", scheduleJob);

    Map<String, String> trigger = new HashMap<>();
    trigger.put("name", jobName + "_TRIGGER");
    trigger.put("type", "2");
    trigger.put("expression", random.nextInt(60) + " " + random.nextInt(60) + "/20 * * * ?");
    scheduleJob.put("trigger", trigger);

    Map<String, Object> listener = new HashMap<>();
    List<Map<String, Object>> listeners = new ArrayList<>();
    listeners.add(listener);
    batchJob.put("batchListeners", listeners);

    listener.put("afterType", 2);
    listener.put("afterOperate", "changeKey(MINROWSCN),store(MINROWSCN)[MAXROWSCN]");
    listener.put("beforeType", 1);
    Map<String, Object> beforeListenerOperate = new HashMap<>();
    beforeListenerOperate.put("name", jobName + "_MAX_ROWSCN");
    beforeListenerOperate.put("content", service.getMaxRowscnSqlForOracle(table.getName()));
    beforeListenerOperate.put("dbConfig", oracle);
    listener.put("beforeOperateConfig", beforeListenerOperate);

    Map<String, Object> task = new HashMap<>();
    task.put("name", jobName + "_TASK");
    task.put("batchSize", 1024);
    List<Map<String, Object>> tasks = new ArrayList<>();
    tasks.add(task);
    batchJob.put("batchTasks", tasks);

    Map<String, Object> taskForGetData = new HashMap<>();
    taskForGetData.put("name", jobName + "_DATAS");
    taskForGetData.put("content", service.getDataSqlOfMinMaxRowscnForOracle(table));
    taskForGetData.put("dbConfig", oracle);
    task.put("sourceOperate", taskForGetData);

    Map<String, Object> taskForInsert = new HashMap<>();
    taskForInsert.put("name", jobName + "_INSERT_OR_UPDATE");
    taskForInsert.put("content", service.getInsertOrUpdateSqlForSpringBatchWithMysql(table)
        .replace(tableName, newTableName));
    taskForInsert.put("dbConfig", mysql);
    task.put("targetOperate", taskForInsert);

    log.info(JSONObject.toJSONString(batchJob));
    log.info(JSONObject.toJSONString(beforeListenerOperate));
  }

  public static void main(String[] args) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(1490916337000l);
    System.out.println("1486973095000".length());
    System.out.println("1980-01-01 00:00:00".length());
    System.out.println(Util.formatDate(calendar));
  }
}

