package com.cc.report.excel;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import com.cc.report.Constant;
import com.cc.report.style.ReportColor;
import com.cc.report.util.SourceFromClass;
import com.cc.report.xml.Td;

/**
 * 单态对象
 * @author 陈文龙
 * @version 2013-05-25
 */
@SuppressWarnings("unchecked")
public class ExcelStyle {
	private ExcelStyle(){
		//截取后，有重复的值名称，以下进行修正结果集
		CellStyleMap.put(Constant.CENTER,HSSFCellStyle.ALIGN_CENTER);
		CellStyleMap.put(Constant.FILL,HSSFCellStyle.ALIGN_FILL);
		CellStyleMap.put(Constant.MIDDLE,HSSFCellStyle.VERTICAL_CENTER);
		CellStyleMap.put(Constant.JUSTIFY,HSSFCellStyle.ALIGN_JUSTIFY);
		CellStyleMap.put(Constant.VJUSTIFY,HSSFCellStyle.VERTICAL_JUSTIFY);
	}
	private static final Map<String,Short> CellStyleMap=(Map<String,Short>)SourceFromClass.getFields(CellStyle.class,"_",2,1);
	private Map<Td,HSSFCellStyle> tdStyles=new HashMap<Td, HSSFCellStyle>();
	private static ExcelStyle instance;
	public static ExcelStyle getInstance(){
//		if(instance==null)
			instance=new ExcelStyle();
		return instance;
	}
	/**
	 * 
	 * 背景颜色填充模式值
		NO_FILL
		SOLID_FOREGROUND 平铺
		FINE_DOTS 点状
		ALT_BARS
		SPARSE_DOTS 细斑点
		THICK_HORZ_BANDS 粗水平格线
		THICK_VERT_BANDS 粗垂直格线
		THICK_BACKWARD_DIAG粗右斜线斑点
		THICK_FORWARD_DIAG粗左斜线斑点
		BIG_SPOTS 大斑点
		BRICKS
		THIN_HORZ_BANDS 水平格线
		THIN_VERT_BANDS 垂直格线
		THIN_BACKWARD_DIAG右斜线斑点
		THIN_FORWARD_DIAG 左斜线斑点
		SQUARES 方格网状
		DIAMONDS 钻石型网状
	 * @param style
	 * @param myStyle
	 */
	public void createStyle(HSSFWorkbook excel,Td td){
		HSSFCellStyle style=excel.createCellStyle();//创建一个样式
		style.setWrapText(true);
		//边框设置
		short borderColor=ReportColor.getExcelColor(td.getBorderColor()).getIndex();
		style.setTopBorderColor(borderColor);
		style.setBottomBorderColor(borderColor);
		style.setLeftBorderColor(borderColor);
		style.setRightBorderColor(borderColor);
		short borderType=CellStyleMap.get(td.getBorderType());
		if(td.isBorderBottom())style.setBorderBottom(borderType);
		if(td.isBorderRight())style.setBorderRight(borderType);
		if(td.isBorderTop())style.setBorderTop(borderType);
		if(td.isBorderLeft())style.setBorderLeft(borderType);
		
		style.setAlignment(CellStyleMap.get(td.getAlign()));//设置水平对齐方式
		
		style.setVerticalAlignment(CellStyleMap.get(td.getVAlign()));//设置垂直对齐方式
		
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );//背景模式
		
		style.setFillForegroundColor(ReportColor.getExcelColor(td.getBlackColor()).getIndex());
//		
		HSSFFont font=excel.createFont();
//		if(font==null)font=excel.createFont();
		//设置字体
		font.setColor(ReportColor.getExcelColor(td.getFontColor()).getIndex());//字体颜色
		font.setFontHeightInPoints((short)td.getFontSize());//字体大小
		font.setFontName(td.getFontFamily());//字体类型
		
		String myFontStyle=td.getFontStyle();//字体样式
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		font.setItalic(false);
//		font.setStrikeout(false);
//		font.setTypeOffset(HSSFFont.SS_NONE);
//		font.setUnderline(HSSFFont.U_NONE);
		if(myFontStyle.contains(Constant.BOLD))font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		if(myFontStyle.contains(Constant.ITALIC))font.setItalic(true);
		if(myFontStyle.contains(Constant.STRIKE))font.setStrikeout(true);
		if(myFontStyle.contains(Constant.SUP))font.setTypeOffset(HSSFFont.SS_SUPER);
		if(myFontStyle.contains(Constant.SUB))font.setTypeOffset(HSSFFont.SS_SUB);
		if(myFontStyle.contains(Constant.UNDERLINE))font.setUnderline(HSSFFont.U_SINGLE);
		
		style.setFont(font);//设置字体样式
		
		tdStyles.put(td,style);
	}
	
	public HSSFCellStyle getStyle(Td td){
		return tdStyles.get(td);
	}
}
