package com.cc.report.datas.store;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.cc.report.xml.DaoXml;

/**
 * 本类用于提供数据库连接
 * @author cwl
 * @version 1.0
 */
public class DaoDefault extends DaoAbstract{
	private static DaoXml daoXml;
	private static DaoDefault dao;
	private static final String closeConError="**** 关闭数据库连接时候错误！ ***";
	private static final String getConError="**** 获取数据库连接失败！ ***";
	private static final String loadDriverError="**** 加载db驱动器失败！ ***";
	/**
	 * 一个私有构造器<br/>
	 * 包含加载驱动器和获取一个连接
	 */
	private DaoDefault(){
		try {
			Class.forName(daoXml.getDriver());
		} catch (ClassNotFoundException e) {
			System.out.println(loadDriverError);
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取本类单态实例
	 * @param daoXml1
	 * @return
	 */
	public static DaoDefault getInstance(DaoXml daoXml1){
		if(dao==null){
			daoXml=daoXml1;
			dao=new DaoDefault();
		}
		return dao;
	}
	
	@Override
	public void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println(closeConError);
			e.printStackTrace();
		}
		
	}

	@Override
	public Connection getCon() {
		Connection con=null;
		try {
			con=DriverManager.getConnection(
					daoXml.getUrl(),daoXml.getUser(),daoXml.getPass());
		} catch (SQLException e) {
			System.out.println(getConError);
			e.printStackTrace();
		}
		return con;
	}
	public static void main(String[] args) {

		DaoXml daoXml=new DaoXml();
		daoXml.setDriver("com.mysql.jdbc.Driver");
		daoXml.setPass("123qwe");
		daoXml.setUrl("jdbc:mysql://127.0.0.1:3306/itom3?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true");
		daoXml.setUser("root");
//		dao=dao.getInstance(daoXml);
//		dao.close(dao.getCon());
	}
}
