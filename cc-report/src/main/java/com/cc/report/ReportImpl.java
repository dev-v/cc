package com.cc.report;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import com.cc.report.csv.CSVImpl;
import com.cc.report.datas.store.DaoAbstract;
import com.cc.report.datas.store.DaoDefault;
import com.cc.report.datas.store.IStore;
import com.cc.report.datas.store.StoreImpl;
import com.cc.report.excel.ExcelImpl;
import com.cc.report.pdf.PdfImpl;
import com.cc.report.xml.DBXmlImpl;
import com.cc.report.xml.DaoXml;
import com.cc.report.xml.ISaxXml;
import com.cc.report.xml.Page;
import com.cc.report.xml.Report;
import com.cc.report.xml.SaxReportImpl;
import com.cc.report.xml.XmlUtil;

/**
 * 对外部应用接口的实现<br/>
 * 创建一个报表流程：<br/>
 * 1、构建本类，提供数据库操作对象；<br/>
 * 2、调用init方法，初始化信息;<br/>
 * 3、创建报表，并写入到一个输出流中
 * @author cwl
 * @version 1.0
 */
public class ReportImpl implements IReport{
	/**
	 * 所有值域的封装
	 */
//	private Values values;
	private IStore store;
	
	/**
	 * 页面配置信息
	 */
	private Page page;
	
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 文件类型
	 */
	private String type;
	
	/**
	 * 数据库操作对象
	 */
	private DaoAbstract dao;
	
	/**
	 * 一个构造器<br/>
	 * 通过提供的数据库配置文件路径提供默认的数据库操作对象，数据库配置文件需包含如下内容<br/>
	 * &lt;database&gt;<br/>
	 * &lt;user&gt;&lt;/user&gt;<br/>
	 * &lt;pass&gt;&lt;/pass&gt;<br/>
	 * &lt;url&gt;jdbc:oracle:thin:@localhost:1521:orcl&lt;/url&gt;<br/>
	 * &lt;driver&gt;oracle.jdbc.driver.OracleDriver&lt;/driver&gt;<br/>
	 * &lt;/database&gt;<br/>
	 * @param dbFilePath
	 */
	public ReportImpl(String dbFilePath){
		DaoXml dbx=DBXmlImpl.getInstance(dbFilePath).
			getDaoXml();
		dao=DaoDefault.getInstance(dbx);
	}
	
	/**
	 * 一个构造器<br/>
	 * 通过用户提供的数据库操作对象构造本类实例
	 * @param dao
	 */
	public ReportImpl(DaoAbstract dao){
		this.dao=dao;
	}
	
