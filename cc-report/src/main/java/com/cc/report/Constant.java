package com.cc.report;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于一些常量的管理
 * @author cwl
 * @version 1.0
 */
public class Constant {
	public static final String ALIGN="align";
	public static final String BEGINROW="beginRow";
	public static final String BEGINCOL="beginCol";
	public static final String BORDER="border";
	public static final String BORDERTYPE="borderType";
	public static final String BORDERWEIGHT="borderWeight";
	public static final String BORDERCOLOR="borderColor";
	public static final String BORDERTOP="borderTop";
	public static final String BORDERBOTTOM="borderBottom";
	public static final String BORDERLEFT="borderLeft";
	public static final String BORDERRIGHT="borderRight";
	public static final String BOTTOM="bottom";
	public static final String BLACK="black";
	public static final String BLACKCOLOR="blackColor";
	public static final String BLANK="blank";
	public static final String BEGIN="begin";
	public static final String BEGINX="beginx";
	public static final String BEGINY="beginy";
	public static final String BOLD="bold";
	public static final String CENTER="center";
	public static final String COLS="cols";
	public static final String COLSPAN="colSpan";
	public static final String COMBINE="combine";
	public static final String COMPLEMENT="complement";
	public static final String CSV="csv";
	/**
	 * 无数据时，是否补齐空白内容对象
	 */
	public static final Map<String,Object> COMPLEMENTMAP=new HashMap<String, Object>();
	public static final String DAO="dao";
	public static final String DOC="doc";
	public static final String ENDING="ending";
	public static final String ENDX="endx";
	public static final String ENDY="endy";
	public static final String FILL="fill";
	public static final String FILENAME="filename";
	public static final String FONTFAMILLY="fontFamily";
	public static final String FONTSTYLE="fontStyle";
	public static final String FONTSIZE="fontSize";
	public static final String FONTCOLOR="fontColor";
	public static final String FIELD="field";
	public static final String FIELDS="fields";
	public static final String HEIGHT="height";
	public static final String HIBERNATE="hibernate";
	public static final String IMAGE="image";
	public static final String IMG="img";
	public static final String ITALIC="italic";
	public static final String JUSTIFY="justify";
	public static final String KEY="key";
	public static final String LABEL="label";
	public static final String LABELS="labels";
	public static final String lENGTH="length";
	public static final String LEFT="left";
	public static final String LIST="list";
	public static final String LOCAL="local";
	public static final String MARGIN="margin";
	public static final String MAP="map";
	public static final String MIDDLE="middle";
	public static final String MARGINLEFT="marginLeft";
	public static final String MARGINRIGHT="marginRight";
	public static final String MARGINTOP="marginTop";
	public static final String MARGINBOTTOM="marginBottom";
	public static final String METHOD="method";
	public static final String NAME="name";
	public static final String NORMAL="normal";
	public static final String PARAMS="params";
	public static final String PAGE="page";
	public static final String PAGETYPE="pageType";
	public static final String PARENT="parent";
	public static final String PDF="pdf";
	public static final String PO="po";
	public static final String ROWSPAN="rowSpan";
	public static final String RIGHT="right";
	public static final String SOURCES="sources";
	public static final String SQL="sql";
	public static final String SRC="src";
	public static final String SOURCE="source";
	public static final String SOURCETYPE="sourceType";
	public static final String STRIKE="strike";
	public static final String SUP="sup";
	public static final String SUB="sub";
	public static final String TOP="top";
	public static final String TYPE="type";
	public static final String TRANSPARENCE="transparence";
	public static final String TRANSPARENCELEFT="transparenceLeft";
	public static final String TRANSPARENCERIGHT="transparenceRight";
	public static final String TABLE="table";
	public static final String THIN="thin";
	public static final String UNDERLINE="underline";
	public static final String VENUSIP="venusIp";
	public static final String VALUE="value";
	public static final String VALIGN="vAlign";
	public static final String VJUSTIFY="vjustify";
	public static final String WIDTH="width";
	public static final String XLS="xls";
	
	public static final String openBrace_="{";
	public static final String openAngle_="<";
	public static final String dot_="\\.";
	
	public static final String elPattern_
		="\\$\\{((\\p{Alpha}+(\\[\\d\\])?(\\.)?)+)\\}";
	public static final String elValPattern_
		="(\\p{Alpha}+)(\\[(\\d)\\])?";
	
	/* 一些值域常量 */
	public static final String params="params";
	public static final String sources="sources";
	public static final String customer="customer";

	//用于数据量（内存足以存放所有数据）不大情况下CSV导出时，存放文件的初始缓存大小
	public static final int CSVFileBuffer=1024*1024;
	
	//Excel报表所选常量
	public static final int EXCEL_FONT_SIZE_BASE=25;
	/**
	 * 列宽度基数（30）
	 */
	public static final int EXCEL_COLUMN_WIDTH_BASE=256;
	public static final String EXCEL_DEFAULT_FONT_LENGTH="123456789";
}
