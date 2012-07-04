package pala.common.util;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableUtil {
	public static long getSelectedID(JTable table) {
		int selectedRow = table.getSelectedRow();
		long selectedID = -1;
		if(selectedRow != -1) {
			selectedID = Long.parseLong(((DefaultTableModel)table.getModel()).getValueAt(selectedRow, 0).toString());
		}
		return selectedID;
	}
}
