package com.cc.db.orm.mybatis.ext;

import java.lang.reflect.Field;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.log4j.Logger;

/**
 * @author wenlongchen
 * @since Nov 3, 2016
 */
public class MybatisUtil {

  private Configuration configuration;
  private XMLLanguageDriver xmlDriver;
  private Field rootSqlNodeField;

  private Logger logger = Logger.getLogger(MybatisUtil.class);

  public MybatisUtil(Configuration configuration) {
    this.configuration = configuration;
    xmlDriver = new XMLLanguageDriver();
    try {
      rootSqlNodeField = DynamicSqlSource.class.getDeclaredField("rootSqlNode");
      rootSqlNodeField.setAccessible(true);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  public String parseXmlSql(String xmlSqlScript, Object parameterObj) {
    XPathParser parser = new XPathParser("<select>" + xmlSqlScript + "</select>");
    XNode xnode = parser.evalNode("select");

    SqlSource source = xmlDriver.createSqlSource(configuration, xnode, parameterObj.getClass());

    if (source instanceof DynamicSqlSource) {
      try {
        SqlNode sqlNode = (SqlNode) rootSqlNodeField.get(source);

        DynamicContext context = new DynamicContext(configuration, parameterObj);
        sqlNode.apply(context);

        return context.getSql();
      } catch (Exception e) {
        logger.error(xmlSqlScript, e);
        return xmlSqlScript;
      }
    } else {
      return source.getBoundSql(parameterObj).getSql();
    }
  }
}

