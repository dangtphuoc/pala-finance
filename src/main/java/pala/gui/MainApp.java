package pala.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import pala.bean.InputItem;
import pala.bean.Item;
import pala.repository.ApplicationContent;
import pala.repository.InputItemRepositoryImpl;
import pala.repository.ItemRepositoryImpl;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;
import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.util.JDatePickerUtil;

import org.springframework.data.neo4j.conversion.EndResult;
import java.awt.Component;

public class MainApp {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	private JFrame frame;
	private JTextField txtName;
	private JTextField txtDescription;
	private JTable table;
	private JComboBox cbxItem;
	private JTextField txtCost;
	private DateField inputDate;
	private JTable tblInput;
	private JTable tblReport;
	private DateField dateFieldReport;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainApp window = new MainApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 513, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel pnlAdmin = new JPanel();
		tabbedPane.addTab("Administration", null, pnlAdmin, null);
		pnlAdmin.setLayout(null);
		
		JLabel lblItemName = new JLabel("Item Name:");
		lblItemName.setBounds(10, 11, 71, 14);
		pnlAdmin.add(lblItemName);
		
		txtName = new JTextField();
		txtName.setBounds(89, 8, 213, 20);
		pnlAdmin.add(txtName);
		txtName.setColumns(10);
		final ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				itemRepo.addItem(txtName.getText(), txtDescription.getText());
				Item item = itemRepo.findItemNamed(txtName.getText());
				if(item != null) {
					((DefaultTableModel)table.getModel()).addRow(new String[]{item.getId().toString(), item.getName(), item.getDescription()});
					JOptionPane.showMessageDialog(frame, "Added Item successfully.");
				} else {
					JOptionPane.showMessageDialog(frame, "Added Item unsuccessfully.");
				}
			}
		});
		btnAdd.setBounds(318, 38, 89, 23);
		pnlAdmin.add(btnAdd);
		
		txtDescription = new JTextField();
		txtDescription.setBounds(89, 39, 213, 20);
		pnlAdmin.add(txtDescription);
		txtDescription.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(10, 42, 71, 14);
		pnlAdmin.add(lblDescription);
		
		loadItemTable();
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 72, 397, 139);

		// Add the scroll pane to this panel.
		pnlAdmin.add(scrollPane);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long selectedID = Long.parseLong(((DefaultTableModel)table.getModel()).getValueAt(table.getSelectedRow(), 0).toString());
				ItemRepositoryImpl itemRep = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
				itemRep.deleteItem(selectedID);
				((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
			}
		});
		btnDelete.setBounds(10, 229, 89, 23);
		pnlAdmin.add(btnDelete);
		
		JPanel pnlInput = new JPanel();
		tabbedPane.addTab("Input", null, pnlInput, null);
		pnlInput.setLayout(null);
		
		JLabel lblItem = new JLabel("Item:");
		lblItem.setBounds(10, 11, 46, 14);
		pnlInput.add(lblItem);
		
		cbxItem = new JComboBox();
		cbxItem = loadCbxItems();
		cbxItem.setBounds(66, 8, 148, 17);
		pnlInput.add(cbxItem);
		
		txtCost = new JTextField();
		txtCost.setBounds(66, 39, 148, 20);
		pnlInput.add(txtCost);
		txtCost.setColumns(10);
		
		JButton btnInputAdd = new JButton("Add");
		btnInputAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
				InputItemRepositoryImpl inputItemRepo = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
				Item item = (Item)cbxItem.getSelectedItem();
				
				InputItem inputItem = inputItemRepo.addItem(item, Double.parseDouble(txtCost.getText()), (Date)inputDate.getValue());
				
				if(inputItem != null) {
					DefaultTableModel tableModel = (DefaultTableModel)tblInput.getModel();
					tableModel.addRow(new String[]{inputItem.getId().toString(), inputItem.getItem().getName(), String.valueOf(inputItem.getCost()), sdf.format(inputItem.getDate())});
				}
				
			}
		});
		btnInputAdd.setBounds(243, 67, 89, 23);
		pnlInput.add(btnInputAdd);
		
		inputDate = CalendarFactory.createDateField();
		inputDate.setBounds(66, 70, 148, 20);
		pnlInput.add(inputDate);
		
		JScrollPane scrollBarInput = new JScrollPane((Component) null);
		scrollBarInput.setBounds(20, 136, 397, 139);
		pnlInput.add(scrollBarInput);
		
		tblInput = new JTable();
		loadInputItemTable();
		scrollBarInput.setViewportView(tblInput);
		
		JPanel pnlReport = new JPanel();
		tabbedPane.addTab("Report", null, pnlReport, null);
		pnlReport.setLayout(null);
		
		dateFieldReport = CalendarFactory.createDateField();
		dateFieldReport.setBounds(218, 5, 109, 18);
		pnlReport.add(dateFieldReport);
		
		JButton btnShow = new JButton("show");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date selectedDate = (Date)dateFieldReport.getValue();
				try {
					Date firstDateOfMonth = DateUtil.getFirstDay(selectedDate);
					Date lastDateOfMonth = DateUtil.getLastDay(selectedDate);
					InputItemRepositoryImpl inputItemRep = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
					Iterator<InputItem> iter = inputItemRep.findAllItems().iterator();
					List<InputItem> results = new ArrayList<InputItem>();
					while(iter.hasNext()) {
						InputItem item = iter.next();
						if(item.getDate() != null) {
							if(item.getDate().compareTo(firstDateOfMonth) >= 0 && item.getDate().compareTo(lastDateOfMonth) <= 0) {
								results.add(item);
							}
						}
					}
					
					loadReportTable(results);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, e.getMessage());
				}				
			}
		});
		btnShow.setBounds(349, 5, 89, 23);
		pnlReport.add(btnShow);
		
		JScrollPane scrollBarReport = new JScrollPane();
		scrollBarReport.setBounds(59, 74, 389, 247);
		pnlReport.add(scrollBarReport);
		
		tblReport = new JTable();
		scrollBarReport.setViewportView(tblReport);
	}

	protected void loadReportTable(List<InputItem> results) {
		
		String[] columnNames = {"ID", "Input Name", "Cost", "Date"};
		Vector<String> columns = new Vector<String>(Arrays.asList(columnNames));
		
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		
		for(InputItem inputItem : results) {
			Vector<String> row = new Vector<String>();
			row.add(inputItem.getId().toString());
			row.add(inputItem.getItem().getName());
			row.add(String.valueOf(inputItem.getCost()));
			String date = inputItem.getDate() != null ? sdf.format(inputItem.getDate()) : "";
			row.add(date);
			rows.add(row);
		}
		
		((DefaultTableModel)tblReport.getModel()).setDataVector(rows, columns);
	}

	private JComboBox<Item> loadCbxItems() {
		ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
		Iterator<Item> items = itemRepo.findAllItems().iterator();
		Vector<Item> data = new Vector<Item>();
		while(items.hasNext()) {
			data.add(items.next());
		}
		JComboBox<Item> cbxItem = new JComboBox<Item>(data);
		return cbxItem;
	}

	private void loadItemTable() {
		ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
		EndResult<Item> items = itemRepo.findAllItems();
		String[] columnNames = {"ID", "Name", "Description"};
		Vector<String> columns = new Vector<String>(Arrays.asList(columnNames));
		Iterator<Item> iter = items.iterator();
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		while (iter.hasNext()) {
			Item type = (Item) iter.next();
			Vector<String> row = new Vector<String>();
			row.add(type.getId().toString());
			row.add(type.getName());
			row.add(type.getDescription());
			rows.add(row);
		}
		
		table = new JTable(rows, columns);
	}
	
	private void loadInputItemTable() {
		InputItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
		EndResult<InputItem> items = itemRepo.findAllItems();
		String[] columnNames = {"ID", "Input Name", "Cost", "Date"};
		Vector<String> columns = new Vector<String>(Arrays.asList(columnNames));
		Iterator<InputItem> iter = items.iterator();
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		
		while (iter.hasNext()) {
			InputItem type = iter.next();
			Vector<String> row = new Vector<String>();
			row.add(type.getId().toString());
			row.add(type.getItem().getName());
			row.add(String.valueOf(type.getCost()));
			String date = type.getDate() != null ? sdf.format(type.getDate()) : "";
			row.add(date);
			rows.add(row);
		}
		
		tblInput = new JTable(rows, columns);
	}
}