	/**
	 * 初始化操作,为建立报表提供数据源和数据显示模板等<br/>
	 * 内部执行了以下操作：<br/>
	 * 1、完成报表配置文件xml的解析；<br/>
	 * 2、将外部或用户提供的数据传入值域；<br/>
	 * 3、根据配置数据库配置信息读取数据库并存入值域；<br/>
	 * 4、初始化本类中的部分变量
	 * @param reportFilePath 报表配置文件路径
	 * @param params数据格式为：<br/>
	 * 1、json:{key1:value1,key2:value2,...,keyn:valuen}<br/>
	 * 2、xml:<br/>
	 * &lt;rootName&gt;<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * 	......<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * &lt;/rootName&gt;<br/>
	 */
	public void init(String reportFilePath,String params){
		try{
			ISaxXml reportXml=new SaxReportImpl();
			reportXml.saxXmlToReport(XmlUtil.getRootElement(reportFilePath));
			Report report=reportXml.getReport();

			this.store=new StoreImpl(report, dao, params);
			
			fileName=report.getFilename();
			type=report.getType();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化操作,为建立报表提供数据源和数据显示模板等<br/>
	 * 内部执行了以下操作：<br/>
	 * 1、完成报表配置文件xml的解析；<br/>
	 * 2、将外部或用户提供的数据传入值域；<br/>
	 * 3、根据配置数据库配置信息读取数据库并存入值域；<br/>
	 * 4、初始化本类中的部分变量
	 * @param reportFilePath 报表配置文件路径
	 * @param params Map&lt;String,String&gt;
	 */
	public void init(String reportFilePath,Map<String,String> params){
		try{
			ISaxXml reportXml=new SaxReportImpl();
			reportXml.saxXmlToReport(XmlUtil.getRootElement(reportFilePath));
			Report report=reportXml.getReport();
			
			this.store=new StoreImpl(report, dao, null,params);
			
			page=report.getPage();
			fileName=report.getFilename();
			this.fileName=System.currentTimeMillis()+".pdf";
			type=report.getType();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	 
	/**
	 * 创建pdf或xls格式报表，并将其绑定到一个输出流中<br/>
	 * 需首先调用init方法，初始化信息
	 * @param OutputStream
	 * @throws IOException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void createReport(final OutputStream out) throws IOException{
		try {
			if(type.equals(Constant.PDF)){
				new PdfImpl(page,store,out);
			}else if(type.equals(Constant.XLS)){
				new ExcelImpl(page,store,out);
//			}else if (type.equals(Constant.DOC)) {
//				new WordImpl(page,values,out);
			}else if(type.equals(Constant.CSV)){
				//需机器拥有足够内容存放输入流所有内容
//				ByteArrayOutputStream temp=new ByteArrayOutputStream(Constant.CSVFileBuffer);
//				new ExcelImpl(page,values,temp);
//				ByteArrayInputStream in=new ByteArrayInputStream(temp.toByteArray());
//				XLS2CSV.xls2csv(in,out);
//				in.close();
//				temp.close();
				new CSVImpl(page, store, out);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取文件名称
	 */
	public String getFileName(){
		return fileName;
	}
	
	/**
	 * 获取文件类型
	 */
	public String getType(){
		return type;
	}
}


// 使用Piped 的输入输出流
//PipedInputStream in = new PipedInputStream();
//final PipedOutputStream temp = new PipedOutputStream(in);
// 启动线程，让数据产生者单独运行
//new Thread(new Runnable() {
//  public void run() {
//		 	byte[] bs = new byte[2];
//		 for (int i = 0; i <= 100; i++) {
// 		 	bs[0] = (byte) i;
//  		bs[1] = (byte) (i + 1);
  		// 测试写入字节数组
//		new ExcelImpl(page,values,temp);
//  		temp.write(bs);
//  		temp.flush();
   		 // 等待0.1秒
//   		 try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		 }
//  }
//}).start();
//byte[] b=new byte[1024];
//while((in.read(b))!=-1){
//	out.write(b);
//}
//BufferedInputStream bi=new BufferedInputStream(in,1024);
//XLS2CSV.xls2csv(in,out);
//in.close();
//temp.close();
// 使用CircularByteBuffer  
//final CircularByteBuffer cbb = new CircularByteBuffer();  
//new ExcelImpl(page,values,new BufferedOutputStream(cbb.getOutputStream(),1024*1024)); 
// 启动线程，让数据产生者单独运行  
//new Thread(new Runnable() {   
//  public void run() {   
//    	new ExcelImpl(page,values,new BufferedOutputStream(cbb.getOutputStream(),1024*1024)); 
////   		 try {
////		Thread.sleep(10000);
////    	flag=true;
////	} catch (InterruptedException e) {
////		e.printStackTrace();
////	}
//  }   
//}).start(); 
//new Thread(new Runnable() {   
//      public void run() {   
//    	  while(true){
//    		  if(flag){
//					System.out.println("开始！");
//					XLS2CSV.xls2csv(new BufferedInputStream(cbb.getInputStream(),1024*1024),out);
//					System.out.println("完成！");
//    		  }
//    	  }
//      }   
//    }).start(); 
// 数据使用者处理数据  
// 也可以使用线程来进行并行处理  
//try {
//Thread.sleep(10000);
//flag=true;
//} catch (InterruptedException e) {
//e.printStackTrace();
//}
//System.out.println("开始！");
//XLS2CSV.xls2csv(new BufferedInputStream(cbb.getInputStream(),1024*1024),out);
//System.out.println("完成！");
//}
