package com.cc.report.word;
import java.awt.Color;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.document.RtfDocument;
import com.cc.report.Constant;
import com.cc.report.datas.Values;
import com.cc.report.venustech.VenusTechUtil;
import com.cc.report.style.ReportColor;
import com.cc.report.util.TransformValue;
import com.cc.report.xml.Labels;
import com.cc.report.xml.Page;
import com.cc.report.xml.Table;
import com.cc.report.xml.Td;
import com.cc.report.xml.Tr;

/**
 * 本类用于实现pdf报表的生成
 * @author 陈文龙
 */
public class WordImpl{
	
	/**
	 * 解析出的页面格式信息
	 */
	private Page page;
	
	/**
	 * 获取的数据
	 */
	private Values values;
	
	/**
	 * pdf文档
	 */
	private RtfDocument doc;

	/**
	 * 一个构造器<br/>
	 * 根据配置的页面信息和存储的数据生成pdf报表，
	 * 并绑定到一输出流中
	 * @param page
	 * @param values
	 * @param out
	 */
	public WordImpl(Page page,Values values,OutputStream out){
		this.page=page;
		this.values=values;
		createPage(out);
	}
	
	/**
	 * 创建pdf报表，并设置内容栏的样式
	 * @param out
	 */
	private void createPage(OutputStream out){
//		Rectangle rec=null;
//		//设置页面宽度和高度
//		if(page.getPageType()!=null){
//			rec=new Rectangle(PageSize.getRectangle(page.getPageType()));
//		}else{
//			rec=new Rectangle(page.getWidth(),page.getHeight());
//		}
		//设置页面背景色
//		rec.setBackgroundColor(Color.white);
		rec.setBackgroundColor(ReportColor.getWordColor(page.getBlackColor(),Color.white));
		//新建一个文档
		doc=new RtfDocument(rec,page.getMarginLeft(),page.getMarginRight(),
				page.getMarginTop(),page.getMarginBottom());
//		RtfWriter2 writer=null;
		try {//将文档绑定到一个输出流中
//			writer=RtfWriter2.getInstance(doc,out);
			RtfWriter2.getInstance(doc,out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//打开文档
		doc.open();
//		writer.setPageEvent(new MyPageEvent(writer,page.getImages(),font));
		//往文档中写入数据
		List<Table> tables=page.getTable();
		try {
			for(Table tempTable:tables){
				if(tempTable.isBorder()){//如果带有边框，按表格处理
					com.lowagie.text.Table wordTable=createTable(tempTable,values.getSources());
					doc.add(wordTable);
				}else{//若不带有边框，按段落处理
					List<Paragraph> phrases=createPhrase(tempTable,values.getSources());
					for(Paragraph phrase:phrases){
						phrase.setAlignment(tempTable.getAlign());
						doc.add(phrase);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//关闭文档
		doc.close();
	}
	
	@SuppressWarnings("unchecked")
	private List<Paragraph> createPhrase(Table table,Map<String,Object> map){
		List<Tr> trs=table.getTrs();
		List<Paragraph> phrases=new ArrayList<Paragraph>();
		for(Tr tr:trs){
			Paragraph row=null;
			String sourceName=tr.getSource();
			if(sourceName.equals(Constant.LOCAL)){//
				row=createTrPhrase(tr,null);
			}else if(sourceName.equals(Constant.PARENT)){
				row=createTrPhrase(tr,map);
			}else if(sourceName.equals(Constant.SOURCES)){
				row=createTrPhrase(tr,values.getSources());
			}else{//
				List<Paragraph> rowsTemp;
				List<Map<String,Object>> source=(List<Map<String,Object>>)map.get(sourceName);
				if(source==null)throw new RuntimeException("<tr>所配置数据源  "+sourceName+"  不存在");
				rowsTemp=createTrsParagraph(tr,source);
				phrases.addAll(rowsTemp);
			}
			if(row!=null)phrases.add(row);
		}
		return phrases;
	}
	
	private List<Paragraph> createTrsParagraph(Tr tr,List<Map<String,Object>> source){
		List<Paragraph> ps=new ArrayList<Paragraph>();
		for(Map<String,Object> map:source){
			ps.add(createTrPhrase(tr, map));
		}
		return ps;
	}
	private Paragraph createTrPhrase(Tr tr,Map<String,Object> map){
		Paragraph p=new Paragraph();
//		p.setAlignment(alignment)
//		p.setLeading(fixedLeading)
		List<Td> tds=tr.getTds();
		for(Td td:tds){
			p.add(createTdChunk(td,map));
		}
		return p;
	}
	
	private Chunk createTdChunk(Td td,Map<String,Object> map){
		String field=td.getField();
		String value=null;
		if(field==null){
			value=values.replaceNamesToValues(td.getValue(),map);
		}else{
			Object o=null;
			if(map==null)
				o=values.replaceNamesToValues(td.getField(),null);
			else{
				o=map.get(field);	
			}
			value=o.toString();
		}
		value=TransformValue.saxValueToLabel(value,td.getLabel());
		value=TransformValue.subStr(value,td.getBegin(), td.getLength(),td.getEnding());
		Chunk chunk=new Chunk(value);
		chunk.setBackground(ReportColor.getWordColor(td.getBlackColor(),Color.white));
		chunk.setFont(WordStyle.getFont(td));
		return chunk;
	}
	/**
	 * 创建表格
	 * @param table
	 * @param map
	 * @return
	 * @throws BadElementException 
	 */
	@SuppressWarnings("unchecked")
	private com.lowagie.text.Table createTable(Table table,Map<String,Object> map) throws BadElementException {
		com.lowagie.text.Table wordtable=new com.lowagie.text.Table(table.getCols());
		wordtable.setAlignment(table.getAlign());
		wordtable.setWidth(table.getFill());
		List<Tr> trs=table.getTrs();
		List<Row> rows=new ArrayList<Row>();
		for(Tr tr:trs){
			Row row=null;
			String sourceName=tr.getSource();
			if(sourceName.equals(Constant.LOCAL)){//
				row=createTr(tr,null);
			}else if(sourceName.equals(Constant.PARENT)){
				row=createTr(tr,map);
			}else if(sourceName.equals(Constant.SOURCES)){
				row=createTr(tr,values.getSources());
			}else{//
				List<Row> rowsTemp;
				List<Map<String,Object>> source=(List<Map<String,Object>>)map.get(sourceName);
				if(source==null)throw new RuntimeException("<tr>所配置数据源  "+sourceName+"  不存在");
				rowsTemp=createTrs(tr,source);
				rows.addAll(rowsTemp);
			}
			if(row!=null)rows.add(row);
		}
		for(Row row:rows){
			for(Cell cell:row.getCells()){
				if(cell!=null)
					wordtable.addCell(cell);
			}
		}
		return wordtable;
	}
	
	/**
	 * 创建单行
	 * @param tr
	 * @param map
	 * @return
	 * @throws BadElementException 
	 */
	private Row createTr(Tr tr,Map<String,Object> map) throws BadElementException{
		List<Td> tds=tr.getTds();
		int size=tds.size();
		Cell[] cells=new Cell[size];
		Row row=new Row(cells);
		for(int i=0;i<size;i++){
			cells[i]=createTd(tds.get(i),map);
		}
		return row;
	}

	/**
	 * 创建多行数据形式<br/>
	 * 内含和并列的操作
	 * @param tr
	 * @param map
	 * @return
	 * @throws BadElementException 
	 */
	@SuppressWarnings("unchecked")
	private List<Row> createTrs(Tr tr,List<Map<String,Object>> source) throws BadElementException{
//		String sourceName=tr.getSource();
		//------------一些需要用到的临时变量---------------
		List<Row> rows=new ArrayList<Row>();
		//此处开始建立多行具体值
		int size=source.size();
		for(int i=0;i<size;i++){//首先写入值,并初始化要操作的数据
			Map<String,Object> m=source.get(i);		
			rows.add(createTr(tr,m));
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
		for(int col:cols){
			int rowSize=rows.size();
			for(int i=0;i<rowSize;){
				Cell[] cells1=rows.get(i).getCells();
				String value1=cells1[col].getChunks().get(0).toString();
				int j=i+1;
				for(;j<rowSize;j++){
					Cell[] cells2=rows.get(j).getCells();
					String value2=cells2[col].getChunks().get(0).toString();
					if(value1.equals(value2)){//如果与前一列相同合并
						cells1[col].setRowspan(cells1[col].getRowspan()+cells2[col].getRowspan());
						cells2[col]=null;
					}else{//如果与前一列不同，重新定义顶部列的值
						break;
					}
				}
				i=j;
			}
		}
		return rows;
	}
	
	/**
	 * 创建一行的具体数据
	 * @param tr
	 * @param map
	 * @return
	 * @throws BadElementException 
	 */
	private Cell createTd(Td td,Map<String,Object> map) throws BadElementException{
		Table table=td.getTable();
		Cell cell=null;
		if(table==null){//表示其下没有了table进行填入值操作
			cell=fillValue(td, map);
		}else{
			cell=new Cell();
			cell.setColspan(td.getColSpan());
			com.lowagie.text.Table wordTable=createTable(table, map);
			cell.addElement(wordTable);
		}
		return cell;
	}
	
	/**
	 * 往td中写入值
	 * @param td
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Cell fillValue(Td td,Map<String,Object> map){
		Cell cell=new Cell();
		
		String field=td.getField();
		String value=null;
		if(field==null){
			value=values.replaceNamesToValues(td.getValue(),map);
		}else{
			Object o=null;
			if(map==null)
				o=values.replaceNamesToValues(td.getField(),null);
			else{
				o=map.get(field);	
			}
			value=o.toString();
		}
		Labels labels=td.getLabels();
		if(labels!=null){
			List<Map<String,String>> list=null;
			String source=labels.getSource();
			if(labels.getSourceType().equals(Constant.SOURCES)){
				list=(List<Map<String,String>>)values.getSources().get(source);
			}else{
				list=(List<Map<String,String>>)map.get(source);
			}
			Map<String,String> labelMap=new HashMap<String, String>();
			for(Map<String,String> temp:list){
				labelMap.put(temp.get(labels.getValue()),temp.get(labels.getLabel()));
			}
			td.setLabel(labelMap);
		}
		value=TransformValue.saxValueToLabel(value,td.getLabel());
		value=TransformValue.subStr(value,td.getBegin(), td.getLength(),"...");
		if(td.isVenusIp()){
			value=VenusTechUtil.Long2Ip(Long.parseLong(value));
		}
		Chunk c=new Chunk(value,WordStyle.getFont(td));
		cell.add(c);
		return WordStyle.setCellStyle(cell, td);
	}
}
