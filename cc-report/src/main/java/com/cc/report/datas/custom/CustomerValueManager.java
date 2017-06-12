package com.cc.report.datas.custom;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 客户实现的单值获取方式的管理
 * @author 陈文龙
 * @version 2013-05-28
 * </pre>
 */
public class CustomerValueManager {
	/**
	 * 用于存储客户实现的获取单值数据的方法
	 */
	private static Map<String,ICustomerValue> customerValue=new HashMap<String, ICustomerValue>();

	/**
	 * 注册客户实现的获取单值的java对象
	 * @param key 配置文件配置的键
	 * @param customerValueImpl 用户实现的获取单值的对象
	 */
	public static void registerCustomerValue(String key,ICustomerValue customerValueImpl){
		customerValue.put(key, customerValueImpl);
	}
	
	/**
	 * 解注册客户实现的获取单值的java对象
	 * @param key 要解注册的单值对象对应的键
	 */
	public static void unRegisterCustomerValue(String key){
		customerValue.remove(key);
	}
	
	/**
	 * 通过键获取单值对象
	 * @param key
	 * @return
	 */
//	public static ICustomerValue getCustomerValue(String key){
//		return customerValue.get(key);
//	}
	
	/**
	 * 通过键获取通过单值对象能够获取的值
	 * @param key 注册时使用的键
	 * @return 
	 */
	protected static String getCustomerValue(String key){
		return customerValue.get(key).getValue();
	}
}
