package com.cc.lucene;

import java.io.IOException;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public interface IndexManagerApi {

  /**
   * <pre>
   * 根据lucene配置文件中的doc元素中obj配置的名称获取lucene引擎
   * </pre>
   * 
   * @param doc_obj
   * @return
   */
  IndexApi getIndexApi(String doc_obj) throws IOException;

  /**
   * <pre>
   * 根据lucene配置文件中的doc元素中obj配置的名称及自定义autoKey获取lucene引擎
   * </pre>
   * 
   * @param doc_obj
   * @return
   */
  IndexApi getIndexApi(String doc_obj, String autoKey) throws IOException;
}

