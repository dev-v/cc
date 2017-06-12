package com.cc.report.datas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cc.report.datas.store.IStore;
import com.cc.report.util.TransformValue;
import com.cc.report.xml.Labels;
import com.cc.report.xml.Td;

/**
 * <li>文件名称: ValueUtil.java</li>
 * <li>文件描述: </li>
 * <li>其他说明: 无</li>
 * <li>创建日期: 2013-8-16</li>
 * <li>修改记录: 无</li>
 * <pre>
 * 	内容摘要: 无
 * </pre>
 * @version 版本: 1.0
 * @author  作者姓名: chenwl
 */
public class ValueUtil {
	@SuppressWarnings("unchecked")
	public static String parseTdValue(Td td,IStore store,Map<String,Object> parentMap){
		String value=store.replaceAll(td.getValue(),parentMap);
		if(value==null)value=td.getBlank();
		
		Labels labels=td.getLabels();
		if(labels!=null&&td.getLabel()!=null){
			List<Map<String,String>> list=(List<Map<String,String>>)store.getValue(labels.getSource());
			Map<String,String> labelMap=new HashMap<String, String>();
			for(Map<String,String> temp:list){
				labelMap.put(temp.get(labels.getValue()),temp.get(labels.getLabel()));
			}
			td.setLabel(labelMap);
		}
		if(td.getLabel()!=null)value=TransformValue.saxValueToLabel(value,td.getLabel());
		value=TransformValue.subStr(value, td.getBegin(),td.getLength(),td.getEnding());
		return value;
	}
}


