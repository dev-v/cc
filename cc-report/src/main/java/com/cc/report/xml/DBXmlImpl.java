package com.cc.report.xml;
import org.dom4j.Element;

/**
 * 提供了解析xml配置信息为DaoXml对象的实现<br/>
 * 依赖于dom4j
 * @author cwl
 * @version 1.0
 */
public class DBXmlImpl implements IDBXml {
	private static DBXmlImpl db;
	private static DaoXml dao;
	/**
	 * 定义一个私有构造器，禁止用户自己建立本类实例
	 */
	private DBXmlImpl(){}
	
	/**
	 * 获取本类实例的方法，保证本类为一个单态类
	 * @param dbFilePath
	 * @return DBXmlImpl
	 */
	public static DBXmlImpl getInstance(String dbFilePath){
		if(db==null){
			db=new DBXmlImpl();
			db.saxXmlToDaoXml(dbFilePath);
		}
		return db;
	}

	/**
	 * 获取解析出来的db配置信息
	 */
	public DaoXml getDaoXml() {
		return dao;
	}
	
	/**
	 * 根据xml文件路径解析其为一个DaoXml的实列
	 */
	private DaoXml saxXmlToDaoXml(String dbFilePath) {
		Element root=XmlUtil.getRootElement(dbFilePath);//读取根节点
		dao=new DaoXml();
		if(root!=null&&root.getName().toLowerCase().equals("database")){
			Element user=root.element("user");
			if(user==null)throw new RuntimeException("db.xml中user配置错误！");
			else dao.setUser(user.getText().trim());
			
			Element pass=root.element("pass");
			if(pass==null)throw new RuntimeException("db.xml中pass配置错误！");
			else dao.setPass(pass.getText().trim());
			
			Element url=root.element("url");
			if(url==null||url.equals(""))throw new RuntimeException("db.xml中url配置错误！");
			else dao.setUrl(url.getText().trim());
			
			Element driver=root.element("driver");
			if(driver==null||driver.equals(""))throw new RuntimeException("db.xml中driver配置错误！");
			else dao.setDriver(driver.getText().trim());
		}else{
			throw new RuntimeException("db.xml的根节点配置错误！");
		}
		return dao;
	}
}
