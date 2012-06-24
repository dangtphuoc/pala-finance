package pala.gui.table;

import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import pala.bean.InputItem;
import pala.bean.Item;
import pala.gui.DateUtil;

public class ReportTableModel extends AbstractTableModel {
	
	private final int ID = 0, ITEM_DATE = 1, ITEM_NAME = 2, COST = 3;
	
	private String[] columnNames = {"ID", "Date", "Item Name", "Cost"};
	
	private Vector<InputItem> rowData = new Vector<InputItem>();
	public ReportTableModel() {
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		InputItem item = rowData.elementAt(rowIndex);
		Object returnedValue = "";
		switch (columnIndex) {
		case ID:
			//returnedValue = item.getId().toString();
			returnedValue = item.getId();
			break;
			
		case ITEM_DATE:
			//returnedValue = DateUtil.sdf.format(item.getDate());
			returnedValue = item.getDate();
			break;
			
		case ITEM_NAME:
			//returnedValue = item.getItem().getName();
			returnedValue = item.getItem().getName();
			break;
			
		case COST:
			//returnedValue = NumberFormat.getNumberInstance().format(item.getCost());
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
	
	public void addRow(InputItem item) {
		rowData.add(item);
		//fireTableDataChanged();
	}
}
