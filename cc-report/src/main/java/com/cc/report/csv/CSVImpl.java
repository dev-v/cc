package com.cc.report.csv;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import com.cc.report.Constant;
import com.cc.report.datas.ValueUtil;
import com.cc.report.datas.store.IStore;
import com.cc.report.xml.Page;
import com.cc.report.xml.Table;
import com.cc.report.xml.Td;
import com.cc.report.xml.Tr;
public class CSVImpl {
	private Page page;
	private IStore store;
	private String[][] cells=new String[100][];
	private StringBuilder stringBuilder=new StringBuilder();
	private static final char cellDelimit=',';
	private static final String lineDelimit="\r\n";
	
	/**
	 * 一个构造器<br/>
	 * 根据配置的页面信息和存储的数据生成csv报表，
	 * 并绑定到一输出流中
	 * @param page
	 * @param values
	 * @param out
	 */
	public CSVImpl(Page page,IStore store,OutputStream out){
		this.page=page;
		this.store=store;
		createPage();
		for(String[] row:cells){
			if(row!=null){
				for(String cell:row){
					if(cell!=null)stringBuilder.append(cell);
					stringBuilder.append(cellDelimit);
				}
			}
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
			stringBuilder.append(lineDelimit);
		}
		try {
			out.write(stringBuilder.toString().getBytes("gbk"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createPage(){
		for(Table table:page.getTable()){
			createTable(table,store.getSources());
		}
	}
	
	private void createTable(Table table,Map<String,Object> map){
		table.setRows(0);
		table.setCols(0);
		table.setBeginRow(page.getCurrentRow());
		table.setBeginCol(page.getCurrentCol());
		for(Tr tr:table.getTrs()){
			Table parent=tr.getParent();
			tr.setRows(0);
			tr.setCols(0);
			String sourceName=tr.getSource();
			if(sourceName==null){
				createTr(tr,Constant.COMPLEMENTMAP);
			}else{
				List<Map<String,Object>> sources=(List<Map<String,Object>>)store.getAll(sourceName, map);
				if(sources==null||sources.size()==0){
					if(tr.isComplement()){
						createTr(tr,Constant.COMPLEMENTMAP);
					}
				}else{
					int[] maxRC=new int[2];
					for(Map<String,Object>tempMap:sources){
						createTr(tr,tempMap);
						maxRC[0]+=tr.getRows();
						if(maxRC[1]<tr.getCols())maxRC[1]=tr.getCols();
						page.setCurrentCol(parent.getBeginCol());
						page.setCurrentRow(page.getCurrentRow()+tr.getRows());
						tr.setRows(0);
						tr.setCols(0);
					}
					tr.setRows(maxRC[0]);
					tr.setCols(maxRC[1]);
				}
			}
			parent.setRows(parent.getRows()+tr.getRows());
			if(parent.getCols()<tr.getCols())parent.setCols(tr.getCols());
			page.setCurrentRow(parent.getBeginRow()+parent.getRows());
			page.setCurrentCol(parent.getBeginCol());
		}
	}
	
	private void createTr(Tr tr,Map<String,Object> map){
		int currentCol=page.getCurrentCol()+tr.getTds().size();
		int currentRow=page.getCurrentRow();
		safeCell(currentRow,currentCol);
		for(Td td:tr.getTds()){
			Table table=td.getTable();
			if(table!=null){
				createTable(table, map);
				td.setRowSpan(table.getRows());
				td.setColSpan(table.getCols());
				page.setCurrentRow(page.getCurrentRow()-td.getRowSpan());
				safeCell(currentRow,currentCol+td.getColSpan()-1);
			}else{
				fillValue(td,map);
			}
			//设置最大行
			if(tr.getRows()<td.getRowSpan())tr.setRows(td.getRowSpan());
			//设置行的总列数
			tr.setCols(tr.getCols()+td.getColSpan());
			page.setCurrentCol(page.getCurrentCol()+td.getColSpan());
		}
	}
	
	private void safeCell(int rowIndex,int size){
		int rowLength=cells.length;
		if(rowLength-1<rowIndex){
			String[][] tempCells=cells;
			cells=new String[rowLength*2][];
			for(int i=0;i<rowLength;i++){
				cells[i]=tempCells[i];
			}
		}
		String[] row=cells[rowIndex];
		if(row==null){
			cells[rowIndex]=new String[size];
		}else{
			int length=row.length;
			if(length<size){
				String[] temp=cells[rowIndex]=new String[size];
				for(int i=0;i<length;i++){
					temp[i]=row[i];
				}
			}
		}
	}
	
	private void fillValue(Td td,Map<String,Object> map){
		String value=ValueUtil.parseTdValue(td, store,map);
		cells[page.getCurrentRow()][page.getCurrentCol()]=value;
	}
}
