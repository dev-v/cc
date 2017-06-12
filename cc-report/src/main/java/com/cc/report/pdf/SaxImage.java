package com.cc.report.pdf;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.cc.report.xml.Img;

/**
 * 本类根据配置信息生成普通图片或者背景图片
 * @author cwl
 * @version 1.0
 */
public class SaxImage{
	
	/**
	 * 要生成图片的配置信息
	 */
	private Img img;
	
	/**
	 * 一个比例参数
	 */
	private static float percent=100.0f;
	
	/**
	 * 一个构造器
	 * @param img
	 */
	public SaxImage(Img img){
		this.img=img;
	}
	
	/**
	 * 设置图片显示模式（采取从左到右渐变方式）
	 * @return
	 */
	private byte[] getGradient(){
		float transparence=img.getTransparence()/percent;
		float left=img.getTransparenceLeft()/percent*transparence;
		float right=img.getTransparenceRight()/percent*transparence;
		float range=(right-left)/256;
	    byte[] gradient = new byte[256];
	    for (int k = 0; k < 256; ++k) {
	        gradient[k] =(byte)((left+range*k)*255);
	    }
		return gradient;
	}
	
	/**
	 * 生成图片的显示模式
	 * @return
	 */
	private Image getMask(){
		Image image=null;
		try {
			image = Image.getInstance(256
						,1,1,8,getGradient());
			image.makeMask();
		} catch (DocumentException e) {
			System.out.println("********    制作图片显示模式错误！        ********");
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * 将图片写出到一个PdfContentByte对象中<br/>
	 * pdf页面的左下角为坐标原点
	 */
	public void write(PdfContentByte con){
		try {
			PdfWriter writer=con.getPdfWriter();
			Image mask=getMask();
			Image image=createImage();
			if(image==null)return;
			image.setImageMask(mask);
			Rectangle page=writer.getPageSize();
			float pageWidth=page.getWidth();
			float pageHeight=page.getHeight();
			
			float width=image.getWidth()*(img.getWidth()/100);
			float height=image.getHeight()*(img.getHeight()/100);
			
			float beginx=img.getBeginx()<0?0:img.getBeginx();
			float beginy=img.getBeginy()<0?0:img.getBeginy();
			
			Float endx=img.getEndx();
			Float endy=img.getEndy();
			
			endx=(endx==null||endx>pageWidth)?pageWidth:endx;
			endy=(endy==null||endy>pageHeight)?pageHeight:endy;
			
			int rows=(int)((endy-beginy)/height+1);
			int cols=(int)((endx-beginx)/width+1);
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					float x=beginx+j*width;
					float y=beginy+i*height;
					image.setAbsolutePosition(x,y);
					con.addImage(image);
				}
			}
		} catch (DocumentException e) {
			System.out.println("**********    设置图片模式错误！      *************");
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建图片的重载模式
	 * @return
	 */
	private Image createImage(){
		return createImage(this.img,null);
	}
	
	/**
	 * 根据配置信息生成图片
	 * @param img
	 * @param align
	 * @return
	 */
	public static Image createImage(Img img,Integer align){
		Image image=null;
		try {
			image=Image.getInstance(img.getSrc());
			if(align!=null)image.setAlignment(align);//设置水平对齐方式
			//设置宽度和高度（高度暂时不起作用）
			image.scalePercent(img.getWidth(),img.getHeight());
		} catch (Exception e) {
			System.out.println("**********    生成图片错误      *************");
			e.printStackTrace();
		}
		return image;
	}
}