package com.cc.report.xml;
import java.util.List;

import com.cc.report.style.IStyle;
/**
 * 本类用于存储一行的样式信息
 * @author cwl
 * @version 1.0
 */
public class Tr implements IStyle{
	private boolean border;
	private float borderWeight;	
	private String borderType;
	private String borderColor;
	private boolean borderLeft;
	private boolean borderRight;
	private boolean borderTop;
	private boolean borderBottom;
	
	//设置当从数据库中查出无数据时，是否补齐空值
	private boolean complement;
	
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
	 * 所取的数据源名称<br/>
	 * 若为local（默认）则表示直接在配置文件中配置
	 */
	private String source;
	
	/**
	 * 用于记录行所占据行数<br/>
	 * 不由配置文件配置，根据数据量自动变化
	 */
	private int rows;
	
	/**
	 * 所占据列数<br/>
	 * 不由配置文件配置，根据数据量自动变化
	 */
	private int cols;//用于记录行所占据行数
	
	/**
	 * 父节点
	 */
	private Table parent;
	
	/**
	 * 内置单元格配置
	 */
	private List<Td> tds;
	
	/**
	 * 是否每页重复显示<br/>
	 * 默认为false
	 */
//	private Boolean viewInEveryPage=false;
	
	/**
	 * 是否为表头<br/>
	 * 默认为false
	 */
//	private boolean th=false;
	

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public Table getParent() {
		return parent;
	}

	public void setParent(Table parent) {
		this.parent = parent;
	}

	public List<Td> getTds() {
		return tds;
	}

	public void setTds(List<Td> tds) {
		this.tds = tds;
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

	public boolean isComplement() {
		return complement;
	}

	public void setComplement(boolean complement) {
		this.complement = complement;
	}
}
