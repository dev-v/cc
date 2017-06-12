package com.cc.lucene.impl.mapper;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cc.tool.IConstant;
import com.cc.tool.Util;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class Lucene {
  
  private static Logger logger=Logger.getLogger(Lucene.class);

  String path;
  String lang;
  Doc[] docs;
  private static final SAXReader reader = new SAXReader();
  
  @SuppressWarnings("unchecked")
  public Lucene(String luceneConfigPath,String luceneStorePath){
    Element lucene=getRootElement(luceneConfigPath);
    
    this.path=luceneStorePath+File.separator;
    this.lang=lucene.attributeValue(IConstant.str_lang);
    
    List<Element> docsE=lucene.elements();
    this.docs=parseDoc(docsE);
  }
  
  public Doc[] getDocs(){
    return docs;
  }
  
  private static Element getRootElement(final String filePath) {
    File file=new File(filePath);
    Element root=null;
      try {
        root=reader.read(file).getRootElement();
    } catch (DocumentException e) {
      logger.error(e);
    }
    return root;
  }
  
  @SuppressWarnings("unchecked")
  private Doc[] parseDoc(List<Element> docsE){
    int dSize=docsE.size();
    Doc[] docs=new Doc[dSize];
    for(int j=0;j<dSize;j++){
      Element docE=docsE.get(j);
      List<Element> indexesE=docE.elements();
      Index[] indexes=parseIndex(indexesE);
      
      String lang=docE.attributeValue(IConstant.str_lang);
      lang=Util.isBlank(lang)?this.lang:lang;
      
      String maxFiles=docE.attributeValue(IConstant.str_maxFiles);
      docs[j]=new Doc(
        docE.attributeValue(IConstant.str_obj),
        docE.attributeValue(IConstant.str_keyField),
        lang,
        Integer.parseInt(maxFiles),
        this.path+docE.attributeValue(IConstant.str_obj),
        Integer.parseInt(docE.attributeValue(IConstant.str_searchMaxDocs)),
        indexes);
    }
    return docs;
  }
  
  private Index[] parseIndex(List<Element> indexesE){
    int size=indexesE.size();
    Index[] indexes=new Index[size];
    for(int i=0;i<size;i++){
      Element indexE=indexesE.get(i);
      indexes[i]=new Index(
        indexE.attributeValue(IConstant.str_field), 
        indexE.attributeValue(IConstant.str_type), 
        Boolean.parseBoolean(indexE.attributeValue(IConstant.str_store))
      );
    }
    return indexes;
  }
}

