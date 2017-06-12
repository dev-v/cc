package com.cc.report.word;
import com.lowagie.text.Cell;
import com.lowagie.text.Table;
public class Row {
	private Table parent;
	private Cell[] cells;
	
	public Row(Cell[] cells){
		this.cells=cells;
	}
	public Table getParent() {
		return parent;
	}
	public void setParent(Table parent) {
		this.parent = parent;
	}
	public Cell[] getCells() {
		return cells;
	}
	public void setCells(Cell[] cells) {
		this.cells = cells;
	}
}
