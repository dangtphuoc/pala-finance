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
import java.util.Locale;
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
import pala.bean.ReportByItemResult;
import pala.bean.ReportByMonthResult;
import pala.common.utillity.DateUtil;
import pala.common.utillity.ErrorConstants;
import pala.common.utillity.TableUtil;
import pala.gui.dialog.InputItemDialog;
import pala.gui.dialog.ItemDialog;
import pala.gui.handler.CallBackHandler;
import pala.gui.table.ReportByItemTableModel;
import pala.gui.table.ReportByMonthTableModel;
import pala.gui.table.ReportTableModel;
import pala.gui.utility.UIUtil;
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
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import java.util.ResourceBundle;

public class MainApp implements CallBackHandler{

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
	private JTable tblReportByItem;
	private JComboBox cbxMonthByItem;
	private JComboBox cbxYearByItem;
	private JComboBox cbxItemByItem;
	private JTextField txtTotalItemCost;

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
		//Locale.setDefault(new Locale("vi", "VN"));
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Phuoc Dang\\git\\pala-finance\\src\\main\\resources\\piggy-bank-icon.png"));
		frame.setBounds(100, 100, 723, 502);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel pnlAdmin = new JPanel();
		tabbedPane.addTab(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.tabAdministration"), null, pnlAdmin, null);
		final ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
		pnlAdmin.setLayout(new BorderLayout(0, 0));
		
		tblItem = new JTable();
		tblItem.setOpaque(false);
		JScrollPane scrollPane = new JScrollPane(tblItem);
		scrollPane.setBackground(new Color(255, 0, 0));

		// Add the scroll pane to this panel.
		pnlAdmin.add(scrollPane, BorderLayout.CENTER);
		
		JPanel pnlButtons = new JPanel();
		pnlButtons.setBackground(new Color(255, 240, 245));
		FlowLayout fl_pnlButtons = (FlowLayout) pnlButtons.getLayout();
		fl_pnlButtons.setAlignment(FlowLayout.LEFT);
		pnlAdmin.add(pnlButtons, BorderLayout.NORTH);
		JButton btnAdd = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnAdd.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlButtons.add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemDialog dialog = new ItemDialog(frame);
				dialog.setVisible(true);
				if(dialog.isOk()) {
					itemRepo.addItem(dialog.getName(), dialog.getDescription());
					Item item = itemRepo.findItemNamed(dialog.getName());
					if(item != null) {
						loadItemTable();
						UIUtil.initializeInputComboBox(cbxItemByItem, true);
					}
				}
			}
		});
		
		JButton btnEdit = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnEdit.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlButtons.add(btnEdit);
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
						UIUtil.initializeInputComboBox(cbxItemByItem, true);
					}
				} else  {
					JOptionPane.showMessageDialog(frame, "Please selected an item to edit");
				}
			}
		});
		
		JButton btnDelete = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnDelete.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlButtons.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long selectedID = TableUtil.getSelectedID(tblItem);
				ItemRepositoryImpl itemRep = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
				itemRep.deleteItem(selectedID);
				loadItemTable();
			}
		});
		
		JPanel pnlInput = new JPanel();
		tabbedPane.addTab("Input", null, pnlInput, null);
		pnlInput.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollBarInput = new JScrollPane((Component) null);
		pnlInput.add(scrollBarInput, BorderLayout.CENTER);
		
		tblInput = new JTable();
		scrollBarInput.setViewportView(tblInput);
		
		JPanel pnlInputButtons = new JPanel();
		pnlInputButtons.setBackground(new Color(224, 255, 255));
		FlowLayout flowLayout = (FlowLayout) pnlInputButtons.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pnlInput.add(pnlInputButtons, BorderLayout.NORTH);
		
		JButton btnInputAdd = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnAdd.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlInputButtons.add(btnInputAdd);
		
		JButton btnEditInputItem = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnEdit.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlInputButtons.add(btnEditInputItem);
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
		
		JButton btnInputDelete = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnDelete.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlInputButtons.add(btnInputDelete);
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
		btnInputAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InputItemDialog dialog = new InputItemDialog(frame, MainApp.this);
				dialog.setVisible(true);
				if(dialog.isOk()) {
					//currently, do nothing...
				}
				
			}
		});
		
		JPanel pnlIncome = new JPanel();
		tabbedPane.addTab("Income", null, pnlIncome, null);
		pnlIncome.setLayout(new BorderLayout(0, 0));
		
		incomeTable = new JTable();
		JScrollPane scrollPaneIncome = new JScrollPane(incomeTable);
		pnlIncome.add(scrollPaneIncome);
		
		JPanel pnlIncomeButtons = new JPanel();
		pnlIncomeButtons.setBackground(new Color(224, 255, 255));
		FlowLayout flowLayout_1 = (FlowLayout) pnlIncomeButtons.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		pnlIncome.add(pnlIncomeButtons, BorderLayout.NORTH);
		
		dateFieldIncome = CalendarFactory.createDateField();
		dateFieldIncome.setPreferredSize(new Dimension(100, 18));
		pnlIncomeButtons.add(dateFieldIncome);
		
		cbxIncome = new JComboBox();
		pnlIncomeButtons.add(cbxIncome);
		cbxIncome.setModel(new DefaultComboBoxModel(IncomeType.values()));
		
		txtIncomeCost = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtIncomeCost.setMinimumSize(new Dimension(100, 20));
		txtIncomeCost.setPreferredSize(new Dimension(100, 20));
		pnlIncomeButtons.add(txtIncomeCost);
		
		JButton btnAddIncome = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnAdd.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlIncomeButtons.add(btnAddIncome);
		
		JButton btnIncomeDelete = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnDelete.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlIncomeButtons.add(btnIncomeDelete);
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
		
		JPanel pnlReport = new JPanel();
		pnlReport.setBackground(new Color(255, 0, 255));
		tabbedPane.addTab("Report", null, pnlReport, null);
		pnlReport.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollBarReport = new JScrollPane();
		scrollBarReport.setBackground(new Color(135, 206, 250));
		pnlReport.add(scrollBarReport, BorderLayout.CENTER);
		
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
		
		JPanel pnlReportButtons = new JPanel();
		pnlReportButtons.setBackground(new Color(224, 255, 255));
		FlowLayout flowLayout_2 = (FlowLayout) pnlReportButtons.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		pnlReport.add(pnlReportButtons, BorderLayout.NORTH);
		
		dateFieldReport = CalendarFactory.createDateField();
		dateFieldReport.setPreferredSize(new Dimension(100, 18));
		pnlReportButtons.add(dateFieldReport);
		
		chxByDate = new JCheckBox(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.chxByDate.text")); //$NON-NLS-1$ //$NON-NLS-2$
		chxByDate.setBackground(new Color(224, 255, 255));
		pnlReportButtons.add(chxByDate);
		
		JButton btnShow = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnShow.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlReportButtons.add(btnShow);
		
		JPanel pnlReportSummary = new JPanel();
		pnlReport.add(pnlReportSummary, BorderLayout.SOUTH);
		pnlReportSummary.setLayout(new GridLayout(3, 2, 2, 5));
		
		JLabel lblTotal = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblTotal.text")); //$NON-NLS-1$ //$NON-NLS-2$
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlReportSummary.add(lblTotal);
		
		txtTotalCost = new JTextField();
		pnlReportSummary.add(txtTotalCost);
		txtTotalCost.setEditable(false);
		txtTotalCost.setColumns(10);
		
		JLabel lblTotalIncome = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblTotalIncome.text")); //$NON-NLS-1$ //$NON-NLS-2$
		lblTotalIncome.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlReportSummary.add(lblTotalIncome);
		
		txtTotalIncome = new JTextField();
		pnlReportSummary.add(txtTotalIncome);
		txtTotalIncome.setEditable(false);
		txtTotalIncome.setColumns(10);
		
		JLabel lblRemaining = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblRemaining.text")); //$NON-NLS-1$ //$NON-NLS-2$
		lblRemaining.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlReportSummary.add(lblRemaining);
		
		txtRemaining = new JTextField();
		pnlReportSummary.add(txtRemaining);
		txtRemaining.setEditable(false);
		txtRemaining.setColumns(10);
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
		
		JPanel pnlReportByMonth = new JPanel();
		tabbedPane.addTab("Report By Month", null, pnlReportByMonth, null);
		pnlReportByMonth.setLayout(new BorderLayout(0, 0));
		
		tblReportByMonth = new JTable();
		JScrollPane scrPnlReportByMonth = new JScrollPane(tblReportByMonth);
		pnlReportByMonth.add(scrPnlReportByMonth);
		
		JPanel pnlReportByMonthButtons = new JPanel();
		pnlReportByMonthButtons.setBackground(new Color(224, 255, 255));
		FlowLayout flowLayout_3 = (FlowLayout) pnlReportByMonthButtons.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		pnlReportByMonth.add(pnlReportByMonthButtons, BorderLayout.NORTH);
		
		JLabel lblMonth = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblMonth.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlReportByMonthButtons.add(lblMonth);
		
		final JComboBox cbxMonth = new JComboBox();
		pnlReportByMonthButtons.add(cbxMonth);
		cbxMonth.setMaximumRowCount(12);
		cbxMonth.setModel(new DefaultComboBoxModel(Month.values()));
		
		JLabel lblYear = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblYear.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlReportByMonthButtons.add(lblYear);
		
		final JComboBox cbxYear = new JComboBox();
		pnlReportByMonthButtons.add(cbxYear);
		cbxYear.setModel(new DefaultComboBoxModel(new String[] {"2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"}));
		
		JButton btnReportByMonth = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnShow.text")); //$NON-NLS-1$ //$NON-NLS-2$
		pnlReportByMonthButtons.add(btnReportByMonth);
		
		JPanel rptReportByItem = new JPanel();
		tabbedPane.addTab("Report By Item", null, rptReportByItem, null);
		rptReportByItem.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel.setBackground(new Color(224, 255, 255));
		rptReportByItem.add(panel, BorderLayout.NORTH);
		
		JLabel label = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblMonth.text")); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(label);
		
		cbxMonthByItem = new JComboBox();
		cbxMonthByItem.setModel(new DefaultComboBoxModel(Month.values()));
		cbxMonthByItem.setMaximumRowCount(12);
		panel.add(cbxMonthByItem);
		
		JLabel label_1 = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblYear.text")); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(label_1);
		
		cbxYearByItem = new JComboBox();
		cbxYearByItem.setModel(new DefaultComboBoxModel(new String[] {"2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"}));
		panel.add(cbxYearByItem);
		
		JLabel lblItem = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblItem.text")); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(lblItem);
		
		cbxItemByItem = new JComboBox();
		cbxItemByItem.setModel(new DefaultComboBoxModel(new String[] {"All"}));
		panel.add(cbxItemByItem);
		
		UIUtil.initializeInputComboBox(cbxItemByItem, true);
		
		JButton btnShowByItem = new JButton(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.btnShow.text")); //$NON-NLS-1$ //$NON-NLS-2$
		btnShowByItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportService inputRep = ApplicationContent.applicationContext.getBean(ReportService.class);
				int year = Integer.parseInt(cbxYearByItem.getSelectedItem().toString());
				int month = ((Month)cbxMonthByItem.getSelectedItem()).getValue();
				DateTime selectedDate = new DateTime(year, month, 1, 0, 0, 0);
				DateTime fromDate = selectedDate.dayOfMonth().withMinimumValue();
				DateTime toDate = selectedDate.dayOfMonth().withMaximumValue();
				toDate= toDate.plusDays(1);
				String sFromDate = String.valueOf(fromDate.getMillis());
				String sToDate = String.valueOf(toDate.getMillis());
				List<ReportByItemResult> results = inputRep.reportByItem(sFromDate, sToDate);
				loadReportByItemTable(results);
				double totalCost = 0;
				for(ReportByItemResult item : results) {
					totalCost += item.getCost();
				}
				
				NumberFormat nf = NumberFormat.getNumberInstance();
				txtTotalItemCost.setText(nf.format(totalCost));
			}
		});
		panel.add(btnShowByItem);
		
		JScrollPane scrollPaneReportByItem = new JScrollPane();
		rptReportByItem.add(scrollPaneReportByItem, BorderLayout.CENTER);
		
		tblReportByItem = new JTable();
		scrollPaneReportByItem.setViewportView(tblReportByItem);
		
		JPanel panel_1 = new JPanel();
		rptReportByItem.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(1, 2, 5, 5));
		
		JLabel lblTotalItem = new JLabel(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.lblTotal.text")); //$NON-NLS-1$ //$NON-NLS-2$
		lblTotalItem.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(lblTotalItem);
		
		txtTotalItemCost = new JTextField();
		txtTotalItemCost.setEditable(false);
		panel_1.add(txtTotalItemCost);
		txtTotalItemCost.setColumns(10);
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
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tblInput.getModel());
		sorter.toggleSortOrder(3);
		sorter.toggleSortOrder(3);
		tblInput.setRowSorter(sorter);
	}
	
	private void loadReportByMonthTable(List<ReportByMonthResult> data) {
		ReportByMonthTableModel model = new ReportByMonthTableModel();
		model.addAll(data);
		tblReportByMonth.setModel(model);
	}
	
	private void loadReportByItemTable(List<ReportByItemResult> data) {
		ReportByItemTableModel model = new ReportByItemTableModel();
		model.addAll(data);
		tblReportByItem.setModel(model);
	}

	@Override
	public int addInputItem(InputItem inputItem) {
		InputItemRepositoryImpl inputItemRepo = ApplicationContent.applicationContext.getBean(InputItemRepositoryImpl.class);
		inputItem = inputItemRepo.addItem(inputItem);
		
		if(inputItem != null) {
			loadInputItemTable();
			return ErrorConstants.SUCCESS;
		}
		return ErrorConstants.FAIL;
		
	}
}
