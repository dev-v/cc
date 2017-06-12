package com.cc.report.datas.store;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * 本包依赖的数据库操作抽象接口，所有提供的外部数据库操作接口均需实现本接口
 * @author cwl
 * @version 1.0
 */
public abstract class DaoAbstract{
	private static Map<String,Connection> cons=new HashMap<String, Connection>();
	private static Map<String,Set<Statement>> states=new HashMap<String, Set<Statement>>();
	public DaoAbstract(){}
	
	/**
	 * 需用户覆盖的关闭方法
	 * @param con
	 */
	protected abstract void close(Connection con);
	
	/**
	 * 需用户覆盖的获取数据库连接的方法
	 * @param con
	 * @return Connection
	 */
	protected abstract Connection getCon();
	
	/**
	 * 用于根据提供的sql语句查询数据库
	 * @param sql
	 * @return
	 */
	public final ResultSet query(String conId,String sql){
		ResultSet rs=null;
		try {
			rs=getSta(conId).executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * 刷新数据库连接
	 */
	public final void initCon(String id){
		cons.put(id, getCon());
		states.put(id, new HashSet<Statement>());
	}
	
	/**
	 * 数据连接使用完成后处理方法
	 */
	public final void close(String conId){
		try {
			for(Statement sta:states.get(conId))sta.close();
			close(cons.get(conId));
			cons.remove(conId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一个操作数据库的Statement对象
	 * @return
	 */
	private final Statement getSta(String conId){
		try {
			Statement sta=cons.get(conId).createStatement();
			states.get(conId).add(sta);
			return sta;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
