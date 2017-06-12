package com.cc.report.pdf;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.cc.report.Constant;
import com.cc.report.datas.ValueUtil;
import com.cc.report.datas.store.IStore;
import com.cc.report.style.IStyle;
import com.cc.report.style.ReportColor;
import com.cc.report.util.TransformValue;
import com.cc.report.venustech.VenusTechUtil;
import com.cc.report.xml.Img;
import com.cc.report.xml.Labels;
import com.cc.report.xml.Page;
import com.cc.report.xml.Table;
import com.cc.report.xml.Td;
import com.cc.report.xml.Tr;

/**
 * 本类用于实现pdf报表的生成
 * @author cwl
 * @version 1.0
 */
public class PdfImpl20130816{
	
	/**
	 * 解析出的页面格式信息
	 */
	private Page page;
	
	/**
	 * 获取的数据
	 */
	private IStore store;
	
	/**
	 * pdf文档
	 */
	private Document doc;
	
	/**
	 * 基本字体样式
	 */
	private Font font;

	/**
	 * 一个构造器<br/>
	 * 根据配置的页面信息和存储的数据生成pdf报表，
	 * 并绑定到一输出流中
	 * @param page
	 * @param values
	 * @param out
	 */
	public PdfImpl20130816(Page page,IStore store,OutputStream out){
		this.page=page;
		this.store=store;
		initFont();
		createPage(out);
	}
	
