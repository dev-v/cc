package com.cc.report.xml;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

import com.cc.report.Constant;
import com.cc.report.style.IStyle;
/**
 * 本类实现了SaxXmlInterface接口<br/>
 * 提供了解析xml文件为Report对象的实现</br>
 * 依赖于dom4j的支持
 * @author cwl
 * @version 1.0
 */
public class SaxReportImpl implements ISaxXml {
	private Report report;
	private Element root;
	private Page page;
	/**
	 * 根据配置文件路径解析配置文件为一个Report对象
	 */
	public void saxXmlToReport(Element reportRoot) {
		//获取根节点
		root=reportRoot;
		//创建一个报表对象
		report=new Report();
		//设置文件类型
		report.setType(root.attributeValue(Constant.TYPE));
		//解析文件名称
		Element filenameE=root.element(Constant.FILENAME);
		report.setFilename(filenameE.getText());//设置文件名称
		report.setFilenameLength(filenameE.attributeValue(Constant.lENGTH));//设置配置的文件名称长度
		//解析基本参数(当前存储配置文件所配置的参数)
		report.setParams(saxParams());
		//解析数据源
		report.setSources(saxSources());
		//解析文档格式
		saxPage();
		report.setPage(page);
	}
	
	/**
	 * 方法用于解析基本参数配置
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String,String> saxParams() {
		Map<String,String> tempParams=new HashMap<String,String>();
		Element params=root.element(Constant.PARAMS);
		if(params!=null){
			List<Element> es=params.elements();
			for(Element e:es){
				tempParams.put(
					e.attributeValue(Constant.NAME),
					e.attributeValue(Constant.VALUE));
			}
		}
		return tempParams;
	}
	
	/**
	 * 方法用于解析数据源sources配置信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Source> saxSources() {
		Element tempSourcesE=root.element(Constant.SOURCES);
		List<Source> sources=new ArrayList<Source>(16);
		if(tempSourcesE!=null){
			List<Element> sourcesE=tempSourcesE.elements();
			for(Element source:sourcesE){
				Source tempSource=new Source();
				tempSource.setName(source.attributeValue(Constant.NAME));
				tempSource.setType(source.attributeValue(Constant.TYPE));
				Element subSource=(Element)source.elements().get(0);
				if(!tempSource.getType().equals(subSource.getName()))//检查配置的数据源类型是否与标志一致
					throw new RuntimeException("数据源:"+tempSource.getName()+"配置类型不匹配\n" +
							"数据源类型设置为："+tempSource.getType()+"\n配置节点类型为："+subSource.getName());
				if(tempSource.getType().equals(Constant.SQL)){//数据源类型为sql查询语句
					tempSource.setSql(saxSql(subSource));
				}else {//解析hibernate型
					tempSource.setHibernate(saxHibernate(subSource));
				}
				sources.add(tempSource);
			}
		}
		return sources;
	}
	
	/**
	 * 解析sql标签
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Sql saxSql(Element sql){
		Sql tempSql=new Sql();
		tempSql.setValue(sql.attributeValue(Constant.VALUE));
		Element fields=(Element)sql.elements(Constant.FIELDS).get(0);
		tempSql.setFields(saxFields(fields));
		List<Element> list=sql.elements(Constant.LIST);
		tempSql.setList(saxList(list));
		return tempSql;
	}
	
	/**
	 * 方法用于解析list，存储一些固定的由用户配置的列表数据
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> saxList(List<Element> list) {
		List<Map<String,String>> tempList=null;
		if(list.size()>0){
			tempList=new ArrayList<Map<String,String>>();
			List<Element> tempMaps=list.get(0).elements();
			for(Element tempMap:tempMaps){
				Map<String,String> map=new HashMap<String,String>();
				List<Element> keys=tempMap.elements();
				for(Element key:keys){
					map.put(key.attributeValue(Constant.NAME),key.getText());
				}
				tempList.add(map);
			}
		}
		return tempList;
	}

	/**
	 * 方法用于解析fields标签
	 * @param field
	 */
	@SuppressWarnings("unchecked")
	private List<Field> saxFields(Element e){
		List<Element> fields=e.elements();
		List<Field> tempFields=new ArrayList<Field>();
		for(Element field:fields){
			Field tempField=new Field();
			tempField.setName(field.attributeValue(Constant.NAME));//设置字段名
			List<Element> subQuery=field.elements();
			if(subQuery!=null&&subQuery.size()>0){//判断是否含有子数据源
				Element el=subQuery.get(0);
				//判断子查询是否为sql型，若为sql型解析sql，否则为hibernate型，解析hibernate型
				if(el.getName().equals(Constant.SQL)){
					tempField.setSql(saxSql(el));
				}else{
					tempField.setHibernate(saxHibernate(el));
				}
			}
			tempFields.add(tempField);
		}
		return tempFields;
	}
	
