package com.cc.report.xml;
import java.util.List;
import java.util.Map;
/**
 * 用于存储配置的sql数据源配置信息
 * @author cwl
 * @version 1.0
 */
public class Sql {
	/**
	 * sql语句
	 */
	private String value;
	/**
	 * 内含字段
	 */
	private List<Field> fields;
	//存储xml用户配置数据
	private List<Map<String,String>> list;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public List<Map<String, String>> getList() {
		return list;
	}
	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
}
