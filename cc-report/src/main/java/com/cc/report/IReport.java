package com.cc.report;
import java.io.IOException;
import java.io.OutputStream;
/**
 * 提供给外部应用的借口
 * @author cwl
 * @version 1.0
 */
public interface IReport {
	
	/**
	 * 创建报表，并将报表内容写入到一个输出流中
	 * @param out
	 */
	void createReport(OutputStream out) throws IOException;
	
	/**
	 * 返回报表文件名称
	 * @return
	 */
	String getFileName();	
	
	/**
	 * 返回报表文件类型
	 * @return
	 */
	String getType();
}
