package com.cc.report.datas.store;

import java.util.Map;


/**
 * <li>文件名称: IStore.java</li>
 * <li>内容摘要: 无</li>
 * <li>其他说明: 无</li>
 * <li>完成日期: 2013-8-15</li>
 * <li>修改记录: 无</li>
 * @author  作者姓名: chenwl
 */
public interface IStore extends Map<String,Object>{
	/**
	 * 根据单个el表达式获取数据,没有数据返回null
	 * @param el
	 * @return
	 */
	Object getValue(String el);
	
	/**
	 * 根据单个el表达式从指定集合中获取数据
	 * @param el
	 * @return
	 */
	Object getValue(String el,Map<String,Object> map);
	
	/**
	 * 根据单个el表达式从指定集合中和全局数据域中获取数据，没有找到返回null
	 * @param el
	 * @return
	 */
	Object getAll(String el,Map<String,Object> map);
	
	/**
	 * 替换字符串中的所有el表达式为实际值
	 * @param elStr
	 * @return 返回替换后的字符串
	 */
	String replaceValue(String elStr);
	
	/**
	 * 替换字符串中的所有el表达式为指定集合中的实际值
	 * @param elStr
	 * @return 返回替换后的字符串
	 */
	String replaceValue(String elStr,Map<String,Object> map);
	
	/**
	 * 替换字符串中的所有el表达式为指定集合和全局数据域中的实际值
	 * @param elStr
	 * @return 返回替换后的字符串
	 */
	String replaceAll(String elStr,Map<String,Object> map);
	
	Map<String,Object> getParams();
	
	Map<String,Object> getSources();
}


