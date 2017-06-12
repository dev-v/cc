package com.cc.report.xml;
import java.util.List;
import java.util.Map;
/**
 * 本类用于存储解析的xml文件信息以提供外界使用
 * @author 陈文龙
 * @version 1.0
 */
public class Report {
	
	/**
	 * 配置的原始文件名称
	 */
	String filename;
	
	/**
	 * 文件名长度
	 */
	String filenameLength;
	
	/**
	 * 报表类型（目前支持pdf、doc、xls、csv）
	 */
	String type;
	
	/**
	 * 基本参数（配置文件配置传递）
	 */
	Map<String,String> params;
	
	/**
	 * 数据源
	 */
	List<Source> sources;
	
	/**
	 * 页面格式配置信息
	 */
	Page page;
	
	public Map<String, String> getParams() {
		return params;
	}
	void setParams(Map<String, String> params) {
		this.params = params;
	}
	public List<Source> getSources() {
		return sources;
	}
	public void setSources(List<Source> sources) {
		this.sources = sources;
	}
	public Page getPage() {
		return page;
	}
	void setPage(Page page) {
		this.page = page;
	}
	public String getType() {
		return type;
	}
	void setType(String type) {
		this.type = type;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilenameLength() {
		return filenameLength;
	}
	public void setFilenameLength(String filenameLength) {
		this.filenameLength = filenameLength;
	}
}
