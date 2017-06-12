package com.cc.report.datas.store;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.cc.report.Constant;

/**
 * <li>文件名称: Parser.java</li>
 * <li>文件描述: </li>
 * <li>其他说明: 无</li>
 * <li>创建日期: 2013-8-15</li>
 * <li>修改记录: 无</li>
 * <pre>
 * 	内容摘要: 无
 * </pre>
 * @version 版本: 1.0
 * @author  作者姓名: chenwl
 */
public class Parser {
	private static Pattern elPattern=Pattern.compile(Constant.elPattern_,Pattern.MULTILINE);
	private static Pattern elValPattern=Pattern.compile(Constant.elValPattern_);
	
	private Matcher elMatcher;
	private Matcher elValMatcher;
	private ELTag tag=new ELTag();
	
	/**
	 * 预处理一个可能含有${}表达式的字符串
	 * @param str
	 */
	protected Parser(String str){
		this.elMatcher=elPattern.matcher(str);
	}
	
	/**
	 * 找到一个${}表达式，并组装成ELTag标签对象，若没有找到返回null
	 * @return
	 */
	protected ELTag find(){
		if(this.elMatcher.find()){
			tag.input=this.elMatcher.group(0);
			String[] elVal=this.elMatcher.group(1).split(Constant.dot_);
			tag.len=elVal.length;
			tag.names=new String[tag.len];
			tag.idx=new int[tag.len];
			for(int i=0;i<tag.len;i++){
				this.elValMatcher=Parser.elValPattern.matcher(elVal[i]);
				this.elValMatcher.find();
				tag.names[i]=this.elValMatcher.group(1);
				if(this.elValMatcher.group(2)!=null){
					tag.idx[i]=Integer.parseInt(this.elValMatcher.group(2));
				}else{
					tag.idx[i]=-1;
				}
			}
			return tag;
		}else{
			return null;
		}
	}
}


