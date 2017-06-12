package com.cc.report.xml;
/**
 * 解析持久层配置信息
 * @author cwl
 * @version 1.0
 */
public interface IDBXml {
	/**
	 * 根据xml文件路径解析其为一个DaoXml的实列
	 * @param filePath
	 */
	DaoXml getDaoXml();
}
