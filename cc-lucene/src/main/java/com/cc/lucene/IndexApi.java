package com.cc.lucene;

import java.io.InputStream;
import java.util.Collection;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public interface IndexApi {


  /**
   * 建立索引
   * 
   * @param obj 要索引的对象（只索引配置文件中配置了的index元素，obj中的字段对应元素的field属性）
   * @param inps 将流加入到索引中
   */
  void createIndex(Object obj, InputStream[] inps);

  /**
   * 建立索引
   * 
   * @param obj要索引的对象（只索引配置文件中配置了的index元素，obj中的字段对应元素的field属性）
   * @author zhangdongdong
   * @date 2014年12月10日
   */
  void createIndex(Object obj);

  /**
   * 批量建立索引时推荐使用该方法 已提高性能
   * 
   * @param obj 要索引的对象（只索引配置文件中配置了的index元素，obj中的字段对应元素的field属性）
   */
  void createIndexes(Collection<IndexObj> objs);

  /**
   * 更改索引
   * 
   * @param obj 要索引的对象（只索引配置文件中配置了的index元素，obj中的字段对应元素的field属性）
   * @param inps 将流加入到索引中（带有流的修改将重建该条数据的索引，所以修改时要将该条记录对应的所有附件加入到参数中）
   */
  void updateIndex(Object obj, InputStream... inps);

  /**
   * 批量更改索引时，推荐使用该方法 已提高性能
   * 
   * @param obj 要索引的对象（只索引配置文件中配置了的index元素，obj中的字段对应元素的field属性）
   * @param inps 将流加入到索引中（带有流的修改将重建该条数据的索引，所以修改时要将该条记录对应的所有附件加入到参数中）
   */
  void updateIndexes(Collection<IndexObj> objs);

  /**
   * 删除索引
   * 
   * @param keyFieldValue 配置文件中doc元素的keyField属性的值，使用数据库表时，推荐使用表主键
   */
  void deleteIndex(long keyFieldValue);

  /**
   * 删除索引
   * 
   * @param keyFieldValue 配置文件中doc元素的keyField属性的值，使用数据库表时，推荐使用表主键
   */
  void deleteIndex(Collection<Long> keyFieldValue);

  void deleteAll();

  /**
   * 查询数据
   * 
   * @param words 查询关键词，多个词用空格分隔
   * @return 返回建立或删除索引传递的 long objKey值数组，使用数据库时，常为表主键
   */
  long[] search(String words);

  /**
   * 查询数据
   * 
   * @param words 查询关键词，多个词用空格分隔
   * @return 返回建立或删除索引传递的 long objKey值数组，使用数据库时，常为表主键
   */
  long[] search(String words, int count);

  /**
   * 分页查询数据
   * 
   * @param words 查询关键词，多个词用空格分隔
   * @param begin 起始行，从0开始
   * @param count 返回的数量
   * @return 返回建立或删除索引传递的 long objKey值数组，使用数据库时，常为表主键
   */
  IndexPage search(String words, int begin, int count);
}

