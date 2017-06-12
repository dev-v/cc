package com.cc.report.datas.store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cc.report.Constant;
import com.cc.report.util.TransformValue;
import com.cc.report.xml.Field;
import com.cc.report.xml.Report;
import com.cc.report.xml.Source;
import com.cc.report.xml.Sql;


/**
 * <li>文件名称: StoreImpl.java</li>
 * <li>内容摘要: 无</li>
 * <li>其他说明: 无</li>
 * <li>创建日期: 2013-8-15</li>
 * <li>修改记录: 无</li>
 * @author  作者姓名: chenwl
 */
public class StoreImpl extends HashMap<String,Object> implements IStore {

	private static final long serialVersionUID = 8343453941710529088L;
	
	DaoAbstract dao;
	String conId;

	private Map<String,Object> params;
	private Map<String,Object> sources;
	
	/**
	 * 重置文件名称作为最终文件名称(含文件类型后缀名)
	 * @param report
	 * @param dao
	 * @param params
	 * @param maps
	 */
	public StoreImpl(Report report,DaoAbstract dao,String params,Map<String,? extends Object>...maps){
		//解析静态参数（配置文件配置的静态参数、外界传入的静态参数）
		this.initParams(report.getParams(), params, maps);
		
		//解析配置文件中配置的数据源
		this.dao=dao;
		this.conId=Constant.BLANK+Math.random();
		this.dao.initCon(this.conId);
		this.saxSources(report.getSources());
		this.dao.close(this.conId);
		this.dao=null;
		
	}

	/**
	 * <pre>
	 * 将报表中配置的参数、外界传入的参数解析入值域中
	 * 若含有相同名称的参数，后面的参数将覆盖前面的参数值
	 * params数据格式为：<br/>
	 * 1、json:{key1:value1,key2:value2,...,keyn:valuen}
	 * 2、xml:<br/>
	 * &lt;rootName&gt;<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * 	......<br/>
	 * 	&lt;keyName&gt;value&lt;/keyName&gt;<br/>
	 * &lt;/rootName&gt;<br/>
	 * </pre>
	 * @param reportParams 报表中配置的参数
	 * @param params 外界传入的字符串形式参数 json或xml格式字符串
	 * @param maps 外界传入的maps形式参数
	 */
	private void initParams(Map<String,String> reportParams,String params,Map<String,? extends Object>...maps){
		this.params=new HashMap<String, Object>();
		this.put(Constant.params,this.params);
		if(reportParams!=null){
			this.params.putAll(reportParams);
		}
		if(params!=null){
			if(params.startsWith(Constant.openBrace_)){//解析json格式
				this.params.putAll(TransformValue.jsonToMap(params));
			}else if(params.startsWith(Constant.openAngle_)){//解析xml格式
				this.params.putAll(TransformValue.xmlToMap(params));
			}
		}
		for(Map<String,? extends Object> map:maps){
			this.params.putAll(map);
		}
	}
	
	/**
	 * 封装从数据库中取来的数据
	 * @param sources
	 * @param dao
	 */
	private void saxSources(List<Source> sources) {
		this.sources=new HashMap<String, Object>();
		this.put(Constant.sources,this.sources);
		for(Source source:sources){
			List<Map<String,?>> list=null;//用于存储从每个数据源解析出来的内容
			String type=source.getType();//获取数据源从数据库中取值方式
			if(type.equals(Constant.SQL)){
				list=saxSqlSource(source.getSql(),null);
			}else if(type.equals(Constant.HIBERNATE)){//目前暂不支持
//				list=saxHibernateSource(source.getHibernate());
			}
			this.sources.put(source.getName(),list);
		}
	}

