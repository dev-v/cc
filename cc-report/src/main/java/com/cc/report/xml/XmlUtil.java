package com.cc.report.xml;
import java.io.File;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * xml文件解析工具
 * @author cwl
 * @version 1.0
 */
public class XmlUtil {
	/**
	 * 解析器
	 */
	private static final SAXReader reader = new SAXReader();
	
	/**
	 * 方法用于读取配置文件，并获取根节点
	 * @param path
	 */
	public static Element getRootElement(final String path) {
		File file=new File(path);
		Element root=null;
    	try {
    		root=reader.read(file)//读取报表配置文件
    			.getRootElement();//获取配置文件根节点
		} catch (DocumentException e) {
			System.out.println("解析xml文件路径错误！");
			e.printStackTrace();
		}
		return root;
	}
}
