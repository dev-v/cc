package com.cc.report.xml;
import java.util.Map;
import com.cc.report.style.IStyle;

/**
 * 本类用于存储一个单元格的配置
 * @author cwl
 * @version 1.0
 */
public class Td implements IStyle{
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
	
	private boolean venusIp;
	/**
	 * 用于替换的文本标签
	 */
	private Map<String,String> label;
	
	private Labels labels;
	/**
	 * 存储普通图片
	 */
	private Img img;
	
	/**
	 * 列是否合并
	 */
	private boolean combine=false;

	/**
	 * 截取字符的起始位置
	 */
	private Integer begin;

	/**
	 * 截取字符的长度
	 */
	private Integer length;

	/**
	 * 跨行数（pdf目前无用）
	 */
	private Integer rowSpan;
	
	/**
	 * 跨列数
	 */
	private Integer colSpan;
	
	/**
	 * 直接填写在配置文件中的值<br/>
	 * 包括直接填入的或通过{$name}从params中取出的单个值
	 */
	private String value;
	
	/**
	 * 对应数据源中的field
	 */
//	private String field;
	
	/**
	 * 内置的table配置
	 */
	private Table table;
	
	private Double id;	
	
	/**
	 * 父节点
	 */
	private Tr Parent;

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

	public boolean isVenusIp() {
		return venusIp;
	}

	public void setVenusIp(boolean venusIp) {
		this.venusIp = venusIp;
	}

	public Map<String, String> getLabel() {
		return label;
	}

	public void setLabel(Map<String, String> label) {
		this.label = label;
	}

	public Labels getLabels() {
		return labels;
	}

	public void setLabels(Labels labels) {
		this.labels = labels;
	}

	public Img getImg() {
		return img;
	}

	public void setImg(Img img) {
		this.img = img;
	}

	public boolean isCombine() {
		return combine;
	}

	public void setCombine(boolean combine) {
		this.combine = combine;
	}

	public Integer getBegin() {
		return begin;
	}

	public void setBegin(Integer begin) {
		this.begin = begin;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}

	public Integer getColSpan() {
		return colSpan;
	}

	public void setColSpan(Integer colSpan) {
		this.colSpan = colSpan;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

//	public String getField() {
//		return field;
//	}
//
//	public void setField(String field) {
//		this.field = field;
//	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Tr getParent() {
		return Parent;
	}

	public void setParent(Tr parent) {
		Parent = parent;
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

	public Double getId() {
		return id;
	}
	
	public void setId(Double id) {
		this.id = id;
	}
}