	/**
	 * 创建pdf报表，并设置内容栏的样式
	 * @param out
	 */
	private void createPage(OutputStream out){
		Rectangle rec=null;
		//设置页面宽度和高度
		if(page.getPageType()!=null){
			rec=new Rectangle(PageSize.getRectangle(page.getPageType()));
		}else{
			rec=new Rectangle(page.getWidth(),page.getHeight());
		}
		//设置页面背景色
		rec.setBackgroundColor(ReportColor.getPdfColor(page.getBlackColor(),BaseColor.WHITE));
		//新建一个文档
		doc=new Document(rec,page.getMarginLeft(),page.getMarginRight(),
				page.getMarginTop(),page.getMarginBottom());
		PdfWriter writer=null;
		try {//将文档绑定到一个输出流中
			writer=PdfWriter.getInstance(doc,out);
//			HtmlWriter hw=new HtmlWriter(doc,out);
		} catch (DocumentException e) {
			System.out.println("*********绑定pdf文档输出流错误！*********");
			e.printStackTrace();
		}
		//打开文档
		doc.open();
		writer.setPageEvent(new MyPageEvent(writer,page.getImages(),font));
		//往文档中写入数据
		List<Table> tables=page.getTable();
		try {
			for(Table tempTable:tables){
				PdfPTable pdfTable=createTable(tempTable,store.getSources());
				doc.add(pdfTable);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		//关闭文档
		doc.close();
	}
	/**
	 * 创建表格
	 * @param table
	 * @param map
	 * @return
	 */
	private PdfPTable createTable(Table table,Map<String,Object> map) {
		PdfPTable pdftable=new PdfPTable(1);//顶层表格
		setTableStyle(pdftable, table);//设置表格样式
		List<Tr> trs=table.getTrs();
		List<PdfPCell[]> tempCells=null;
		for(Tr tr:trs){//每个行
			PdfPCell cell=new PdfPCell();
			setCellStyle(cell,tr);//设置行样式
			PdfPTable pdftableTemp=new PdfPTable(getWidths(tr));
			setTableStyle(pdftableTemp, table);//设置表格样式
			PdfPCell[] cells=null;
			String sourceName=tr.getSource();
			if(sourceName==null){//
				cells=createTr(tr,null);
			}else{//
				tempCells=createTrs(tr,map);
			}
			if(cells!=null){
				for(PdfPCell c:cells){
					if(c!=null)pdftableTemp.addCell(c);
				}
				cells=null;
			}else{
				for(PdfPCell[] cellsTemp:tempCells){
					for(PdfPCell c:cellsTemp){
						if(c!=null)pdftableTemp.addCell(c);
					}
				}
				tempCells=null;
			}
			cell.addElement(pdftableTemp);
			pdftable.addCell(cell);
		}
		return pdftable;
	}
	
	/**
	 * 创建多行数据形式<br/>
	 * 内含和并列的操作
	 * @param tr
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PdfPCell[]> createTrs(Tr tr,Map<String,Object> map){
		String sourceName=tr.getSource();
		System.out.println(sourceName);
		List<PdfPCell[]> rows=new ArrayList<PdfPCell[]>();
		List<Map<String,Object>> source=(List<Map<String,Object>>)store.getAll(sourceName, map);
		if(source==null||source.size()==0){
			if(tr.isComplement()){
				rows.add(createTr(tr,Constant.COMPLEMENTMAP));
			}
		}else{
			//此处开始建立多行具体值
			int size=source.size();
			for(int i=0;i<size;i++){//首先写入值,并初始化要操作的数据
				Map<String,Object> m=source.get(i);
				PdfPCell[] temp=createTr(tr,m);
				rows.add(temp);
			}
			
			//首先获取需要合并的列索引
			List<Integer> cols=new ArrayList<Integer>();
			List<Td> tds=tr.getTds();
			int columnSize=tds.size();
			for(int i=0;i<columnSize;i++){
				Td td=tds.get(i);
				if(td.isCombine()){
					cols.add(i);
				}
			}
			//------------合并列-----------------
			for(int i=0;i<rows.size();i++){
				PdfPCell[] row=rows.get(i);
				for(int col:cols){//合并需要合并的列
					PdfPCell tempCell=row[col];
					String value=tempCell.getPhrase().getChunks().get(0).toString();
					for(int j=i-1;j>=0;j--){
						PdfPCell[] prevRow=rows.get(j);
						PdfPCell prevCell=prevRow[col];
						if(prevCell==null)continue;
						String prevValue=prevCell.getPhrase().getChunks().get(0).toString();
						if(value.equals(prevValue)){
							row[col]=null;
							prevCell.setRowspan(prevCell.getRowspan()+1);
							break;
						}else{
							break;
						}
					}
				}
			}
		}
		return rows;
	}
	
	/**
	 * 创建单行
	 * @param tr
	 * @param map
	 * @return
	 */
	private PdfPCell[] createTr(Tr tr,Map<String,Object> map){
		List<Td> tds=tr.getTds();
		int len=tds.size();
		PdfPCell[] cells=new PdfPCell[len];
		for(int i=0;i<len;i++){
			Td td=tds.get(i);
			cells[i]=createTd(td,map);
		}
		return cells;
	}
	
	/**
	 * 创建一行的具体数据
	 * @param tr
	 * @param map
	 * @return
	 */
	private PdfPCell createTd(Td td,Map<String,Object> map){
		Table table=td.getTable();
		if(table==null){//表示其下没有了table进行填入值操作
			return fillValue(td, map);
		}else{
			PdfPCell cell=new PdfPCell();
			//设置边框
			setCellStyle(cell,td);
			cell.setColspan(td.getColSpan());
			PdfPTable pdfTable=createTable(table, map);
			cell.addElement(pdfTable);
			return cell;
		}
	}
	/**
	 * 往td中写入值
	 * @param td
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PdfPCell fillValue(Td td,Map<String,Object> map){
		PdfPCell cell=new PdfPCell();
		cell.setRowspan(td.getRowSpan());//设置跨行
		cell.setColspan(td.getColSpan());//设置跨列
		setCellStyle(cell,td);
		Img img=td.getImg();
		if(img!=null){
			int align=Image.ALIGN_CENTER;
			if(td.getAlign().equals("left"))align=Image.ALIGN_LEFT;
			else if(td.getAlign().equals("right"))align=Image.ALIGN_RIGHT;
			cell.addElement(SaxImage.createImage(img,align));
			return cell;
		}

//		Object o=null;
//		String field=td.getField();
//		if(field!=null){
//			o=map.get(field);
//		}else{
//			o=values.replaceNamesToValues(td.getValue(),map);
//		}
//		if(o==null)o=td.getBlank();
//		String value=null;
//		
//		Labels labels=td.getLabels();
//		if(labels!=null){
//			List<Map<String,String>> list=null;
//			String source=labels.getSource();
//			if(labels.getSourceType().equals(Constant.SOURCES)){
//				list=(List<Map<String,String>>)values.getSources().get(source);
//			}else{
//				list=(List<Map<String,String>>)map.get(source);
//			}
//			Map<String,String> labelMap=new HashMap<String, String>();
//			for(Map<String,String> temp:list){
//				labelMap.put(temp.get(labels.getValue()),temp.get(labels.getLabel()));
//			}
//			td.setLabel(labelMap);
//		}
		
//		value=TransformValue.saxValueToLabel(o.toString(),td.getLabel());
//		value=TransformValue.subStr(value, td.getBegin(),td.getLength(),td.getEnding());
		String value=ValueUtil.parseTdValue(td, store, map);
		Phrase p=new Phrase();
		Font font=new Font(this.font);
		resetFont(font,td);//重设字体
		Chunk chunk=new Chunk();
		chunk.setFont(font);
		chunk.append(value);
		p.add(chunk);
		cell.setPhrase(p);
		return cell;
	}

	/**
	 * 初始化字体样式
	 */
	private void initFont(){
		BaseFont base=null;
		try {
			base=BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		} catch (Exception e) {
			System.out.println("*******创建基本字体错误***********");
			e.printStackTrace();
		}
		font=new Font(base);
		font.setSize(page.getFontSize());
		font.setColor(ReportColor.getPdfColor(page.getFontColor()));//设置字体颜色
		font.setStyle(page.getFontStyle());
		font.setFamily(page.getFontFamily());
	}
	
	/**
	 * 重置字体样式
	 * @param font
	 * @param td
	 */
	private void resetFont(Font font,Td td){ 
		font.setColor(ReportColor.getPdfColor(td.getFontColor()));//设置字体颜色
		font.setSize(td.getFontSize());
		font.setStyle(td.getFontStyle());
		font.setFamily(td.getFontFamily());
	}
	
	private void setCellStyle(PdfPCell cell,IStyle style){
		String temp;
		cell.setBackgroundColor(ReportColor.getPdfColor(style.getBlackColor()));
		
		//边框样式还需要设置
		cell.setBorderColor(ReportColor.getPdfColor(style.getBorderColor()));
		cell.setBorder(0);
		float borderWeight=style.getBorderWeight();
		if(style.isBorderBottom())cell.setBorderWidthBottom(borderWeight);
		if(style.isBorderLeft())cell.setBorderWidthLeft(borderWeight);
		if(style.isBorderRight())cell.setBorderWidthRight(borderWeight);
		if(style.isBorderTop())cell.setBorderWidthTop(borderWeight);
		
		int value=PdfPCell.ALIGN_CENTER;
		temp=style.getAlign();
		if(temp.equals(Constant.LEFT))value=PdfPCell.ALIGN_LEFT;
		else if(temp.equals(Constant.RIGHT))value=PdfPCell.ALIGN_RIGHT;
		cell.setHorizontalAlignment(value);
		
		temp=style.getVAlign();
		if(temp.equals(Constant.MIDDLE))value=PdfPCell.ALIGN_MIDDLE;
		else if(temp.equals(Constant.TOP))value=PdfPCell.ALIGN_TOP;
		else value=PdfPCell.ALIGN_BOTTOM;
		cell.setVerticalAlignment(value);

		cell.setPaddingBottom(style.getMarginBottom());
		cell.setPaddingLeft(style.getMarginLeft());
		cell.setPaddingRight(style.getMarginRight());
		cell.setPaddingTop(style.getMarginTop());
//		cell.setPadding(0);
	}
	
	/**
	 * 设置单元格内容间距
	 * @param cell
	 * @param style
	 */
//	private void setCellPadding(PdfPCell cell,IStyle style){
//	}
	
	private void setTableStyle(PdfPTable table1,Table table2){
		table1.setSplitLate(false); //解决下面空白问题 
		table1.setSplitRows(true);  
		table1.setWidthPercentage(table2.getFill());
	}
	
	/**
	 * 设置单元格中各列所占据宽度的百分比
	 * @param tr
	 * @return
	 */
	private float[] getWidths(Tr tr){
		List<Td> tds=tr.getTds();
		int size=tds.size();
		float[] temp=new float[size];
		int[] widths=new int[size];
		float all=0;
		for(int i=0;i<size;i++){
			widths[i]=tds.get(i).getWidth();
			if(widths[i]==0)widths[i]=20;
			all+=widths[i];
		}
		for(int i=0;i<size;i++){
			temp[i]=widths[i]/all;
		}
		return temp;
	}
}
