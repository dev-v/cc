package com.cc.report.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import com.cc.report.ReportImpl;


/**
 * <li>文件名称: TestReport.java</li>
 * <li>文件描述: </li>
 * <li>内容摘要: 无</li>
 * <li>其他说明: 无</li>
 * <li>完成日期: 2013-8-12</li>
 * <li>修改记录: 无</li>
 * @version 产品版本: 3.X
 * @author  作者姓名: chenwl
 */
public class TestReport {
	ReportImpl report;
	String dbFilePath;
	String configPath;
	private void init() throws URISyntaxException{
		String path=this.getClass().getResource("").toString().replace("file:/","");
		this.dbFilePath=path+"db.xml";
		this.configPath=path+"config/";
		this.report=new ReportImpl(this.dbFilePath);
	}
	
	public static void main(String[] args) throws Exception {
		TestReport test=new TestReport();
		test.init();
		File file;
		OutputStream out;
		long t1=System.currentTimeMillis();
		//导出xls
//		test.report.init(test.configPath+"xls/xls_1.xml",new HashMap<String, String>());
//		file=new File("D:/myReportTest/"+test.report.getFileName());
//		out=new FileOutputStream(file);
//		test.report.createReport(out);
//		out.close();
		//导出pdf
		test.report.init(test.configPath+"pdf/pdf.xml",new HashMap<String, String>());
		file=new File("D:/myReportTest/"+test.report.getFileName());
		out=new FileOutputStream(file);
		test.report.createReport(out);
		out.close();
//		//导出csv
//		test.report.init(test.configPath+"csv/csv.xml",new HashMap<String, String>());
//		file=new File("D:/myReportTest/"+test.report.getFileName());
//		out=new FileOutputStream(file);
//		test.report.createReport(out);
//		out.close();
		
		long t2=System.currentTimeMillis();
		System.out.println(t2-t1);
	}
}