	/**
	 * sql方式获取数据
	 * @param sql
	 * @param tempMap
	 * @return
	 */
	private List<Map<String, ?>> saxSqlSource(Sql sql,Map<String,Object> map){
		String sqlValue=this.replaceAll(sql.getValue(), map);//此处注意还需解析sql语句中的表达式
		System.out.println("sql:"+sqlValue+"********");
		ResultSet rs=dao.query(this.conId,sqlValue);
		List<Map<String, ?>> list=new ArrayList<Map<String,?>>();
		List<Field> fields=sql.getFields();
		String fieldName=null;
		try {
			if(rs==null)return list;
			while(rs.next()){
				//用于存储查询出来的结果集
				Map<String,Object> po=new HashMap<String,Object>();
				for(Field field:fields){
					fieldName=field.getName();
					if(field.getSql()==null){//若其下不含有sql元素
						po.put(fieldName,rs.getString(fieldName));
					}else{
						po.put(fieldName,saxSqlSource(field.getSql(),po));
					}
				}
				list.add(po);
			}
			if(sql.getList()!=null)list.addAll(sql.getList());//加入用户配置的固定列表
		} catch (SQLException e) {
			System.out.println("******获取rs数据信息时错误！配置列 "+fieldName+" ******");
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据标签从给定集合中获取数据，没有找到返回null
	 * @param tag
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object getValue(ELTag tag,Map<String,Object> map){
		try{
			if(map==null)return this.getValue(tag);
			int index=0;
			int len=tag.len-1;
			for(;index<len;index++){//从sources中获取数据，取到倒数第二个为止
				int idx=tag.idx[index];
				if(idx==-1){
					map=(Map<String,Object>)map.get(tag.names[index]);
				}else{
					map=((List<Map<String,Object>>)map.get(tag.names[index])).get(idx);
				}
			}
			//取标签链中最后一个数据
//			System.out.println(tag.input+"  "+tag.len+"  "+tag.idx.length);
			if(tag.idx[index]==-1){
				return map.get(tag.names[index]);
			}else{
				return ((List<Object>)map.get(tag.names[index])).get(tag.idx[index]);
			}
		}catch(Exception e){
			System.out.println("获取值域错误，可能原因： input:"+tag.input+"  map:"+map);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * <pre>
	 * 根据ELTag标签对象在全局域获取数据，没有找到返回null
	 * 首先判断是否有限定域,有限定域则从限定域中找，
	 * 否则若带有索引则从sources中查找
	 * 否则先从params中查找然后从sources中查找
	 * </pre>
	 * @param tag
	 * @return
	 */
	private Object getValue(ELTag tag) {
		int index=0;
		String zone=tag.names[index];
		if(!zone.equals(Constant.params)&&!zone.equals(Constant.sources)){
			Object o=this.getValue(tag, this.params);
			if(o!=null)return o;
			return this.getValue(tag,this.sources);
		}
		return this.getValue(tag,this);
	}

	@Override
	public Object getValue(String elStr) {
		Parser parser=new Parser(elStr);
		ELTag tag=parser.find();
		if(tag!=null){
			return this.getValue(tag);
		}
		return null;
	}

	@Override
	public Object getValue(String elStr, Map<String, Object> map) {
		Parser parser=new Parser(elStr);
		ELTag tag=parser.find();
		if(tag!=null){
			return this.getValue(tag,map);
		}
		return null;
	}

	@Override
	public Object getAll(String elStr, Map<String, Object> map) {
		Parser parser=new Parser(elStr);
		ELTag tag=parser.find();
		if(tag!=null){
			Object o=this.getValue(tag,map);
			if(o==null)o=this.getValue(tag);
			return o;
		}
		return null;
	}

	@Override
	public String replaceValue(String elStr) {
		Parser parser=new Parser(elStr);
		ELTag tag;
		while((tag=parser.find())!=null){
			Object o=this.getValue(tag);
			if(o!=null)elStr=elStr.replace(tag.input,o.toString());
		}
		return elStr;
	}

	@Override
	public String replaceValue(String elStr, Map<String, Object> map) {
		Parser parser=new Parser(elStr);
		ELTag tag;
		while((tag=parser.find())!=null){
			Object o=this.getValue(tag,map);
			if(o!=null)elStr=elStr.replace(tag.input,o.toString());
		}
		return elStr;
	}

	@Override
	public String replaceAll(String elStr, Map<String, Object> map) {
		Parser parser=new Parser(elStr);
		ELTag tag;
		while((tag=parser.find())!=null){
			Object o=this.getValue(tag,map);
			if(o==null)o=this.getValue(tag);
			if(o!=null)elStr=elStr.replace(tag.input,o.toString());
		}
		return elStr;
	}

	@Override
	public Map<String, Object> getParams() {
		return this.params;
	}

	@Override
	public Map<String, Object> getSources() {
		return this.sources;
	}
}