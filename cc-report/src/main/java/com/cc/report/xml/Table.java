package com.cc.report.xml;
import java.util.List;

import com.cc.report.style.IStyle;
/**
 * 类用于存储一个表格的格式
 * @author cwl
 * @version 1.0
 */
public class Table implements IStyle{
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

	private int margin;
	private int marginLeft;
	private int marginRight;
	private int marginTop;
	private int marginBottom;
	
	/**
	 * 填充占上层标签的百分率
	 */
	private int fill;
	
	/**
	 * 表格开始处理的列位置<br/>
	 * 不由配置文件决定，填充数据时，预设为page.currentCol
	 */
	private Integer beginCol;
	private Integer beginRow;
	
	/**
	 * 表格行数<br/>
	 * 不由配置文件控制,随着数据量自动变化
	 */
	private int rows;
	
	/**
	 * 表格列数<br/>
	 * 不由配置文件控制，随着数据量自动变化
	 */
	private int cols;
	
	/**
	 * 父节点<br/>
	 * 若不为顶层table，则存储
	 */
	private Td parent;
	
	/**
	 * 子节点tr获取数据源方式<br/>
	 * normal:表示一般方式
	 * subtr：表示从某个子节点中获取数据源
	 */
//	private String sourceType;
	
	/**
	 * 子节点作为数据源时的子节点索引
	 */
//	private Integer sourceTrIndex;
	
	/**
	 * 内置的行集合
	 */
	private List<Tr> trs;

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

	public int getFill() {
		return fill;
	}

	public void setFill(int fill) {
		this.fill = fill;
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

	public Td getParent() {
		return parent;
	}

	public void setParent(Td parent) {
		this.parent = parent;
	}

	public Integer getBeginCol() {
		return beginCol;
	}

	public void setBeginCol(Integer beginCol) {
		this.beginCol = beginCol;
	}

	public List<Tr> getTrs() {
		return trs;
	}

	public void setTrs(List<Tr> trs) {
		this.trs = trs;
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

	public Integer getBeginRow() {
		return beginRow;
	}

	public void setBeginRow(Integer beginRow) {
		this.beginRow = beginRow;
	}
}
