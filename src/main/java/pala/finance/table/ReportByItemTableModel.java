package pala.finance.table;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import pala.bean.ReportByItemResult;

public class ReportByItemTableModel extends AbstractTableModel {
	
	private final int NAME = 0, COST = 1;
	
	private String[] columnNames = {"Name", "Cost"};
	
	private Vector<ReportByItemResult> rowData = new Vector<ReportByItemResult>();
	public ReportByItemTableModel() {
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ReportByItemResult item = rowData.elementAt(rowIndex);
		Object returnedValue = "";
		switch (columnIndex) {
		case NAME:
			returnedValue = item.getName();
			break;
			
		case COST:
			returnedValue = item.getCost();
			break;

		default:
			break;
		}
		
		return returnedValue;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex >= 0 && columnIndex <= getColumnCount())
			return getValueAt(0, columnIndex).getClass();
		else
			return Object.class;
	}

	@Override
	public int getRowCount() {
		return rowData.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public void addRow(ReportByItemResult item) {
		rowData.add(item);
		//fireTableDataChanged();
	}
	
	public void addAll(List<ReportByItemResult> items) {
		rowData.addAll(items);
	}
}
