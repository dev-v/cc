//package com.cc.report.venustech;
//import java.sql.Connection;
//import com.cc.report.datas.DaoAbstract;
//import com.venustech.utilities.database.ConnectionPool;
//import com.venustech.utilities.database.DBConnectionException;
//public class VenusTechDao extends DaoAbstract {
//	private ConnectionPool conPool;
//	private static VenusTechDao dao;
//	private VenusTechDao(){
//		try {
//			conPool=ConnectionPool.getInstance();
//		} catch (DBConnectionException e) {
//			e.printStackTrace();
//		}
//	}
//	public static VenusTechDao getInstance(){
//		if(dao==null)dao=new VenusTechDao();
//		return dao;
//	}
//	
//	@Override
//	public void close(Connection con) {
//		try {
//			conPool.freeConnection(con);
//		} catch (DBConnectionException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public Connection getCon() {
//		Connection connection=null;
//		try {
//			connection=conPool.getConnection();
//		} catch (DBConnectionException e) {
//			e.printStackTrace();
//		}
//		return connection;
//	}
//}
