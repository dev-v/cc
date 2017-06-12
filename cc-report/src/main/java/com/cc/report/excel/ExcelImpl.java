package com.cc.report.excel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.cc.report.Constant;
import com.cc.report.datas.ValueUtil;
import com.cc.report.datas.store.IStore;
import com.cc.report.util.TransformValue;
import com.cc.report.venustech.VenusTechUtil;
import com.cc.report.xml.Labels;
import com.cc.report.xml.Page;
import com.cc.report.xml.Table;
import com.cc.report.xml.Td;
import com.cc.report.xml.Tr;

/**
 * excel报表创建实现
 * 写入数据解析流程<br/>
 * 1、开始解析page标签：设置xls开始写入数据的行和列，由用户通过beginCol和beginRow属性设置，
 * 并设置当前操作的行列位置currentCol和currentRow，同时设置页面最大列数cols
 * <br/>
 * 2、开始解析table标签：设置表格跨行数rows和跨列数cols为0，设置表格的起始创建列位置beginCol为当前操作的列位置currentCol
 * <br/>
 * 3、开始解析tr标签：设置行的跨行数rows和跨列数cols为0，获取当前行操作对象，若不存在则创建，
 * （1、）若其下含有存在table元素的td，则先解析此类td，并将最大的跨行数存储在rows中，
 * 完成后，设置当前操作定位列为table的beginCol，然后解析其他td（第一次解析需记录所有列总和，以便生成数据格式与配置格式一致）
 * （2、）若不存在，则直接解析
 * <br/>
 * 4、开始解析td标签：
 * （1、）若其下含有table：首先解析存在table的td
 * （2、）若其下不含有table：写入数据
 * <br/>
 * 5、解析完成td标签：
 * （1、）若其下含有table：将table的跨列数和跨行数记录在td的跨行数和跨列数中
 * （2、）不管是否含有table：将当前操作的定位列加上td所跨列数colSpan，累积本单元格跨列数到tr总列数中去，
 * 比较是否为本行跨行最大的行，若是，则替换父标签tr的跨行数
 * <br/>
 * 6、解析完成tr标签：
 * 设置当前操作定位行加上tr的rows，设置当前操作定位列减去tr的cols，累加父标签table的跨行数rows，并设置最大的跨列数
 * <br/>
 * 7、解析完成table标签：
 * ***********将当前定位列加上table的跨列数cols，到达表格的右下角（解析完成td后已做了本部分工作）***********
 * 若含有父节点td，设置父节点td的跨行数为table的rows，跨列数为table的cols
 * 
 * 2013-05-25 修改实现方式
 * 1、开始建立table时，设置好table创建的起始行和列，并保证跨行跨列数为0（由程序自动设置），
 *   建立完成一个table后，需将页面指针移动到表格的左下角下面一列的位置(建立tr完成后已经完成了次操作，所以此操作是多余的)；
 * 2、开始建立一个tr时，保证其跨行跨列数为0（由程序自动设置），
 *   建立完成一个tr后，需将页面指针移动到tr的左下角下面单元格的位置，
 *   同时设置好tr的跨行跨列数以及重置父table的跨行跨列数；
 * 3、新建一个单元格时，需重置其跨行数以保证与其他同行的单元格相同的跨行数，
 *   建立完成一个td后，需将指针移动到td的首行右边的位置；
 * 4、td下面存在嵌套的table时，将首先解析其下的table，目的是自动计算该td所在的tr所跨的总行数；
 * @author cwl
 * @version 1.0 2012-02-14
 * @update 2013-05-25
 */
public class ExcelImpl{
	private Page page;//页面格式配置信息
//	private Values values;//值域
	private IStore store;//值域
	private HSSFWorkbook excel;//创建的报表
	private HSSFSheet sheet;//一个表单
	private HSSFRow row;
	private ExcelStyle styleManage=ExcelStyle.getInstance();
	//用于存储合并列的临时单元格信息
	private Map<Double,CombineColumn> combines;
	public ExcelImpl(Page page,IStore store, OutputStream out){
		this.page=page;
		this.store=store;
		excel=new HSSFWorkbook();
		createSheet();
		try {
			excel.write(out);//将创建成的excel报表写入到一个输出流中
		} catch (IOException e) {
			e.printStackTrace();
		}
 	};

