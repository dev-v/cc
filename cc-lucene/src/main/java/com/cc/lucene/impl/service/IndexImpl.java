package com.cc.lucene.impl.service;

import java.awt.TextField;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FieldType.LegacyNumericType;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.function.valuesource.LongFieldSource;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.queryparser.xml.QueryBuilderFactory;
import org.apache.lucene.queryparser.xml.builders.LegacyNumericRangeQueryBuilder;
import org.apache.lucene.queryparser.xml.builders.RangeQueryBuilder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.LegacyNumericRangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.NoLockFactory;
import org.apache.lucene.util.QueryBuilder;
import org.apache.lucene.util.Version;

import com.cc.lucene.IndexApi;
import com.cc.lucene.IndexObj;
import com.cc.lucene.IndexPage;
import com.cc.lucene.impl.mapper.Doc;
import com.cc.lucene.impl.mapper.Index;
import com.cc.lucene.impl.mmsegext.CCMMSegAnalyzer;
import com.cc.tool.BeanUtil;
import com.cc.tool.IConstant;
import com.cc.tool.Util;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class IndexImpl implements IndexApi {

  private static final Version version=Version.LUCENE_6_2_1;
  
  private static final Logger LOGGER=Logger.getLogger(IndexImpl.class);
  
  /**
   * 中文分词器
   */
  private static final Analyzer analyzer=new CCMMSegAnalyzer();
  
  private static final FieldType keyFT=new FieldType();
  static{
    keyFT.setNumericType(FieldType.LegacyNumericType.LONG.LONG);
    keyFT.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
    keyFT.setTokenized(false);
    keyFT.setStored(true);
  }
  
  private IndexWriter iw;
  
  private MultiFieldQueryParser mfqp;
  
  private Directory directory;
  
  private final String DOC_KEY_FIELD;
  
  private final int DOC_SEARCH_MAX_DOCS;
  
  private final Index[] DOC_INDEXES;
  
  private final String[] DOC_FILES;
  
  private IndexReader ir;
  
  private IndexSearcher is;
  private static final InputStream[] EMPTY_INPUTSTREAM= {};
  
  @Override
  public void createIndex(Object obj,InputStream[]inps) {
    try {
      this.ready();
      iw.addDocument(this.createDocument(obj,inps));
      this.commit();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void createIndex(Object obj) {
    createIndex(obj, EMPTY_INPUTSTREAM);
  }
  
  @Override
  public void createIndexes(Collection<IndexObj> objs) {
    try {
      IndexObj obj;
      IndexObj[] iObjs=new IndexObj[0];
      iObjs=objs.toArray(iObjs);
      this.ready();
      for(int i=0,len=objs.size();i<len;i++){
        obj=iObjs[i];
        iw.addDocument(this.createDocument(obj.getObj(),obj.getInps()));
        if(i/1000==0){
          this.commit();
        }
      }
      this.commit();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteIndex(long tableKey) {
    try {
      this.ready();
      iw.deleteDocuments(LegacyNumericRangeQuery.newLongRange(DOC_KEY_FIELD, tableKey,tableKey, true,true));
      this.commit();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteIndex(Collection<Long> keyFieldValue) {
    long keyV;
    Long[] keys=new Long[0];
    keys=keyFieldValue.toArray(keys);
    try {
      this.ready();
      for(int i=0,len=keyFieldValue.size();i<len;i++){
        keyV=keys[i];
        iw.deleteDocuments(LegacyNumericRangeQuery.newLongRange(DOC_KEY_FIELD, keyV,keyV, true,true));
      }
      this.commit();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteAll() {
    try {
      this.ready();
      this.iw.deleteAll();
      this.commit();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateIndex(Object obj,InputStream...inps) {
    this.deleteIndex(getKeyFieldVal(obj));
    this.createIndex(obj, inps);
  }

  @Override
  public void updateIndexes(Collection<IndexObj> objs) {
    int size=objs.size();
    IndexObj[] indexObjs=new IndexObj[size];
    indexObjs=objs.toArray(indexObjs);
    Collection<Long> keys=new ArrayList<Long>(size);
    for(int i=0;i<size;i++){
      keys.add(getKeyFieldVal(indexObjs[i].getObj()));
    }
    deleteIndex(keys);
    createIndexes(objs);
  }

  @Override
  public long[] search(String words, int count) {
    long[] keys={};
    try {
      if(this.is==null)this.resetIndexSearch();
      ScoreDoc[] docs=this.is.search(this.mfqp.parse(words.trim()),count).scoreDocs;
      int len=docs.length;
      keys=new long[docs.length];
      for(int i=0;i<len;i++){
        keys[i]=Long.parseLong(this.is.doc(docs[i].doc).get(DOC_KEY_FIELD));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return keys;
  }

  @Override
  public long[] search(String words) {
    return search(words, DOC_SEARCH_MAX_DOCS);
  }

  @Override
  public IndexPage search(String words, int begin, int count) {
    long[] keys=search(words, DOC_SEARCH_MAX_DOCS);
    IndexPage page=new IndexPage();
    page.setTotal(keys.length);
    if(begin<page.getTotal()){
      long len=page.getTotal()-begin;
      if(count>len)count=(int)len;
      long[] keys1=new long[count];
      for(int i=0;i<count;i++){
        keys1[i]=keys[begin+i];
      }
      page.setKeys(keys1);
    }else{
      page.setKeys(new long[0]);
    }
    return page;
  }

  public IndexImpl(Doc doc){
    DOC_KEY_FIELD=doc.getKeyField();
    DOC_SEARCH_MAX_DOCS=doc.getSearchMaxDocs();
    DOC_INDEXES=doc.getIndexes();
    DOC_FILES=doc.getFiles();
    
    try {
      this.directory=NIOFSDirectory.open(new File(doc.getDirectory()).toPath(),NoLockFactory.INSTANCE);
      this.mfqp=new MultiFieldQueryParser(doc.getQueryFields(),analyzer);
      this.mfqp.setDefaultOperator(Operator.OR);
    } catch (IOException e) {
      LOGGER.error(e);
    }
  }
  
  private void ready(){
    if(this.iw==null){
      try {
        this.iw=new IndexWriter(this.directory,new IndexWriterConfig(analyzer));
      } catch (IOException e) {
        LOGGER.error(e);
        e.printStackTrace();
      }
    }
  }
  
  private void commit(){
    try {
      this.iw.commit();
      this.resetIndexSearch();
    } catch (IOException e) {
      LOGGER.error(e);
    }
  }
  
  private Long getKeyFieldVal(Object obj){
    if(obj==null)return IConstant.TEMP_KEY_VAL;
    try {
      obj=BeanUtil.getProperty(obj,DOC_KEY_FIELD);
      return obj==null?IConstant.TEMP_KEY_VAL:Long.parseLong(obj.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return IConstant.TEMP_KEY_VAL;
  }
  
  private String getFieldVal(Object obj,String field){
    if(obj==null)return null;
    try {
      return BeanUtil.getProperty(obj,field).toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private Document createDocument(Object obj,InputStream[] inps){
    Document doc=new Document();
    
    doc.add(new Field(DOC_KEY_FIELD, getKeyFieldVal(obj), keyFT));
    String val=null;
    for(Index idx:DOC_INDEXES){
      if(Util.isBlank(val=getFieldVal(obj,idx.getField())))continue;
      IndexableField idxField=new TextField(idx.getField(), val.toString(),Store.NO);
      doc.add(idxField);
    }
    if(inps!=null){
      int len=inps.length;
      for(int i=0;i<len;i++){
        InputStream is=inps[i];
        doc.add(new TextField(DOC_FILES[i],new InputStreamReader(is)));
      }
    }
    return doc;
  }
  
  private void resetIndexSearch(){
    try {
      if(this.ir!=null)this.ir.close();
      this.ir=DirectoryReader.open(this.directory);
    } catch (IOException e) {
      this.createIndex(null);
      try {
        this.ir=DirectoryReader.open(this.directory);
      } catch (IOException e1) {
      }
      this.deleteIndex(IConstant.TEMP_KEY_VAL);
    }
    this.is=new IndexSearcher(this.ir);
  }
  
  @Override
  protected void finalize() throws Throwable {
    this.iw.close();
    super.finalize();
  }
}

