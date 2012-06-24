package pala.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;

import pala.bean.IncomeItem;
import pala.bean.InputItem;
import pala.bean.Item;
import pala.bean.ReportByMonthResult;
import pala.gui.table.ReportByMonthTableModel;
import pala.gui.table.ReportTableModel;
import pala.repository.ApplicationContent;
import pala.repository.IncomeItemRepositoryImpl;
import pala.repository.InputItemRepositoryImpl;
import pala.repository.ItemRepository;
import pala.repository.ItemRepositoryImpl;
import pala.repository.ReportService;

import javax.swing.JCheckBox;

public class MainApp {

	private JFrame frame;
	private JTextField txtName;
	private JTextField txtDescription;
	private JTable tblItem;
	private JTable incomeTable;
	private JComboBox cbxItem;
	private JFormattedTextField txtCost;
	private DateField inputDate;
	private JTable tblInput;
	private JTable tblReport;
	private JTable tblReportByMonth;
	private DateField dateFieldReport;
	private JTextField txtTotalCost;
	private JFormattedTextField txtIncomeCost;
	private JComboBox cbxIncome;
	private DateField dateFieldIncome;
	private JTextField txtTotalIncome;
	private JTextField txtRemaining;
	private JCheckBox chxByDate;
	
	@Autowired
	ItemRepository itemRepository;

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
					loadItemTable();
					loadCbxItem();
				}
			}
		});
		btnAdd.setBounds(312, 7, 89, 23);
		pnlAdmin.add(btnAdd);
		
		txtDescription = new JTextField();
		txtDescription.setBounds(89, 39, 213, 20);
		pnlAdmin.add(txtDescription);
		txtDescription.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(10, 42, 71, 14);
		pnlAdmin.add(lblDescription);
		
		tblItem = new JTable();
		JScrollPane scrollPane = new JScrollPane(tblItem);
		scrollPane.setBounds(10, 72, 397, 239);

		// Add the scroll pane to this panel.
		pnlAdmin.add(scrollPane);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long selectedID = Long.parseLong(((DefaultTableModel)tblItem.getModel()).getValueAt(tblItem.getSelectedRow(), 0).toString());
				ItemRepositoryImpl itemRep = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
				itemRep.deleteItem(selectedID);
				loadItemTable();
			}
		});
		btnDelete.setBounds(312, 38, 89, 23);
		pnlAdmin.add(btnDelete);
		
		JPanel pnlInput = new JPanel();
		tabbedPane.addTab("Input", null, pnlInput, null);
		pnlInput.setLayout(null);
		
		JLabel lblItem = new JLabel("Item:");
		lblItem.setBounds(10, 11, 46, 14);
		pnlInput.add(lblItem);
		
		cbxItem = new JComboBox();
		cbxItem.setBounds(66, 8, 148, 17);
		pnlInput.add(cbxItem);
		
		txtCost = new JFormattedTextField(NumberFormat.getNumberInstance());
		
		txtCost.setBounds(66, 39, 148, 20);
		pnlInput.add(txtCost);
		txtCost.setColumns(10);
		
		JButton btnInputAdd = new JButton("Add");
		btnInputAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
				InputItemRepositoryImpl inputItemRepo = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
				Item item = (Item)cbxItem.getSelectedItem();
				
				long inputCost = ((Number)txtCost.getValue()).longValue();
				InputItem inputItem = inputItemRepo.addItem(item, inputCost, (Date)inputDate.getValue());
				
				if(inputItem != null) {
					loadInputItemTable();
				}
				
			}
		});
		btnInputAdd.setBounds(224, 67, 89, 23);
		pnlInput.add(btnInputAdd);
		
		inputDate = CalendarFactory.createDateField();
		inputDate.setBounds(66, 70, 148, 20);
		pnlInput.add(inputDate);
		
		JScrollPane scrollBarInput = new JScrollPane((Component) null);
		scrollBarInput.setBounds(10, 101, 397, 228);
		pnlInput.add(scrollBarInput);
		
		tblInput = new JTable();
		scrollBarInput.setViewportView(tblInput);
		
		JLabel lblCost = new JLabel("Cost:");
		lblCost.setBounds(10, 42, 46, 14);
		pnlInput.add(lblCost);
		
		JLabel lblTime = new JLabel("Time:");
		lblTime.setBounds(10, 71, 46, 14);
		pnlInput.add(lblTime);
		
		JButton btnInputDelete = new JButton("Delete");
		btnInputDelete.setBounds(318, 67, 89, 23);
		pnlInput.add(btnInputDelete);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Income", null, panel, null);
		panel.setLayout(null);
		
		cbxIncome = new JComboBox();
		cbxIncome.setModel(new DefaultComboBoxModel(IncomeType.values()));
		cbxIncome.setBounds(93, 10, 129, 20);
		panel.add(cbxIncome);
		
		txtIncomeCost = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtIncomeCost.setBounds(232, 10, 134, 20);
		panel.add(txtIncomeCost);
		
		JButton btnAddIncome = new JButton("Add");
		btnAddIncome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IncomeItemRepositoryImpl incomeItemRep = ApplicationContent.applicationContext.getBean(IncomeItemRepositoryImpl.class);
				long cost = ((Number)txtIncomeCost.getValue()).longValue();
				IncomeItem item = incomeItemRep.addIncomeItem((IncomeType)cbxIncome.getSelectedItem(), cost, (Date)dateFieldIncome.getValue());
				if(item != null) {
					loadIncomeTable();
				}
			}
		});
		btnAddIncome.setBounds(376, 10, 89, 23);
		panel.add(btnAddIncome);
		
		incomeTable = new JTable();
		JScrollPane scrollPane_1 = new JScrollPane(incomeTable);
		scrollPane_1.setBounds(10, 95, 455, 248);
		panel.add(scrollPane_1);
		
		dateFieldIncome = CalendarFactory.createDateField();
		dateFieldIncome.setBounds(10, 10, 67, 20);
		panel.add(dateFieldIncome);
		
		JPanel pnlReport = new JPanel();
		tabbedPane.addTab("Report", null, pnlReport, null);
		pnlReport.setLayout(null);
		
		dateFieldReport = CalendarFactory.createDateField();
		dateFieldReport.setBounds(131, 11, 109, 23);
		pnlReport.add(dateFieldReport);
		
		JButton btnShow = new JButton("show");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date selectedDate = (Date)dateFieldReport.getValue();
				try {
					DateTime dt = new DateTime(selectedDate);
					LocalDate fromDate = dt.toLocalDate();
					LocalDate toDate = dt.toLocalDate();
					
					if(!chxByDate.isSelected()) {
						Date firstDateOfMonth = DateUtil.getFirstDay(selectedDate);
						Date lastDateOfMonth = DateUtil.getLastDay(selectedDate);
						dt = new DateTime(firstDateOfMonth);
						fromDate = dt.toLocalDate();
						dt = new DateTime(lastDateOfMonth);
						toDate = dt.toLocalDate();
					}
					
					loadReportTable(fromDate, toDate);
					
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, e.getMessage());
				}				
			}
		});
		btnShow.setBounds(349, 11, 89, 23);
		pnlReport.add(btnShow);
		
		JScrollPane scrollBarReport = new JScrollPane();
		scrollBarReport.setBounds(10, 67, 428, 172);
		pnlReport.add(scrollBarReport);
		
		tblReport = new JTable();
		scrollBarReport.setViewportView(tblReport);
		
		JLabel lblTotal = new JLabel("Total cost:");
		lblTotal.setBounds(219, 253, 61, 14);
		pnlReport.add(lblTotal);
		
		txtTotalCost = new JTextField();
		txtTotalCost.setEditable(false);
		txtTotalCost.setBounds(315, 250, 120, 20);
		pnlReport.add(txtTotalCost);
		txtTotalCost.setColumns(10);
		
		JLabel lblTotalIncome = new JLabel("Total Income:");
		lblTotalIncome.setBounds(218, 287, 89, 14);
		pnlReport.add(lblTotalIncome);
		
		txtTotalIncome = new JTextField();
		txtTotalIncome.setBounds(315, 284, 123, 20);
		pnlReport.add(txtTotalIncome);
		txtTotalIncome.setColumns(10);
		
		txtRemaining = new JTextField();
		txtRemaining.setColumns(10);
		txtRemaining.setBounds(315, 323, 123, 20);
		pnlReport.add(txtRemaining);
		
		JLabel lblRemaining = new JLabel("Remaining:");
		lblRemaining.setBounds(218, 326, 89, 14);
		pnlReport.add(lblRemaining);
		
		chxByDate = new JCheckBox("By Date");
		chxByDate.setBounds(246, 11, 78, 23);
		pnlReport.add(chxByDate);
		
		JPanel pnlReportByMonth = new JPanel();
		tabbedPane.addTab("Report By Month", null, pnlReportByMonth, null);
		pnlReportByMonth.setLayout(null);
		
		final JComboBox cbxMonth = new JComboBox();
		cbxMonth.setMaximumRowCount(12);
		cbxMonth.setModel(new DefaultComboBoxModel(Month.values()));
		cbxMonth.setBounds(51, 11, 108, 20);
		pnlReportByMonth.add(cbxMonth);
		
		final JComboBox cbxYear = new JComboBox();
		cbxYear.setModel(new DefaultComboBoxModel(new String[] {"2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"}));
		cbxYear.setBounds(218, 11, 114, 20);
		pnlReportByMonth.add(cbxYear);
		
		JLabel lblMonth = new JLabel("Month:");
		lblMonth.setBounds(10, 14, 39, 14);
		pnlReportByMonth.add(lblMonth);
		
		JLabel lblYear = new JLabel("Year:");
		lblYear.setBounds(169, 14, 39, 14);
		pnlReportByMonth.add(lblYear);
		
		tblReportByMonth = new JTable();
		JScrollPane scrPnlReportByMonth = new JScrollPane(tblReportByMonth);
		scrPnlReportByMonth.setBounds(20, 42, 428, 223);
		pnlReportByMonth.add(scrPnlReportByMonth);
		
		JButton btnReportByMonth = new JButton("Show");
		btnReportByMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportService inputRep = ApplicationContent.applicationContext.getBean(ReportService.class);
				int year = Integer.parseInt(cbxYear.getSelectedItem().toString());
				int month = ((Month)cbxMonth.getSelectedItem()).getValue();
				DateTime selectedDate = new DateTime(year, month, 1, 0, 0, 0);
				DateTime fromDate = selectedDate.dayOfMonth().withMinimumValue();
				DateTime toDate = selectedDate.dayOfMonth().withMaximumValue();
				toDate= toDate.plusDays(1);
				String sFromDate = String.valueOf(fromDate.getMillis());
				String sToDate = String.valueOf(toDate.getMillis());
				List<ReportByMonthResult> results = inputRep.reportByMonth(sFromDate, sToDate);
				loadReportByMonthTable(results);
			}
		});
		btnReportByMonth.setBounds(359, 10, 89, 23);
		pnlReportByMonth.add(btnReportByMonth);
		
		//fetch all data
		loadAllData();
	}

	private void loadAllData() {
		loadItemTable();
		loadCbxItem();
		loadInputItemTable();
		loadIncomeTable();
	}

	private void loadIncomeTable() {
		IncomeItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(IncomeItemRepositoryImpl.class);
		EndResult<IncomeItem> items = itemRepo.findAllItems();
		String[] columnNames = {"ID", "Date", "Name", "Cost"};
		Vector<String> columns = new Vector<String>(Arrays.asList(columnNames));
		Iterator<IncomeItem> iter = items.iterator();
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		while (iter.hasNext()) {
			IncomeItem type = iter.next();
			Vector<String> row = new Vector<String>();
			row.add(type.getId().toString());
			String formattedDate = type.getDate() != null ? DateUtil.sdf.format(type.getDate()) : "";
			row.add(formattedDate);
			row.add(type.getType().getTitle());
			row.add(NumberFormat.getNumberInstance().format(type.getCost()));
			
			rows.add(row);
		}
		
		((DefaultTableModel)incomeTable.getModel()).setDataVector(rows, columns);
	}

	private void loadReportTable(LocalDate fromDate, LocalDate toDate) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		String[] columnNames = {"ID", "Input Name", "Cost", "Date"};
		Vector<String> columns = new Vector<String>(Arrays.asList(columnNames));
		
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		
		double totalCost = 0;
		IncomeItemRepositoryImpl incomeRep = ApplicationContent.applicationContext.getBean(IncomeItemRepositoryImpl.class);
		InputItemRepositoryImpl inputItemRep = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
		Iterator<InputItem> iter = inputItemRep.findAllItems().iterator();
		List<InputItem> results = new ArrayList<InputItem>();
		
		ReportTableModel model = new ReportTableModel();
		
		while(iter.hasNext()) {
			InputItem item = iter.next();
			
			if(item.getDate() != null) {
				DateTime dateTime = new DateTime(item.getDate());
				LocalDate ld = dateTime.toLocalDate();
				if(ld.compareTo(fromDate) >= 0 && ld.compareTo(toDate) <= 0) {
					model.addRow(item);
					totalCost += item.getCost();
				}
			}
		}
		
		tblReport.setModel(model);
		txtTotalCost.setText(nf.format(totalCost));
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tblReport.getModel());
		tblReport.setRowSorter(sorter);
		
		//set total income
		Iterator<IncomeItem> iter1 = incomeRep.findAllItems().iterator();
		double totalIncome = 0;
		while(iter1.hasNext()) {
			IncomeItem income = iter1.next();
			totalIncome += income.getCost();
		}
		
		txtTotalIncome.setText(nf.format(totalIncome));
		
		double remaining = totalIncome - totalCost;
		
		txtRemaining.setText(nf.format(remaining));
	}

	private void loadCbxItem() {
		ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
		Iterator<Item> items = itemRepo.findAllItems().iterator();
		DefaultComboBoxModel model = (DefaultComboBoxModel)cbxItem.getModel();
		model.removeAllElements();
		while(items.hasNext()) {
			Item item = items.next();
			if(item.isActive()) {
				model.addElement(item);
			}
		}
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
			if(type.isActive()) {
				Vector<String> row = new Vector<String>();
				row.add(type.getId().toString());
				row.add(type.getName());
				row.add(type.getDescription());
				rows.add(row);
			}
		}
		
		((DefaultTableModel)tblItem.getModel()).setDataVector(rows, columns);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tblItem.getModel());
		tblItem.setRowSorter(sorter);
	}
	
	private void loadInputItemTable() {
		InputItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
		EndResult<InputItem> items = itemRepo.findAllItems();
		String[] columnNames = {"ID", "Input Name", "Cost", "Date"};
		Vector<String> columns = new Vector<String>(Arrays.asList(columnNames));
		Iterator<InputItem> iter = items.iterator();
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		
		NumberFormat nf = NumberFormat.getNumberInstance();
		while (iter.hasNext()) {
			InputItem type = iter.next();
			Vector<String> row = new Vector<String>();
			row.add(type.getId().toString());
			row.add(type.getItem().getName());
			row.add(nf.format(type.getCost()));
			String date = type.getDate() != null ? DateUtil.sdf.format(type.getDate()) : "";
			row.add(date);
			rows.add(row);
		}
		
		((DefaultTableModel)tblInput.getModel()).setDataVector(rows, columns);
	}
	
	private void loadReportByMonthTable(List<ReportByMonthResult> data) {
		ReportByMonthTableModel model = new ReportByMonthTableModel();
		model.addAll(data);
		tblReportByMonth.setModel(model);
	}
}
