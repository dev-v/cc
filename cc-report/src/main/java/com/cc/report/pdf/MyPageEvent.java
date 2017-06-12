package com.cc.report.pdf;
import java.awt.geom.AffineTransform;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.events.PdfPageEventForwarder;
import com.cc.report.xml.Img;

/**
 * 本类用于设置页面和处理背景图片问题
 * @author cwl
 * @version 1.0
 */
public class MyPageEvent extends PdfPageEventForwarder {
	private final List<Img> imgs;
	private final float fontSize;
	private final BaseFont baseFont;
	private final float width;
	private final float height;
	private float firstX;
	private final float y;
	private int currentPage;
	private String msg;
	private int len;
	private final PdfTemplate pt;
	private final PdfTemplate backGroundpt;
	private AffineTransform aff;
	private final AffineTransform backGroundAff;
	/**
	 * 提供的pdf文档处理事件<br/>
	 * 1、背景图片处理<br/>
	 * 2、页码处理
	 * @param writer 
	 * @param imgs
	 * @param font
	 */
	public MyPageEvent(PdfWriter writer,List<Img> imgs,Font font){
		super();
		this.imgs=imgs;
		fontSize=font.getSize();
		baseFont=font.getBaseFont();
		Rectangle page=writer.getPageSize();
		width=page.getWidth();
		height=page.getHeight();
		y=5;
		
		pt=PdfTemplate.createTemplate(writer,width,height);

		backGroundAff=AffineTransform.getTranslateInstance(0,0);
		backGroundpt=PdfTemplate.createTemplate(writer, width, height);
	}
	/**
	 * 写完一页后，添加页码
	 */
	@Override
	public void onEndPage(PdfWriter writer, Document doc) {
		msg="- - -  "+(++currentPage);
		int tempLen=msg.length();
		if(tempLen>len){
			len=tempLen;
			float temp=((len-7)*2+7)*fontSize/4f;
			firstX=width/2-temp;
			aff=AffineTransform.getTranslateInstance(firstX+temp,y);
		}
		PdfContentByte con=writer.getDirectContent();
		con.setFontAndSize(baseFont,fontSize);
		con.beginText();
		con.setTextMatrix(firstX,y);
		con.showText(msg);
		con.endText();
		
		con.addTemplate(pt,aff);
		con.addTemplate(backGroundpt,backGroundAff);
		
		super.onEndPage(writer, doc);
	}

	/**
	 * 关闭文档时，写入页码结束标签和背景图片
	 */
	@Override
	public void onCloseDocument(PdfWriter writer, Document doc) {
		writeBlackGround(backGroundpt, imgs);
		pt.setFontAndSize(baseFont,fontSize);
		pt.beginText();
		pt.showText(" / "+(currentPage)+"  - - -");
		pt.endText();
		super.onCloseDocument(writer, doc);
	}

	/**
	 * 写入背景图片
	 * @param con
	 * @param imgs
	 */
	private void writeBlackGround(PdfContentByte con,List<Img> imgs){
		if(imgs!=null){
			int size=imgs.size();
			for(int i=0;i<size;i++){
				SaxImage sax=new SaxImage(imgs.get(i));
				sax.write(con);
			}
		}
	}
}