	/**
	 * 开始创建报表
	 * @param out
	 */
	private void createSheet() {
		sheet=excel.createSheet();
		HSSFRow row=sheet.createRow(0);
		styleManage=ExcelStyle.getInstance();
		combines=new HashMap<Double, CombineColumn>();
		page.setCurrentCol(page.getBeginCol());//设置创建一个sheet页面的开始列
		page.setCurrentRow(page.getBeginRow());//设置创建一个sheet页面的开始行
		List<Table> tables=page.getTable();
		for(Table table:tables){
			createTable(table,store.getSources());//开始创建表格
		}
		for(CombineColumn combine:combines.values()){	
			HSSFCell cell=null;
			for(int i=combine.firstRow;i<=combine.lastRow;i++){
				row=sheet.getRow(i);
				if(row==null)row=sheet.createRow(i);
				for(int j=combine.firstColumn;j<=combine.lastColumn;j++){
					cell=row.getCell(j);
					if(cell==null){
						cell=row.createCell(j);
						cell.setCellStyle(styleManage.getStyle(combine.td));
					}
				}
			}
			row=sheet.getRow(combine.firstRow);
			cell=row.getCell(combine.firstColumn);
			cell.setCellValue(combine.value);
			CellRangeAddress range=new CellRangeAddress(
					combine.firstRow,combine.lastRow,//跨行（含起始和结束行）
					combine.firstColumn,combine.lastColumn);//跨列（含起始和结尾列）
			sheet.addMergedRegion(range);
		}
	}


	/**
	 * 开始建立table时，设置好table创建的起始行和列，并保证跨行跨列数为0（由程序自动设置），
	 * 建立完成一个table后，需将页面指针移动到表格的左下角下面一列的位置(建立tr完成后已经完成了次操作，所以此操作是多余的)；
	 * @param table
	 * @param map
	 */
	private void createTable(Table table,Map<String,Object> map){
		//初始化表格开始创建的开始列为sheet页面当前正在写数据的列
		table.setRows(0);
		table.setCols(0);
		table.setBeginCol(page.getCurrentCol());
		table.setBeginRow(page.getCurrentRow());
		List<Tr> trs=table.getTrs();
		for(Tr tr:trs){
			createTr(tr,map);//开始创建一个行,并且创建完成后指针将自动移动到行的下面
		}
	}
	/**
	 * <pre>
	 * 开始建立一个tr时，保证其跨行跨列数为0（由程序自动设置），
	 * 建立完成一个tr后，需将页面指针移动到tr的左下角下面单元格的位置，
	 * 同时设置好tr的跨行跨列数以及重置父table的跨行跨列数；
	 * @param tr
	 * @param map
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	private void createTr(Tr tr,Map<String,Object> map){
		Table parent=tr.getParent();
		tr.setRows(0);
		tr.setCols(0);
		String sourceName=tr.getSource();//获取创建该行的数据的来源方式
		if(sourceName==null){
			createTds(tr,Constant.COMPLEMENTMAP);
		}else{
			List<Map<String,Object>> sources=(List<Map<String,Object>>)store.getAll(sourceName, map);
			if(sources==null||sources.size()==0){
				if(tr.isComplement()){
					createTds(tr,Constant.COMPLEMENTMAP);
				}
			}else{
				int[] maxRC=new int[2];
				for(Map<String,Object>tempMap:sources){
					createTds(tr,tempMap);
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
	
	/**
	 * <pre>
	 * 新建一个单元格时，需重置其跨行数以保证与其他同行的单元格相同的跨行数，
	 * 创建td时将计算tr的跨行跨列数
	 * td下面存在嵌套的table时，将首先解析其下的table，目的是自动计算该td所在的tr所跨的总行数；
	 * 建立完成一个td后，需将指针移动到td的首行右边的位置；
	 * @param tr
	 * @param map
	 * </pre>
	 */
	private void createTds(Tr tr,Map<String,Object> map){
		List<Td> tds=tr.getTds();
		//首先生成嵌套表格
		for(Td td:tds){
			Table table=td.getTable();
			if(table!=null){
				createTable(table,map);
				td.setColSpan(table.getCols());
				td.setRowSpan(table.getRows());
				//若创建了表格，则将指针位置上移到该表格的起始位置
				page.setCurrentRow(page.getCurrentRow()-td.getRowSpan());
			}
			//设置最大行
			if(tr.getRows()<td.getRowSpan())tr.setRows(td.getRowSpan());
			//设置行的总列数
			tr.setCols(tr.getCols()+td.getColSpan());
			//移动指针，已在适当位置生成嵌套表格
			page.setCurrentCol(page.getCurrentCol()+td.getColSpan());
		}
		//移动到改行的起始位置，已处理非嵌套单元格
		page.setCurrentCol(tr.getParent().getBeginCol());
		int bufferTdColspan;
		for(Td td:tds){
			if(td.getTable()==null){
				row=sheet.getRow(page.getCurrentRow());
				//初始化sheet行
				if(row==null)row=sheet.createRow(page.getCurrentRow());
				//将单元格的跨行数设置为行跨行数
				bufferTdColspan=td.getRowSpan();
				td.setRowSpan(tr.getRows());
				fillValues(td, map);
				td.setRowSpan(bufferTdColspan);
			}
			//往后移动单元格位置，已写入下一个单元格的值
			page.setCurrentCol(page.getCurrentCol()+td.getColSpan());
		}
	}

