package com.cc.lucene.impl.mapper;

import com.cc.tool.IConstant;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class Doc {

  String obj;
  String keyField;
  String lang;
  String directory;
  int searchMaxDocs;
  Index[] indexes;
  String[] files;
  String[] queryFields;

  public Doc() {}

  Doc(String obj, String keyField, String lang, int maxFiles, String directory, int searchMaxDocs,
      Index[] indexes) {
    this.obj = obj;
    this.keyField = keyField;
    this.lang = lang;
    this.directory = directory;
    this.indexes = indexes;
    this.searchMaxDocs = searchMaxDocs;

    int indexesLength = this.indexes.length;
    queryFields = new String[indexesLength + maxFiles];
    int i = 0;
    for (; i < indexesLength; i++) {
      queryFields[i] = this.indexes[i].getField();
    }

    files = new String[maxFiles];
    for (int j = 0; j < maxFiles; j++) {
      queryFields[i++] = files[j] = IConstant.str_file + j;
    }
  }

  public String getDirectory() {
    return directory;
  }

  public void setDirectory(String directory) {
    this.directory = directory;
  }

  public Index[] getIndexes() {
    return indexes;
  }

  public String getLang() {
    return lang;
  }

  public String[] getFiles() {
    return files;
  }

  public String[] getQueryFields() {
    return queryFields;
  }

  public String getObj() {
    return obj;
  }

  public void setObj(String obj) {
    this.obj = obj;
  }

  public String getKeyField() {
    return keyField;
  }

  public void setKeyField(String keyField) {
    this.keyField = keyField;
  }

  public int getSearchMaxDocs() {
    return searchMaxDocs;
  }

  public void setSearchMaxDocs(int searchMaxDocs) {
    this.searchMaxDocs = searchMaxDocs;
  }
}

