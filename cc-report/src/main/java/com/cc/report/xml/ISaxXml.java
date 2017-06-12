package com.cc.report.xml;
import org.dom4j.Element;

/**
 * 本接口提供了将xml配置文件解析为Report对象的方法定义<br/>
 * 提供了获取Report对象的方法
 * @author cwl
 * @version 1.0
 */
public interface ISaxXml {
	
	/**
	 * 根据xml文件路径解析成Report对象
	 * @param path
	 */
	void saxXmlToReport(Element reportRoot);
	
	/**
	 * 获取一个Report对象<br/>
	 * 需首先执行saxXmlToReport方法，否则返回null
	 * @return
	 */
	Report getReport();
}
