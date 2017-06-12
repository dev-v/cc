package com.cc.report.xml;

/**
 * 封装了持久层的配置信息
 * @author cwl
 * @version 1.0
 */
public class DaoXml {
	
	/**
	 * 用户名
	 */
	private String user;
	
	/**
	 * 密码
	 */
	private String pass;
	
	/**
	 * 路径
	 */
	private String url;
	
	/**
	 * 驱动
	 */
	private String driver;
	
	public void setUser(String user) {
		this.user = user;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUser() {
		return user;
	}
	public String getPass() {
		return pass;
	}
	public String getUrl() {
		return url;
	}
	public String getDriver() {
		return driver;
	}
	
}
