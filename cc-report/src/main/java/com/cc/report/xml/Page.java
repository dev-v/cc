package com.cc.report.xml;
import java.util.List;

import com.cc.report.style.IStyle;

/**
 * 本类用于存储配置的页面格式
 * @author cwl
 * @version 1.0
 */
public class Page implements IStyle{
	private boolean border;
	private float borderWeight;	
	private String borderType;
	private String borderColor;
	private boolean borderLeft;
	private boolean borderRight;
	private boolean borderTop;
	private boolean borderBottom;
	
	private String fontFamily;
	private String fontStyle="normal";
	private int fontSize;
	private String fontColor;
	private String blank;
	private String ending;
	
	private String blackColor;
	private int width;
	private int height;
	private String align;
	private String vAlign;
	/**
	 * 页面纸张类型
	 */
	private String pageType;
	private int margin;
	private int marginLeft;
	private int marginRight;
	private int marginTop;
	private int marginBottom;
	

	private List<Img> images;
	/**
	 * 页面开始处理的列位置
	 */
	private int beginCol=0;
	
	/**
	 * 页面开始处理的行位置
	 */
	private int beginRow=0;
	
	/**
	 * 用于定位页面当前处理的列位置(由程序控制，不可配置)
	 */
	private int currentCol;
	
	/**
	 * 用于定位页面当前处理的行位置(由程序控制，不可配置)
	 */
	private int currentRow;
	
	/**
	 * 页面的总行数(由程序控制，不可配置)
	 */
	private int rows=0;
	
	/**
	 * 页面的总列数
	 */
	private int cols=0;
	
	/**
	 * 内置的顶层表格
	 */
	private List<Table> table;

	public boolean isBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public float getBorderWeight() {
		return borderWeight;
	}

	public void setBorderWeight(float borderWeight) {
		this.borderWeight = borderWeight;
	}

	public String getBorderType() {
		return borderType;
	}

	public void setBorderType(String borderType) {
		this.borderType = borderType;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public boolean isBorderLeft() {
		return borderLeft;
	}

	public void setBorderLeft(boolean borderLeft) {
		this.borderLeft = borderLeft;
	}

	public boolean isBorderRight() {
		return borderRight;
	}

	public void setBorderRight(boolean borderRight) {
		this.borderRight = borderRight;
	}

	public boolean isBorderTop() {
		return borderTop;
	}

	public void setBorderTop(boolean borderTop) {
		this.borderTop = borderTop;
	}

	public boolean isBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(boolean borderBottom) {
		this.borderBottom = borderBottom;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getBlank() {
		return blank;
	}

	public void setBlank(String blank) {
		this.blank = blank;
	}

	public String getEnding() {
		return ending;
	}

	public void setEnding(String ending) {
		this.ending = ending;
	}

	public String getBlackColor() {
		return blackColor;
	}

	public void setBlackColor(String blackColor) {
		this.blackColor = blackColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getVAlign() {
		return vAlign;
	}

	public void setVAlign(String align) {
		vAlign = align;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	public int getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}

	public int getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	public int getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(int marginBottom) {
		this.marginBottom = marginBottom;
	}

	public List<Img> getImages() {
		return images;
	}

	public void setImages(List<Img> images) {
		this.images = images;
	}

	public int getBeginCol() {
		return beginCol;
	}

	public void setBeginCol(int beginCol) {
		this.beginCol = beginCol;
	}

	public int getBeginRow() {
		return beginRow;
	}

	public void setBeginRow(int beginRow) {
		this.beginRow = beginRow;
	}

	public int getCurrentCol() {
		return currentCol;
	}

	public void setCurrentCol(int currentCol) {
		this.currentCol = currentCol;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public List<Table> getTable() {
		return table;
	}

	public void setTable(List<Table> table) {
		this.table = table;
	}
}
