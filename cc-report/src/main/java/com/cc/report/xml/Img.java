package com.cc.report.xml;

/**
 * 类用于存储图片配置信息
 * @author cwl
 * @version 1.0
 */
public class Img {
	
	/**
	 * 图片路径
	 */
	private String src;
	
	/**
	 * 图片宽度
	 */
	private Float width;
	
	/**
	 * 图片高度（暂不可用）
	 */
	private Float height;
	
	/**
	 * 图片总体透明度
	 */
	private int transparence;

	/**
	 * 左边透明度
	 */
	private int transparenceLeft;
	
	/**
	 * 右边透明度
	 */
	private int transparenceRight;
	
	/**
	 * 起始横坐标位置
	 */
	private float beginx;
	
	/**
	 * 起始纵坐标
	 */
	private float beginy;
	
	/**
	 * 终止横坐标位置
	 */
	private Float endx;
	
	/**
	 * 终止纵坐标
	 */
	private Float endy;

	public Img(){}
	
	public Img(String src){
		this.src=src;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public Float getWidth() {
		return width;
	}
	public void setWidth(Float width) {
		this.width = width;
	}
	public Float getHeight() {
		return height;
	}
	public void setHeight(Float height) {
		this.height = height;
	}
	public int getTransparenceLeft() {
		return transparenceLeft;
	}
	public void setTransparenceLeft(int transparenceLeft) {
		this.transparenceLeft = transparenceLeft;
	}
	public int getTransparenceRight() {
		return transparenceRight;
	}
	public void setTransparenceRight(int transparenceRight) {
		this.transparenceRight = transparenceRight;
	}
	public int getTransparence() {
		return transparence;
	}
	public void setTransparence(int transparence) {
		this.transparence = transparence;
	}
	public Float getEndx() {
		return endx;
	}

	public void setEndx(Float endx) {
		this.endx = endx;
	}

	public Float getEndy() {
		return endy;
	}

	public void setEndy(Float endy) {
		this.endy = endy;
	}

	public float getBeginx() {
		return beginx;
	}

	public void setBeginx(float beginx) {
		this.beginx = beginx;
	}

	public float getBeginy() {
		return beginy;
	}

	public void setBeginy(float beginy) {
		this.beginy = beginy;
	}
}
