package com.cc.report.xml;
/**
 * 用于存储需解析的字段<br/>
 * @author cwl
 * @version 1.0
 */
public class Field{
	/**
	 * 字段名
	 */
	private String name;
	/**
	 * 字段类型，默认为java.lang.String
	 */
	private String type;
	/**
	 * 是否为集合类型
	 */
	private boolean isSet;
	/**
	 * 若为对象，是否延迟加载
	 */
	private boolean lazy=true;
	/**
	 * 其下是否为一个数据源hibernate
	 */
	private Hibernate hibernate;
	/**
	 * 其下是否为一个数据源sql
	 */
	private Sql sql;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Sql getSql() {
		return sql;
	}
	public void setSql(Sql sql) {
		this.sql = sql;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isSet() {
		return isSet;
	}
	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}
	public boolean isLazy() {
		return lazy;
	}
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}
	public Hibernate getHibernate() {
		return hibernate;
	}
	public void setHibernate(Hibernate hibernate) {
		this.hibernate = hibernate;
	}
}
