package com.cc.report.style;
/**
 * 定义元素样式的接口
 * @author cwl
 * @version 1.0
 */
public interface IStyle {
	public int getHeight();
	public void setHeight(int height);
	public int getWidth();
	public void setWidth(int width);
	public String getBlackColor();
	public void setBlackColor(String blackColor);
	public String getAlign();
	public void setAlign(String align);
	public String getVAlign();
	public void setVAlign(String align);
	
	public String getFontStyle();
	public void setFontStyle(String fontStyle);
	public int getFontSize();
	public void setFontSize(int fontSize);
	public String getFontColor();
	public void setFontColor(String fontColor);
	public String getFontFamily();
	public void setFontFamily(String fontFamily);
	
	public String getEnding();
	public void setEnding(String ending);
	public String getBlank();
	public void setBlank(String blank);
	
	public boolean isBorder();
	public void setBorder(boolean border);
	public boolean isBorderLeft();
	public void setBorderLeft(boolean borderLeft);
	public boolean isBorderRight();
	public void setBorderRight(boolean borderRight);
	public boolean isBorderTop();
	public void setBorderTop(boolean borderTop);
	public boolean isBorderBottom();
	public void setBorderBottom(boolean borderBottom);
	public float getBorderWeight();
	public void setBorderWeight(float borderWeight);
	public String getBorderType();
	public void setBorderType(String borderType);
	public String getBorderColor();
	public void setBorderColor(String borderColor);

	public int getMargin();
	public void setMargin(int margin);
	public int getMarginLeft();
	public void setMarginLeft(int marginLeft);
	public int getMarginRight();
	public void setMarginRight(int marginRight);
	public int getMarginTop();
	public void setMarginTop(int marginTop);
	public int getMarginBottom();
	public void setMarginBottom(int marginBottom);
}