	/**
	 * 填充单元格文本数据并设置单元格样式
	 * @param td
	 * @param map
	 */
	private void fillValues(Td td,Map<String,Object> map){
		String value=ValueUtil.parseTdValue(td, store,map);
		if(styleManage.getStyle(td)==null){
			styleManage.createStyle(excel,td);
		}
		if(td.isCombine()){
			CombineColumn combine=combines.get(td.getId());
			if(combine==null){
				combine=new CombineColumn();
				combine.firstRow=page.getCurrentRow();
				combine.lastRow=page.getCurrentRow()+td.getRowSpan()-1;
				combine.firstColumn=page.getCurrentCol();
				combine.lastColumn=page.getCurrentCol()+td.getColSpan()-1;
				combine.value=value;
				td.setId(Math.random());
				combine.td=td;
				combines.put(td.getId(),combine);
			}else{
				if(combine.value.equals(value)){
					combine.lastRow+=td.getRowSpan();
				}else{
					combine=new CombineColumn();
					combine.firstRow=page.getCurrentRow();
					combine.lastRow=page.getCurrentRow()+td.getRowSpan()-1;
					combine.firstColumn=page.getCurrentCol();
					combine.lastColumn=page.getCurrentCol()+td.getColSpan()-1;
					combine.value=value;
					td.setId(Math.random());
					combine.td=td;
					combines.put(td.getId(),combine);
				}
			}
		}else{
			HSSFCell cell=null;
			if(td.getRowSpan()>1||td.getColSpan()>1){
				CombineColumn combine=new CombineColumn();
				combine.firstRow=page.getCurrentRow();
				combine.lastRow=page.getCurrentRow()+td.getRowSpan()-1;
				combine.firstColumn=page.getCurrentCol();
				combine.lastColumn=page.getCurrentCol()+td.getColSpan()-1;
				combine.value=value;
				td.setId(Math.random());
				combine.td=td;
				combines.put(td.getId(),combine);
			}else{
				cell=sheet.getRow(page.getCurrentRow()).createCell(page.getCurrentCol());
				cell.setCellStyle(styleManage.getStyle(td));//设置表格样式
				cell.setCellValue(value);
				Integer width=td.getWidth();
				if(width!=null)sheet.setColumnWidth(page.getCurrentCol(), width*Constant.EXCEL_COLUMN_WIDTH_BASE);
			}
		}
	}
}
