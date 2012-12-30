package pala.finance.table;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import pala.bean.ReportByMonthResult;

public class ReportByMonthTableModel extends AbstractTableModel {
	
	private final int DATE = 0, COST = 1;
	
	private String[] columnNames = {"Date", "Cost"};
	
	private Vector<ReportByMonthResult> rowData = new Vector<ReportByMonthResult>();
	public ReportByMonthTableModel() {
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ReportByMonthResult item = rowData.elementAt(rowIndex);
		Object returnedValue = "";
		switch (columnIndex) {
		case DATE:
			returnedValue = item.getDate();
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
	
	public void addRow(ReportByMonthResult item) {
		rowData.add(item);
		//fireTableDataChanged();
	}
	
	public void addAll(List<ReportByMonthResult> items) {
		rowData.addAll(items);
	}
}