	/**
	 * 解析hibernate标签
	 * @param hibernate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Hibernate saxHibernate(Element hibernate){
		Hibernate tempHibernate=new Hibernate();
		//设置基本信息开始
		tempHibernate.setDao(hibernate.attributeValue("dao"));
		tempHibernate.setMethod(hibernate.attributeValue("method"));
		tempHibernate.setPo(hibernate.attributeValue("po"));
		tempHibernate.setSql(hibernate.attributeValue("sql"));
		//设置基本信息结束
		List<Element> elements=hibernate.elements();
		for(Element e:elements){
			List<Element> ps=e.elements();
			String name=e.getName().toLowerCase();
			if(name.equals("params")){//设置方法执行时配置的参数
				String[] paramsType=new String[ps.size()];
				String[] paramsValue=new String[ps.size()];
				for(int i=0;i<ps.size();i++){
					Element param=ps.get(i);
					String type=param.attributeValue("type");
					if(type==null)paramsType[i]="java.lang.String";
					else paramsType[i]=type;
					paramsValue[i]=ps.get(i).getText();
				}
				tempHibernate.setParamsType(paramsType);
				tempHibernate.setParamsValue(paramsValue);
			}else if(name.equals("fields")){//设置方法执行结果需存储的字段
				tempHibernate.setFields(saxFields(e));
			}
		}
		return tempHibernate;
	}
	
	/**
	 * 方法用于获取一个Report对象<br/>
	 * 需先调用saxXmlFileToReport(String filePath)方法<br/>
	 * 否则返回一个null
	 * @return
	 */
	public Report getReport(){
		return report;
	}
	
	/**
	 * 方法用于解析页面基本配置信息page
	 */
	@SuppressWarnings("unchecked")
	private void saxPage() {
		page=new Page();
		Element PageE=root.element(Constant.PAGE);
		setStyle(page,null,page,PageE);//设置页面样式
		String beginCol=PageE.attributeValue(Constant.BEGINCOL);
		if(beginCol!=null)page.setBeginCol(Integer.parseInt(beginCol));
		String beginRow=PageE.attributeValue(Constant.BEGINROW);
		if(beginRow!=null)page.setBeginRow(Integer.parseInt(beginRow));
		page.setPageType(PageE.attributeValue(Constant.PAGETYPE));
		List<Element> imgs=PageE.elements(Constant.IMAGE);
		if(imgs!=null&&imgs.size()>0){
			List<Img> images=new ArrayList<Img>();
			for(Element img:imgs){
				Img image=new Img(img.attributeValue(Constant.SRC));
				String height1=img.attributeValue(Constant.HEIGHT);
				image.setHeight(Float.parseFloat(height1));
				String width1=img.attributeValue(Constant.WIDTH);
				image.setWidth(Float.parseFloat(width1));
				String endx=img.attributeValue(Constant.ENDX);
				if(endx!=null)image.setEndx(Float.parseFloat(endx));
				String endy=img.attributeValue(Constant.ENDY);
				if(endy!=null)image.setEndy(Float.parseFloat(endy));
				String beginx=img.attributeValue(Constant.BEGINX);
				image.setBeginx(Float.parseFloat(beginx));
				String beginy=img.attributeValue(Constant.BEGINY);
				image.setBeginy(Float.parseFloat(beginy));
				String transparence=img.attributeValue(Constant.TRANSPARENCE);
				image.setTransparence(Integer.parseInt(transparence));
				String transparenceLeft=img.attributeValue(Constant.TRANSPARENCELEFT);
				image.setTransparenceLeft(Integer.parseInt(transparenceLeft));
				String transparenceRight=img.attributeValue(Constant.TRANSPARENCERIGHT);
				image.setTransparenceRight(Integer.parseInt(transparenceRight));
				images.add(image);
			}
			page.setImages(images);
		}
		
		List<Element> tables=PageE.elements(Constant.TABLE);
		page.setTable(saxTables(tables));
	}

