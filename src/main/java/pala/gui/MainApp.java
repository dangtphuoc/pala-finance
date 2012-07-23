package pala.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import pala.common.util.TableUtil;
import pala.gui.dialog.InputItemDialog;
import pala.gui.dialog.ItemDialog;
import pala.gui.table.ReportByMonthTableModel;
import pala.gui.table.ReportTableModel;
import pala.repository.ApplicationContent;
import pala.repository.IncomeItemRepositoryImpl;
import pala.repository.InputItemRepositoryImpl;
import pala.repository.ItemRepository;
import pala.repository.ItemRepositoryImpl;
import pala.repository.ReportService;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MainApp {

	public JFrame frame;
	private JTable tblItem;
	private JTable incomeTable;
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
					/*final SplashScreen splash = SplashScreen.getSplashScreen();
			        if (splash != null) {
			        	Graphics2D g = splash.createGraphics();
				        if (g == null) {
				            System.out.println("g is null");
				            return;
				        }
			        }*/
			        
					MainApp window = new MainApp();
					/*if(splash != null)
						splash.close();*/
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = {"foo", "bar", "baz"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading "+comps[(frame/5)%3]+"...", 120, 150);
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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Phuoc Dang\\git\\pala-finance\\src\\main\\resources\\piggy-bank-icon.png"));
		frame.setBounds(100, 100, 513, 420);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel pnlAdmin = new JPanel();
		tabbedPane.addTab("Administration", null, pnlAdmin, null);
		pnlAdmin.setLayout(null);
		final ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemDialog dialog = new ItemDialog(frame);
				dialog.setVisible(true);
				if(dialog.isOk()) {
					itemRepo.addItem(dialog.getName(), dialog.getDescription());
					Item item = itemRepo.findItemNamed(dialog.getName());
					if(item != null) {
						loadItemTable();
					}
				}
			}
		});
		btnAdd.setBounds(204, 11, 89, 23);
		pnlAdmin.add(btnAdd);
		
		tblItem = new JTable();
		JScrollPane scrollPane = new JScrollPane(tblItem);
		scrollPane.setBounds(10, 45, 472, 298);

		// Add the scroll pane to this panel.
		pnlAdmin.add(scrollPane);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long selectedID = TableUtil.getSelectedID(tblItem);
				ItemRepositoryImpl itemRep = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
				itemRep.deleteItem(selectedID);
				loadItemTable();
			}
		});
		btnDelete.setBounds(393, 11, 89, 23);
		pnlAdmin.add(btnDelete);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long selectedID = TableUtil.getSelectedID(tblItem);
				if(selectedID != -1) {
					ItemRepositoryImpl itemRep = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
					Item item = itemRep.findByID(selectedID);
					ItemDialog dialog = new ItemDialog(frame, item);
					dialog.setVisible(true);
					if(dialog.isOk()) {
						item.setName(dialog.getName());
						item.setDescription(dialog.getDescription());
						itemRep.saveItem(item);
						loadItemTable();
					}
				} else  {
					JOptionPane.showMessageDialog(frame, "Please selected an item to edit");
				}
			}
		});
		btnEdit.setBounds(300, 11, 89, 23);
		pnlAdmin.add(btnEdit);
		
		JPanel pnlInput = new JPanel();
		tabbedPane.addTab("Input", null, pnlInput, null);
		pnlInput.setLayout(null);
		
		JButton btnInputAdd = new JButton("Add");
		btnInputAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InputItemDialog dialog = new InputItemDialog(frame);
				dialog.setVisible(true);
				if(dialog.isOk()) {
					InputItemRepositoryImpl inputItemRepo = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
					InputItem item = dialog.getInputItem();
					InputItem inputItem = inputItemRepo.addItem(item);
					
					if(inputItem != null) {
						loadInputItemTable();
					}
				}
				
			}
		});
		btnInputAdd.setBounds(210, 11, 89, 23);
		pnlInput.add(btnInputAdd);
		
		JScrollPane scrollBarInput = new JScrollPane((Component) null);
		scrollBarInput.setBounds(10, 45, 472, 298);
		pnlInput.add(scrollBarInput);
		
		tblInput = new JTable();
		scrollBarInput.setViewportView(tblInput);
		
		JButton btnInputDelete = new JButton("Delete");
		btnInputDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputItemRepositoryImpl inputItemRepo = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
				long selectedID = TableUtil.getSelectedID(tblInput);
				if(selectedID != -1) {
					inputItemRepo.deleteInputItem(selectedID);
					loadInputItemTable();
				}
			}
		});
		btnInputDelete.setBounds(393, 11, 89, 23);
		pnlInput.add(btnInputDelete);
		
		JButton btnEditInputItem = new JButton("Edit");
		btnEditInputItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputItemRepositoryImpl inputItemRepo = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
				long selectedID = TableUtil.getSelectedID(tblInput);
				if(selectedID != -1) {
					InputItem item = inputItemRepo.findByID(selectedID);
					InputItemDialog dialog = new InputItemDialog(frame, item);
					dialog.setVisible(true);
					if(dialog.isOk()) {
						
						item = dialog.getInputItem();
						InputItem inputItem = inputItemRepo.saveInputItem(item);
						
						if(inputItem != null) {
							loadInputItemTable();
						}
					}
				}
			}
		});
		btnEditInputItem.setBounds(302, 11, 89, 23);
		pnlInput.add(btnEditInputItem);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Income", null, panel, null);
		panel.setLayout(null);
		
		cbxIncome = new JComboBox();
		cbxIncome.setModel(new DefaultComboBoxModel(IncomeType.values()));
		cbxIncome.setBounds(93, 10, 97, 20);
		panel.add(cbxIncome);
		
		txtIncomeCost = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtIncomeCost.setBounds(200, 10, 134, 20);
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
		btnAddIncome.setBounds(344, 9, 67, 23);
		panel.add(btnAddIncome);
		
		incomeTable = new JTable();
		JScrollPane scrollPane_1 = new JScrollPane(incomeTable);
		scrollPane_1.setBounds(10, 41, 472, 302);
		panel.add(scrollPane_1);
		
		dateFieldIncome = CalendarFactory.createDateField();
		dateFieldIncome.setBounds(10, 10, 67, 20);
		panel.add(dateFieldIncome);
		
		JButton btnIncomeDelete = new JButton("Delete");
		btnIncomeDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IncomeItemRepositoryImpl inputItemRepo = ApplicationContent.applicationContext.getBean(IncomeItemRepositoryImpl.class);
				long selectedID = TableUtil.getSelectedID(incomeTable);
				if(selectedID != -1) {
					inputItemRepo.deleteIncomeItem(selectedID);
					loadIncomeTable();
				}
			}
		});
		btnIncomeDelete.setBounds(415, 9, 67, 23);
		panel.add(btnIncomeDelete);
		
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
		scrollBarReport.setBounds(10, 45, 428, 172);
		pnlReport.add(scrollBarReport);
		
		tblReport = new JTable();
		tblReport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblReport.rowAtPoint(e.getPoint());
				int col = tblReport.columnAtPoint(e.getPoint());
				if(row >= 0 && col == 4) {
					String attachedFile = tblReport.getValueAt(row, col).toString();
					File file = new File("attachment/" + attachedFile);
					Desktop dt = Desktop.getDesktop();
					try {
						dt.open(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println(attachedFile);
				}
			}
		});
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
		txtTotalIncome.setEditable(false);
		txtTotalIncome.setBounds(315, 284, 123, 20);
		pnlReport.add(txtTotalIncome);
		txtTotalIncome.setColumns(10);
		
		txtRemaining = new JTextField();
		txtRemaining.setEditable(false);
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
		scrPnlReportByMonth.setBounds(10, 42, 472, 301);
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
		btnReportByMonth.setBounds(393, 10, 89, 23);
		pnlReportByMonth.add(btnReportByMonth);
		
		//fetch all data
		loadAllData();
	}

	private void loadAllData() {
		loadItemTable();
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
			if(income.getDate() != null) {
				DateTime dateTime = new DateTime(income.getDate());
				LocalDate ld = dateTime.toLocalDate();
				if(ld.compareTo(fromDate) >= 0 && ld.compareTo(toDate) <= 0) {
					totalIncome += income.getCost();
				}
			}
		}
		
		txtTotalIncome.setText(nf.format(totalIncome));
		
		double remaining = totalIncome - totalCost;
		
		txtRemaining.setText(nf.format(remaining));
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
