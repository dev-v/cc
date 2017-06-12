package com.cc.report.util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 本类用于各种数据格式的相互转换，涉及到的数据格式包括：<br/>
 * xml、json、map、list、array、String、date
 * @author 陈文龙
 */
public class TransformValue {
	
	/**
	 * xml格式字符串头部<br/>
	 * 默认：&lt;?xml version="1.0" encoding="UTF-8"?&gt;
	 */
	private static String xmlHeader="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String jsonExceptionMessage="****json方式解析基本参数数据格式错误，" +
				"请检查你的数据格式是否为{name1:value1,name2:value2,name3:value3}类似格式!";
	
	/**
	 * 将一个xml数据格式的字符串转换为Map集合<br/>
	 * 此xml需包含一个根节点，子列表中的每个元素中：<br/>
	 * 节点名对应转换为集合的key；<br/>
	 * 节点包含文本对应转换为集合的value
	 * @param xml 格式：<br/>
	 * &lt;rootName&gt;<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * 	......<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * &lt;/rootName&gt;<br/>
	 * @return Map&lt;String,String&gt;
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> xmlToMap(String xml){
		xml=xml.trim();
		if(!xml.startsWith("<?xml"))xml=xmlHeader+xml;
		Map<String,String> map=new HashMap<String, String>();
		try {
			Element root=DocumentHelper.parseText(xml).getRootElement();
			List<Element> es=root.elements();
			for(Element e:es){
				map.put(e.getName(),e.getText());
			}
		} catch (DocumentException e1) {
			System.out.println("map:"+map+"\n传入的xml字符串为（头部可能为系统所加）:\n"+xml);
			e1.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 将一个json数据格式的字符串转换为Map集合<br/>
	 * @param json 格式：<br/>
	 * {key1:value1,key2:value2,...,keyn:valuen}
	 * @return Map&lt;String,String&gt;
	 */
	public static Map<String,String> jsonToMap(String json){
		Map<String,String> map=new HashMap<String, String>();
		try {
			JSONObject jsons=JSONObject.parseObject(json);
			Set<String> set=jsons.keySet();
			for(String name:set){
				map.put(name,jsons.getString(name));
			}
		} catch (JSONException e1) {
			System.out.println(jsonExceptionMessage+"\n传入的json字符串为：\n"+json);
			e1.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 将一个Map&lt;String,String[]&gt;:HttpServletRequest.getParameterMap()
	 * 数据格式的字符串转换为Map&lt;String,String&gt;集合<br/>
	 * 将源集合每个键对应的值String[]数组中取第一个值存储到新的Map&lt;String,String&gt;集合中<br/>
	 * <b>常用于HttpServletRequest.getParameterMap()</b>
	 * @param requestMap 格式：Map&lt;String,String[]&gt;HttpServletRequest.getParameterMap()<br/>
	 * @return Map&lt;String,String&gt;
	 */
	public static Map<String,String> requestMapToMap(Map<String,String[]> requestMap){
		Map<String,String> map=new HashMap<String, String>();
		Set<String> keySet=requestMap.keySet();
		for(String key:keySet){
//			String values="";
//			for(String value:requestMap.get(key)){
//				values+=value+',';
//			}
//			map.put(key,values);
			map.put(key,requestMap.get(key)[0]);
		}
		return map;
	}
	
	/**
	 * 设置xml头部字符，默认为：<br/>
	 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
	 * @param xmlHeader
	 */
	public void setXmlHeader(String xmlHeader){
		TransformValue.xmlHeader=xmlHeader;
	}
	
	public String getXmlHeader(){
		return TransformValue.xmlHeader;
	}


	/**
	 * 方法用于截取字符串
	 * @param str
	 * @param begin
	 * @param end
	 * @param ending
	 * @return
	 */
	public static String subStr(String str,Integer begin,Integer length,String ending){
		int tempBegin=0;
		int tempEnd=str.length();
		if(begin!=null&&begin>=0)tempBegin=begin<tempEnd?begin:tempEnd;
		if(length!=null&&length>0){
			int end=tempBegin+length;
			tempEnd=tempEnd<end?tempEnd:end;
		}
		String tempStr=str.substring(tempBegin,tempEnd);
		if(tempEnd<str.length())tempStr=tempStr+ending;
		return tempStr;
	}

	/**
	 * 将数据库值替换为标签显示
	 * @param str
	 * @param label
	 * @return
	 */
	public static String saxValueToLabel(String str,Map<String,String> label){
		String value=label.get(str);
		if(value==null)return str;
		return value;
	}
}