	/**
	 * 方法用于解析tables
	 */
	private List<Table> saxTables(List<Element> tables) {
		List<Table> tempTables=new ArrayList<Table>();
		for(Element e:tables){
			tempTables.add(saxTable(e,null));
		}
		return tempTables;
	}

	/**
	 * 方法用于解析table
	 */
	@SuppressWarnings("unchecked")
	private Table saxTable(Element e,Td parent) {
		Table table=new Table();
		setStyle(table,parent,page,e);//设置样式
		List<Element> trs=e.elements();
		String fill=e.attributeValue(Constant.FILL);
		if(fill!=null)table.setFill(Integer.parseInt(fill));
		table.setParent(parent);
		table.setCols(saxTrs(trs,table));
		return table;
	}

	/**
	 * 方法用于解析trs
	 */
	@SuppressWarnings("unchecked")
	private int saxTrs(List<Element> trs,Table parent) {
		int cols=0;
		List<Tr> tempTrs=new ArrayList<Tr>();
		parent.setTrs(tempTrs);
		for(Element e:trs){
			Tr tr=new Tr();
			tr.setParent(parent);
			setStyle(tr,parent,page,e);//设置样式
			tr.setSource(e.attributeValue(Constant.SOURCE));
			tr.setComplement(Boolean.parseBoolean(e.attributeValue(Constant.COMPLEMENT)));
			List<Element> tds=e.elements();
			int[] colAndRow=saxTds(tds,tr);
			tr.setRows(colAndRow[1]);
			tr.setCols(colAndRow[0]);
			cols=cols>colAndRow[0]?cols:colAndRow[0];
			tempTrs.add(tr);
		}
		return cols;
	}

