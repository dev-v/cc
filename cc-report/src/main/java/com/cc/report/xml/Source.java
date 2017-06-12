package com.cc.report.xml;
/**
 * 本类用于存储配置文件配置的数据源
 * @author cwl
 * @version 1.0
 */
public class Source {
	
	/**
	 * 数据名称，区分数据源的唯一标识符
	 */
	String name;
	
	/**
	 * 数据类型 hibernate,sql(默认)
	 */
	String type="sql";
	
	/**
	 * 配置的sql类型数据源
	 */
	Sql sql;
	
	/**
	 * 配置的hibernate类型的数据源
	 */
	Hibernate hibernate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Hibernate getHibernate() {
		return hibernate;
	}
	public void setHibernate(Hibernate hibernate) {
		this.hibernate = hibernate;
	}
	public Sql getSql() {
		return sql;
	}
	public void setSql(Sql sql) {
		this.sql = sql;
	}
	
}
