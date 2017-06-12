package com.cc.report.style;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.util.HSSFColor;
import com.cc.report.Constant;
import com.itextpdf.text.BaseColor;
/**
 * 对报表颜色进行统一管理
 * @author 陈文龙
 * @version 1.0
 * @update 2013-05-28
 */
public class ReportColor {
	private static final Pattern excelPattern=Pattern.compile(".*[$](.*)@.*");
	private static final Pattern javaPattern=Pattern.compile("^class java.awt.Color$");
	private static final Pattern pdfPattern=Pattern.compile("^class com.itextpdf.text.BaseColor$");
	
	//存储java中的Color的默认颜色对象
	private static final Map<String,Color> javaColor=new HashMap<String, Color>(32);
	private static final Map<String,BaseColor> PdfColor=new HashMap<String, BaseColor>(32);
	private static final Map<String,HSSFColor> excelColor=new HashMap<String, HSSFColor>(32);
//	private static final Map<String,Color> WordColor;
	static{
		initJavaColor();//初始化java默认颜色对象
		initPdfColor();
		initExcelColor();
//		WordColor=javaColor;
	}
	
	/**
	 * 方法用于根据颜色名称获取一个pdf的颜色对象返回（默认颜色值为black）
	 * @param colorName
	 * @return
	 */
	public static BaseColor getPdfColor(String colorName){
		BaseColor color=PdfColor.get(colorName);
		if(color==null)color=PdfColor.get(Constant.BLACK);
		return color;
	}
	
	/**
	 * 方法用于根据颜色名称获取一个pdf的颜色对象返回(若colorName所提供的颜色值不存在，使用默认提供的颜色值)
	 * @param colorName
	 * @return
	 */
	public static BaseColor getPdfColor(String colorName,BaseColor defaultColor){
		BaseColor color=PdfColor.get(colorName);
		return color==null?defaultColor:color;
	}
	
	/**
	 * 方法用于根据颜色名称获取一个Word的颜色对象返回（默认颜色值为black）
	 * @param colorName
	 * @return
	 */
	public static Color getWordColor(String colorName){
		Color color=javaColor.get(colorName);
		if(color==null)color=javaColor.get(Constant.BLACK);
		return color;
	}
	
	/**
	 * 方法用于根据颜色名称获取一个Word的颜色对象返回(若colorName所提供的颜色值不存在，使用默认提供的颜色值)
	 * @param colorName
	 * @return
	 */
	public static Color getWordColor(String colorName,Color defaultColor){
		Color color=javaColor.get(colorName);
		return color==null?defaultColor:color;
	}
	
	/**
	 * 根据颜色名称获取Excel报表颜色值（没有对应颜色名称的颜色返回black）
	 * @param colorName
	 * @return
	 */
	public static HSSFColor getExcelColor(String colorName){
		HSSFColor color=excelColor.get(colorName);
		return color==null?excelColor.get(Constant.BLACK):color;
	}
	
	/**
	 * 根据颜色名称获取Excel报表颜色值（没有对应颜色名称的颜色返回black）
	 * @param colorName
	 * @return
	 */
	public static HSSFColor getExcelColor(String colorName,String defaultColor){
		HSSFColor color=excelColor.get(colorName);
		return color==null?excelColor.get(defaultColor):color;
	}
	
	/**
	 * 初始化Excel报表所需颜色值
	 */
	private static void initExcelColor(){
		//获取HssfColor内置所有颜色支持
		Map<Integer,HSSFColor> map=HSSFColor.getIndexHash();
		for(HSSFColor color:map.values()){
			Matcher matcher=excelPattern.matcher(color.toString());
			matcher.find();
			excelColor.put(matcher.group(1).toLowerCase(),color);
		}
	}
	
	/**
	 * 填充java默认颜色对象的数据
	 */
	private static void initJavaColor(){
		Field[] colors=Color.class.getDeclaredFields();
		for(Field co:colors){
			if(javaPattern.matcher(co.toString()).find()){
				String colorName=co.getName();
					javaColor.put(colorName.toLowerCase(),
						Color.getColor(colorName));
			}
		}
	}
	
	/**
	 * 初始化pdf颜色值
	 */
	private static void initPdfColor(){
		BaseColor temp=BaseColor.BLUE;
		Field[] colors=BaseColor.class.getDeclaredFields();
		for(Field co:colors){
			if(pdfPattern.matcher(co.getType().toString()).find()){
				try {
					PdfColor.put(co.getName().toLowerCase(),
							(BaseColor)co.get(temp));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {}
}
