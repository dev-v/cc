package com.cc.lucene.impl.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cc.exception.defaults.IllegalDefineException;
import com.cc.lucene.IndexApi;
import com.cc.lucene.IndexManagerApi;
import com.cc.lucene.impl.mapper.Doc;
import com.cc.lucene.impl.mapper.Lucene;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class IndexMangerImpl implements IndexManagerApi {

  private static final Map<String, IndexApi> indexApies = new HashMap<String, IndexApi>();

  private static final Map<String, Doc> docMap = new HashMap<String, Doc>();


  @Override
  public IndexApi getIndexApi(String doc_obj) throws IOException {
    IndexApi api = indexApies.get(doc_obj);
    if (api == null) {
      Doc doc = docMap.get(doc_obj);
      if (doc == null)
        throw new NoMapperDocException(doc_obj);
      indexApies.put(doc_obj, api = new IndexImpl(doc));
    }
    return api;
  }

  @Override
  public IndexApi getIndexApi(String doc_obj, String autoKey) throws IOException {
    String key = doc_obj + autoKey;
    IndexApi api = indexApies.get(key);
    if (api == null) {
      Doc doc = docMap.get(doc_obj);
      if (doc == null)
        throw new IllegalDefineException("没有在lucene.xml中配置doc元素:" + doc_obj);
      doc.setObj(key);
      indexApies.put(key, api = new IndexImpl(doc));
    }
    return api;
  }

  /**
   * <pre>
   * 设置lucene配置文件和索引文件存储路径
   * </pre>
   * 
   * @param luceneConfigPath
   * @param luceneStorePath
   */
  public void setLuceneStorePath(String luceneConfigPath, String luceneStorePath) {
    Lucene lucene = new Lucene(luceneConfigPath, luceneStorePath + File.separatorChar + "lucene");
    Doc[] docs = lucene.getDocs();
    Doc doc;
    for (int i = 0, len = docs.length; i < len; i++) {
      doc = docs[i];
      docMap.put(doc.getObj(), doc);
    }
  }
}