	/**
	 * 方法用于解析tds
	 */
	@SuppressWarnings("unchecked")
	private int[] saxTds(List<Element> tds,Tr parent) {
		int colAndRow[]=new int[2];
		int maxRowSpan=1;
		List<Td> tempTds=new ArrayList<Td>();
		parent.setTds(tempTds);
		for(Element e:tds){
			int tempColSpan=1;
			int tempRowSpan=1;
			Td td=new Td();
			td.setParent(parent);//设置父节点
//			td.setField(e.attributeValue(Constant.FIELD));
			setStyle(td, parent,page,e);//设置样式
			List<Element> tables=e.elements(Constant.TABLE);
			if(tables!=null&&tables.size()>0){//若 其下含有tabel
				td.setRowSpan(0);
				td.setColSpan(0);
				Element table=tables.get(0);
				td.setTable(saxTable(table,td));
				tempColSpan=td.getTable().getCols();
				td.setColSpan(tempColSpan);
				tempRowSpan=td.getTable().getRows();
				td.setRowSpan(tempRowSpan);
			}else{//若其下不含有table
				tempColSpan=Integer.parseInt(e.attributeValue(Constant.COLSPAN));
				td.setColSpan(tempColSpan);
				tempRowSpan=Integer.parseInt(e.attributeValue(Constant.ROWSPAN));
				td.setRowSpan(tempRowSpan);
				String value=e.getText().trim();
				td.setValue(value);
				String combine=e.attributeValue(Constant.COMBINE);
				td.setCombine(Boolean.parseBoolean(combine));
				String begin=e.attributeValue(Constant.BEGIN);
				if(begin!=null)td.setBegin(Integer.parseInt(begin));
				String length=e.attributeValue(Constant.lENGTH);
				if(length!=null)td.setLength(Integer.parseInt(length));
				String venusIp=e.attributeValue(Constant.VENUSIP);
				td.setVenusIp(Boolean.parseBoolean(venusIp));
				List<Element> ls=e.elements(Constant.LABEL);
				if(ls!=null){//替换值的标签
					Map<String,String> label=new HashMap<String, String>();
					for(Element el:ls){
						label.put(el.attributeValue(Constant.VALUE),el.getText());
					}
					td.setLabel(label);
				}
				List<Element> lss=e.elements(Constant.LABELS);
				if(lss!=null&&lss.size()>0){//替换值的标签
//					Map<String,String> label=new HashMap<String, String>();
					Element el=lss.get(0);
					Labels labels=new Labels();
					labels.setLabel(el.attributeValue(Constant.LABEL));
					labels.setSource(el.attributeValue(Constant.SOURCE));
					labels.setValue(el.attributeValue(Constant.VALUE));
					td.setLabels(labels);
				}
				Element img=e.element(Constant.IMG);
				if(img!=null){//若其下含有img
					Img tempImg=new Img();
					tempImg.setSrc(img.attributeValue(Constant.SRC));
					String width1=img.attributeValue(Constant.WIDTH);
					tempImg.setWidth(Float.parseFloat(width1));
					String height=img.attributeValue(Constant.HEIGHT);
					tempImg.setHeight(Float.parseFloat(height));
					td.setImg(tempImg);
				}
			}
			if(maxRowSpan<tempRowSpan)maxRowSpan=tempRowSpan;
			tempTds.add(td);
			colAndRow[0]+=tempColSpan;
		}
		colAndRow[1]=maxRowSpan;
		return colAndRow;
	}
	
