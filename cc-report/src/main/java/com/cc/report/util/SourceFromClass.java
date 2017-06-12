package com.cc.report.util;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;

public class SourceFromClass {
	@SuppressWarnings("unchecked")
	public static Object getFinalField(String fieldName,Class c){
		Field field=null;
		Object o=null;
		try {
			field = c.getField(fieldName);
			o=field.get(c);
		} catch (Exception e) {
			System.out.println("********获取类 "+c+" 的final字段 "+fieldName+" 失败!********");
			e.printStackTrace();
		}
		return o;
	}
	
	/**
	 * 获取一个类中声明的字段对象值，并将字段名称和值存在一个Map集合中
	 * @param c 数据来源类
	 * @param patter 分割字段名称的模式
	 * @param times 分割后返回的数组最大的长度（0表示无限制、<0表示不分割）
	 * @param index 分割成的数组中要作为名称的值的索引（若索引大于分割形成数组的长度，则存储索引为0的值）
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static final Map<String,?> getFields(Class c,String patter,int times,int index){
		Map<String,Object> map=new HashMap<String, Object>(64);
		Field[] fields=c.getFields();
		String str="";
		for(Field field:fields){
			String name=field.getName();
			String[] names=null;
			Object value=null;
			String key=null;
			try {
				value=field.get(c);
				if(times<0){
					key=name.toLowerCase();
				}else{
					names=name.split(patter,times);
					key=names[index].toLowerCase();
				}
			} catch (Exception e) {
				key=names[0].toLowerCase();
//				e.printStackTrace();
			}finally{
				map.put(key,value);
				str+=name+'|';
//				if(names[0].startsWith("VERTICAL"))str+=key+'|';
			}
		}
		System.out.println(map);
		System.out.println(str);
		return map;
	}
	
	public static void main(String[] args) {
		getFields(CellStyle.class,"_",2,1);
		System.out.println(HSSFCellStyle.ALIGN_CENTER);
	}
}
