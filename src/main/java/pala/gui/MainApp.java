package pala.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import pala.bean.Item;
import pala.repository.ApplicationContent;
import pala.repository.ItemRepositoryImpl;

public class MainApp {

	private JFrame frame;
	private JTextField txtName;
	private JTextField txtDescription;
	private JTable table;
	private JTextField txtConsole;

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
					txtConsole.setText(item.getName() + item.getDescription());
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
		
		String[] columnNames = { "First Name", "Last Name", "Sport",
				"# of Years", "Vegetarian" };

		Object[][] data = {
				{ "Kathy", "Smith", "Snowboarding", new Integer(5),
						new Boolean(false) },
				{ "John", "Doe", "Rowing", new Integer(3), new Boolean(true) },
				{ "Sue", "Black", "Knitting", new Integer(2),
						new Boolean(false) },
				{ "Jane", "White", "Speed reading", new Integer(20),
						new Boolean(true) },
				{ "Joe", "Brown", "Pool", new Integer(10), new Boolean(false) } };

		final JTable table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 189, 397, 139);

		// Add the scroll pane to this panel.
		pnlAdmin.add(scrollPane);
		
		txtConsole = new JTextField();
		txtConsole.setBounds(10, 96, 397, 20);
		pnlAdmin.add(txtConsole);
		txtConsole.setColumns(10);
		
		JPanel pnlInput = new JPanel();
		tabbedPane.addTab("New tab", null, pnlInput, null);
		pnlInput.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Hang muc:");
		lblNewLabel.setBounds(10, 11, 59, 14);
		pnlInput.add(lblNewLabel);
		
		JPanel pnlReport = new JPanel();
		tabbedPane.addTab("New tab", null, pnlReport, null);
	}
}
