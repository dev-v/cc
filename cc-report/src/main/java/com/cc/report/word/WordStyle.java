package com.cc.report.word;
import java.awt.Color;

import com.cc.report.Constant;
import com.cc.report.style.IStyle;
import com.cc.report.style.ReportColor;
import com.cc.report.xml.Td;
import com.lowagie.text.Cell;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;

public class WordStyle {
	static{
		/**
		 * 初始化字体样式
		 */
		try {
//			base=BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		} catch (Exception e) {
			System.out.println("*******创建基本字体错误***********");
			e.printStackTrace();
		}
	}
	
	/**
	 * 基本字体样式
	 */
	private static Font font=new Font();
	
	/**
	 * 重置字体样式
	 * @param font
	 * @param td
	 */
	static Font getFont(IStyle style){ 
		Font fontTemp=new Font(font);
		fontTemp.setColor(ReportColor.getWordColor(style.getFontColor(),Color.black));//设置字体颜色
		fontTemp.setSize(style.getFontSize());
		fontTemp.setStyle(style.getFontStyle());
//		font.setFamily("仿宋_GB2312");
		return fontTemp;
	}

	static Cell setCellStyle(Cell cell,Td td){
		cell.setWidth(td.getWidth());
		int align=Element.ALIGN_CENTER;
		if(td.getAlign().equals(Constant.LEFT))align=Element.ALIGN_LEFT;
		else if(td.getAlign().equals(Constant.RIGHT))align=Element.ALIGN_RIGHT;
		cell.setHorizontalAlignment(align);//水平对齐方式
		if(td.getVAlign().equals(Constant.TOP))align=Element.ALIGN_TOP;
		else if(td.getVAlign().equals(Constant.BOTTOM))align=Element.ALIGN_BOTTOM;
		else align=Element.ALIGN_MIDDLE;
		cell.setVerticalAlignment(align);//设置垂直对齐方式
//		Img img=td.getImg();
//		if(img!=null){
//			if(td.getAlign().equals(Constant.LEFT))align=Image.ALIGN_LEFT;
//			else if(td.getAlign().equals(Constant.RIGHT))align=Image.ALIGN_RIGHT;
//			else align=Image.ALIGN_CENTER;
//			cell.addElement(SaxImage.createImage(img,align));
//			return cell;
//		}
		//设置边框
		int border=0;
		//设置边框
		if(!td.isBorderTop())border+=Rectangle.TOP;
		if(!td.isBorderBottom())border+=Rectangle.BOTTOM;
		if(!td.isBorderLeft())border+=Rectangle.LEFT;
		if(!td.isBorderRight())border+=Rectangle.RIGHT;
		cell.setBorder(border);
		//设置单元格背景色
		cell.setBorderColor(ReportColor.getWordColor(td.getBlackColor(),Color.black));
		cell.setBackgroundColor(
				ReportColor.getWordColor(td.getBlackColor(),Color.white));//背景色
		cell.setRowspan(td.getRowSpan());//设置跨行
		cell.setColspan(td.getColSpan());//设置跨列
		return cell;
	}
}
