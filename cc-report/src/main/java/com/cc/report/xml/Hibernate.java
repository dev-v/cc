package com.cc.report.xml;
import java.util.List;
/**
 * 用于存储hibernate型数据源配置信息
 * @author cwl
 * @version 1.0
 */
public class Hibernate {
	/**
	 * 持久层类的 完全限定名
	 */
	private String dao;
	/**
	 * 需执行的持久层方法
	 */
	private String method;
	/**
	 * 存储查询结果的实体类的完全限定名
	 */
	private String po;
	/**
	 * hibernate的HQL查询语句
	 */
	private String sql;
	/**
	 * 方法参数类型数组
	 */
	private String[] paramsType;
	/**
	 * 方法参数值
	 */
	private String[] paramsValue;
	/**
	 * 需解析出来的字段集合
	 */
	private List<Field> fields;
	
	public String getDao() {
		return dao;
	}
	public void setDao(String dao) {
		this.dao = dao;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPo() {
		return po;
	}
	public void setPo(String po) {
		this.po = po;
	}
	public String[] getParamsType() {
		return paramsType;
	}
	public void setParamsType(String[] paramsType) {
		this.paramsType = paramsType;
	}
	public String[] getParamsValue() {
		return paramsValue;
	}
	public void setParamsValue(String[] paramsValue) {
		this.paramsValue = paramsValue;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
}
