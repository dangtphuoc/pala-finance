package pala.gui.table;

import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import pala.bean.InputItem;
import pala.bean.Item;
import pala.common.utillity.DateUtil;

public class ReportTableModel extends AbstractTableModel {
	
	private final int ITEM_DATE = 0, ITEM_NAME = 1, COST = 2, DETAIL = 3, ATTACHMENT = 4;
	
	private String[] columnNames = {"Date", "Item Name", "Cost", "Detail", "Attachment"};
	
	private Vector<InputItem> rowData = new Vector<InputItem>();
	public ReportTableModel() {
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		InputItem item = rowData.elementAt(rowIndex);
		Object returnedValue = "";
		switch (columnIndex) {
		
			
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
			
		case DETAIL:
			if(item.getDetail() != null)
				returnedValue = item.getDetail();
			break;
			
		case ATTACHMENT:
			if(item.getAttachment() != null)
				returnedValue = item.getAttachment();
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