	/**
	 * 为展示元素设置样式
	 * @param sub 要设置样式的子对象
	 * @param parent 父对象，当subE未存储子对象样式时，使用父对象的样式
	 * @param page 顶层页面元素对象，通过父对象为子对象设置样式，若父对象不存在时，使用顶层页面元素对象的样式
	 * @param subE 配置文件配置的子对象的样式
	 */
	private void setStyle(IStyle sub,IStyle parent,IStyle page,Element subE){
		String temp=subE.attributeValue(Constant.HEIGHT);
		if(temp!=null)sub.setHeight(Integer.parseInt(temp));
		else if(parent!=null)sub.setHeight(parent.getHeight());
		
		temp=subE.attributeValue(Constant.WIDTH);
		if(temp!=null)sub.setWidth(Integer.parseInt(temp));
		else if(parent!=null)sub.setWidth(parent.getWidth());
		
		temp=subE.attributeValue(Constant.BLACKCOLOR);
		if(temp!=null)sub.setBlackColor(temp);
		else if(parent!=null)sub.setBlackColor(parent.getBlackColor());
		else sub.setBlackColor(page.getBlackColor());
		
		temp=subE.attributeValue(Constant.ALIGN);
		if(temp!=null)sub.setAlign(temp);
		else if(parent!=null)sub.setAlign(parent.getAlign());
		else sub.setAlign(page.getAlign());
		
		temp=subE.attributeValue(Constant.VALIGN);
		if(temp!=null)sub.setVAlign(temp);
		else if(parent!=null)sub.setVAlign(parent.getVAlign());
		else sub.setVAlign(page.getVAlign());
		
		temp=subE.attributeValue(Constant.FONTCOLOR);
		if(temp!=null)sub.setFontColor(temp);
		else if(parent!=null)sub.setFontColor(parent.getFontColor());
		else sub.setFontColor(page.getFontColor());
		
		temp=subE.attributeValue(Constant.FONTFAMILLY);
		if(temp!=null)sub.setFontFamily(temp);
		else if(parent!=null)sub.setFontFamily(parent.getFontFamily());
		else sub.setFontFamily(page.getFontFamily());
		
		temp=subE.attributeValue(Constant.FONTSTYLE);
		if(temp!=null)sub.setFontStyle(temp);
		else if(parent!=null)sub.setFontStyle(parent.getFontStyle());
		else sub.setFontStyle(page.getFontStyle());
		
		temp=subE.attributeValue(Constant.FONTSIZE);
		if(temp!=null)sub.setFontSize(Integer.parseInt(temp));
		else if(parent!=null)sub.setFontSize(parent.getFontSize());
		else sub.setFontSize(page.getFontSize());
		
		temp=subE.attributeValue(Constant.ENDING);
		if(temp!=null)sub.setEnding(temp);
		else if(parent!=null)sub.setEnding(parent.getEnding());
		else sub.setEnding(page.getEnding());
		
		temp=subE.attributeValue(Constant.BLANK);
		if(temp!=null)sub.setBlank(temp);
		else if(parent!=null)sub.setBlank(parent.getBlank());
		else sub.setBlank(page.getBlank());
		
		temp=subE.attributeValue(Constant.BORDER);
		if(temp!=null)sub.setBorder(Boolean.parseBoolean(temp));
		else if(parent!=null)sub.setBorder(parent.isBorder());
		else sub.setBorder(page.isBorder());
		
		temp=subE.attributeValue(Constant.BORDERBOTTOM);
		if(temp!=null)sub.setBorderBottom(Boolean.parseBoolean(temp));
		else sub.setBorderBottom(sub.isBorder());
		
		temp=subE.attributeValue(Constant.BORDERLEFT);
		if(temp!=null)sub.setBorderLeft(Boolean.parseBoolean(temp));
		else sub.setBorderLeft(sub.isBorder());
		
		temp=subE.attributeValue(Constant.BORDERRIGHT);
		if(temp!=null)sub.setBorderRight(Boolean.parseBoolean(temp));
		else sub.setBorderRight(sub.isBorder());
		
		temp=subE.attributeValue(Constant.BORDERTOP);
		if(temp!=null)sub.setBorderTop(Boolean.parseBoolean(temp));
		else sub.setBorderTop(sub.isBorder());
		
		temp=subE.attributeValue(Constant.BORDERCOLOR);
		if(temp!=null)sub.setBorderColor(temp);
		else if(parent!=null)sub.setBorderColor(parent.getBorderColor());
		else sub.setBorderColor(page.getBorderColor());

		temp=subE.attributeValue(Constant.BORDERTYPE);
		if(temp!=null)sub.setBorderType(temp);
		else if(parent!=null)sub.setBorderType(parent.getBorderType());
		else sub.setBorderType(page.getBorderType());
		
		temp=subE.attributeValue(Constant.BORDERWEIGHT);
		if(temp!=null)sub.setBorderWeight(Float.parseFloat(temp));
		else if(parent!=null)sub.setBorderWeight(parent.getBorderWeight());
		else sub.setBorderWeight(page.getBorderWeight());
		
		temp=subE.attributeValue(Constant.MARGIN);
		if(temp!=null)sub.setMargin(Integer.parseInt(temp));
		else if(parent!=null)sub.setMargin(parent.getMargin());
//		else sub.setMargin(page.getMargin());
		
		temp=subE.attributeValue(Constant.MARGINBOTTOM);
		if(temp!=null)sub.setMarginBottom(Integer.parseInt(temp));
		else sub.setMarginBottom(sub.getMargin());
		
		temp=subE.attributeValue(Constant.MARGINTOP);
		if(temp!=null)sub.setMarginTop(Integer.parseInt(temp));
		else sub.setMarginTop(sub.getMargin());
		
		temp=subE.attributeValue(Constant.MARGINRIGHT);
		if(temp!=null)sub.setMarginRight(Integer.parseInt(temp));
		else sub.setMarginRight(sub.getMargin());
		
		temp=subE.attributeValue(Constant.MARGINLEFT);
		if(temp!=null)sub.setMarginLeft(Integer.parseInt(temp));
		else sub.setMarginLeft(sub.getMargin());
	}
}
