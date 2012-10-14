package pala.common.utillity;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableUtil {
	public static long getSelectedID(JTable table) {
		int selectedRow = table.getSelectedRow();
		long selectedID = -1;
		if(selectedRow != -1) {
			TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
			if(sorter != null) {
				selectedRow = sorter.convertRowIndexToModel(selectedRow);
			}
			selectedID = Long.parseLong(((DefaultTableModel)table.getModel()).getValueAt(selectedRow, 0).toString());
		}
		return selectedID;
	}
}
